package bio.tit.narise.rsas.model.factory.clstmode;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.mode.factory.clsmode.product.Cluster;
import bio.tit.narise.rsas.mode.factory.clsmode.product.HeatmapMatrix;
import bio.tit.narise.rsas.mode.factory.clsmode.product.Member;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tn
 */
public class ClstModeFactory {
    
    private final ParsedArgs pargs;
    private HeatmapMatrix matrix;
    private List<Cluster> rowClustersTop;
    private List<Cluster> rowClustersBottom;
    private List<Cluster> colClustersTop;
    private List<Cluster> colClustersBottom;
    
    public ClstModeFactory(ParsedArgs pargs){
        this.pargs = pargs;
        this.rowClustersTop = new ArrayList();
        this.rowClustersBottom = new ArrayList();
        this.colClustersTop = new ArrayList();
        this.colClustersBottom = new ArrayList();
    }
    
    public void parseXlsFile() throws IOException {
        HeatmapMatrix beforeclustering = ClstModeFileParserUtility.parseXlsFile(pargs.getXlspath(), pargs.getMinConNum());
        this.matrix = beforeclustering;
    }
    
    public void mkRowClusters() {
        List<String> rownamesTop = matrix.getRownamesTop();
        int colNumTop = matrix.getColnamesTop().size();
        int[][] matTop = matrix.getMatrixTop();
        for(int i = 0; i < rownamesTop.size(); i++) {
            int[] rowVec = new int[colNumTop];
            for(int j = 0; j < colNumTop; j++) {
                rowVec[j] = matTop[i][j];
            }
            Cluster mem = new Member(rownamesTop.get(i), null, null, 0, 0, rowVec);
            rowClustersTop.add(mem);
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
            rowClustersBottom.add(mem);
        }
    }
    
    public void mkColClusters() {
        List<String> colnamesTop = matrix.getColnamesTop();
        int rowNumTop = matrix.getRownamesTop().size();
        int[][] matTop = matrix.getMatrixTop();
        for(int i = 0; i < colnamesTop.size(); i++) {
            int[] colVec = new int[rowNumTop];
            for(int j = 0; j < rowNumTop; j++) {
                colVec[j] = matTop[j][i];
            }
            Cluster mem = new Member(colnamesTop.get(i), null, null, 0, 0, colVec);
            colClustersTop.add(mem);
        }
        List<String> colnamesBottom = matrix.getColnamesBottom();
        int rowNumBottom = matrix.getRownamesBottom().size();
        int[][] matBottom = matrix.getMatrixBottom();
        for(int i = 0; i < colnamesBottom.size(); i++) {
            int[] colVec = new int[rowNumBottom];
            for(int j = 0; j < rowNumBottom; j++) {
                colVec[j] = matBottom[j][i];
            }
            Cluster mem = new Member(colnamesBottom.get(i), null, null, 0, 0, colVec);
            colClustersBottom.add(mem);
        }
    }
    
    public void mkHierarClusters() {
        
    }
}
