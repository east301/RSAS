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
    
    public SubHeatmapCalculator(List<Cluster> ctClustersCol, int[][] matrix, List<String> orderedNameRow) {
        this.ctClustersCol = ctClustersCol;
        this.matrix = matrix;
        this.orderedNameRow = orderedNameRow;
    }
    
    public List<SubHeatmapMatrix> calc() {
        List<SubHeatmapMatrix> ret = new ArrayList();
        
        int colStart = 0;
        for(Cluster ctCluster: ctClustersCol) {
            
            // filt column
            int[][] subMat = new int[orderedNameRow.size()][ctCluster.getMemberNum()];
            int nonZeroRowNum = 0;
            int pixelNum = 0;
            int coloredPixelNum = 0;
            Set<Integer> nonZeroRowIndex = new HashSet();
            
            for(int i = 0; i < orderedNameRow.size(); i++) {
                
                boolean nonZeroFlag = false;
            
                for(int j = 0; j < ctCluster.getMemberNum(); j++) {
                    subMat[i][j] = matrix[i][colStart + j];
                    pixelNum++;
                    if(subMat[i][j] == 1) {
                        nonZeroFlag = true;
                        coloredPixelNum++;
                    }
                }
                
                if(nonZeroFlag) {
                    nonZeroRowNum++;
                    nonZeroRowIndex.add(i);
                }
            }
            
            // filt row
            int[][] subsubMat = new int[nonZeroRowNum][ctCluster.getMemberNum()];
            int subsubMatRowIndex = 0;
            List<String> rowName = new ArrayList();
            List<String> colName = ctCluster.getOrderedName();
            
            for(int i = 0; i < orderedNameRow.size(); i++) {
                
                if(nonZeroRowIndex.contains(i)) {
                    for(int j = 0; j < ctCluster.getMemberNum(); j++) {
                        subsubMat[subsubMatRowIndex][j] = subMat[i][j];
                    }
                    subsubMatRowIndex++;
                    rowName.add(orderedNameRow.get(i));
                }
            }
            
            SubHeatmapMatrix subHeatMap;
            subHeatMap = new SubHeatmapMatrix(subsubMat, rowName, colName, pixelNum, coloredPixelNum);
            
            ret.add(subHeatMap);
            
            colStart += ctCluster.getMemberNum();
        }
        
        return ret;
    }
}
