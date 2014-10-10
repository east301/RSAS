package bio.tit.narise.rsas.mode.factory.clsmode.product;

import java.util.List;

/**
 *
 * @author tn
 */
public abstract class Cluster {
    protected int id;
    protected String name;
    
    protected Cluster left;
    protected Cluster right;
    
    protected int depth;
    protected double height;
    
    public Cluster(int id, String name, Cluster left, Cluster right, int depth, double height){
        this.id = id;
        this.name = name;
        this.left = left;
        this.right = right;
        this.depth = depth;
        this.height = height;
    }
    
    public abstract List<Member> getMembers();
    public abstract Cluster getLeft();
    public abstract Cluster getRight();
    public abstract Cluster getDepth();
    public abstract Cluster getHeight();
    public abstract void addMembers();
    
}
