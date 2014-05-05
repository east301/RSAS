package bio.tit.narise.rsas.model.factory.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import bio.tit.narise.rsas.model.factory.product.Result;
import bio.tit.narise.rsas.model.factory.product.FCRRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;

/**
 *
 * @author TN
 */
public class ToolRnkpFCRCalculator implements Callable<Map<String, FCRRes>>, Tool {

    private final String itemSetID;
    private final List<String> orderedItems;
    private final boolean pos;
    private final int indexAtMax;
    private final List<String> itemRnk;
    private final int N;
    private final int m;
    private final List<Double> itemP;
    
    public ToolRnkpFCRCalculator(OrderedItemSetRes orderedItemSet, RunSumRes runSumRes, List<String> itemRnk, int N, List<Double> itemP) {
        this.itemSetID = orderedItemSet.getItemSetID();
        this.orderedItems = orderedItemSet.getOrderedItems();
        this.pos = runSumRes.isPos();
        this.indexAtMax = runSumRes.getIndexAtMax();
        this.itemRnk = itemRnk;
        this.N = N;
        this.m = orderedItemSet.getNumOfItems();
        this.itemP = itemP;
    }
    
    @Override
    public Map<String, FCRRes> call() throws Exception {
        FCRRes resultsOfCalcFCR = (FCRRes) calc();
        Map<String, FCRRes> IDandResultsOfCalcFCR = new HashMap();
        IDandResultsOfCalcFCR.put(this.itemSetID, resultsOfCalcFCR);
        
        // Report
        CreateReportFileUtility.reportFCRCalc(itemSetID, IDandResultsOfCalcFCR);
        
        return IDandResultsOfCalcFCR;
    }
    
    @Override
    public Result calc() {
        List<Double> FCRs = new ArrayList(m);
        double FCRAtMax = -1.0;
        int indexOfFCRAtMax = -1;
        
        double count = 0;
        if( pos ) {
            for(int i = 0; i < N; i++) {
                if( orderedItems.contains(itemRnk.get(i)) ) {
                    count++;
                    FCRs.add( ((double)( m * itemP.get(i) ))/((double)( count )) );
                    if( i == indexAtMax) {
                        FCRAtMax = ((double)( m * itemP.get(i) ))/((double)( count ));
                        indexOfFCRAtMax = FCRs.size() - 1;
                    }
                }
            }
        }
        else {
            for(int i = 0; i < N; i++) {
                if( orderedItems.contains(itemRnk.get(i)) ) {
                    count++;
                    FCRs.add( ((double)( m * ( 1 - itemP.get(i) ) ))/((double)( m - count + 1 )) );
                    if( i == indexAtMax) {
                        FCRAtMax = ((double)( m * ( 1 - itemP.get(i) ) ))/((double)( m - count + 1 ));
                        indexOfFCRAtMax = FCRs.size() - 1;
                    }
                }
            }
        }
        
        FCRRes resultsOfCalcFCR = new FCRRes(FCRs, FCRAtMax, indexOfFCRAtMax);
        return resultsOfCalcFCR;
    }
    
}
