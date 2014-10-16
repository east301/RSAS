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
    
    public CoeffCalculator(Cluster clst1, List<Cluster> clst2List, boolean jc) {
        this.clst1 = clst1;
        this.clst2List = clst2List;
        this.jc = jc;
    }

    @Override
    public HashMap<Cluster, HashMap<Cluster, Double>> call() throws Exception {
        HashMap<Cluster, HashMap<Cluster, Double>> coeffResult;
        if(jc) {
            coeffResult = calcJc();
        }
        else {
            coeffResult = calcOc();
        }
        return coeffResult;
    }
    
    private HashMap<Cluster, HashMap<Cluster, Double>> calcJc() {
        int[] vec1 = clst1.getVector();
        HashMap clst2AndCoeff = new HashMap();
        
        for(Cluster clst2: clst2List) {
            int[] vec2 = clst2.getVector();
            double numer = 0;
            double denomin = 0;
            double coeff = 0;
            for(int i = 0; i < vec1.length; i++) {
                if(vec1[i] == 1 && vec2[i] == 1) {
                    numer++;
                    denomin++;
                }
                else if (vec1[i] == 1 || vec2[i] == 1) {
                    denomin++;
                }
            }
            if(denomin > 0) {
                coeff = numer / denomin;
            }
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
            double numer = 0;
            double vec1Size = 0;
            double vec2Size = 0;
            double coeff = 0;
            for(int i = 0; i < vec1.length; i++) {
                if(vec1[i] == 1 && vec2[i] == 1) {
                    numer++;
                    vec1Size++;
                    vec2Size++;
                }
                else if (vec1[i] == 1 && vec2[i] == 0) {
                    vec1Size++;
                }
                else if (vec1[i] == 0 && vec2[i] == 1) {
                    vec2Size++;
                }
            }
            if(vec1Size <= vec2Size && vec1Size > 0) {
                coeff = numer / vec1Size;
            }
            else if(vec1Size > vec2Size && vec2Size > 0) {
                coeff = numer / vec2Size;
            }
            clst2AndCoeff.put(clst2, coeff);
        }
        HashMap ret = new HashMap();
        ret.put(clst1, clst2AndCoeff);
        return ret;
    }
}