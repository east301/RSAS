package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tn
 */
public class CutTree {
    private final Cluster topClst;
    private final double cutD;
    private final int cutK;
    
    public CutTree (Cluster topClst, double cutD, int cutK) {
        this.topClst = topClst;
        this.cutD = cutD;
        this.cutK = cutK;
    }
    
    public List<Cluster> cut() {
        List<Cluster> clusters = new ArrayList();
        List<Cluster> nextClusters = new ArrayList();
        clusters.add(topClst);
        
        if(cutD < 1) {
            while(true) {
                
                for(Cluster clst: clusters) {
                    if(clst.getDist() >= cutD) {
                        nextClusters.add(clst.getLeft());
                        nextClusters.add(clst.getRight());
                    }
                    else {
                        nextClusters.add(clst);
                    }
                }
        
                if(clusters.size() == nextClusters.size()) {
                    break;
                } 
                else {
                    clusters.clear();
                    clusters.addAll(nextClusters);
                    nextClusters.clear();
                }
            }
            
            return clusters;
        }
        else if(cutK > 1) {
            double dist = 0.99;
            while(true) {
                while(true) {
                    for(Cluster clst: clusters) {
                        if(clst.getDist() >= dist) {
                            nextClusters.add(clst.getLeft());
                            nextClusters.add(clst.getRight());
                        }
                        else {
                            nextClusters.add(clst);
                        }
                    }
        
                    if(clusters.size() == nextClusters.size()) {
                        break;
                    } 
                    else {
                        clusters.clear();
                        clusters.addAll(nextClusters);
                        nextClusters.clear();
                    }
                }
                
                if(clusters.size() >= cutK) {
                    break;
                }
                else {
                    dist -= 0.01;
                }
            }
            
            return clusters;
        }
        else {
            System.out.println("Error in cutting tree");
            return clusters;
        }
    }
}
