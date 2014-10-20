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
        
        saveAsHeatmapText(ofileTop, matrixTop, rownamesTop, colnamesTop);
        saveAsHeatmapText(ofileBottom, matrixBottom, rownamesBottom, colnamesBottom);
        
        File ofileTop2 = new File("heamapEnrichedAtTopResource.tsv");
        File ofileBottom2 = new File("heamapEnrichedAtBottomResource.tsv");
        
        saveAsHeatmapResource(ofileTop2, matrixTop, rownamesTop, colnamesTop);
        saveAsHeatmapResource(ofileBottom2, matrixBottom, rownamesBottom, colnamesBottom);
    }
    
    static void saveSubHeatmaps(List<SubHeatmapMatrix> subHeatmaps, int cns, String topOrBottom) throws IOException {
        
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
        
        Collections.sort(subHeatmaps, withColoredPixelNum);
        
        for(int i = 0; i < subHeatmaps.size(); i++) {
            SubHeatmapMatrix subMat = subHeatmaps.get(i);
            int rowNum = subHeatmaps.get(i).getRownames().size();
            int colNum = subHeatmaps.get(i).getColnames().size();
            
            String filename = "heatmap" + topOrBottom + "_cluster" + (i+1) 
                    + "_col" + rowNum 
                    + "_row" + colNum 
                    + "_score" + subMat.getColoredPixelNum() 
                    + ".tsv";
            File file = new File(filename);
            saveAsHeatmapText(file, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
            
            String filename2 = "heatmap" + topOrBottom + "_cluster" + (i+1)
                    + "_resource"
                    + ".tsv";
            File file2 = new File(filename2);
            saveAsHeatmapResource(file2, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
            
            if(i == cns - 1) {
                break;
            }
        }
    }
    
    // private method
    static private void saveAsHeatmapText (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        
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
    
    // private method
    static private void saveAsHeatmapResource (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
            
            pw.write("rowname\tcolname\tvalue");
            pw.println();
            
            for(int i = 0; i < rownames.size(); i++) {
                for(int j = 0; j < colnames.size(); j++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(rownames.get(i)).append("\t")
                        .append(colnames.get(j)).append("\t")
                        .append(String.valueOf(mat[i][j]));
                    
                    pw.write(sb.toString().trim());
                    pw.println();
                }
            }
        }
    }
}
