package bio.tit.narise.rsas.controller.mode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.product.RSASResults;

/**
 *
 * @author TN
 */
public abstract class FiltMode extends Mode {
    
    //protected ParsedArgs pargs = null;
    
    FiltMode(ParsedArgs pargs) {
        super(pargs);
    }
    
    @Override
    public abstract RSASResults handle() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException ;
    
}
