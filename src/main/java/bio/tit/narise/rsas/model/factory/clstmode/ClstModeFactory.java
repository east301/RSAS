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
        int[][] matTop = matrix.getMatrixTop();
        for(int i = 0; i < rownamesTop.size(); i++) {
            Cluster mem = new Member(i, rownamesTop.get(i), null, null, 0, 0, matTop[i]);
            rowClustersTop.add(mem);
        }
        List<String> rownamesBottom = matrix.getRownamesBottom();
        int[][] matBottom = matrix.getMatrixBottom();
        for(int i = 0; i < rownamesBottom.size(); i++) {
            Cluster mem = new Member(i, rownamesBottom.get(i), null, null, 0, 0, matBottom[i]);
            rowClustersBottom.add(mem);
        }
    }
    
    public void mkColClusters() {
        List<String> colnamesTop = matrix.getColnamesTop();
        int[][] matTop = matrix.getMatrixTop();
        for(int i = 0; i < colnamesTop.size(); i++) {
            Cluster mem = new Member(i, colnamesTop.get(i), null, null, 0, 0, matTop[i]);
            colClustersTop.add(mem);
        }
        List<String> colnamesBottom = matrix.getColnamesBottom();
        int[][] matBottom = matrix.getMatrixBottom();
        for(int i = 0; i < colnamesBottom.size(); i++) {
            Cluster mem = new Member(i, colnamesBottom.get(i), null, null, 0, 0, matBottom[i]);
            colClustersBottom.add(mem);
        }
    }
    
    
}
