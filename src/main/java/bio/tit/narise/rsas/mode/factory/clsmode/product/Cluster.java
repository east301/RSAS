package bio.tit.narise.rsas.mode.factory.clsmode.product;

/**
 *
 * @author tn
 */
public abstract class Cluster {
    protected String id;
    
    protected Cluster left;
    protected Cluster right;
    
    protected int depth;
    protected double height;
    
    
    public Cluster(String id, Cluster left, Cluster right, int depth, double height){
        this.id = id;
        this.left = left;
        this.right = right;
        this.depth = depth;
        this.height = height;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Cluster getLeft() {
        return this.left;
    }
    
    public Cluster getRight() {
        return this.right;
    }
    
    public abstract Cluster getDepth();
    public abstract Cluster getHeight();
    public abstract int getVecNum();
}
