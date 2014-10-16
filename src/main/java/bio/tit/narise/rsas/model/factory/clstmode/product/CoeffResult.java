package bio.tit.narise.rsas.model.factory.clstmode.product;

/**
 *
 * @author Takafumi
 */
public class CoeffResult {
    private Cluster clst1;
    private Cluster clst2;
    private double coeff;

    public CoeffResult(Cluster clst1, Cluster clst2, double coeff) {
        this.clst1 = clst1;
        this.clst2 = clst2;
        this.coeff = coeff;
    }

    /**
     * @return the clst1
     */
    public Cluster getClst1() {
        return clst1;
    }

    /**
     * @param clst1 the clst1 to set
     */
    public void setClst1(Cluster clst1) {
        this.clst1 = clst1;
    }

    /**
     * @return the clst2
     */
    public Cluster getClst2() {
        return clst2;
    }

    /**
     * @param clst2 the clst2 to set
     */
    public void setClst2(Cluster clst2) {
        this.clst2 = clst2;
    }

    /**
     * @return the coeff
     */
    public double getCoeff() {
        return coeff;
    }

    /**
     * @param coeff the coeff to set
     */
    public void setCoeff(double coeff) {
        this.coeff = coeff;
    }

}