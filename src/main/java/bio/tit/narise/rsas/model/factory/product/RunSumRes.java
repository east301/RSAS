package bio.tit.narise.rsas.model.factory.product;

/**
 *
 * @author TN
 */
public class RunSumRes extends Result {

    private final int ES;
    private final int indexAtMax;
    private final boolean pos;
    private double NES;

    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public RunSumRes(int ES, int indexAtMax, boolean pos, double NES) {
        this.ES = ES;
        this.indexAtMax = indexAtMax;
        this.pos = pos;
        this.NES = NES;
    }

    /**
     * @return the ES
     */
    public int getES() {
        return ES;
    }

    /**
     * @return the indexAtMax
     */
    public int getIndexAtMax() {
        return indexAtMax;
    }
    
    /**
     * @return the pos
     */
    public boolean isPos() {
        return pos;
    }

    /**
     * @return the NES
     */
    public double getNES() {
        return NES;
    }

    /**
     * @param NES the NES to set
     */
    public void setNES(double NES) {
        this.NES = NES;
    }
    
}
