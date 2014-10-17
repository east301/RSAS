package bio.tit.narise.rsas.controller;

import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import bio.tit.narise.rsas.model.factory.clstmode.product.SubHeatmapMatrix;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Takafumi
 */
public class SaveHeatmapUtility {

    // this is an utility class
    private SaveHeatmapUtility(){ throw new UnsupportedOperationException(); }
    
    static void saveHeatmap(HeatmapMatrix matrix) throws IOException {
        System.out.println("[ Info ] Saving matrix");
        
        int matrixTop[][] = matrix.getMatrixTop();
        List<String> rownamesTop = matrix.getRownamesTop();
        List<String> colnamesTop = matrix.getColnamesTop();

        int matrixBottom[][] = matrix.getMatrixBottom();
        List<String> rownamesBottom = matrix.getRownamesBottom();
        List<String> colnamesBottom = matrix.getColnamesBottom();
        
        File ofileTop = new File("heamapEnrichedAtTop.tsv");
        File ofileBottom = new File("heamapEnrichedAtBottom.tsv");
        
        save(ofileTop, matrixTop, rownamesTop, colnamesTop);
        save(ofileBottom, matrixBottom, rownamesBottom, colnamesBottom);
    }
    
    static void saveSubHeatmaps(List<SubHeatmapMatrix> subHeatmapsTop, List<SubHeatmapMatrix> subHeatmapsBottom) throws IOException {
        
        Comparator withColoredPixelNum = new Comparator() {
            @Override
            public int compare(Object obj0, Object obj1) {
                Integer c0 = ((SubHeatmapMatrix) obj0).getColoredPixelNum();
                Integer c1 = ((SubHeatmapMatrix) obj1).getColoredPixelNum();
                int ret;
                if ((ret = c1.compareTo(c0)) == 0) {
                    Integer p0 = ((SubHeatmapMatrix) obj0).getPixelNum();
                    Integer p1 = ((SubHeatmapMatrix) obj1).getPixelNum();
                    ret = p1.compareTo(p0);
                }
                return ret;
            }
        };
        
        Collections.sort(subHeatmapsTop, withColoredPixelNum);
        Collections.sort(subHeatmapsBottom, withColoredPixelNum);
        
        for(int i = 0; i < subHeatmapsTop.size(); i++) {
            SubHeatmapMatrix subMat = subHeatmapsTop.get(i);
            
            String filename = "heamapEnrichedAtTop_cluster" + (i+1) + "_size" + subMat.getColoredPixelNum() + ".tsv";
            File file = new File(filename);
            
            save(file, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
        }
        
        for(int i = 0; i < subHeatmapsBottom.size(); i++) {
            SubHeatmapMatrix subMat = subHeatmapsBottom.get(i);
            
            String filename = "heamapEnrichedAtBottom_cluster" + (i+1) + "_size" + subMat.getColoredPixelNum() + ".tsv";
            File file = new File(filename);
            
            save(file, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
        }
    }
    
    // private method
    static private void save (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
            StringBuilder sb1 = new StringBuilder();
            for(String col: colnames) {
                sb1.append(col).append("\t");
            }
            
            pw.write("\t");
            pw.write(sb1.toString().trim());
            pw.println();
            
            for(int i = 0; i < rownames.size(); i++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(rownames.get(i)).append("\t");
                for(int j = 0; j < colnames.size(); j++) {
                    sb2.append(String.valueOf(mat[i][j])).append("\t");
                }
                pw.write(sb2.toString().trim());
                pw.println();
            }
        }
    }
}
