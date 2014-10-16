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
    private int depth = 0;
    private final List<Cluster> clusters;
    private final ParsedArgs pargs;
    private final HashMap<Cluster, HashMap<Cluster, Double>> coeffResults = new HashMap(); // HashMap<clst1, HashMap<clst2, coeff>>
    
    public HClustAve(List<Cluster> clusters, ParsedArgs pargs) {
        this.clusters = clusters;
        this.pargs = pargs;
        this.clstId = clusters.size();
    }
    
    public Cluster hClust() throws InterruptedException, ExecutionException {
        Cluster ret;
        
        // calc coefficients
        // clst1.getId() < each clst2.getId()
        ExecutorService threadPoolCalcCoeff = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<CoeffCalculator> processesCalcCoeff = new LinkedList();
        for(int i = 0; i < clusters.size() -1; i++) {
            final Cluster clst1 = clusters.get(i);
            List<Cluster> clst2List = new ArrayList();
            for(int j = i + 1; j < clusters.size(); j++) {
                final Cluster clst2 = clusters.get(j);
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
        this.coeffResults.put(clusters.get(clusters.size()-1), empty);
        
        // hClust
        while(true) {
            double maxCoeff = -0.1;
            Cluster left = null;
            Cluster right = null;
            for(Cluster clst1: coeffResults.keySet()) {
                HashMap<Cluster, Double> clst2AndCoeff = coeffResults.get(clst1);
                for(Cluster clst2: clst2AndCoeff.keySet()) {
                    if(clst2AndCoeff.get(clst2) > maxCoeff) {
                        maxCoeff = clst2AndCoeff.get(clst2);
                        left = clst1;
                        right = clst2;
                    }
                }
            }
            if(left == null) { throw new NullPointerException("left is null"); }
            if(right == null) { throw new NullPointerException("right is null"); }
            double leftVecNum = left.getClstNum();
            double rightVecNum = right.getClstNum();
            double lrVecNum = leftVecNum + rightVecNum;
            HashMap<Cluster, Double> left_clst2AndCoeff = coeffResults.get(left);
            HashMap<Cluster, Double> right_clst2AndCoeff = coeffResults.get(right);
            
            // create new Cluster (left + right)
            this.clstId++;
            this.depth++;
            Cluster newClst = new Members(clstId, null, left, right, depth, 1 - maxCoeff);
            System.out.println("[ Info ] Depth of new cluster: " + depth);
            System.out.println("Distance: " + (1 - maxCoeff));
            
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