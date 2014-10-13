package bio.tit.narise.rsas.model.factory.clstmode;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import bio.tit.narise.rsas.model.factory.clstmode.product.Member;
import bio.tit.narise.rsas.model.factory.clstmode.tool.HClustAve;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author tn
 */
public class ClstModeFactory {
    
    private final ParsedArgs pargs;
    private HeatmapMatrix matrix;
    private final List<Cluster> clustersRowTop = new ArrayList();
    private final List<Cluster> clustersColTop = new ArrayList();
    private final List<Cluster> clustersRowBottom = new ArrayList();
    private final List<Cluster> clustersColBottom = new ArrayList();
    private Cluster topClstRowTop = null;
    private Cluster topClstColTop = null;
    private Cluster topClstRowBottom = null;
    private Cluster topClstColBottom = null;
    
    public ClstModeFactory(ParsedArgs pargs){
        this.pargs = pargs;        
    }
    
    public void parseXlsFile() throws IOException {
        HeatmapMatrix beforeClustering = ClstModeFileParserUtility.parseXlsFile(pargs.getXlspath(), pargs.getMinConNum());
        this.matrix = beforeClustering;
    }
    
    public void mkClusters() {
        List<String> rownamesTop = matrix.getRownamesTop();
        int colNumTop = matrix.getColnamesTop().size();
        int[][] matTop = matrix.getMatrixTop();
        for(int i = 0; i < rownamesTop.size(); i++) {
            int[] rowVec = new int[colNumTop];
            for(int j = 0; j < colNumTop; j++) {
                rowVec[j] = matTop[i][j];
            }
            Cluster mem = new Member(rownamesTop.get(i), null, null, 0, 0, rowVec);
            clustersRowTop.add(mem);
        }
        
        List<String> rownamesBottom = matrix.getRownamesBottom();
        int colNumBottom = matrix.getColnamesBottom().size();
        int[][] matBottom = matrix.getMatrixBottom();
        for(int i = 0; i < rownamesBottom.size(); i++) {
            int[] rowVec = new int[colNumBottom];
            for(int j = 0; j < colNumBottom; j++) {
                rowVec[j] = matBottom[i][j];
            }
            Cluster mem = new Member(rownamesBottom.get(i), null, null, 0, 0, rowVec);
            clustersRowBottom.add(mem);
        }

        List<String> colnamesTop = matrix.getColnamesTop();
        int rowNumTop = matrix.getRownamesTop().size();
        for(int i = 0; i < colnamesTop.size(); i++) {
            int[] colVec = new int[rowNumTop];
            for(int j = 0; j < rowNumTop; j++) {
                colVec[j] = matTop[j][i];
            }
            Cluster mem = new Member(colnamesTop.get(i), null, null, 0, 0, colVec);
            clustersColTop.add(mem);
        }
        
        List<String> colnamesBottom = matrix.getColnamesBottom();
        int rowNumBottom = matrix.getRownamesBottom().size();
        for(int i = 0; i < colnamesBottom.size(); i++) {
            int[] colVec = new int[rowNumBottom];
            for(int j = 0; j < rowNumBottom; j++) {
                colVec[j] = matBottom[j][i];
            }
            Cluster mem = new Member(colnamesBottom.get(i), null, null, 0, 0, colVec);
            clustersColBottom.add(mem);
        }
    }
    
    public void mkHClusters() throws InterruptedException, ExecutionException {
        HClustAve hClustAveRowTop = new HClustAve(clustersRowTop, pargs);
        HClustAve hClustAveColTop = new HClustAve(clustersColTop, pargs);
        HClustAve hClustAveRowBottom = new HClustAve(clustersRowBottom, pargs);
        HClustAve hClustAveColBottom = new HClustAve(clustersColBottom, pargs);
        topClstRowTop = hClustAveRowTop.hClust();
        topClstColTop = hClustAveColTop.hClust();
        topClstRowBottom = hClustAveRowBottom.hClust();
        topClstColBottom = hClustAveColBottom.hClust();
    }
    
    public void orderHeatmap() {
        List<String> orderedIdRowTop = topClstRowTop.getOrderedId();
        List<String> orderedIdColTop = topClstColTop.getOrderedId();
        List<String> orderedIdRowBottom = topClstRowBottom.getOrderedId();
        List<String> orderedIdColBottom = topClstColBottom.getOrderedId();
        
        List<Integer> indexFromToRowTop = new ArrayList();
        for(String id: matrix.getRownamesTop()) {
            for(int i = 0; i < orderedIdRowTop.size(); i++) {
                if(id.equals(orderedIdRowTop.get(i))) {
                    indexFromToRowTop.add(i);
                    break;
                }
            }
        }
        
        List<Integer> indexFromToColTop = new ArrayList();
        for(String id: matrix.getColnamesTop()) {
            for(int i = 0; i < orderedIdColTop.size(); i++) {
                if(id.equals(orderedIdColTop.get(i))) {
                    indexFromToColTop.add(i);
                    break;
                }
            }
        }
        
        List<Integer> indexFromToRowBottom = new ArrayList();
        for(String id: matrix.getRownamesBottom()) {
            for(int i = 0; i < orderedIdRowBottom.size(); i++) {
                if(id.equals(orderedIdRowBottom.get(i))) {
                    indexFromToRowBottom.add(i);
                    break;
                }
            }
        }
        
        List<Integer> indexFromToColBottom = new ArrayList();
        for(String id: matrix.getColnamesBottom()) {
            for(int i = 0; i < orderedIdColBottom.size(); i++) {
                if(id.equals(orderedIdColBottom.get(i))) {
                    indexFromToColBottom.add(i);
                    break;
                }
            }
        }
        
        // order matrix
        int[][] orderedMatrixTop = new int[orderedIdRowTop.size()][orderedIdColTop.size()];
        int[][] orderedMatrixBottom = new int[orderedIdRowBottom.size()][orderedIdColBottom.size()];
        int[][] matrixTop = matrix.getMatrixTop();
        int[][] matrixBottom = matrix.getMatrixBottom();
        
        for(int i = 0; i < indexFromToRowTop.size(); i++) {
            for(int j = 0; j < indexFromToColTop.size(); j++) {
                orderedMatrixTop[i][j] = matrixTop[indexFromToRowTop.get(i)][indexFromToColTop.get(j)];
            }
        }
        for(int i = 0; i < indexFromToRowBottom.size(); i++) {
            for(int j = 0; j < indexFromToColBottom.size(); j++) {
                orderedMatrixBottom[i][j] = matrixBottom[indexFromToRowBottom.get(i)][indexFromToColBottom.get(j)];
            }
        }
        
        HeatmapMatrix afterClustering = new HeatmapMatrix(
                orderedMatrixTop, orderedIdRowTop, orderedIdColTop, 
                orderedMatrixBottom, orderedIdRowBottom, orderedIdColBottom);
        this.matrix = afterClustering;
    }
    
    public HeatmapMatrix getHeatmap() {
        return this.matrix;
    }
}
