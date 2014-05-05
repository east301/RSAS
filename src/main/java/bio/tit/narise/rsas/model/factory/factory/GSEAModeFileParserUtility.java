package bio.tit.narise.rsas.model.factory.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import bio.tit.narise.rsas.model.factory.product.ItemSetRes;

/**
 *
 * @author TN
 */
class GSEAModeFileParserUtility {
    
    // this is an utility class
    private GSEAModeFileParserUtility(){ throw new UnsupportedOperationException(); }
    
    public static List<String> parseRnkFile(String rpath) throws FileNotFoundException, IOException {
        
        File rfile = new File(rpath);
        System.out.println("[ Info ] Reading rnk file");
        
        // variables for ids and statistics
        List<String> itemRnk = new ArrayList();
        Map<String, Double> itemRnkBeforeSort = new HashMap();
        List<String> ids1 = new ArrayList();
        Set<String> ids2 = new HashSet(); // for check for redundant IDs
        List<Double> sVals = new ArrayList();
        
        // only rnk
        List<String> onlyRnkIds1 = new ArrayList();
        Set<String> onlyRnkIds2 = new HashSet(); // for check for redundant IDs
        
        if(checkBeforeReadfile(rfile)){
            BufferedReader rbr = new BufferedReader(new FileReader(rfile));
            String line;
            while( (line = rbr.readLine()) != null ){
                if( line.startsWith("#") ){ continue; } // comment line
                String[] lineList = line.trim().split("\t");
                if( lineList.length == 2 ) { // id and statistic
                    ids1.add( lineList[0] ); 
                    ids2.add( lineList[0] );
                    sVals.add( Double.parseDouble( lineList[1]) );
                }
                else if( lineList.length == 1 ) { // only rnk
                    if( !line.trim().isEmpty()) {
                        onlyRnkIds1.add( lineList[0] );
                        onlyRnkIds2.add( lineList[0] );
                    }
                }
                else { throw new IllegalArgumentException("Error in reading rnk file: illegal line in rnk file"); }
            }
        }
        else {
            { throw new IllegalArgumentException("Error in reading rnk file: file does not exist or cannot be opened"); }
        }
        
        if( ids1.isEmpty() && onlyRnkIds1.isEmpty() ){ throw new IllegalArgumentException("Error in reading rnk file: rnk data is empty"); }
        
        if( sVals.size() == 1 ){ 
            throw new IllegalArgumentException("Error in reading rnk file: unable to compare statistics");
        }
        else if( sVals.size() > 1 ) { // id and statistic
            if( !onlyRnkIds1.isEmpty() ){ System.out.println( "Items without a value will be ignored: " + onlyRnkIds1);}
            if( ids2.size() < ids1.size() ){ throw new IllegalArgumentException("Error in reading rnk file: redundant ID"); }
            for(int i = 0; i < ids1.size(); i++) {
                itemRnkBeforeSort.put(ids1.get(i), sVals.get(i));
            }
            if( ids1.size() == 1 ){ throw new IllegalArgumentException("Error in reading rnk file: item number must be more than one"); }
            
            List<Map.Entry> itemRnkList = new ArrayList<Map.Entry>(itemRnkBeforeSort.entrySet());
            Collections.sort(itemRnkList, new Comparator<Map.Entry>() {
                @Override
                public int compare(Map.Entry o1, Map.Entry o2) {
                    return ((Double) o2.getValue()).compareTo((Double) o1.getValue()); // decreasing is TRUE
                }
            });
            for( Map.Entry s : itemRnkList ){
                itemRnk.add((String) s.getKey());
            }
            
            // Report
            CreateReportFileUtility.reportItemRnkParse(itemRnk);
            
            return itemRnk;
        }
        else { // only rnk
            if( onlyRnkIds2.size() < onlyRnkIds1.size() ){ throw new IllegalArgumentException("Error in reading rnk file: redundant ID"); }
            if( onlyRnkIds1.size() == 1 ){ throw new IllegalArgumentException("Error in reading rnk file: item number must be more than one"); }
            // Report
            CreateReportFileUtility.reportItemRnkParse(onlyRnkIds1);
            
            return onlyRnkIds1;
        }
    }
    
