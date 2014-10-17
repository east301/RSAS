package bio.tit.narise.rsas.model.factory.clstmode;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.clstmode.product.ClstModeResults;
import bio.tit.narise.rsas.model.factory.clstmode.product.Cluster;
import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import bio.tit.narise.rsas.model.factory.clstmode.product.Member;
import bio.tit.narise.rsas.model.factory.clstmode.product.SubHeatmapMatrix;
import bio.tit.narise.rsas.model.factory.clstmode.tool.CutTree;
import bio.tit.narise.rsas.model.factory.clstmode.tool.HClustAve;
import bio.tit.narise.rsas.model.factory.clstmode.tool.SubHeatmapCalculator;
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
    private HeatmapMatrix heatmap;
    private final List<Cluster> firstClustersRowTop = new ArrayList();
    private final List<Cluster> firstClustersColTop = new ArrayList();
    private final List<Cluster> firstClustersRowBottom = new ArrayList();
    private final List<Cluster> firstClustersColBottom = new ArrayList();
    private Cluster topClstRowTop = null;
    private Cluster topClstColTop = null;
    private Cluster topClstRowBottom = null;
    private Cluster topClstColBottom = null;

    private List<Cluster> ctClustersColTop = new ArrayList();
    private List<Cluster> ctClustersColBottom = new ArrayList();
    private List<SubHeatmapMatrix> subHeatmapsTop = new ArrayList();
    private List<SubHeatmapMatrix> subHeatmapsBottom = new ArrayList();
    
    public ClstModeFactory(ParsedArgs pargs){
        this.pargs = pargs;
    }
    
    public void parseXlsFile() throws IOException {
        HeatmapMatrix beforeClustering = ClstModeFileParserUtility.parseXlsFile(pargs.getXlspath(), pargs.getMinConNum());
        this.heatmap = beforeClustering;
    }
    
    public void mkClusters() {
        System.out.println("[ Info ] Reading matrix");
        List<String> rownamesTop = heatmap.getRownamesTop();
        int colNumTop = heatmap.getColnamesTop().size();
        int[][] matTop = heatmap.getMatrixTop();
        for(int i = 0; i < rownamesTop.size(); i++) {
            int[] rowVec = new int[colNumTop];
            for(int j = 0; j < colNumTop; j++) {
                rowVec[j] = matTop[i][j];
            }
            Cluster mem = new Member(i, rownamesTop.get(i), null, null, 0, 0, rowVec);
            firstClustersRowTop.add(mem);
        }
        
        List<String> rownamesBottom = heatmap.getRownamesBottom();
        int colNumBottom = heatmap.getColnamesBottom().size();
        int[][] matBottom = heatmap.getMatrixBottom();
        for(int i = 0; i < rownamesBottom.size(); i++) {
            int[] rowVec = new int[colNumBottom];
            for(int j = 0; j < colNumBottom; j++) {
                rowVec[j] = matBottom[i][j];
            }
            Cluster mem = new Member(i, rownamesBottom.get(i), null, null, 0, 0, rowVec);
            firstClustersRowBottom.add(mem);
        }

        List<String> colnamesTop = heatmap.getColnamesTop();
        int rowNumTop = heatmap.getRownamesTop().size();
        for(int i = 0; i < colnamesTop.size(); i++) {
            int[] colVec = new int[rowNumTop];
            for(int j = 0; j < rowNumTop; j++) {
                colVec[j] = matTop[j][i];
            }
            Cluster mem = new Member(i, colnamesTop.get(i), null, null, 0, 0, colVec);
            firstClustersColTop.add(mem);
        }
        
        List<String> colnamesBottom = heatmap.getColnamesBottom();
        int rowNumBottom = heatmap.getRownamesBottom().size();
        for(int i = 0; i < colnamesBottom.size(); i++) {
            int[] colVec = new int[rowNumBottom];
            for(int j = 0; j < rowNumBottom; j++) {
                colVec[j] = matBottom[j][i];
            }
            Cluster mem = new Member(i, colnamesBottom.get(i), null, null, 0, 0, colVec);
            firstClustersColBottom.add(mem);
        }
    }
    
    public List<Cluster> mkHClusters() throws InterruptedException, ExecutionException {
        System.out.println("[ Info ] Clustering");
        System.out.println("Clustering of the item sets enriched at the top");
        HClustAve hClustAveRowTop = new HClustAve(firstClustersRowTop, pargs);
        topClstRowTop = hClustAveRowTop.hClust();
        System.out.println("Clustering of the items in the item sets enriched at the top");
        HClustAve hClustAveColTop = new HClustAve(firstClustersColTop, pargs);
        topClstColTop = hClustAveColTop.hClust();
        System.out.println("Clustering of the item sets enriched at the bottom");
        HClustAve hClustAveRowBottom = new HClustAve(firstClustersRowBottom, pargs);
        topClstRowBottom = hClustAveRowBottom.hClust();
        System.out.println("Clustering of the items in the item sets enriched at the bottom");
        HClustAve hClustAveColBottom = new HClustAve(firstClustersColBottom, pargs);
        topClstColBottom = hClustAveColBottom.hClust();
        
        List<Cluster> clsts = new ArrayList();
        clsts.add(topClstRowTop);
        clsts.add(topClstColTop);
        clsts.add(topClstRowBottom);
        clsts.add(topClstColBottom);
        return clsts;
    }
    
    public void orderHeatmap() {
        System.out.println("[ Info ] Sorting matrix");
        int[][] matrixTop = heatmap.getMatrixTop();
        int[][] matrixBottom = heatmap.getMatrixBottom();
        List<Integer> orderedIdRowTop = topClstRowTop.getOrderedId();
        List<Integer> orderedIdColTop = topClstColTop.getOrderedId();
        List<Integer> orderedIdRowBottom = topClstRowBottom.getOrderedId();
        List<Integer> orderedIdColBottom = topClstColBottom.getOrderedId();
        
        // order matrix
        int[][] orderedMatrixTop = new int[orderedIdRowTop.size()][orderedIdColTop.size()];
        int[][] orderedMatrixBottom = new int[orderedIdRowBottom.size()][orderedIdColBottom.size()];
        
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
        this.heatmap = afterClustering;
    }
    
    public void cutTree() {
        System.out.println("[ Info ] Cutting tree");
        CutTree ctColTop = new CutTree(topClstColTop, pargs.getCutD(), pargs.getCutK());
        CutTree ctColBottom = new CutTree(topClstColBottom, pargs.getCutD(), pargs.getCutK());
        
        ctClustersColTop = ctColTop.cut();
        ctClustersColBottom = ctColBottom.cut();
    }
    
    public void getSubHeatmaps() {
        
        int minColNum = 2;
        int minRowSum = 2;
        int minRowNum = 1;
        
        int[][] matrixTop = heatmap.getMatrixTop();
        int[][] matrixBottom = heatmap.getMatrixBottom();
        List<String> orderedNameRowTop = topClstRowTop.getOrderedName();
        List<String> orderedNameRowBottom = topClstRowBottom.getOrderedName();
        
        System.out.println("[ Info ] Sorting sub matrix");
        SubHeatmapCalculator smcTop = new SubHeatmapCalculator(ctClustersColTop, matrixTop, orderedNameRowTop, minColNum, minRowSum, minRowNum);
        SubHeatmapCalculator smcBottom = new SubHeatmapCalculator(ctClustersColBottom, matrixBottom, orderedNameRowBottom, minColNum, minRowSum, minRowNum);
        
        subHeatmapsTop = smcTop.calc();
        subHeatmapsBottom = smcBottom.calc();
    }

    public ClstModeResults getResults() {
        ClstModeResults ret = new ClstModeResults(
                heatmap, topClstRowTop, topClstColTop, topClstRowBottom, topClstColBottom, 
                subHeatmapsTop, subHeatmapsBottom, ctClustersColTop, ctClustersColBottom);
        return ret;
    }
    
}
