package bio.tit.narise.rsas.model.factory.factory;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.RSASRes;
import bio.tit.narise.rsas.model.factory.product.RSASResults;
import bio.tit.narise.rsas.model.factory.tool.ToolOrganizedResCalculator;

/**
 *
 * @author TN
 */
public abstract class Factory {
    
    protected ParsedArgs pargs = null;
    
    public Factory(ParsedArgs pargs){
        this.pargs = pargs;
    }
    
    public RSASResults createResults() {
        RSASResults rsasResults = new RSASResults();
        for(int i = 0; i < Storehouse.setNum; i++) {
            ToolOrganizedResCalculator toolOrganizedResCalculator = new ToolOrganizedResCalculator(
                                Storehouse.orderedItemSets.get(i), 
                                Storehouse.IDandRunSumRes.get(Storehouse.orderedItemSets.get(i).getItemSetID()),
                                Storehouse.IDandFCRRes.get(Storehouse.orderedItemSets.get(i).getItemSetID()),
                                Storehouse.IDandContriRes.get(Storehouse.orderedItemSets.get(i).getItemSetID()),
                                Storehouse.IDandPValRes.get(Storehouse.orderedItemSets.get(i).getItemSetID())
                                );
            RSASRes rsasResultsForAnItemSet = (RSASRes) toolOrganizedResCalculator.calc();
            rsasResults.add(rsasResultsForAnItemSet);
        }
        
        return rsasResults;
    }
}
