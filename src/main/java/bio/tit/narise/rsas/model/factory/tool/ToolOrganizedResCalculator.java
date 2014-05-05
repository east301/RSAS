package bio.tit.narise.rsas.model.factory.tool;

import bio.tit.narise.rsas.model.factory.product.Result;
import bio.tit.narise.rsas.model.factory.product.ContriRes;
import bio.tit.narise.rsas.model.factory.product.FCRRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.PValRes;
import bio.tit.narise.rsas.model.factory.product.RSASRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;

/**
 *
 * @author TN
 */
public class ToolOrganizedResCalculator implements Tool {

    private final OrderedItemSetRes orderedItemSet;
    private final RunSumRes resultsOfCalcRunSum;
    private final FCRRes resultsOfCalcFCR;
    private final ContriRes resultsOfCalcContributor;
    private final PValRes resultsOfCalcPVal;
    
    public ToolOrganizedResCalculator(OrderedItemSetRes orderedItemSet, RunSumRes resultsOfCalcRunSum, FCRRes resultsOfCalcFCR, 
                                    ContriRes resultsOfCalcContributor, PValRes resultsOfCalcPVal) {
        
        this.orderedItemSet = orderedItemSet;
        this.resultsOfCalcRunSum = resultsOfCalcRunSum;
        this.resultsOfCalcFCR = resultsOfCalcFCR;
        this.resultsOfCalcContributor = resultsOfCalcContributor;
        this.resultsOfCalcPVal = resultsOfCalcPVal;
    }
    
    @Override
    public Result calc() {
        
        int rankAtMax;
        if(resultsOfCalcRunSum.isPos()) { rankAtMax = resultsOfCalcRunSum.getIndexAtMax() + 1; }
        else { rankAtMax = resultsOfCalcRunSum.getIndexAtMax(); }
        
        RSASRes RSASResultsForAnItemSet = new RSASRes(
                orderedItemSet.getItemSetID(), orderedItemSet.getDescription(), orderedItemSet.getNumOfItems(),
                resultsOfCalcPVal.getpVal(), resultsOfCalcContributor.getFCRValAtThreshold(), resultsOfCalcFCR.getFCRAtMax(),
                resultsOfCalcRunSum.getES(), resultsOfCalcRunSum.getNES(), rankAtMax, 
                resultsOfCalcContributor.getContributors(), resultsOfCalcRunSum.isPos()
                );
        
        return RSASResultsForAnItemSet;
    }
    
}