    static Map<String, Double> parseRnkpFile(String rppath, boolean FCRRnkpMode) throws FileNotFoundException, IOException {
        
        File rpfile = new File(rppath);
        System.out.println("[ Info ] Reading rnkp file");
        
        // variables for ids and p-values, Map<id, p-value>
        Map<String, Double> itemRnkp = new LinkedHashMap();
        Map<String, Double> itemRnkpBeforeSort = new HashMap();
        List<String> ids1 = new ArrayList();
        Set<String> ids2 = new HashSet(); // for check for redundant IDs
        List<Double> pVals = new ArrayList();
        List<String> onlyRnkIds = new ArrayList();
        
        if(checkBeforeReadfile(rpfile)){
            BufferedReader rpbr = new BufferedReader(new FileReader(rpfile));
            String line;
            while( (line = rpbr.readLine()) != null ){
                if( line.startsWith("#") ){ continue; } // comment line
                String[] lineList = line.trim().split("\t");
                if( lineList.length == 2 ) { // id and p-value
                    ids1.add( lineList[0] ); 
                    ids2.add( lineList[0] );
                    pVals.add( Double.parseDouble(lineList[1]) );
                } 
                else if( lineList.length == 1 ) { // only id
                    if( !line.trim().isEmpty()) { onlyRnkIds.add( lineList[0] ); }
                }
                else { throw new IllegalArgumentException("Error in reading rnkp file: illegal line in rnkp file"); }
            }
        }
        else {
            { throw new IllegalArgumentException("Error in reading rnkp file: file does not exist or cannot be opened"); }
        }
        
        if( ids1.isEmpty() ){ throw new IllegalArgumentException("Error in reading rnkp file: rnkp file data is empty"); }
        if( ids2.size() < ids1.size()){  throw new IllegalArgumentException("Error in reading rnkp file: redundant ID"); }
        if( !onlyRnkIds.isEmpty() ){ System.out.println( "Items without a value will be ignored: " + onlyRnkIds);}
        
        for(int i = 0; i < ids1.size(); i++) {
            itemRnkpBeforeSort.put(ids1.get(i), pVals.get(i));
            
            if(FCRRnkpMode) {
                if(pVals.get(i) < 0 | pVals.get(i) > 1.0){
                    throw new IllegalArgumentException("Error in reading rnkp file: illegal p-value, -cr option is required when using rank-based FCR with rnkp file");
                }
            }
        }
        List<Map.Entry> itemRnkpList = new ArrayList<Map.Entry>(itemRnkpBeforeSort.entrySet());
        Collections.sort(itemRnkpList, new Comparator<Map.Entry>() {
            @Override
            public int compare(Map.Entry o1, Map.Entry o2) {
                return ((Double) o1.getValue()).compareTo((Double) o2.getValue());
            }
        });
        for (Map.Entry s : itemRnkpList) {
            itemRnkp.put((String) s.getKey(), (Double) s.getValue());
        }
        
        // Report
        CreateReportFileUtility.reportItemRnkpParse(itemRnkp);
        
        return itemRnkp;
    }
    
    static List<ItemSetRes> parseSetFile(String spath, boolean gmt) throws FileNotFoundException, IOException {
        
        File sfile = new File(spath);
        System.out.println("[ Info ] Reading set file");
        
        List<ItemSetRes> itemSets;
        if(gmt) {
            itemSets = GSEAModeFileParserUtility.parseGmtFile(sfile);
        }
        else {
            itemSets = GSEAModeFileParserUtility.parseGmxFile(sfile);
        }
        
        // Report
        CreateReportFileUtility.reportItemSetParse(itemSets);
        
        return itemSets;
    }
    
