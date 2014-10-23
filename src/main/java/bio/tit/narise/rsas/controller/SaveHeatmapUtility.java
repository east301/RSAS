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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Takafumi
 */
public class SaveHeatmapUtility {

    // this is an utility class
    public SaveHeatmapUtility(){ throw new UnsupportedOperationException(); }
    
    static void saveHeatmap(HeatmapMatrix matrix) throws IOException, ScriptException, NoSuchMethodException {
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
        
        File ofileTop2 = new File("clst_mode_results" + sep + "heamapEnrichedAtTopResource.tsv");
        File ofileBottom2 = new File("clst_mode_results" + sep + "heamapEnrichedAtBottomResource.tsv");
        
        saveHeatmapAsTsv(ofileTop2, matrixTop, rownamesTop, colnamesTop);
        saveHeatmapAsTsv(ofileBottom2, matrixBottom, rownamesBottom, colnamesBottom);
        
        saveHeatmapAsSvg("clst_mode_results" + sep + "heamapEnrichedAtTop", "clst_mode_results" + sep + "heamapEnrichedAtTopResource.tsv");
        saveHeatmapAsSvg("clst_mode_results" + sep + "heamapEnrichedAtBottom", "clst_mode_results" + sep + "heamapEnrichedAtBottomResource.tsv");
    }
    
    static void saveSubHeatmaps(List<SubHeatmapMatrix> subHeatmaps, int cns, String topOrBottom) throws IOException, ScriptException, NoSuchMethodException {
        
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
            
            String filename2 = filenamePrefix
                    + "_resource"
                    + ".tsv";
            File file2 = new File(filename2);
            saveHeatmapAsTsv(file2, subMat.getMatrix(), subMat.getRownames(), subMat.getColnames());
            
            saveHeatmapAsSvg(filenamePrefix, filename1);
            
            if(i == cns - 1) {
                break;
            }
        }
    }
    
    // private method
    static private void saveHeatmapAsTable (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        
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
    static private void saveHeatmapAsTsv (File f, int[][] mat, List<String> rownames, List<String> colnames) throws IOException {
        
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
    static private void saveHeatmapAsSvg (String filenamePrefix, String filename) throws IOException, ScriptException, NoSuchMethodException {

        ScriptEngineManager scriptEngineMgr = new ScriptEngineManager();
        ScriptEngine jsEngine = scriptEngineMgr.getEngineByName("JavaScript");
        
        BufferedReader brJQ = new BufferedReader(new InputStreamReader(SaveHeatmapUtility.class.getClassLoader().getResourceAsStream("jquery-1.11.1.min.js")));
        //String line;
        //while( (line = brJQ.readLine()) != null ){
        //    System.out.println(line);
        //}
        jsEngine.eval(brJQ);

        BufferedReader brD3 = new BufferedReader(new InputStreamReader(SaveHeatmapUtility.class.getClassLoader().getResourceAsStream("d3.min.js")));
        //String line2;
        //while( (line2 = brD3.readLine()) != null ){
        //    System.out.println(line2);
        //}
        jsEngine.eval(brD3);
        
        BufferedReader brDh = new BufferedReader(new InputStreamReader(SaveHeatmapUtility.class.getClassLoader().getResourceAsStream("draw_heatmap.js")));
        //String line3;
        //while( (line3 = brDh.readLine()) != null ){
        //    System.out.println(line3);
        //}
        jsEngine.eval(brDh);

        Invocable inv = (Invocable) jsEngine;
        Object jsResult = inv.invokeFunction("drawHeatmap", filename);
        
        File newDir = new File("clst_mode_results");
        if(!newDir.exists()){
            newDir.mkdir();
        }
        
        File file = new File(filenamePrefix + ".svg");
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            pw.write((String) jsResult);
        }
    }
}
