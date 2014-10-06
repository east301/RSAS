package bio.tit.narise.rsas.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import bio.tit.narise.rsas.model.factory.product.ItemSetRes;
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
// Report
public class CreateReportFileUtility {
    
    private static String oreppath = "";
    private static boolean wor = false;
    
    // this is an utility class
    private CreateReportFileUtility(){ throw new UnsupportedOperationException(); }
    
    // for test methods
    public static void createReportFile(String oreppathTest, Boolean appendTest) throws IOException {
        
        oreppath = oreppathTest;
        File orepfile = new File(oreppath);
        boolean append = appendTest;
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, append)))) {
            if(append){ pw.println(); } else { pw.print(""); }
            pw.println("<REPORT>");
        }
    }
    
    static void createReportFile(ParsedArgs pargs) throws IOException {
        
        wor = pargs.isWor();
        if(wor){ return; }
        
        oreppath = pargs.getOreppath();
        File orepfile = new File(oreppath);
        boolean append = pargs.isAppend();

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, append)))) {
            if(append){ pw.println(); } else { pw.print(""); }
            pw.println("<REPORT>");
        }
    }
    
    static void closeReportFile() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("</REPORT>");
        }
    }
    
    public static void reportArgs(ParsedArgs pargs) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<ARGS>");
            
            if(pargs.isGseaMode()){ pw.println("<MODE>GSEA</MODE>"); } else { pw.println("<MODE>FILT</MODE>"); }
            
            if(pargs.isFCRRnkMode()){ pw.println("<FCR_FILTMODE>FCR_RNK</FCR_FILTMODE>"); } 
            else { pw.println("<FCR_FILTMODE>FCR_RNKP</FCR_FILTMODE>"); }
            
            if(pargs.isGseaMode()) {
                if(!pargs.getRpath().isEmpty()){ pw.println("<RNKFILE>" + pargs.getRpath() + "</RNKFILE>"); } 
                else if(!pargs.getRppath().isEmpty()) { pw.println("<RNKPFILE>" + pargs.getRppath() + "</RNKPFILE>"); }
                
                pw.println("<SETFILE>" + pargs.getSpath() + "</SETFILE>");
            }
            else if(pargs.isFiltMode()) {
                pw.println("<INPUT_REPFILE>" + pargs.getReppath() + "</INPUT_REPFILE>");
            }
            
            pw.println("<OUTPUT_XLSFILE>" + pargs.getOpath() + "</OUTPUT_XLSFILE>");
            pw.println("<OUTPUT_REPFILE>" + pargs.getOreppath() + "</OUTPUT_REPFILE>");
            pw.print("<THRESHOLD");
            if(pargs.getpVal() != 110.0){ pw.print(" PVALUE=\"" + pargs.getpVal() + "%\""); }
            if(pargs.getFdr() != -1.0){ pw.print(" FDR=\"" + pargs.getFdr() + "%\""); }
            if(pargs.getFcr() != -1.0){ pw.print(" FCR=\"" + pargs.getFcr() + "%\""); }
            pw.println("></THRESHOLD>");
            pw.println("</ARGS>");
        }
    }
    
    public static void reportRepFileLine(String line) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println(line);
        }
    }
    
    public static void reportItemRnkParse(List<String> itemRnk) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<ITEMRNK>");
            StringBuilder sb = new StringBuilder();
            for(String item: itemRnk){ sb.append(item).append("\t"); }
            pw.println(sb.toString().trim());
            pw.println("</ITEMRNK>");
        }
    }
    
    public static void reportItemRnkpParse(Map<String, Double> itemRnkp) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<ITEMRNKP>");
            StringBuilder sbi = new StringBuilder();
            StringBuilder sbp = new StringBuilder();
            for( String item: itemRnkp.keySet() ){ 
                sbi.append(item).append("\t");
                sbp.append(itemRnkp.get(item).toString()).append("\t");
            }
            pw.println(sbi.toString().trim());
            pw.println(sbp.toString().trim());
            pw.println("</ITEMRNKP>");
        }
    }
    
    public static void reportItemSetParse(List<ItemSetRes> itemSets) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<ITEMSET>");
            for( ItemSetRes s: itemSets ){ pw.println( s.toString() ); }
            pw.println("</ITEMSET>");
        }
    }
    
    public static void reportOrderedItemSetCalc(List<OrderedItemSetRes> orderedItemSets) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<ORDERED_ITEMSET>");
            for( OrderedItemSetRes s: orderedItemSets ){ pw.println( s.toString() ); }
            pw.println("</ORDERED_ITEMSET>");
        }
    }
    
    public static void reportRunSumCalcStart() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<RUNNINGSUM><!-- itemSetID, ES, NES, RankAtMax, isPositive, RankAndRunningSum -->");
        }
    }
    public static void reportRunSumCalcEnd() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("</RUNNINGSUM>");
        }
    }
    public static synchronized void reportRunSumCalc(String itemSetID, RunSumRes resultsOfCalcRunSum, List<Integer> runSumVector, int[] setOrNot) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            // itemSetID, ES, RankAtMax, Positive, RankAndRunningSum
            pw.print(itemSetID + "\t");
            pw.print(resultsOfCalcRunSum.getES() + "\t");
            pw.print(resultsOfCalcRunSum.getNES() + "\t");
            if(resultsOfCalcRunSum.isPos()){
                pw.print(Integer.toString(resultsOfCalcRunSum.getIndexAtMax() + 1) + "\t");
            }
            else {
                pw.print(Integer.toString(resultsOfCalcRunSum.getIndexAtMax()) + "\t");
            }    
            pw.print(Boolean.toString(resultsOfCalcRunSum.isPos()) + "\t");
            StringBuilder rankAndRunSum = new StringBuilder();
            for(int i = 0; i < setOrNot.length; i++) {
                if(setOrNot[i] == 1){
                    rankAndRunSum.append(Integer.toString(i+1)).append(",").append(runSumVector.get(i+1).toString()).append("\t");
                }
            }
            pw.println(rankAndRunSum.toString().trim());
        }
    }
    
    public static void reportPValCalcStart() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<PVALUE><!-- itemSetID, itemSetSize, P-value, SmallerCase, AllCase -->" );
        }
    }
    public static void reportPValCalcEnd() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("</PVALUE>" );
        }
    }
    public static synchronized void reportPValCalc(List<OrderedItemSetRes> itemSetsWithSameItemNum, 
            int m, List<PValRes> listOfPValRes, BigInteger[] caseSmallerES, BigInteger caseAll) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            
            // itemSetID, itemSetSize, P-value, SmallerCase, AllCase
            for(int i = 0; i < itemSetsWithSameItemNum.size(); i++) {
                
                String itemSetID = itemSetsWithSameItemNum.get(i).getItemSetID();
                StringBuilder sb = new StringBuilder();
                sb.append(itemSetID).append("\t").append(Integer.toString(m)).append("\t")
                                .append(Double.toString(listOfPValRes.get(i).getpVal())).append("\t")
                                .append(caseSmallerES[i].toString()).append("\t")
                                .append(caseAll.toString()).append("\t");
                pw.println(sb.toString().trim());
            }
        }
    }
    
    public static void reportFCRCalcStart() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("<FCR><!-- itemSetID, FCR'AtMaxES, indexOfFCR'AtMaxES, FCR's -->");
        }
    }
    public static void reportFCRCalcEnd() throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            pw.println("</FCR>" );
        }
    }
    public static synchronized void reportFCRCalc(String itemSetID, Map<String, FCRRes> IDandResultsOfCalcFCR) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            // itemSetID, FCRAtMax, indexOfFCRAtMax, FCRs
            pw.print(itemSetID + "\t");
            pw.print(IDandResultsOfCalcFCR.get(itemSetID).getFCRAtMax() + "\t");
            pw.print(IDandResultsOfCalcFCR.get(itemSetID).getIndexOfFCRAtMax() + "\t");
            StringBuilder sb = new StringBuilder();
            for(Double contri: IDandResultsOfCalcFCR.get(itemSetID).getFCRs()){
                sb.append(contri.toString()).append("\t");
            }
            pw.println(sb.toString().trim());
        }
    }
    
    public static void reportContriCalcStart(double fcr) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        if(fcr == -1.0) {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
                pw.println("<CONTRIBUTORS_WITHOUT_FCRFILT><!-- itemSetID, FCR', Contributors -->");
            }
        }
        else {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
                pw.println("<CONTRIBUTORS_WITHOUT_AND_WITH_FCRFILT><!-- itemSetID, FCR', Contributors -->");
            }
        }
    }
    public static void reportContriCalcEnd(double fcr) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        if(fcr == -1.0) {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
                pw.println("</CONTRIBUTORS_WITHOUT_FCRFILT>");
            }
        }
        else {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
                pw.println("</CONTRIBUTORS_WITHOUT_AND_WITH_FCRFILT>");
            }
        }
    }
    public static synchronized void reportContriCalcWithoutFilt(String itemSetID, ContriRes resultsOfCalcContributor) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            // itemSetID, FCRThreshold, Contributors
            pw.print(itemSetID + "\t");
            pw.print(resultsOfCalcContributor.getFCRValAtThreshold() + "\t");
            StringBuilder sb = new StringBuilder();
            for(String contri: resultsOfCalcContributor.getContributors()){
                sb.append(contri).append("\t");
            }
            pw.println(sb.toString().trim());
            pw.println();
        }
    }
    public static synchronized void reportContriCalcWithFilt(String itemSetID, ContriRes resultsOfCalcContributor, ContriRes resultsOfCalcContributorFilted) throws IOException {
        if(wor){ return; }
        
        File orepfile = new File(oreppath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(orepfile, true)))) {
            
            // itemSetID, FCRThreshold, Contributors
            pw.print(itemSetID + "\t");
            pw.print(resultsOfCalcContributor.getFCRValAtThreshold() + "\t");
            StringBuilder sb = new StringBuilder();
            for(String contri: resultsOfCalcContributor.getContributors()){
                sb.append(contri).append("\t");
            }
            pw.println(sb.toString().trim());
            // itemSetID, FCRThreshold, Contributors
            pw.print(itemSetID + "\t");
            pw.print(resultsOfCalcContributorFilted.getFCRValAtThreshold() + "\t");
            StringBuilder sbf = new StringBuilder();
            for(String contri: resultsOfCalcContributorFilted.getContributors()){
                sbf.append(contri).append("\t");
            }
            pw.println(sbf.toString().trim());
        }
    }
}
