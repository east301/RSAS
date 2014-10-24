package bio.tit.narise.rsas.model.factory.clstmode.tool;

import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import bio.tit.narise.rsas.model.factory.clstmode.product.SubHeatmapMatrix;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author tn
 */
public class SubHeatmapCalculator {
    private final List<Cluster> ctClustersCol;
    private final int[][] matrix;
    private final List<String> orderedNameRow;
    private final int minColNum;
    private final int minRowSum;
    private final int minRowNum;
    private final int colTotalNum;
    
    public SubHeatmapCalculator(List<Cluster> ctClustersCol, int[][] matrix, List<String> orderedNameRow, int minColNum, int minRowSum, int minRowNum, int colTotalNum) {
        this.ctClustersCol = ctClustersCol;
        this.matrix = matrix;
        this.orderedNameRow = orderedNameRow;
        this.minColNum = minColNum;
        this.minRowSum = minRowSum;
        this.minRowNum = minRowNum;
        this.colTotalNum = colTotalNum;
    }
    
    public List<SubHeatmapMatrix> calc() {
        List<SubHeatmapMatrix> ret = new ArrayList();
        
        int colStart = 0;
        for(Cluster ctCluster: ctClustersCol) {
            
            int memNum = ctCluster.getMemberNum();
            
            // filt with colNum
            if(memNum < minColNum) {
                colStart += memNum;
                continue;
            }
            
            // filt column
            int[][] subMat = new int[orderedNameRow.size()][memNum];
            int rowNum = 0;
            Set<Integer> rowIndex = new HashSet();
            
            for(int i = 0; i < orderedNameRow.size(); i++) {
                
                int rowSum = 0;
            
                for(int j = 0; j < memNum; j++) {
                    subMat[i][j] = matrix[i][colStart + j];
                    if(subMat[i][j] == 1) {
                        rowSum++;
                    }
                }
                
                if(rowSum >= minRowSum) {
                    rowNum++;
                    rowIndex.add(i);
                }
            }
            
            // filt with rowNum
            if(rowNum < minRowNum) {
                colStart += memNum;
                continue;
            }
            
            // filt row
            int[][] subsubMat = new int[rowNum][memNum];
            int subsubMatRowIndex = 0;
            List<String> rowName = new ArrayList();
            List<String> colName = ctCluster.getOrderedName();
            int pixelNum = 0;
            int coloredPixelNum = 0;
            
            for(int i = 0; i < orderedNameRow.size(); i++) {
                
                if(rowIndex.contains(i)) {
                    rowName.add(orderedNameRow.get(i));
                    for(int j = 0; j < memNum; j++) {
                        subsubMat[subsubMatRowIndex][j] = subMat[i][j];
                        pixelNum++;
                        if(subMat[i][j] == 1) {
                            coloredPixelNum++;
                        }
                    }
                    subsubMatRowIndex++;
                }
            }
            
            SubHeatmapMatrix subHeatMap = new SubHeatmapMatrix(subsubMat, rowName, colName, pixelNum, coloredPixelNum);
            ret.add(subHeatMap);
            
            colStart += memNum;
        }
        if(colStart != colTotalNum){ throw new InternalError("illegal subheatmaps column num"); }
        return ret;
    }
}
