package bio.tit.narise.rsas.model.factory.clstmode;

import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author tn
 */
public class ClstModeFileParserUtility {

    // this is an utility class
    private ClstModeFileParserUtility(){ throw new UnsupportedOperationException(); }
    
    public static HeatmapMatrix parseXlsFile(String xlspath, int mcn) throws FileNotFoundException, IOException {
        File xlsfile = new File(xlspath);
        System.out.println("[ Info ] Reading xls file");
        
        // variables for return
        int matrixTop[][];
        List<String> rownamesTop = new ArrayList();
        List<String> colnamesTop = new ArrayList();
        List<String[]> itemSetsTop = new ArrayList();
        int matrixBottom[][];
        List<String> rownamesBottom = new ArrayList();
        List<String> colnamesBottom = new ArrayList();
        List<String[]> itemSetsBottom = new ArrayList();
        
        boolean bottomFlag = false;
        if(checkBeforeReadfile(xlsfile)){
            BufferedReader br = new BufferedReader(new FileReader(xlsfile));
            String line;
            while( (line = br.readLine()) != null ){
                
                if(line.trim().isEmpty()) {
                    bottomFlag = true;
                    continue;
                }
                
                String[] lineList = line.trim().split("\t");
                if(lineList.length == 11) {
                    
                    String[] contris = lineList[9].split(", ");
                    if(contris.length >= mcn) {
                        if(!bottomFlag) {
                            rownamesTop.add(lineList[0]);
                            itemSetsTop.add(contris);
                            for(String contri: contris) {
                                if(!colnamesTop.contains(contri)){
                                    colnamesTop.add(contri);
                                }
                            }
                        }
                        else {
                            rownamesBottom.add(lineList[0]);
                            itemSetsBottom.add(contris);
                            for(String contri: contris) {
                                if(!colnamesBottom.contains(contri)){
                                    colnamesBottom.add(contri);
                                }
                            }
                        }
                    }
                }
            }
            
            matrixTop = new int[rownamesTop.size()][colnamesTop.size()];
            matrixBottom = new int[rownamesBottom.size()][colnamesBottom.size()];
            for(int i = 0; i < rownamesTop.size(); i++) {
                List<String> contris = Arrays.asList(itemSetsTop.get(i));
                
                for(int j = 0; j < colnamesTop.size(); j++) {
                    
                    if(contris.contains(colnamesTop.get(j))) {
                        matrixTop[i][j] = 1;
                    }
                    else {
                        matrixTop[i][j] = 0;
                    }
                }
            }
            for(int i = 0; i < rownamesBottom.size(); i++) {
                List<String> contris = Arrays.asList(itemSetsBottom.get(i));
                
                for(int j = 0; j < colnamesBottom.size(); j++) {
                    
                    if(contris.contains(colnamesBottom.get(j))) {
                        matrixBottom[i][j] = 1;
                    }
                    else {
                        matrixBottom[i][j] = 0;
                    }
                }
            }
            
            HeatmapMatrix beforeclustering = new HeatmapMatrix(matrixTop, rownamesTop, colnamesTop, matrixBottom, rownamesBottom, colnamesBottom);
            return beforeclustering;
        }
        else {
            { throw new IllegalArgumentException("Error in reading xls file: file does not exist or cannot be opened"); }
        }
    }
    
    // private methods
    private static boolean checkBeforeReadfile(File file) {
        
        if ( file.exists() ){
            if ( file.isFile() && file.canRead() ){
                return true;
            }
        }
        return false;
    }
}
