package bio.tit.narise.rsas.model.factory.clstmode.product;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tn
 */
public class Members extends Cluster {
    private final int vecNum;
    
    public Members(String id, Cluster left, Cluster right, int depth, double height) {
        super(id, left, right, depth, height);
        this.vecNum = left.getVecNum() + right.getVecNum();
    }

    @Override
    public int getVecNum() {
        return this.vecNum;
    }

    @Override
    public int[] getVector() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<String> getOrderedId() {
        List<String> orderedID = new ArrayList();
        
        // for debug
        // System.out.println(this.id);
        
        orderedID.addAll(left.getOrderedId());
        orderedID.addAll(right.getOrderedId());
        return orderedID;
    }
    
}
