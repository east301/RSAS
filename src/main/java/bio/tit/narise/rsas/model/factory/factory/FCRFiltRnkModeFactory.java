package bio.tit.narise.rsas.model.factory.factory;

import java.io.FileNotFoundException;
import bio.tit.narise.rsas.model.factory.tool.ToolRnkFCRCalculator;
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
import bio.tit.narise.rsas.model.factory.product.FCRRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;

/**
 *
 * @author TN
 */
public class FCRFiltRnkModeFactory extends FCRFiltModeFactory {

    // protected ParsedArgs pargs = null;

    
    public FCRFiltRnkModeFactory(ParsedArgs pargs){
        super(pargs);
    }
    
    // public static Factory getFactory(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    // public void setParameter(ParsedArgs pargs)
    // public RSASResults createResults()
    
    // set Map<String, ContriRes> IDandContriRes 
    // public void calcContri() throws InterruptedException, ExecutionException, IOException
    
    
    @Override // set List<String> itemRnk (and List<Double> itemRnkp) for GSEAMode
    public void parseRnkOrRnkpFile() throws FileNotFoundException, IOException {
        if(pargs.getRpath().isEmpty()){
            Map<String, Double> itemRnkp = GSEAModeFileParserUtility.parseRnkpFile(pargs.getRppath(), pargs.isFCRRnkpMode());
            for(String item: itemRnkp.keySet()){
                Storehouse.itemRnk.add(item);
            }
        }
        else{
            Storehouse.itemRnk = GSEAModeFileParserUtility.parseRnkFile(pargs.getRpath());
        }
        Storehouse.N = Storehouse.itemRnk.size();
    }
    
    @Override // set Map<String, FCRRes> IDandFCRRes
    public void calcFCR() throws InterruptedException, ExecutionException, IOException {
        
        System.out.println("[ Info ] Calculating FCR");
        
        // Report
        CreateReportFileUtility.reportFCRCalcStart();
        
        final Map<String, RunSumRes> IDandResultsOfCalcRunSum = Storehouse.IDandRunSumRes;
        ExecutorService threadPoolCalcFCR = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<ToolRnkFCRCalculator> processesCalcFCR = new LinkedList();
        for(final OrderedItemSetRes orderedItemSet: Storehouse.orderedItemSets) {
            ToolRnkFCRCalculator callableFCRResults = new ToolRnkFCRCalculator(orderedItemSet, 
                    IDandResultsOfCalcRunSum.get(orderedItemSet.getItemSetID()), Storehouse.itemRnk, Storehouse.N);
            processesCalcFCR.add(callableFCRResults);
        }
        List<Future<Map<String, FCRRes>>> IDandResultsOfCalcFCRFutures = new LinkedList();
        try {
            IDandResultsOfCalcFCRFutures = threadPoolCalcFCR.invokeAll(processesCalcFCR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolCalcFCR.shutdown();
        }
        final Map<String, FCRRes> IDandResultsOfCalcFCR = new HashMap(Storehouse.setNum);
        for( Future<Map<String, FCRRes>> IDandResultsOfCalcFCRFuture: IDandResultsOfCalcFCRFutures) {
            final Map<String, FCRRes> AnIDandResultsOfCalcFCR = IDandResultsOfCalcFCRFuture.get();
            IDandResultsOfCalcFCR.putAll(AnIDandResultsOfCalcFCR);
        }
        
        Storehouse.IDandFCRRes = IDandResultsOfCalcFCR;
        
        // Report
        CreateReportFileUtility.reportFCRCalcEnd();
    }
}
