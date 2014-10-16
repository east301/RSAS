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
        System.out.println("[ Info ] Reading matrix");
        List<String> rownamesTop = matrix.getRownamesTop();
        int colNumTop = matrix.getColnamesTop().size();
        int[][] matTop = matrix.getMatrixTop();
        for(int i = 0; i < rownamesTop.size(); i++) {
            int[] rowVec = new int[colNumTop];
            for(int j = 0; j < colNumTop; j++) {
                rowVec[j] = matTop[i][j];
            }
            Cluster mem = new Member(i, rownamesTop.get(i), null, null, 0, 0, rowVec);
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
            Cluster mem = new Member(i, rownamesBottom.get(i), null, null, 0, 0, rowVec);
            clustersRowBottom.add(mem);
        }

        List<String> colnamesTop = matrix.getColnamesTop();
        int rowNumTop = matrix.getRownamesTop().size();
        for(int i = 0; i < colnamesTop.size(); i++) {
            int[] colVec = new int[rowNumTop];
            for(int j = 0; j < rowNumTop; j++) {
                colVec[j] = matTop[j][i];
            }
            Cluster mem = new Member(i, colnamesTop.get(i), null, null, 0, 0, colVec);
            clustersColTop.add(mem);
        }
        
        List<String> colnamesBottom = matrix.getColnamesBottom();
        int rowNumBottom = matrix.getRownamesBottom().size();
        for(int i = 0; i < colnamesBottom.size(); i++) {
            int[] colVec = new int[rowNumBottom];
            for(int j = 0; j < rowNumBottom; j++) {
                colVec[j] = matBottom[j][i];
            }
            Cluster mem = new Member(i, colnamesBottom.get(i), null, null, 0, 0, colVec);
            clustersColBottom.add(mem);
        }
    }
    
    public void mkHClusters() throws InterruptedException, ExecutionException {
        System.out.println("[ Info ] Clustering");
        System.out.println("Clustering of the item sets enriched at the top");
        HClustAve hClustAveRowTop = new HClustAve(clustersRowTop, pargs);
        topClstRowTop = hClustAveRowTop.hClust();
        System.out.println("Clustering of the items in the item sets enriched at the top");
        HClustAve hClustAveColTop = new HClustAve(clustersColTop, pargs);
        topClstColTop = hClustAveColTop.hClust();
        System.out.println("Clustering of the item sets enriched at the bottom");
        HClustAve hClustAveRowBottom = new HClustAve(clustersRowBottom, pargs);
        topClstRowBottom = hClustAveRowBottom.hClust();
        System.out.println("Clustering of the items in the item sets enriched at the bottom");
        HClustAve hClustAveColBottom = new HClustAve(clustersColBottom, pargs);
        topClstColBottom = hClustAveColBottom.hClust();
    }
    
    public void orderHeatmap() {
        System.out.println("[ Info ] Sorting matrix");
        List<Integer> orderedIdRowTop = topClstRowTop.getOrderedId();
        List<Integer> orderedIdColTop = topClstColTop.getOrderedId();
        List<Integer> orderedIdRowBottom = topClstRowBottom.getOrderedId();
        List<Integer> orderedIdColBottom = topClstColBottom.getOrderedId();
        
        
        // order matrix
        int[][] orderedMatrixTop = new int[orderedIdRowTop.size()][orderedIdColTop.size()];
        int[][] orderedMatrixBottom = new int[orderedIdRowBottom.size()][orderedIdColBottom.size()];
        int[][] matrixTop = matrix.getMatrixTop();
        int[][] matrixBottom = matrix.getMatrixBottom();
        
        for(int i = 0; i < orderedIdRowTop.size(); i++) {
            for(int j = 0; j < orderedIdColTop.size(); j++) {
                orderedMatrixTop[i][j] = matrixTop[orderedIdRowTop.get(i)][orderedIdColTop.get(j)];
            }
        }
        for(int i = 0; i < orderedIdRowBottom.size(); i++) {
            for(int j = 0; j < orderedIdColBottom.size(); j++) {
                orderedMatrixBottom[i][j] = matrixBottom[orderedIdRowBottom.get(i)][orderedIdColBottom.get(j)];
            }
        }
        
        HeatmapMatrix afterClustering = new HeatmapMatrix(
                orderedMatrixTop, topClstRowTop.getOrderedName(), topClstColTop.getOrderedName(), 
                orderedMatrixBottom, topClstRowBottom.getOrderedName(), topClstColBottom.getOrderedName());
        this.matrix = afterClustering;
    }
    
    public HeatmapMatrix getHeatmap() {
        return this.matrix;
    }
}
