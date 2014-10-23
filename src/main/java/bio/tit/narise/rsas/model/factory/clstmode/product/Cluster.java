package bio.tit.narise.rsas.model.factory.clstmode.product;

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
    protected int clstNum;
    
    protected double dist;
    
    
    public Cluster(int id, String name, double dist){
        this.id = id;
        this.name = name;
        this.dist = dist;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Cluster getLeft() {
        return this.left;
    }
    
    public Cluster getRight() {
        return this.right;
    }
    
    public int getMemberNum() {
        return this.clstNum;
    }
    
    public double getDist() {
        return this.dist;
    }
    
    public abstract int[] getVector();
    public abstract List<Integer> getOrderedId();
    public abstract List<String> getOrderedName();
}
