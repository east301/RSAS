package bio.tit.narise.rsas.model.factory.clstmode;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import java.io.IOException;

/**
 *
 * @author tn
 */
public class ClstModeFactory {
    
    private ParsedArgs pargs = null;
    
    public ClstModeFactory(ParsedArgs pargs){
        this.pargs = pargs;
    }

    public void parseXlsFile() throws IOException {
        ClstModeFileParserUtility.parseXlsFile(pargs.getXlspath());
    }
    
    
    
}
