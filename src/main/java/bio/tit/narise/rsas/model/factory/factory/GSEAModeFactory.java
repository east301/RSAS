package bio.tit.narise.rsas.model.factory.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.ChooseRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.PValRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;
import bio.tit.narise.rsas.model.factory.tool.ToolCombinationCalculator;
import bio.tit.narise.rsas.model.factory.tool.ToolOrderedItemSetCalculator;
import bio.tit.narise.rsas.model.factory.tool.ToolPValDPCalculator;
import bio.tit.narise.rsas.model.factory.tool.ToolUnweightedRunSumCalculator;

/**
 *
 * @author TN
 */
// unweighted GSEA using dynamic programming
public class GSEAModeFactory extends Factory {
    
    public GSEAModeFactory(ParsedArgs pargs){
        super(pargs);
    }
    
    // public static Factory getFactory(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    // public void setParameter(ParsedArgs pargs)
    // public RSASResults createResults()
    
    
    // set List<ItemSetRes> itemSets
    public void parseSetFile() throws FileNotFoundException, IOException {
        Storehouse.itemSets = GSEAModeFileParserUtility.parseSetFile(pargs.getSpath(), pargs.isGmt());
    }
    
    // set List<OrderedItemSetRes> orderedItemSets
    public void calcOrderedItemSet() throws IOException {
        if(pargs.getMax() == -1){
            pargs.setMax(Storehouse.itemRnk.size()/2);
        }
        
        for(int i = 0; i < Storehouse.itemSets.size(); i++) {
            ToolOrderedItemSetCalculator orderedItemSetCalculator = new ToolOrderedItemSetCalculator(Storehouse.itemRnk, Storehouse.itemSets.get(i), Storehouse.N);
            final OrderedItemSetRes orderedItemSet = orderedItemSetCalculator.calc();
            if( orderedItemSet.getNumOfItems() >= pargs.getMin() && orderedItemSet.getNumOfItems() <= pargs.getMax()) {
                Storehouse.orderedItemSets.add( orderedItemSet );
                Storehouse.setNum++;
            }
        }
        
        // Report
        CreateReportFileUtility.reportOrderedItemSetCalc(Storehouse.orderedItemSets);
    }
    
    // set Map<String, RunSumRes> IDandRunSumRes
    public void calcRunSum() throws InterruptedException, ExecutionException, IOException {
        
        System.out.println("[ Info ] Calculating running sum");
        
        // Report
        CreateReportFileUtility.reportRunSumCalcStart();
        
        ExecutorService threadPoolCalcRunSum = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<ToolUnweightedRunSumCalculator> processesCalcRunSum = new LinkedList();
        for(final OrderedItemSetRes orderedItemSet: Storehouse.orderedItemSets) {
            ToolUnweightedRunSumCalculator callableRunSumResults = new ToolUnweightedRunSumCalculator(orderedItemSet, Storehouse.itemRnk, Storehouse.N);
            processesCalcRunSum.add(callableRunSumResults);
        }
        List<Future<Map<String, RunSumRes>>> IDandResultsOfCalcRunSumFutures = new LinkedList();
        try {
            IDandResultsOfCalcRunSumFutures = threadPoolCalcRunSum.invokeAll(processesCalcRunSum);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolCalcRunSum.shutdown();
        }
        final Map<String, RunSumRes> IDandResultsOfCalcRunSum = new HashMap(Storehouse.setNum);
        for( Future<Map<String, RunSumRes>> IDandResultsOfCalcRunSumFuture: IDandResultsOfCalcRunSumFutures) {
            final Map<String, RunSumRes> AnIDandResultsOfCalcRunSum = IDandResultsOfCalcRunSumFuture.get();
            IDandResultsOfCalcRunSum.putAll(AnIDandResultsOfCalcRunSum);
        }
        
        Storehouse.IDandRunSumRes = IDandResultsOfCalcRunSum;
        
        // Report
        CreateReportFileUtility.reportRunSumCalcEnd();
    }
    
