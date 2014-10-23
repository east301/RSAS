package bio.tit.narise.rsas.controller;

import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import bio.tit.narise.rsas.model.factory.clstmode.product.SubHeatmapMatrix;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gson.Gson;

/**
 *
 * @author Takafumi
 */
public class SaveHeatmapUtility {

    public SaveHeatmapUtility(){ }
    
    public void saveHeatmap(HeatmapMatrix matrix) throws IOException {
        System.out.println("[ Info ] Saving matrix");
        
        String sep = File.separator;
        File newDir = new File("clst_mode_results");
        if(!newDir.exists()){
            newDir.mkdir();
        }
        
        int matrixTop[][] = matrix.getMatrixTop();
        List<String> rownamesTop = matrix.getRownamesTop();
        List<String> colnamesTop = matrix.getColnamesTop();

        int matrixBottom[][] = matrix.getMatrixBottom();
        List<String> rownamesBottom = matrix.getRownamesBottom();
        List<String> colnamesBottom = matrix.getColnamesBottom();
        
        File ofileTop = new File("clst_mode_results" + sep + "heamapEnrichedAtTop.tsv");
        File ofileBottom = new File("clst_mode_results" + sep + "heamapEnrichedAtBottom.tsv");
        
        saveHeatmapAsTable(ofileTop, matrixTop, rownamesTop, colnamesTop);
        saveHeatmapAsTable(ofileBottom, matrixBottom, rownamesBottom, colnamesBottom);
        
        //File ofileTop2 = new File("clst_mode_results" + sep + "heamapEnrichedAtTopResource.tsv");
        //File ofileBottom2 = new File("clst_mode_results" + sep + "heamapEnrichedAtBottomResource.tsv");
        
        //saveHeatmapAsTsv(ofileTop2, matrixTop, rownamesTop, colnamesTop);
        //saveHeatmapAsTsv(ofileBottom2, matrixBottom, rownamesBottom, colnamesBottom);
        
        File ofileTop2 = new File("clst_mode_results" + sep + "heamapEnrichedAtTop.html");
        File ofileBottom2 = new File("clst_mode_results" + sep + "heamapEnrichedAtBottom.html");
        
        saveHeatmapAsHtml(ofileTop2, matrixTop, rownamesTop, colnamesTop);
        saveHeatmapAsHtml(ofileBottom2, matrixBottom, rownamesBottom, colnamesBottom);
    }
    
    public void saveSubHeatmaps(List<SubHeatmapMatrix> subHeatmaps, int cns, String topOrBottom) throws IOException {
        
        String sep = File.separator;
        File newDir = new File("clst_mode_results");
        if(!newDir.exists()){
            newDir.mkdir();
        }
        
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
            
            String filenamePrefix = "clst_mode_results" + sep + "heatmap" + topOrBottom + "_cluster" + (i+1);
            
            String filename1 = filenamePrefix 
                    + "_col" + rowNum 
                    + "_row" + colNum 
                    + "_score" + subMat.getColoredPixelNum() 
                    + ".tsv";
            File file = new File(filename1);
            saveHeatmapAsTable(file, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
            
            //String filename2 = filenamePrefix
            //        + "_resource"
            //        + ".tsv";
            //File file2 = new File(filename2);
            //saveHeatmapAsTsv(file2, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
            
            String filename2 = filenamePrefix
                    + ".html";
            File file2 = new File(filename2);
            saveHeatmapAsHtml(file2, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
            
            if(i == cns - 1) {
                break;
            }
        }
        
        // copy draw_heatmap.js
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("clst_mode_results" + sep + "draw_heatmap.js"))))) {
            BufferedReader brDh = new BufferedReader(new InputStreamReader(SaveHeatmapUtility.class.getClassLoader().getResourceAsStream("draw_heatmap.js")));
            String line;
            while( (line = brDh.readLine()) != null ){
                pw.println(line);
            }
        }
    }
    
    // private method
    private void saveHeatmapAsTable (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
            
            StringBuilder sb1 = new StringBuilder();
            pw.write("\t");

            for(String col: colnames) {
                sb1.append(col).append("\t");
            }
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
    private void saveHeatmapAsTsv (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
            
            pw.write("row\thas\tcol");
            pw.println();
            
            for(int i = 0; i < rownames.size(); i++) {
                for(int j = 0; j < colnames.size(); j++) {
                    
                    StringBuilder sb = new StringBuilder();
                    sb.append(rownames.get(i)).append("\t")
                        .append(String.valueOf(mat[i][j])).append("\t")
                        .append(colnames.get(j));
                    
                    pw.write(sb.toString());
                    pw.println();
                }
            }
        }
    }
    
    // private method
    private void saveHeatmapAsHtml (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {

        Gson gson = new Gson();
        List<RowHasCol> rhcList = new ArrayList();
        for (int i = 0; i < rownames.size(); i++) {
            for (int j = 0; j < colnames.size(); j++) {

                RowHasCol rhc = new RowHasCol(rownames.get(i), mat[i][j], colnames.get(j));
                rhcList.add(rhc);
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
            pw.println("<!DOCTYPE html>");
            pw.println("<html><head>");
            pw.println("<script src=\"http://code.jquery.com/jquery-1.11.0.min.js\" charset=\"utf-8\"></script>");
            pw.println("<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>");
            pw.println("<script src=\"./draw_heatmap.js\" charset=\"utf-8\"></script>");
            pw.println("</head><body>");
            pw.println("<div id=\"chart\"></div>");
            pw.println("<script>");
            pw.println("var data =" + gson.toJson(rhcList));
            pw.println("drawHeatmap(data)");
            pw.println("</script></body></html>");
        }
    }
    
    // private class
    private class RowHasCol {
        private final String row;
        private final int has;
        private final String col;
        
        private RowHasCol (String row, int has, String col) {
            this.row = row;
            this.has = has;
            this.col = col;
        }
    }
}
