package bio.tit.narise.rsas.model.factory.tool;

import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import bio.tit.narise.rsas.model.factory.product.Result;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;

/**
 *
 * @author TN
 */
public class ToolUnweightedRunSumCalculator implements Callable<Map<String, RunSumRes>>, Tool {
    
    private final String itemSetID;
    private final List<String> orderedItems;
    private final List<String> itemRnk;
    private final int N;
    private final int m;
    private final int N_m;
    private final int _m;
    
    public ToolUnweightedRunSumCalculator(OrderedItemSetRes orderedItemSet, List<String> itemRnk, int N) {
        this.itemSetID = orderedItemSet.getItemSetID();
        this.orderedItems = orderedItemSet.getOrderedItems();

        this.itemRnk = itemRnk;

        this.m = orderedItemSet.getNumOfItems();
        this.N = N;
        this.N_m = N - m;
        this._m = - m;
    }
    
    @Override
    public Map<String, RunSumRes> call() throws Exception {
        RunSumRes resultsOfCalcRunSum = (RunSumRes) this.calc();
        Map<String, RunSumRes> IDandResultsOfCalcRunSum = new HashMap();
        IDandResultsOfCalcRunSum.put(this.itemSetID, resultsOfCalcRunSum);
        return IDandResultsOfCalcRunSum;
    }
    
    @Override
    public Result calc() {
        // prepare a vector having N_m and _m values
        int[] setOrNot = new int[N];
        int[] preRunSumVector = new int[N];
        for(int i = 0; i < N; i++) {
            if( orderedItems.contains(itemRnk.get(i)) ) {
                setOrNot[i] = 1;
                preRunSumVector[i] = N_m;
            }
            else {
                setOrNot[i] = 0;
                preRunSumVector[i] = _m;
            }
        }
        
        // createRunSum
        // Note that runSumVector is longer than preRunSumVector and itemRnk
        List<Integer> runSumVector = new ArrayList(preRunSumVector.length + 1);
        int runSum = 0;
        runSumVector.add(runSum); // runSumVector.get(0) = 0
        for(int i = 0; i < preRunSumVector.length; i++) {
            runSum += preRunSumVector[i];
            runSumVector.add(runSum);
        }
        
        // Note that maxIndexForItemRnk is selected by using lastIndexOf()
        int runSumMax = Collections.max(runSumVector);
        int runSumMin = Collections.min(runSumVector);
        
        // obtain maximum deviation (ES) and the index
        int ES;
        int indexAtMax;
        boolean pos;
        if(runSumMax >= -runSumMin){
            ES = runSumMax;
            // this indexAtMax indicates the index of the item at maximum running sum in the itemRnk
            indexAtMax = runSumVector.lastIndexOf(runSumMax) - 1;
            pos = true;
        } else {
            ES = -runSumMin;
            // Note that this indexAtMax indicates the next index of the item at minimum running sum in the itemRnk
            indexAtMax = runSumVector.indexOf(runSumMin);
            pos = false;
        }
        
        // NES
        double NES = (double) ES/(N_m * m);
        
        RunSumRes resultsOfCalcRunSum = new RunSumRes( ES, indexAtMax, pos, NES );
        
        // Report
        try {
            CreateReportFileUtility.reportRunSumCalc(itemSetID, resultsOfCalcRunSum, runSumVector, setOrNot);
        } catch (IOException ex) {
            Logger.getLogger(ToolUnweightedRunSumCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultsOfCalcRunSum;
    }
    
}