    // set Map<String, PValRes> IDandPValRes
    public void calcPVal() throws InterruptedException, ExecutionException, IOException {
    
        System.out.println("[ Info ] Calculating p-values");
        
        // Report
        CreateReportFileUtility.reportPValCalcStart();
        
        // prepare a list of numbers of items in all sets
        Set<Integer> listOfNumOfItems = new HashSet();
        for(OrderedItemSetRes orderedItemSet: Storehouse.orderedItemSets) {
            listOfNumOfItems.add( orderedItemSet.getNumOfItems() );
        }
        // calculate choose(N, m)
        ExecutorService threadPoolChoose = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<ToolCombinationCalculator> processesChoose = new LinkedList();
        for(final int m: listOfNumOfItems) {
            ToolCombinationCalculator callableChooseResult = new ToolCombinationCalculator(Storehouse.N, m);
            processesChoose.add(callableChooseResult);
        }
        List<Future<Map<Integer, ChooseRes>>> listOfChooseResultsFuture = new LinkedList();
        try {
            listOfChooseResultsFuture = threadPoolChoose.invokeAll(processesChoose);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolChoose.shutdown();
        }
        Map<Integer, ChooseRes> listOfChooseResults = new HashMap();
        for( Future<Map<Integer, ChooseRes>> chooseResultFuture: listOfChooseResultsFuture) {
            Map<Integer, ChooseRes> chooseResult = chooseResultFuture.get();
            listOfChooseResults.putAll(chooseResult);
        }
        
        // organize orderedItemSet with their item numbers, and then calculate p-values
        final Map<Integer, List<OrderedItemSetRes>> mapOfItemSetsWithSameItemNum = new HashMap(listOfNumOfItems.size());
        for(final int itemNum: listOfNumOfItems) {
            final List<OrderedItemSetRes> itemSetsWithSameItemNum = new ArrayList();
            for(final OrderedItemSetRes orderedItemSet: Storehouse.orderedItemSets) {
                if( orderedItemSet.getNumOfItems() == itemNum ){
                    itemSetsWithSameItemNum.add(orderedItemSet);
                }
            }
            mapOfItemSetsWithSameItemNum.put(itemNum, itemSetsWithSameItemNum);
        }
        // calculate p-values
        final Map<String, RunSumRes> IDandResultsOfCalcRunSum = Storehouse.IDandRunSumRes;
        ExecutorService threadPoolCalcPVal = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<ToolPValDPCalculator> processesCalcPVal = new LinkedList();
        for(final int key: mapOfItemSetsWithSameItemNum.keySet()) {
            final List<OrderedItemSetRes> itemSetsWithSameItemNum = mapOfItemSetsWithSameItemNum.get(key);
            final BigInteger caseAll = listOfChooseResults.get(key).getResult();
            ToolPValDPCalculator callablePValResults = new ToolPValDPCalculator(
                                itemSetsWithSameItemNum, caseAll, IDandResultsOfCalcRunSum, Storehouse.N, key);
            processesCalcPVal.add(callablePValResults);
        }
        List<Future<Map<String, PValRes>>> IDandResultsOfCalcPValFutures = new LinkedList();
        try {
            IDandResultsOfCalcPValFutures = threadPoolCalcPVal.invokeAll(processesCalcPVal);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolCalcPVal.shutdown();
        }
        Map<String, PValRes> IDandResultsOfCalcPVal = new HashMap(Storehouse.setNum);
        for( Future<Map<String, PValRes>> IDandResultsOfCalcPValFuture: IDandResultsOfCalcPValFutures) {
            IDandResultsOfCalcPVal.putAll(IDandResultsOfCalcPValFuture.get());
        }
        
        Storehouse.IDandPValRes = IDandResultsOfCalcPVal;
        
        // Report
        CreateReportFileUtility.reportPValCalcEnd();
    }
}
