package bio.tit.narise.rsas.controller.mode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.factory.Factory;
import bio.tit.narise.rsas.model.factory.product.RSASResults;
import bio.tit.narise.rsas.model.factory.factory.FCRFiltRnkpModeFactory;
import bio.tit.narise.rsas.model.factory.factory.GSEAModeFactory;

/**
 *
 * @author TN
 */
public class GSEARnkpMode extends GSEAMode {
    
    //protected ParsedArgs pargs = null;
    private final String PEERNAME1 = "org.trahed.narise.rsas.model.factory.manager.GSEAModeFactory";
    private final String PEERNAME2 = "org.trahed.narise.rsas.model.factory.manager.FCRFiltRnkpModeFactory";
    
    GSEARnkpMode(ParsedArgs pargs) {
        super(pargs);
    }
    
    public static Mode getInstance(ParsedArgs pargs) {
        
        Mode currentMode = new GSEARnkpMode(pargs);
        return currentMode;
    }
    
    @Override
    public RSASResults handle() throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException {
        
        // Report
        CreateReportFileUtility.reportArgs(pargs);
        
        // create Factory
        GSEAModeFactory factory1 = new GSEAModeFactory(pargs);
        FCRFiltRnkpModeFactory factory2 = new FCRFiltRnkpModeFactory(pargs);
        
        // create results
        factory2.parseRnkOrRnkpFile();
        factory1.parseSetFile();
        factory1.calcOrderedItemSet();
        factory1.calcRunSum();
        factory1.calcPVal();
        factory2.calcFCR();
        factory2.calcContri();
        RSASResults rsasResults = factory1.createResults();
        
        // return results        
        return rsasResults;
    }
}
