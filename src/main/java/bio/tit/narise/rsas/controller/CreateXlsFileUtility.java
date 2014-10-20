package bio.tit.narise.rsas.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.RSASRes;
import bio.tit.narise.rsas.model.factory.product.RSASResults;

/**
 *
 * @author TN
 */
public class CreateXlsFileUtility {
    
    // this is an utility class
    private CreateXlsFileUtility(){ throw new UnsupportedOperationException(); }
    
    static void createSaveFile(ParsedArgs pargs) throws IOException {
        
        File ofile = new File(pargs.getOpath());
        boolean append = pargs.isAppend();
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile, append)))) {
            if(append){ pw.println(); } else { pw.print(""); }
        }
    }
    
    static void saveResults(RSASResults results, ParsedArgs pargs) throws IOException {
        
        System.out.println("[ Info ] Saving results");
        
        double pVal = pargs.getpVal();
        double fdr = pargs.getFdr();
        
        if(pVal != 110.0 | fdr != -1.0){ results = filtResults(results, pVal, fdr); }
        
        results.sortWithNES();
        File ofile = new File(pargs.getOpath());
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofile, true)))) {
            
            pw.println("[ Enriched at the top ]");
            pw.println("ID\tDescription\tSize\tP-value\tFCR' at threshold\tFCR' at max\tES\tNES\tRank at max\tContributors\tSize of contributors");
            
            for(RSASRes each: results.getResultsPos()) {
                if(each.getFcrValAtThreshold() != null) {
                    pw.print(each.getItemSetID()+"\t"+each.getDescription()+"\t"+each.getNumOfItems()+"\t"+each.getpVal()+"\t"+
                                    each.getFcrValAtThreshold()+"\t"+each.getFcrValAtMax()+"\t"+each.getES()+"\t"+each.getNES()+"\t"+each.getRankAtMax()+"\t");
                } else {
                    pw.print(each.getItemSetID()+"\t"+each.getDescription()+"\t"+each.getNumOfItems()+"\t"+each.getpVal()+"\t"+
                                    "\t"+each.getFcrValAtMax()+"\t"+each.getES()+"\t"+each.getNES()+"\t"+each.getRankAtMax()+"\t");
                }
                int contriSizeMinusOne = each.getContributors().size() - 1;
                if(contriSizeMinusOne > -1) {
                    for(int i = 0; i < contriSizeMinusOne ; i++){
                        pw.print(each.getContributors().get(i) + ", ");
                    }
                    pw.print(each.getContributors().get(contriSizeMinusOne)+"\t");
                }
                else { pw.print("\t"); }
                pw.println(contriSizeMinusOne + 1);
            }
            
            pw.println();
            pw.println("[ Enriched at the bottom ]");
            pw.println("ID\tDescription\tSize\tP-value\tFCR' at threshold\tFCR' at max\tES\tNES\tRank at max\tContributors\tSize of contributors");
            
            for(RSASRes each: results.getResultsNeg()) {
                if(each.getFcrValAtThreshold() != null) {
                    pw.print(each.getItemSetID()+"\t"+each.getDescription()+"\t"+each.getNumOfItems()+"\t"+each.getpVal()+"\t"+
                                    each.getFcrValAtThreshold()+"\t"+each.getFcrValAtMax()+"\t"+each.getES()+"\t"+each.getNES()+"\t"+each.getRankAtMax()+"\t");
                } else {
                    pw.print(each.getItemSetID()+"\t"+each.getDescription()+"\t"+each.getNumOfItems()+"\t"+each.getpVal()+"\t"+
                                    "\t"+each.getFcrValAtMax()+"\t"+each.getES()+"\t"+each.getNES()+"\t"+each.getRankAtMax()+"\t");
                }
                int contriSizeMinusOneRev = each.getContributors().size() - 1;
                if(contriSizeMinusOneRev > -1) {
                    for(int i = 0; i < contriSizeMinusOneRev ; i++){
                        pw.print(each.getContributors().get(i) + ", ");
                    }
                    pw.print(each.getContributors().get(contriSizeMinusOneRev)+"\t");
                }
                else { pw.print("\t"); }
                pw.println(contriSizeMinusOneRev + 1);
            }
        }
    }
    
    static private RSASResults filtResults(RSASResults results, double pVal, double fdr) {
        
        results.sortWithPVal();
        
        List<RSASRes> resultsPos = results.getResultsPos();
        List<RSASRes> resultsNeg = results.getResultsNeg();
        int setNumPos = results.getSetNumPos();
        int setNumNeg = results.getSetNumNeg();
        double setNumAll = setNumPos + setNumNeg;
        double pValAtThisRank;
        double pValRevAtThisRank;
        boolean fdrCut = true;
        boolean fdrCutRev = true;
        
        if(pVal == 110.0){  } else { pVal = pVal/100; }
        if(fdr == -1.0){ fdrCut = false; fdrCutRev = false; } else { fdr = fdr/100; }
        
        for(int i = setNumPos - 1; i > -1; i--) {
            pValAtThisRank = resultsPos.get(i).getpVal();
            
            if( pValAtThisRank > pVal ){
                resultsPos.remove(i);
            }
            else if(fdrCut){
                if( pValAtThisRank > ((double)(i+1)/setNumAll)*fdr ){
                    resultsPos.remove(i);
                }
                else {
                    fdrCut = false;
                }
            }
        }
        for(int i = setNumNeg - 1; i > -1; i--) {
            pValRevAtThisRank = resultsNeg.get(i).getpVal();

            if(pValRevAtThisRank > pVal ){
                resultsNeg.remove(i);
            }
            else if(fdrCutRev){
                if( pValRevAtThisRank > ((double)(i+1)/setNumAll)*fdr ){
                    resultsNeg.remove(i);
                }
                else {
                    fdrCutRev = false;
                }
            }
        }
        
        RSASResults selectedResults = new RSASResults(resultsPos, resultsNeg);
        return selectedResults;
    }
}
