package bio.tit.narise.rsas.model.factory.tool;

import java.io.IOException;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import bio.tit.narise.rsas.model.factory.product.Result;
import bio.tit.narise.rsas.model.factory.product.ContriRes;
import bio.tit.narise.rsas.model.factory.product.FCRRes;

/**
 *
 * @author TN
 */
public class ToolContriCalculator implements Callable<Map<String, ContriRes>>, Tool {
    
    private final String itemSetID;
    private final List<String> orderedItems;
    private final List<Double> FCRs;
    private final double FCRAtMax;
    private final int indexOfFCRAtMax;
    private final boolean pos;
    private double fcr;
    
    public ToolContriCalculator(String itemSetID, List<String> orderedItems, FCRRes FCRRes, boolean pos, double fcr) {
        this.itemSetID = itemSetID;
        this.orderedItems = orderedItems;
        this.FCRs = FCRRes.getFCRs();
        this.FCRAtMax = FCRRes.getFCRAtMax();
        this.indexOfFCRAtMax = FCRRes.getIndexOfFCRAtMax();
        this.pos = pos;
        this.fcr = fcr;
    }
    
    @Override
    public Map<String, ContriRes> call() throws Exception {
        ContriRes resultsOfCalcContributor = (ContriRes) calc();
        Map<String, ContriRes> IDandResultsOfCalcContributor = new HashMap();
        IDandResultsOfCalcContributor.put(itemSetID, resultsOfCalcContributor);
        
        return IDandResultsOfCalcContributor;
    }
    
    @Override
    public Result calc() {
        
        Double FCRValAtThreshold = FCRAtMax;
        ContriRes resultsOfCalcContributor;
        ContriRes resultsOfCalcContributorFilted;
        
        List<String> contributors = new ArrayList();
        if(pos) {
            for(int i = 0; i <= indexOfFCRAtMax; i++) {
                contributors.add(orderedItems.get(i));
            }
        }
        else {
            for(int i = indexOfFCRAtMax; i < FCRs.size(); i++) {
                contributors.add(orderedItems.get(i));
            }
        }
        
        resultsOfCalcContributor = new ContriRes( contributors, FCRValAtThreshold );
        
        if( fcr == -1.0 ) { // if fcr was not specified

            // Report
            try {
                CreateReportFileUtility.reportContriCalcWithoutFilt(itemSetID, resultsOfCalcContributor);
            } catch (IOException ex) {
                Logger.getLogger(ToolContriCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return resultsOfCalcContributor;
        }
        else { // if fcr was specified, recalculate threshold indexs
            
            double fcrS = fcr/100;
            int thresholdIndex = -1;
            FCRValAtThreshold = null;
            
            if(pos) {
                for(int i = indexOfFCRAtMax; i >= 0; i--) {
                    if( FCRs.get(i) < fcrS ){
                        thresholdIndex = i;
                        break;
                    }
                }
            }
            else {
                for(int i = indexOfFCRAtMax; i < FCRs.size(); i++) {
                    if( FCRs.get(i) < fcrS ){
                        thresholdIndex = i;
                        break;
                    }
                }
            }
            
            // get contributors by using recalculated thresholds
            List<String> filteredContributors = new ArrayList();
            
            if(thresholdIndex != -1){
                FCRValAtThreshold = FCRs.get(thresholdIndex);
                if(pos) {
                    for(int i = 0; i <= thresholdIndex; i++) {
                        filteredContributors.add( orderedItems.get(i) );
                    }
                }
                else {
                    for(int i = thresholdIndex; i < FCRs.size(); i++) {
                        filteredContributors.add( orderedItems.get(i) );
                    }
                }
            }
            
            // return
            resultsOfCalcContributorFilted = new ContriRes( filteredContributors, FCRValAtThreshold );
            
            // Report
            try {
                CreateReportFileUtility.reportContriCalcWithFilt(itemSetID, resultsOfCalcContributor, resultsOfCalcContributorFilted);
            } catch (IOException ex) {
                Logger.getLogger(ToolContriCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return resultsOfCalcContributorFilted;
        }
        
    }
    
}
