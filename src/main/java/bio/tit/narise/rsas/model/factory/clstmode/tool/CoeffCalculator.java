package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.mode.factory.clsmode.product.Cluster;
import bio.tit.narise.rsas.mode.factory.clsmode.product.CoeffResult;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private CoeffResult calcJc() {
        
        return null;
    }
    
    private CoeffResult calcOc() {
        
        return null;
    }
}
