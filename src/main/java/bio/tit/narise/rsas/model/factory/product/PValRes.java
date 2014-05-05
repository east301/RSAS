package bio.tit.narise.rsas.model.factory.product;

/**
 *
 * @author TN
 */
public class PValRes extends Result {
    
    private final double pVal;

    public PValRes(double pVal) {
        this.pVal = pVal;
    }
    
    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @return the pVal
     */
    public double getpVal() {
        return pVal;
    }
}
