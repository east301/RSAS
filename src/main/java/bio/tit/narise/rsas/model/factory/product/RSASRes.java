package bio.tit.narise.rsas.model.factory.product;

import java.util.List;

/**
 *
 * @author TN
 */
public class RSASRes extends Result {

    private final String itemSetID;
    private final String description;
    private final int numOfItems;
    private final double pVal;
    private final Double fcrValAtThreshold;
    private final double fcrValAtMax;
    private final int ES;
    private final double NES;
    private final int rankAtMax;
    private final List<String> contributors;
    private final boolean pos;

    public RSASRes(String itemSetID, String description, int numOfItems, double pVal, 
                        Double fcrValAtThreshold, double fcrValAtMax, int ES, double NES, int rankAtMax, List<String> contributors, boolean pos) {
        
        this.itemSetID = itemSetID;
        this.description = description;
        this.numOfItems = numOfItems;
        this.pVal = pVal;
        this.fcrValAtThreshold = fcrValAtThreshold;
        this.fcrValAtMax = fcrValAtMax;
        this.ES = ES;
        this.NES = NES;
        this.rankAtMax = rankAtMax;
        this.contributors = contributors;
        this.pos = pos;
    }
    
    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the itemSetID
     */
    public String getItemSetID() {
        return itemSetID;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the numOfItems
     */
    public int getNumOfItems() {
        return numOfItems;
    }

    /**
     * @return the pVal
     */
    public double getpVal() {
        return pVal;
    }

    /**
     * @return the fcrValAtThreshold
     */
    public Double getFcrValAtThreshold() {
        return fcrValAtThreshold;
    }

    /**
     * @return the fcrValAtThreshold
     */
    public double getFcrValAtMax() {
        return fcrValAtMax;
    }
    
    /**
     * @return the ES
     */
    public int getES() {
        return ES;
    }

    /**
     * @return the rankAtMax
     */
    public int getRankAtMax() {
        return rankAtMax;
    }

    /**
     * @return the contributors
     */
    public List<String> getContributors() {
        return contributors;
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
    
}
