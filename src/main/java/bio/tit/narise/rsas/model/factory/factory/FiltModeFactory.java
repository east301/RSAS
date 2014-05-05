package bio.tit.narise.rsas.model.factory.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.ContriRes;
import bio.tit.narise.rsas.model.factory.product.FCRRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.PValRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;

/**
 *
 * @author TN
 */
public class FiltModeFactory extends Factory {
    
    // protected ParsedArgs pargs = null;
    private String[] lineList;
    private int itemRnkpLineNum = 1;
    private int contriLineNum = 1;
    
    public FiltModeFactory(ParsedArgs pargs){
        super(pargs);
    }
    
    // public static Factory getFactory(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    // public void setParameter(ParsedArgs pargs)
    // public RSASResults createResults()
    
    
    public boolean parseRepFile() throws FileNotFoundException, IOException {
        
        File repfile = new File(pargs.getReppath());
        System.out.println("[ Info ] Reading rep file");
        
        boolean copyLine = false;
        boolean isSameFCRFiltMode = false;
        boolean isItemRnkOrItemRnkp = false;
        boolean isItemRnkTag = false;
        boolean isOrderedItemSets = false;
        boolean isRunSum = false;
        boolean isPVal = false;
        boolean isFCR = false;
        boolean isContri = false;
        
        BufferedReader repbr = new BufferedReader(new FileReader(repfile));
        String line;
        while( (line = repbr.readLine()) != null ) {
            
            lineList = line.trim().split("\t");
            
            // Report
            if(copyLine) {
                CreateReportFileUtility.reportRepFileLine(line);
            }
            
            // tag judgement
            if(lineList.length == 1 && !lineList[0].trim().isEmpty()) {
                
                if(line.startsWith("<FCR_FILTMODE>")) {
                    String filtModeString = line.replaceFirst("<FCR_FILTMODE>", "").replaceFirst("</FCR_FILTMODE>", "");
                    switch (filtModeString) {
                        case "FCR_RNK":
                            if(pargs.isFCRRnkMode()){ isSameFCRFiltMode = true; }
                            break;
                        case "FCR_RNKP":
                            if(pargs.isFCRRnkpMode()){ isSameFCRFiltMode = true; }
                            break;
                    }
                    continue;
                }
                else if(line.startsWith("</ARGS>")) {
                    copyLine = true;
                    continue;
                }
                else if(line.startsWith("<ITEMRNK>") | line.startsWith("<ITEMRNKP>")) {
                    isItemRnkOrItemRnkp = true;
                    if(line.startsWith("<ITEMRNK>")){ isItemRnkTag = true; }
                    continue;
                }
                else if(line.startsWith("</ITEMRNK>") | line.startsWith("</ITEMRNKP>")) {
                    isItemRnkOrItemRnkp = false;
                    continue;
                }
                else if(line.startsWith("<ORDERED_ITEMSET>")) {
                    isOrderedItemSets = true;
                    continue;
                }
                else if(line.startsWith("</ORDERED_ITEMSET>")) {
                    isOrderedItemSets = false;
                    continue;
                }
                else if(line.startsWith("<RUNNINGSUM>")) {
                    isRunSum = true;
                    continue;
                }
                else if(line.startsWith("</RUNNINGSUM>")) {
                    isRunSum = false;
                    continue;
                }
                else if(line.startsWith("<PVALUE>")) {
                    isPVal = true;
                    continue;
                }
                else if(line.startsWith("</PVALUE>")) {
                    if( isSameFCRFiltMode && pargs.getFcr() == -1.0 ) { 
                        isPVal = false; 
                        continue; 
                    }
                    else { 
                        break;
                    }
                }
                else if(line.startsWith("<FCR>")) {
                    isFCR = true;
                    continue;
                }
                else if(line.startsWith("</FCR>")) {
                    isFCR = false;
                    continue;
                }
                else if(line.startsWith("<CONTRIBUTORS_WITHOUT_FCRFILT>") | line.startsWith("<CONTRIBUTORS_WITHOUT_AND_WITH_FCRFILT>")) {
                    isContri = true;
                    continue;
                }
                else if(line.startsWith("</CONTRIBUTORS_WITHOUT_FCRFILT>") | line.startsWith("</CONTRIBUTORS_WITHOUT_AND_WITH_FCRFILT>")) {
                    //isContri = false;
                    break;
                }
            }
            
            // processes
            if(isItemRnkOrItemRnkp) {
                parseItemRnkOrItemRnkp(isItemRnkTag);
            }
            else if(isOrderedItemSets) {
                parseOrderedItemSet();
            }
            else if(isRunSum) {
                parseRunSum();
            }
            else if(isPVal) {
                parsePVal();
            }
            else if(isFCR) {
                parseFCR();
            }
            else if(isContri) {
                parseContri();
            }
            
        }
        
        return isSameFCRFiltMode;
    }
    
