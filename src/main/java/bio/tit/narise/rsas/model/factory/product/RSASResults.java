package bio.tit.narise.rsas.model.factory.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author TN
 */
public class RSASResults extends Result {

    private List<RSASRes> resultsPos;
    private List<RSASRes> resultsNeg;
    private int setNumPos;
    private int setNumNeg;
    
    public RSASResults() {
        resultsPos = new ArrayList();
        resultsNeg = new ArrayList();
        setNumPos = 0;
        setNumNeg = 0;
    }
    
    public RSASResults(List<RSASRes> resultsPos, List<RSASRes> resultsNeg) {
        this.resultsPos = resultsPos;
        this.resultsNeg = resultsNeg;
        this.setNumPos = resultsPos.size();
        this.setNumNeg = resultsNeg.size();
    }
    
    @Override
    public Result add(Result res) {
        RSASRes resultsForAnItemSet = (RSASRes) res;
        if( resultsForAnItemSet.isPos() ){ 
            getResultsPos().add(resultsForAnItemSet) ;
            setNumPos++;
        }
        else{ 
            getResultsNeg().add(resultsForAnItemSet);
            setNumNeg++;
        }
        return this;
    }
    
    public void sort() {
        
        Comparator withPVal = new Comparator() {
            @Override
            public int compare(Object obj0, Object obj1) {
                Double pVal0 = ((RSASRes) obj0).getpVal();
                Double pVal1 = ((RSASRes) obj1).getpVal();
                int ret;
                if ((ret = pVal0.compareTo(pVal1)) == 0) {
                    String id0 = ((RSASRes) obj0).getItemSetID();
                    String id1 = ((RSASRes) obj1).getItemSetID();
                    ret = id0.compareTo(id1);
                }
                return ret;
            }
        };
        
        Collections.sort(resultsPos, withPVal);
        Collections.sort(resultsNeg, withPVal);
    }
    
    /**
     * @return the resultsPos
     */
    public List<RSASRes> getResultsPos() {
        return resultsPos;
    }

    /**
     * @return the resultsNeg
     */
    public List<RSASRes> getResultsNeg() {
        return resultsNeg;
    }

    /**
     * @return the setNumPos
     */
    public int getSetNumPos() {
        return setNumPos;
    }

    /**
     * @return the setNumNeg
     */
    public int getSetNumNeg() {
        return setNumNeg;
    }

}
