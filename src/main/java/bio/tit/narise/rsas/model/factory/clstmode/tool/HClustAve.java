package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import bio.tit.narise.rsas.model.factory.clstmode.product.CoeffResult;
import bio.tit.narise.rsas.model.factory.clstmode.product.Members;
import java.util.ArrayList;
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
    private Integer clstId = 0;
    private List<Cluster> clusters;
    private final ParsedArgs pargs;
    private List<CoeffResult> coeffResults = new ArrayList();
    
    public HClustAve(List<Cluster> clusters, ParsedArgs pargs) {
        this.clusters = clusters;
        this.pargs = pargs;
    }
    
    public List<Cluster> hClust() throws InterruptedException, ExecutionException {
        
        // calc coefficients
        ExecutorService threadPoolCalcCoeff = Executors.newFixedThreadPool(pargs.getThreadNum());
        List<CoeffCalculator> processesCalcCoeff = new LinkedList();
        for(int i = 0; i < clusters.size() -1; i++) {
            final Cluster clst1 = clusters.get(i);
            for(int j = i + 1; j < clusters.size(); j++) {
                final Cluster clst2 = clusters.get(j);
                CoeffCalculator coeffCalculator = new CoeffCalculator(clst1, clst2, pargs.isJc());
                processesCalcCoeff.add(coeffCalculator);
            }
        }
        List<Future<CoeffResult>> coeffResultFutures = new LinkedList();
        try {
            coeffResultFutures = threadPoolCalcCoeff.invokeAll(processesCalcCoeff);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPoolCalcCoeff.shutdown();
        }
        for( Future<CoeffResult> coeffResultFuture: coeffResultFutures) {
            final CoeffResult coeffResult = coeffResultFuture.get();
            this.coeffResults.add(coeffResult);
        }
        
        // hClust
        while(clusters.size() > 1) {
            double maxCoeff = 0;
            Cluster left = null;
            Cluster right = null;
            for(CoeffResult res: coeffResults) {
                if(res.getCoeff() > maxCoeff) {
                    left = res.getClst1();
                    right = res.getClst2();
                }
            }
            
            // clusters without left and right
            List<Cluster> clustersWoLR = new ArrayList();
            for(Cluster clst: clusters) {
                if(clst != left && clst != right) {
                    clustersWoLR.add(clst);
                }
            }
            
            // create new Cluster (left + right)
            this.clstId++;
            Cluster newCluster = new Members(clstId.toString(), left, right, clstId, 1-maxCoeff);
            
            // coefficients without left and right
            List<CoeffResult> newCoeffResults = new ArrayList();
            List<CoeffResult> coeffResultsWithLR = new ArrayList();
            for(CoeffResult res: coeffResults) {
                if(res.getClst1() != left && res.getClst1() != right && res.getClst2() != left && res.getClst2() != right) {
                    newCoeffResults.add(res);
                }
                else {
                    coeffResultsWithLR.add(res);
                }
            }
            
            // calc coeff between newCluster and cluster in clustersWoLR (UPGMA method)
            double leftVecNum = left.getVecNum();
            double rightVecNum = right.getVecNum();
            double lrVecNum = leftVecNum + rightVecNum;
            for (Cluster target : clustersWoLR) {
                double coeffVsLeft = 0;
                double coeffVsRight = 0;
                int flag = 0;
                for(CoeffResult res: coeffResultsWithLR) {
                    if((res.getClst1() == target && res.getClst2() == left) || 
                            (res.getClst1() == left && res.getClst2() == target)) {
                        coeffVsLeft = res.getCoeff();
                        flag++;
                    }
                    if((res.getClst1() == target && res.getClst2() == right) || 
                            (res.getClst1() == right && res.getClst2() == target)) {
                        coeffVsRight = res.getCoeff();
                        flag++;
                    }
                    if(flag==2){break;}
                }
                double coeffNewClstVsTarget = (coeffVsLeft * leftVecNum / lrVecNum) + (coeffVsRight * rightVecNum / lrVecNum);
                CoeffResult newCoeffRes = new CoeffResult(target, newCluster, coeffNewClstVsTarget);
                newCoeffResults.add(newCoeffRes);
            }

            coeffResults = newCoeffResults;
            
            List<Cluster> newClusters = clustersWoLR;
            newClusters.add(newCluster);
            clusters = newClusters;
        }
        
        return clusters;
    }
}
