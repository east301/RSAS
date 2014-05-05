package bio.tit.narise.rsas.model.factory.product;

import java.util.List;

/**
 *
 * @author TN
 */
public class FCRRes extends Result {

    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private final List<Double> FCRs;
    private final double FCRAtMax;
    private final int indexOfFCRAtMax;
    
    public FCRRes(List<Double> FCRs, double FCRAtMax, int indexOfFCRAtMax) {
        this.FCRs = FCRs;
        this.FCRAtMax = FCRAtMax;
        this.indexOfFCRAtMax = indexOfFCRAtMax;
    }
    
    /**
     * @return the FCR
     */
    public List<Double> getFCRs() {
        return FCRs;
    }

    /**
     * @return the FCRAtMax
     */
    public double getFCRAtMax() {
        return FCRAtMax;
    }

    /**
     * @return the indexOfFCRAtMax
     */
    public int getIndexOfFCRAtMax() {
        return indexOfFCRAtMax;
    }
    
}
