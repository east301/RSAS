package bio.tit.narise.rsas.model.factory.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.ContriRes;
import bio.tit.narise.rsas.model.factory.product.FCRRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;
import bio.tit.narise.rsas.model.factory.tool.ToolContriCalculator;

/**
 *
 * @author TN
 */
public abstract class FCRFiltModeFactory extends Factory {
    
    // protected ParsedArgs pargs = null;
    
    
    public FCRFiltModeFactory(ParsedArgs pargs){
        super(pargs);
    }
    
    // public static Factory getFactory(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    // public void setParameter(ParsedArgs pargs)
    // public RSASResults createResults()
    
    // set List<String> itemRnk (and List<Double> itemRnkp) for GSEAMode
    public abstract void parseRnkOrRnkpFile() throws FileNotFoundException, IOException ;
    // set Map<String, FCRRes> IDandFCRRes
    public abstract void calcFCR() throws InterruptedException, ExecutionException, IOException ;
    
    
    // set Map<String, ContriRes> IDandContriRes
    public void calcContri() throws InterruptedException, ExecutionException, IOException {
        
        System.out.println("[ Info ] Calculating contributors");
        
        // Report
        CreateReportFileUtility.reportContriCalcStart(pargs.getFcr());
        
        final Map<String, FCRRes> IDandResultsOfCalcFCR = Storehouse.IDandFCRRes;
        final Map<String, RunSumRes> IDandResultsOfCalcRunSum = Storehouse.IDandRunSumRes;
        ExecutorService threadPoolCalcContributor = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<ToolContriCalculator> processesCalcContributor = new LinkedList();
        for(final OrderedItemSetRes orderedItemSet: Storehouse.orderedItemSets) {
            final String itemSetID = orderedItemSet.getItemSetID();
            ToolContriCalculator callableContributorResults = new ToolContriCalculator(
                    itemSetID, orderedItemSet.getOrderedItems(), IDandResultsOfCalcFCR.get(itemSetID), 
                    IDandResultsOfCalcRunSum.get(itemSetID).isPos(), pargs.getFcr());
            processesCalcContributor.add(callableContributorResults);
        }
        List<Future<Map<String, ContriRes>>> IDandResultsOfCalcContributorFutures = new LinkedList();
        try {
            IDandResultsOfCalcContributorFutures = threadPoolCalcContributor.invokeAll(processesCalcContributor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolCalcContributor.shutdown();
        }
        Map<String, ContriRes> IDandResultsOfCalcContributor = new HashMap(Storehouse.setNum);
        for( Future<Map<String, ContriRes>> IDandResultsOfCalcContributorFuture: IDandResultsOfCalcContributorFutures) {
            IDandResultsOfCalcContributor.putAll(IDandResultsOfCalcContributorFuture.get());
        }
        
        Storehouse.IDandContriRes = IDandResultsOfCalcContributor;
        
        // Report
        CreateReportFileUtility.reportContriCalcEnd(pargs.getFcr());
    }
}
