/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.tit.narise.rsas.model.factory.clstmode.product;

/**
 *
 * @author tn
 */
public class Member extends Cluster {
    private final int vecNum = 1;
    private final int[] vector;
    
    public Member(String id, Cluster left, Cluster right, int depth, double height, int[] vector) {
        super(id, left, right, depth, height);
        this.vector = vector;
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
    
    public int[] getVector() {
        return this.vector;
    }
}
