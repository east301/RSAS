package bio.tit.narise.rsas.mode.factory.clsmode.product;

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
    public Cluster getDepth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cluster getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getVecNum() {
        return this.vecNum;
    }
    
}
