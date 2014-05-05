package bio.tit.narise.rsas.model.factory.tool;

import java.io.IOException;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import bio.tit.narise.rsas.model.factory.product.Result;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.PValDPCalcResults;
import bio.tit.narise.rsas.model.factory.product.PValRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;

/**
 *
 * @author TN
 */
public class ToolPValDPCalculator implements Callable<Map<String, PValRes>>, Tool {
    
    private final List<OrderedItemSetRes> itemSetsWithSameItemNum;
    private final BigInteger caseAll;
    private final Map<String, RunSumRes> IDandResultsOfCalcRunSum;
    private final int m;
    private final int N_m;
    private final int _m;
    
    public ToolPValDPCalculator(List<OrderedItemSetRes> itemSetsWithSameItemNum, BigInteger caseAll, 
                                Map<String, RunSumRes> IDandResultsOfCalcRunSum, int N, int key) {
        this.itemSetsWithSameItemNum = itemSetsWithSameItemNum;
        this.caseAll = caseAll;
        this.IDandResultsOfCalcRunSum = IDandResultsOfCalcRunSum;
        this.m = key;
        
        this.N_m = N - m;
        this._m = - m;
    }
    
    @Override
    public Map<String, PValRes> call() throws Exception {
        PValDPCalcResults listOfPValRes = (PValDPCalcResults) calc();
        List<PValRes> listOfResultsOfCalcPVal = listOfPValRes.getListOfPValRes();
        Map<String, PValRes> IDandResultsOfCalcPVal = new HashMap();
        for(int i = 0; i < itemSetsWithSameItemNum.size(); i++) {
            IDandResultsOfCalcPVal.put(itemSetsWithSameItemNum.get(i).getItemSetID(), listOfResultsOfCalcPVal.get(i));
        }
        System.out.println("Size " + m + ": Completed!");
        
        return IDandResultsOfCalcPVal;
    }
    
    @Override
    public Result calc() {
        
        int setNum = itemSetsWithSameItemNum.size();
        int[] listOfES = new int[setNum];
        for(int i = 0; i < setNum; i++) {
            String id = itemSetsWithSameItemNum.get(i).getItemSetID();
            listOfES[i] = IDandResultsOfCalcRunSum.get(id).getES();
        }
        
        BigInteger[][] row = new BigInteger[setNum][m+1];
        
        // initialization, i=0
        int threshold;
        for(int j = 0; j <= m; j++) {
            threshold = N_m*j;
            for(int s = 0; s < setNum; s++) {
                if(threshold < listOfES[s]){ row[s][j] = BigInteger.ONE; }
                else { row[s][j] = BigInteger.ZERO; }
            }
        }
        // i = 1~N_m
        for( int i = 1; i <= N_m; i++ ) {
            for( int j = 0; j <= m; j++ ) {
                
                threshold = N_m*j + _m*i;
                for( int s = 0; s < setNum; s++ ) {

                    if( j==0 ){
                        if( threshold <= -listOfES[s] ) {
                            if ( row[s][j] == BigInteger.ZERO ) { continue; }
                            else { row[s][j] = BigInteger.ZERO; continue; }
                        }
                    }
                    else {
                        if( threshold >= listOfES[s] ) { continue; }
                        else if( threshold <= -listOfES[s] ) {
                            if ( row[s][j] == BigInteger.ZERO ) { continue; }
                            else { row[s][j] = BigInteger.ZERO; continue; }
                        }
                        else { row[s][j] = row[s][j].add(row[s][j-1]); }
                    }
                }
            }
        }
        
        BigInteger[] caseSmallerES = new BigInteger[setNum];
        for(int s = 0; s < setNum; s++) { 
            caseSmallerES[s] = row[s][m]; 
            //System.out.println(caseSmallerES[s]);
        }
        
        // calc p-values
        int precision = 20; // the value of 20 could be increased up to around 308 (Double.MAX_VALUE: 1.7976931348623157E308).
        double[] pVals = new double[setNum];
        String caseAllString = caseAll.toString();
        int caseAllLength = caseAllString.length(); 
        
        if(caseAllLength < precision) {
            for(int s = 0; s < setNum; s++) {
                pVals[s] = (  1.0 - caseSmallerES[s].doubleValue()/caseAll.doubleValue()  )/2.0 ;
            }
        }
        else {
            for(int s = 0; s < setNum; s++) {
                
                String caseSmallerString = caseSmallerES[s].toString();
                int diff = caseAllLength - caseSmallerString.length();
            
                if(diff >= precision) { pVals[s] = 0.5; }
                else {
                    if(caseSmallerString.length() < precision) {
                        pVals[s] = (  1.0 - caseSmallerES[s].doubleValue()/( Double.parseDouble( caseAllString.substring(0, precision) ) * Math.pow(10, diff - (precision - caseSmallerString.length())) )  )/2.0;
                    }
                    else {
                        pVals[s] = (  1.0 - Double.parseDouble( caseSmallerString.substring(0, precision) )/( Double.parseDouble( caseAllString.substring(0, precision) ) * Math.pow(10, diff) ) )/2.0;
                    }
                }
            }
        }
        
        PValDPCalcResults listOfPValRes = new PValDPCalcResults();
        for(int s = 0; s < setNum; s++){
            PValRes resultsOfCalcPVal = new PValRes(pVals[s]);
            listOfPValRes.add(resultsOfCalcPVal);
        }
        
        // Report, itemSetID, itemSetSize, P-value, SmallerCase, AllCase
        try {
            CreateReportFileUtility.reportPValCalc(itemSetsWithSameItemNum, m, listOfPValRes.getListOfPValRes(), caseSmallerES, caseAll);
        } catch (IOException ex) {
            Logger.getLogger(ToolPValDPCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listOfPValRes;
    }
    
}
