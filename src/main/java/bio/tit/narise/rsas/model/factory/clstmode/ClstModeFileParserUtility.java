package bio.tit.narise.rsas.model.factory.clstmode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author tn
 */
public class ClstModeFileParserUtility {

    // this is an utility class
    private ClstModeFileParserUtility(){ throw new UnsupportedOperationException(); }
    
    public static void parseXlsFile(String xlspath) throws FileNotFoundException, IOException {
        File xlsfile = new File(xlspath);
        System.out.println("[ Info ] Reading xls file");
        
        // developing
        
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