    // set List<String> itemRnk (and List<Double> itemRnkp)
    private void parseItemRnkOrItemRnkp(Boolean isItemRnkTag) {
        
        if(pargs.isFCRRnkMode()) {
            if(isItemRnkTag) {
                Storehouse.itemRnk.addAll(Arrays.asList(lineList));
            } else {
                if(itemRnkpLineNum == 2) {
                    for(int i = 0; i < lineList.length; i++) {
                        Storehouse.itemP.add(Double.parseDouble(lineList[i]));
                    }
                }
                else if(itemRnkpLineNum == 1) {
                    Storehouse.itemRnk.addAll(Arrays.asList(lineList));
                    itemRnkpLineNum++;
                }
            }
        }
        else if(pargs.isFCRRnkpMode()) {
            if(itemRnkpLineNum == 2) {
                for(int i = 0; i < lineList.length; i++) {
                    Storehouse.itemP.add(Double.parseDouble(lineList[i]));
                }
            }
            else if(itemRnkpLineNum == 1) {
                Storehouse.itemRnk.addAll(Arrays.asList(lineList));
                itemRnkpLineNum++;
            }
        }
        Storehouse.N = Storehouse.itemRnk.size();
    }
    
    // set List<OrderedItemSetRes> orderedItemSets
    private void parseOrderedItemSet() {
        List<String> orderedItems = new ArrayList();
        for(int i = 2; i < lineList.length; i++) {
            orderedItems.add(lineList[i]);
        }
        Storehouse.orderedItemSets.add(new OrderedItemSetRes(lineList[0], lineList[1], orderedItems) );
        Storehouse.setNum++;
    }
    
    // set Map<String, RunSumRes> IDandRunSumRes
    private void parseRunSum() {
        int ES = Integer.parseInt(lineList[1]);
        double NES = Double.parseDouble(lineList[2]);
        boolean pos = Boolean.parseBoolean(lineList[4]);
        int indexAtMax;
        if(pos){
            indexAtMax = Integer.parseInt(lineList[3]) - 1;
        }
        else {
            // Note that this indexAtMax indicates the next index of the item at minimum running sum in the itemRnk
            indexAtMax = Integer.parseInt(lineList[3]);
        }
        Storehouse.IDandRunSumRes.put( lineList[0], new RunSumRes(ES, indexAtMax, pos, NES) );
    }
    
    // set Map<String, PValRes> IDandPValRes
    private void parsePVal() {
        Storehouse.IDandPValRes.put( lineList[0], new PValRes(Double.parseDouble(lineList[2])) );
    }

    private void parseFCR() {
        List<Double> FCRs = new ArrayList();
        for(int i = 3; i < lineList.length; i++) {
            FCRs.add( Double.parseDouble(lineList[i]) );
        }
        Storehouse.IDandFCRRes.put( lineList[0], new FCRRes( FCRs, Double.parseDouble(lineList[1]), Integer.parseInt(lineList[2])) );
    }

    private void parseContri() {
        List<String> contributors = new ArrayList();
        
        //if(!isSameFCRVal) { not supported yet
            if(contriLineNum == 1) {
                contriLineNum++;
                for(int i = 2; i < lineList.length; i++) {
                    contributors.add( lineList[i] );
                }
                Storehouse.IDandContriRes.put( lineList[0], new ContriRes(contributors, Double.parseDouble(lineList[1])) );
            }
            else {
                contriLineNum--;
            }
        }
        //else {
        //    if(contriLineNum == 1) {
        //        contriLineNum++;
        //    }
        //    else {
        //        contriLineNum--;
        //        if( !lineList[1].equals("null") ){
        //            for(int i = 2; i < lineList.length; i++) {
        //                contributors.add( lineList[i] );
        //            }
        //            Storehouse.IDandContriRes.put( lineList[0], new ContriRes( contributors, Double.parseDouble(lineList[1])) );
        //        }
        //        else {   
        //            Storehouse.IDandContriRes.put( lineList[0], new ContriRes(contributors, null) );
        //        }
        //    }
        //}
    //}
}
