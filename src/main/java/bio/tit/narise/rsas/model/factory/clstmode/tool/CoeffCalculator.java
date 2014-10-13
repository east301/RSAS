package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import bio.tit.narise.rsas.model.factory.clstmode.product.CoeffResult;
import java.util.concurrent.Callable;

/**
 *
 * @author tn
 */
public class CoeffCalculator implements Callable<CoeffResult>{
    private final Cluster clst1;
    private final Cluster clst2;
    private final boolean jc;
    
    public CoeffCalculator(Cluster clst1, Cluster clst2, boolean jc) {
        this.clst1 = clst1;
        this.clst2 = clst2;
        this.jc = jc;
    }

    @Override
    public CoeffResult call() throws Exception {
        CoeffResult coeffResult;
        if(jc) {
            coeffResult = calcJc();
        }
        else {
            coeffResult = calcOc();
        }
        return coeffResult;
    }
    
    private CoeffResult calcJc() {
        int[] vec1 = clst1.getVector();
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
        return new CoeffResult(clst1, clst2, coeff);
    }
    
    private CoeffResult calcOc() {
        int[] vec1 = clst1.getVector();
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
        return new CoeffResult(clst1, clst2, coeff);
    }
}
