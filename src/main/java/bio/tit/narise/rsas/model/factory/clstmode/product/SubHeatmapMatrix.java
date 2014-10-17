package bio.tit.narise.rsas.model.factory.clstmode.product;

import java.util.List;

/**
 *
 * @author tn
 */
public class SubHeatmapMatrix {
    private int matrix[][];
    private List<String> rownames;
    private List<String> colnames;
    private int pixelNum;
    private int coloredPixelNum;
    
    public SubHeatmapMatrix(int matrix[][], List<String> rownames, List<String> colnames, int pixelNum, int coloredPixelNum) {
        this.matrix = matrix;
        this.rownames = rownames;
        this.colnames = colnames;
        this.pixelNum = pixelNum;
        this.coloredPixelNum = coloredPixelNum;
    }

    /**
     * @return the matrix
     */
    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * @return the rownames
     */
    public List<String> getRownames() {
        return rownames;
    }

    /**
     * @param rownames the rownames to set
     */
    public void setRownames(List<String> rownames) {
        this.rownames = rownames;
    }

    /**
     * @return the colnames
     */
    public List<String> getColnames() {
        return colnames;
    }

    /**
     * @param colnames the colnames to set
     */
    public void setColnames(List<String> colnames) {
        this.colnames = colnames;
    }

    /**
     * @return the pixelNum
     */
    public int getPixelNum() {
        return pixelNum;
    }

    /**
     * @param pixelNum the pixelNum to set
     */
    public void setPixelNum(int pixelNum) {
        this.pixelNum = pixelNum;
    }

    /**
     * @return the coloredPixelNum
     */
    public int getColoredPixelNum() {
        return coloredPixelNum;
    }

    /**
     * @param coloredPixelNum the coloredPixelNum to set
     */
    public void setColoredPixelNum(int coloredPixelNum) {
        this.coloredPixelNum = coloredPixelNum;
    }
}
