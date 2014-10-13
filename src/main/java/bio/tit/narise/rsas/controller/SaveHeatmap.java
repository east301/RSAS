/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.tit.narise.rsas.controller;

import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Takafumi
 */
public class SaveHeatmap {
    // this is an utility class
    private SaveHeatmap(){ throw new UnsupportedOperationException(); }
    
    static void save(HeatmapMatrix matrix) throws IOException {
        int matrixTop[][] = matrix.getMatrixTop();
        List<String> rownamesTop = matrix.getRownamesTop();
        List<String> colnamesTop = matrix.getColnamesTop();

        int matrixBottom[][] = matrix.getMatrixBottom();
        List<String> rownamesBottom = matrix.getRownamesBottom();
        List<String> colnamesBottom = matrix.getColnamesBottom();
        
        
        File ofileTop = new File("heamapEnrichedAtTop.tsv");
        File ofileBottom = new File("heamapEnrichedAtBottom.tsv");
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofileTop)))) {
            StringBuilder sb1 = new StringBuilder();
            for(String col: colnamesTop) {
                sb1.append(col).append("\t");                
            }
            
            pw.write("\t");
            pw.write(sb1.toString().trim());
            pw.println();
            
            for(int i = 0; i < rownamesTop.size(); i++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(rownamesTop.get(i)).append("\t");
                for(int j = 0; j < colnamesTop.size(); j++) {
                    sb2.append(String.valueOf(matrixTop[i][j])).append("\t");              
                }
                pw.write(sb2.toString().trim());
                pw.println();
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ofileBottom)))) {
            StringBuilder sb1 = new StringBuilder();
            for(String col: colnamesBottom) {
                sb1.append(col).append("\t");
            }
            
            pw.write("\t");
            pw.write(sb1.toString().trim());
            pw.println();
            
            for(int i = 0; i < rownamesBottom.size(); i++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(rownamesBottom.get(i)).append("\t");
                for(int j = 0; j < colnamesBottom.size(); j++) {
                    sb2.append(String.valueOf(matrixBottom[i][j])).append("\t");
                }
                pw.write(sb2.toString().trim());
                pw.println();
            }
        }
    }
}
