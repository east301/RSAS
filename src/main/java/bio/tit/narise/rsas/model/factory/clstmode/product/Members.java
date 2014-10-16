package bio.tit.narise.rsas.model.factory.clstmode.product;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tn
 */
public class Members extends Cluster {
    
    public Members(int id, String name, Cluster left, Cluster right, int depth, double dist) {
        super(id, name, left, right, depth, dist);
        super.clstNum = left.getClstNum() + right.getClstNum();
    }

    @Override
    public int[] getVector() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<Integer> getOrderedId() {
        List<Integer> orderedId = new ArrayList();
        
        orderedId.addAll(left.getOrderedId());
        orderedId.addAll(right.getOrderedId());
        return orderedId;
    }
    
    @Override
    public List<String> getOrderedName() {
        List<String> orderedName = new ArrayList();
        
        orderedName.addAll(left.getOrderedName());
        orderedName.addAll(right.getOrderedName());
        return orderedName;
    }
}
