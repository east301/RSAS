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
public abstract class GSEAMode extends Mode {
    
    //protected ParsedArgs pargs = null;
    GSEAMode(ParsedArgs pargs) {
        super(pargs);
    }
    
    @Override
    public abstract RSASResults handle() throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException;
    
}
