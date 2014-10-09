package bio.tit.narise.rsas.controller.mode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.RSASResults;

/**
 *
 * @author TN
 */
public abstract class Mode {
    
    protected ParsedArgs pargs = null;
    
    public Mode(ParsedArgs pargs) {
        this.pargs = pargs;
    }
    
    public static Mode getInstance(ParsedArgs pargs) {
        
        Mode currentMode = null;
        if(pargs.isGseaMode()) {
            if(pargs.isFCRRnkMode()) {
                currentMode = GSEARnkMode.getInstance(pargs);
            }
            else if(pargs.isFCRRnkpMode()) {
                currentMode = GSEARnkpMode.getInstance(pargs);
            }
        }
        else if(pargs.isFiltMode()){
            if(pargs.isFCRRnkMode()) {
                currentMode = FiltRnkMode.getInstance(pargs);
            }
            else if(pargs.isFCRRnkpMode()) {
                currentMode = FiltRnkpMode.getInstance(pargs);
            }
        }
        else {
            currentMode = ClstMode.getInstance(pargs);
        }
        return currentMode;
    }
    
    public abstract RSASResults handle() throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException;
    
}
