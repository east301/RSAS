package bio.tit.narise.rsas.model.factory.product;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TN
 */
public class PValDPCalcResults extends Result {
    
    private final List<PValRes> listOfPValRes = new ArrayList();
    
    @Override
    public Result add(Result res) {
        this.listOfPValRes.add((PValRes) res);
        return this;
    }
    
    /**
     * @return the listOfResultsOfCalcPVal
     */
    public List<PValRes> getListOfPValRes() {
        return listOfPValRes;
    }
}
