/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.tit.narise.rsas.model.factory.clstmode.product;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tn
 */
public class Member extends Cluster {
    private final int[] vector;
    private final List<Integer> idAsList = new ArrayList();
    private final List<String> nameAsList = new ArrayList();
    
    public Member(int id, String name, Cluster left, Cluster right, double dist, int[] vector) {
        super(id, name, dist);
        super.left = null;
        super.right = null;
        super.clstNum = 1;
        this.vector = vector;
        this.idAsList.add(id);
        this.nameAsList.add(name);
    }
    
    @Override
    public int[] getVector() {
        return this.vector;
    }

    @Override
    public List<Integer> getOrderedId() {
        return this.idAsList;
    }
    
    @Override
    public List<String> getOrderedName() {
        return this.nameAsList;
    }
}
