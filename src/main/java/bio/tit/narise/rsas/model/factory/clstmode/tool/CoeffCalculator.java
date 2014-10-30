package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author tn
 */
public class CoeffCalculator implements Callable<HashMap<Cluster, HashMap<Cluster, Double>>>{
    private final Cluster clst1;
    private final List<Cluster> clst2List;
    private final boolean jc;
    private final boolean oc;
    private final double jcr;
    
    public CoeffCalculator(Cluster clst1, List<Cluster> clst2List, boolean jc, boolean oc, double jcr) {
        this.clst1 = clst1;
        this.clst2List = clst2List;
        this.jc = jc;
        this.oc = oc;
        this.jcr = jcr;
    }

    @Override
    public HashMap<Cluster, HashMap<Cluster, Double>> call() throws Exception {
        HashMap<Cluster, HashMap<Cluster, Double>> coeffResult;
        if(jc) {
            coeffResult = calcJc();
        }
        else if (oc) {
            coeffResult = calcOc();
        }
        else {
            coeffResult = calcJcOc();
        }
        return coeffResult;
    }
    
    private HashMap<Cluster, HashMap<Cluster, Double>> calcJc() {
        int[] vec1 = clst1.getVector();
        HashMap clst2AndCoeff = new HashMap();
        
        for(Cluster clst2: clst2List) {
            int[] vec2 = clst2.getVector();
            double coeff = calcJcCoeff(vec1, vec2);
            clst2AndCoeff.put(clst2, coeff);
        }
        HashMap ret = new HashMap();
        ret.put(clst1, clst2AndCoeff);
        return ret;
    }
    
    private HashMap<Cluster, HashMap<Cluster, Double>> calcOc() {
        int[] vec1 = clst1.getVector();
        HashMap clst2AndCoeff = new HashMap();
        
        for(Cluster clst2: clst2List) {
            int[] vec2 = clst2.getVector();
            double coeff = calcOcCoeff(vec1, vec2);
            clst2AndCoeff.put(clst2, coeff);
        }
        HashMap ret = new HashMap();
        ret.put(clst1, clst2AndCoeff);
        return ret;
    }
    
    private HashMap<Cluster, HashMap<Cluster, Double>> calcJcOc() {
        int[] vec1 = clst1.getVector();
        HashMap clst2AndCoeff = new HashMap();
        
        for(Cluster clst2: clst2List) {
            int[] vec2 = clst2.getVector();
            
            double coeffJc = calcJcCoeff(vec1, vec2);
            double coeffOc = calcOcCoeff(vec1, vec2);
            
            double coeff = coeffJc * jcr + coeffOc * (1 - jcr);
            
            clst2AndCoeff.put(clst2, coeff);
        }
        HashMap ret = new HashMap();
        ret.put(clst1, clst2AndCoeff);
        return ret;
    }
    
    private double calcJcCoeff(int[] vec1, int[] vec2) {
        double coeff = 0;

        double numer = 0;
        double denomin = 0;

        // vec1.length == vec2.length
        for (int i = 0; i < vec1.length; i++) {
            if (vec1[i] == 1 && vec2[i] == 1) {
                numer++;
                denomin++;
            } else if (vec1[i] == 1 || vec2[i] == 1) {
                denomin++;
            }
        }
        if (denomin > 0) {
            coeff = numer / denomin;
        }
        return coeff;
    }
    
    private double calcOcCoeff(int[] vec1, int[] vec2) {
        double coeff = 0;

        double numer = 0;
        double vec1Size = 0;
        double vec2Size = 0;
        
        // vec1.length == vec2.length
        for (int i = 0; i < vec1.length; i++) {
            if (vec1[i] == 1 && vec2[i] == 1) {
                numer++;
                vec1Size++;
                vec2Size++;
            } else if (vec1[i] == 1 && vec2[i] == 0) {
                vec1Size++;
            } else if (vec1[i] == 0 && vec2[i] == 1) {
                vec2Size++;
            }
        }
        if (vec1Size <= vec2Size && vec1Size > 0) {
            coeff = numer / vec1Size;
        } else if (vec1Size > vec2Size && vec2Size > 0) {
            coeff = numer / vec2Size;
        }
        return coeff;
    }
}