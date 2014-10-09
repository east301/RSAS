package bio.tit.narise.rsas.controller.mode;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.clstmode.ClstModeFactory;
import bio.tit.narise.rsas.model.factory.product.RSASResults;
import java.io.IOException;

/**
 *
 * @author tn
 */
public class ClstMode extends Mode {
    
    ClstMode(ParsedArgs pargs) {
        super(pargs);
    }
    
    public static Mode getInstance(ParsedArgs pargs) {
        
        Mode currentMode = new ClstMode(pargs);
        return currentMode;
    }
    
    @Override
    public RSASResults handle() throws IOException {
        
        ClstModeFactory factory = new ClstModeFactory(this.pargs);
        factory.parseXlsFile();
        
        return null;
    }
}
