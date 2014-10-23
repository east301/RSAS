package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import bio.tit.narise.rsas.model.factory.clstmode.product.Members;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Takafumi
 */
public class HClustAve {
    private int clstId;
    private final List<Cluster> firstClusters;
    private final ParsedArgs pargs;
    private final HashMap<Cluster, HashMap<Cluster, Double>> coeffResults = new HashMap(); // HashMap<clst1, HashMap<clst2, coeff>>
    
    public HClustAve(List<Cluster> firstClusters, ParsedArgs pargs) {
        this.firstClusters = firstClusters;
        this.pargs = pargs;
        this.clstId = firstClusters.size();
    }
    
    public Cluster hClust() throws InterruptedException, ExecutionException {
        Cluster ret;
        
        // calc coefficients
        // clst1.getId() < each clst2.getId()
        ExecutorService threadPoolCalcCoeff = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<CoeffCalculator> processesCalcCoeff = new LinkedList();
        int firstClstNum = firstClusters.size();
        for(int i = 0; i < firstClstNum - 1; i++) {
            final Cluster clst1 = firstClusters.get(i);
            List<Cluster> clst2List = new ArrayList();
            for(int j = i + 1; j < firstClstNum; j++) {
                final Cluster clst2 = firstClusters.get(j);
                clst2List.add(clst2);
            }
            final CoeffCalculator coeffCalculator = new CoeffCalculator(clst1, clst2List, pargs.isJc());
            processesCalcCoeff.add(coeffCalculator);
        }
        List<Future<HashMap<Cluster, HashMap<Cluster, Double>>>> coeffResultFutures = new LinkedList();
        try {
            coeffResultFutures = threadPoolCalcCoeff.invokeAll(processesCalcCoeff);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolCalcCoeff.shutdown();
        }
        for(Future<HashMap<Cluster, HashMap<Cluster, Double>>> coeffResultFuture: coeffResultFutures) {
            HashMap<Cluster, HashMap<Cluster, Double>> coeffResult = coeffResultFuture.get();
            this.coeffResults.putAll(coeffResult);
        }
        HashMap<Cluster, Double> empty = new HashMap();
        this.coeffResults.put(firstClusters.get(firstClstNum - 1), empty);
        
        // hClust
        while(true) {
            double maxCoeff = -0.1;
            int leftId = -1;
            int rightId = -1;
            Cluster left = null;
            Cluster right = null;
 
            for(Cluster clst1: coeffResults.keySet()) {
                HashMap<Cluster, Double> clst2AndCoeff = coeffResults.get(clst1);
                for(Cluster clst2: clst2AndCoeff.keySet()) {
                    double coeffNow = clst2AndCoeff.get(clst2);
                    if(coeffNow < maxCoeff) {
                        continue;
                    }
                    
                    if(coeffNow > maxCoeff) {
                        maxCoeff = coeffNow;
                        left = clst1;
                        right = clst2;
                        leftId = left.getId();
                        rightId = right.getId();
                    }
                    else if(coeffNow == maxCoeff) {
                        if(clst1.getId() > leftId) {
                            left = clst1;
                            right = clst2;
                            leftId = left.getId();
                            rightId = right.getId();
                        }
                        else if (clst1.getId() == leftId) {
                            if(clst2.getId() > rightId) {
                                left = clst1;
                                right = clst2;
                                leftId = left.getId();
                                rightId = right.getId();
                            }
                        }
                    }
                }
            }
            if(left == null) { throw new NullPointerException("left is null"); }
            if(right == null) { throw new NullPointerException("right is null"); }
            double leftVecNum = left.getMemberNum();
            double rightVecNum = right.getMemberNum();
            double lrVecNum = leftVecNum + rightVecNum;
            HashMap<Cluster, Double> left_clst2AndCoeff = coeffResults.get(left);
            HashMap<Cluster, Double> right_clst2AndCoeff = coeffResults.get(right);
            
            // create new Cluster (left + right)
            this.clstId++;
            Cluster newClst = new Members(clstId, null, left, right, 1 - maxCoeff);
            System.out.println("[ Info ] Cluster merge distance: " + (1 - maxCoeff));
            
            // update coeffResults
            for(Cluster clst1: coeffResults.keySet()) {
                
                HashMap<Cluster, Double> clst1_clst2AndCoeff = coeffResults.get(clst1);
                double coeffClst1VsLeft = 0;
                double coeffClst1VsRight = 0;
                
                if(clst1.getId() < left.getId()) {
                    coeffClst1VsLeft = clst1_clst2AndCoeff.get(left);
                    coeffClst1VsRight = clst1_clst2AndCoeff.get(right);
                    clst1_clst2AndCoeff.remove(left);
                    clst1_clst2AndCoeff.remove(right);
                }
                else if(clst1 == left) {
                    continue;
                }
                else if(clst1.getId() > left.getId() && clst1.getId() < right.getId()) {
                    coeffClst1VsLeft = left_clst2AndCoeff.get(clst1);
                    coeffClst1VsRight = clst1_clst2AndCoeff.get(right);
                    clst1_clst2AndCoeff.remove(right);
                }
                else if(clst1 == right) {
                    continue;
                }
                else if(clst1.getId() > right.getId()) {
                    coeffClst1VsLeft = left_clst2AndCoeff.get(clst1);
                    coeffClst1VsRight = right_clst2AndCoeff.get(clst1);
                }
                
                double coeffClst1VsNewClst = (coeffClst1VsLeft * leftVecNum / lrVecNum) + (coeffClst1VsRight * rightVecNum / lrVecNum);
                clst1_clst2AndCoeff.put(newClst, coeffClst1VsNewClst);
            }
            coeffResults.remove(left);
            coeffResults.remove(right);
            
            if(coeffResults.isEmpty()) {
                ret = newClst;
                break;
            }
            
            HashMap<Cluster, Double> emptyMap = new HashMap();
            coeffResults.put(newClst, emptyMap);
        }
        
        System.out.println("TopCluster: " + ret.getId());
        return ret;
    }
}
