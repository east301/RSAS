package bio.tit.narise.rsas.model.factory.factory;

import java.io.FileNotFoundException;
import bio.tit.narise.rsas.model.factory.tool.ToolRnkpFCRCalculator;
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
public class FCRFiltRnkpModeFactory extends FCRFiltModeFactory {
    
    // protected ParsedArgs pargs = null;
    // protected final List<OrderedItemSetRes> orderedItemSets = new ArrayList();
    // protected int setNum = 0;
    // protected Map<String, RunSumRes> IDandRunSumRes = new HashMap();
    // protected Map<String, PValRes> IDandPValRes = new HashMap();
    // protected Map<String, FCRRes> IDandFCRRes = new HashMap();
    // protected Map<String, ContriRes> IDandContriRes = new HashMap();
    // protected List<String> itemRnk = new ArrayList();
    // protected List<Double> itemP = new ArrayList();
    // protected int N = 0;
    // protected List<ItemSetRes> itemSets = new ArrayList();
    
    public FCRFiltRnkpModeFactory(ParsedArgs pargs){
        super(pargs);
    }
    
    // public static Factory getFactory(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    // public void setParameter(ParsedArgs pargs)
    // public RSASResults createResults()
    
    // set Map<String, ContriRes> IDandContriRes 
    // public void calcContri() throws InterruptedException, ExecutionException, IOException
    
    
    @Override // set List<String> itemRnk (and List<Double> itemRnkp) for GSEAMode
    public void parseRnkOrRnkpFile() throws FileNotFoundException, IOException {
        
        Map<String, Double> itemRnkp = GSEAModeFileParserUtility.parseRnkpFile(pargs.getRppath(), pargs.isFCRRnkpMode());
        for(String item: itemRnkp.keySet()){
            Storehouse.itemRnk.add(item);
            Storehouse.itemP.add(itemRnkp.get(item));
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
        List<ToolRnkpFCRCalculator> processesCalcFCR = new LinkedList();
        for(final OrderedItemSetRes orderedItemSet: Storehouse.orderedItemSets) {
            ToolRnkpFCRCalculator callableFCRResults = new ToolRnkpFCRCalculator(orderedItemSet, 
                    IDandResultsOfCalcRunSum.get(orderedItemSet.getItemSetID()), Storehouse.itemRnk, Storehouse.N, Storehouse.itemP);
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
