package bio.tit.narise.rsas.model.factory.clstmode.tool;

import java.util.List;

/**
 *
 * @author tn
 */
public class DistCalculator {
    private List<Result>cachedDist;
    
    public double calcDist(){return 0;};
    
    
    private class Result {
        private int id1;
        private int id2;
        private double dist;
    }
}