    private static List<ItemSetRes> parseGmtFile(File sfile) throws FileNotFoundException, IOException {
        
        // variables for itemSets
        ArrayList<ItemSetRes> itemSets = new ArrayList();
        Set<String> ids = new HashSet(); // for check for redundant IDs
        
        if(checkBeforeReadfile(sfile)){
            BufferedReader sbr = new BufferedReader(new FileReader(sfile));
            String line;
            while( (line = sbr.readLine()) != null ){
                if( !line.trim().isEmpty() ) {
                    if( line.startsWith("#") ){ continue; } // comment line
                    Set<String> items = new HashSet();
                    String[] words = line.trim().split("\t");
                    if( words.length < 2 ){
                        // ItemSet: items are able to be empty, however itemSet without items is not applied to calculation
                        throw new IllegalArgumentException("Error in reading line in gmt file: set ID and description are required");
                    }
                    for( int i = 2; i < words.length; i++) {
                        items.add(words[i]);
                    }
                    if(!items.isEmpty()) {
                        ItemSetRes itemset = new ItemSetRes( words[0], words[1], items );
                        itemSets.add(itemset);
                        ids.add(words[0]);
                    }
                }
            }
        }
        else {
            { throw new IllegalArgumentException("Error in reading gmt file: file does not exist or cannot be opened"); }
        }
        
        if( itemSets.isEmpty() ){ throw new IllegalArgumentException("Error in reading gmt file: set data is empty"); }
        if( ids.size() < itemSets.size()){  throw new IllegalArgumentException("Error in reading gmt file: redundant set ID"); }
        return itemSets;
    }
    
    private static List<ItemSetRes> parseGmxFile(File sfile) throws FileNotFoundException, IOException {
        
        int maxItemNumPlus2 = 0; // total line numbers
        ArrayList<String[]> lines = new ArrayList();
        if(checkBeforeReadfile(sfile)){
            BufferedReader sbr = new BufferedReader(new FileReader(sfile));
            String line;
            while( (line = sbr.readLine()) != null ){
                if( !line.trim().isEmpty() ) {
                    if( line.startsWith("#") ){ continue; } // comment line
                    String[] words;
                    if(maxItemNumPlus2 < 2){ words = line.trim().split("\t"); }
                    else { words = line.split("\t");}
                    lines.add(words);
                    maxItemNumPlus2++;
                }
            }
        }
        else {
            { throw new IllegalArgumentException("Error in reading gmx file: file does not exist or cannot be opened"); }
        }
        
        if( lines.isEmpty() ){  throw new IllegalArgumentException("Error in reading gmx file: set file is empty");  }
        int itemSetNum = lines.get(0).length;
        if( lines.get(1).length != itemSetNum){ throw new IllegalArgumentException("Error in reading line in gmx file: set ID and description are required"); }
        
        // variables for itemSets
        ArrayList<ItemSetRes> itemSets = new ArrayList();
        ArrayList<ItemSetRes> itemSetsBeforeFilt = new ArrayList(itemSetNum);
        for(int i = 0; i < itemSetNum; i++) {
            itemSetsBeforeFilt.add(new ItemSetRes());
        }
        Set<String> ids = new HashSet(); // for check for redundant IDs
        
        for(int i = 0; i < itemSetNum; i++) {
            if(lines.get(0)[i].isEmpty()){ throw new IllegalArgumentException("Error in reading gmx file: illegal set ID, set ID is empty");  }
            itemSetsBeforeFilt.get(i).setItemSetID( lines.get(0)[i] );
            ids.add( lines.get(0)[i] );
        }
        if( ids.size() < itemSetNum ){ throw new IllegalArgumentException("Error in reading gmx file: redundant set ID"); }
        
        for(int i = 0; i < itemSetNum; i++) {
            if(lines.get(1)[i].isEmpty()){ throw new IllegalArgumentException("Error in reading gmx file: illegal set description, set description is empty");  }
            itemSetsBeforeFilt.get(i).setDescription( lines.get(1)[i] );
        }
        
        for(int i = 0; i < itemSetNum; i++) {
            Set<String> items = new HashSet();
            for(int j = 2; j < maxItemNumPlus2 ; j++) {
                if(i < lines.get(j).length){
                    if(!lines.get(j)[i].isEmpty()){
                        items.add(lines.get(j)[i]);
                    }
                }
            }
            itemSetsBeforeFilt.get(i).setItems(items);
        }
        
        for(ItemSetRes itemSet: itemSetsBeforeFilt) {
            if( !itemSet.getItems().isEmpty() ) {
                itemSets.add(itemSet);
            }
        }
        
        if( itemSets.isEmpty() ){ throw new IllegalArgumentException("Error in reading gmx file: set data is empty"); }
        return itemSets;
    }
    
    // private methods
    private static boolean checkBeforeReadfile(File file) {
        
        if ( file.exists() ){
            if ( file.isFile() && file.canRead() ){
                return true;
            }
        }
        return false;
    }
}
