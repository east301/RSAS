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
    private final int vecNum = 1;
    private final int[] vector;
    private final List<String> idAsList = new ArrayList();
    
    public Member(String id, Cluster left, Cluster right, int depth, double height, int[] vector) {
        super(id, left, right, depth, height);
        this.vector = vector;
        this.idAsList.add(id);
    }

    @Override
    public int getVecNum() {
        return this.vecNum;
    }
    
    @Override
    public int[] getVector() {
        return this.vector;
    }

    @Override
    public List<String> getOrderedId() {
        // System.out.println(this.id);
        return this.idAsList;
    }
}
