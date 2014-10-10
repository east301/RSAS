/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.tit.narise.rsas.mode.factory.clsmode.product;

import java.util.List;

/**
 *
 * @author tn
 */
public class Member extends Cluster {
    private int[] vector;

    public Member(int id, String name, Cluster left, Cluster right, int depth, double height, int[] vector) {
        super(id, name, left, right, depth, height);
        this.vector = vector;
    }

    @Override
    public List<Member> getMembers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cluster getLeft() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cluster getRight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void addMembers() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
