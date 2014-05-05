package bio.tit.narise.rsas.model.factory.product;

import java.util.List;

/**
 *
 * @author TN
 */
public class ContriRes extends Result {
    
    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private final List<String> contributors;
    private final Double FCRValAtThreshold;

    public ContriRes(List<String> contributors, Double FCRValAtThreshold) {
        this.contributors = contributors;
        this.FCRValAtThreshold = FCRValAtThreshold;
    }
    
    /**
     * @return the contributors
     */
    public List<String> getContributors() {
        return contributors;
    }

    /**
     * @return the FCRValAtThreshold
     */
    public Double getFCRValAtThreshold() {
        return FCRValAtThreshold;
    }

}
