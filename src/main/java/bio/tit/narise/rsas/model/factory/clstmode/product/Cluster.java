package bio.tit.narise.rsas.model.factory.clstmode.product;

import java.util.List;

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
    
    public int getDepth() {
        return this.depth;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public abstract int getVecNum();
    public abstract int[] getVector();
    public abstract List<String> getOrderedId();
}
