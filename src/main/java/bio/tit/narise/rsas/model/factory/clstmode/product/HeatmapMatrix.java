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
public class HeatmapMatrix {
    private int matrixTop[][];
    private List<String> rownamesTop = new ArrayList();
    private List<String> colnamesTop = new ArrayList();

    private int matrixBottom[][];
    private List<String> rownamesBottom = new ArrayList();
    private List<String> colnamesBottom = new ArrayList();
    
    public HeatmapMatrix(int matrixTop[][], List<String> rownamesTop, List<String> colnamesTop, int matrixBottom[][], List<String> rownamesBottom, List<String> colnamesBottom) {
        this.matrixTop = matrixTop;
        this.rownamesTop = rownamesTop;
        this.colnamesTop = colnamesTop;
        
        this.matrixBottom = matrixBottom;
        this.rownamesBottom = rownamesBottom;
        this.colnamesBottom = colnamesBottom;
    }

    /**
     * @return the matrixTop
     */
    public int[][] getMatrixTop() {
        return matrixTop;
    }

    /**
     * @param matrixTop the matrixTop to set
     */
    public void setMatrixTop(int[][] matrixTop) {
        this.matrixTop = matrixTop;
    }

    /**
     * @return the rownamesTop
     */
    public List<String> getRownamesTop() {
        return rownamesTop;
    }

    /**
     * @param rownamesTop the rownamesTop to set
     */
    public void setRownamesTop(List<String> rownamesTop) {
        this.rownamesTop = rownamesTop;
    }

    /**
     * @return the colnamesTop
     */
    public List<String> getColnamesTop() {
        return colnamesTop;
    }

    /**
     * @param colnamesTop the colnamesTop to set
     */
    public void setColnamesTop(List<String> colnamesTop) {
        this.colnamesTop = colnamesTop;
    }

    /**
     * @return the matrixBottom
     */
    public int[][] getMatrixBottom() {
        return matrixBottom;
    }

    /**
     * @param matrixBottom the matrixBottom to set
     */
    public void setMatrixBottom(int[][] matrixBottom) {
        this.matrixBottom = matrixBottom;
    }

    /**
     * @return the rownamesBottom
     */
    public List<String> getRownamesBottom() {
        return rownamesBottom;
    }

    /**
     * @param rownamesBottom the rownamesBottom to set
     */
    public void setRownamesBottom(List<String> rownamesBottom) {
        this.rownamesBottom = rownamesBottom;
    }

    /**
     * @return the colnamesBottom
     */
    public List<String> getColnamesBottom() {
        return colnamesBottom;
    }

    /**
     * @param colnamesBottom the colnamesBottom to set
     */
    public void setColnamesBottom(List<String> colnamesBottom) {
        this.colnamesBottom = colnamesBottom;
    }
    
}
