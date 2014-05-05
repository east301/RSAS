package bio.tit.narise.rsas.controller.mode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import bio.tit.narise.rsas.controller.CreateReportFileUtility;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.factory.FCRFiltRnkpModeFactory;
import bio.tit.narise.rsas.model.factory.factory.FiltModeFactory;
import bio.tit.narise.rsas.model.factory.product.RSASResults;

/**
 *
 * @author TN
 */
public class FiltRnkpMode extends FiltMode {
    
    //protected ParsedArgs pargs = null;
    private final String PEERNAME1 = "org.trahed.narise.rsas.model.factory.manager.FiltModeFactory";
    private final String PEERNAME2 = "org.trahed.narise.rsas.model.factory.manager.FCRFiltRnkpModeFactory";
    
    FiltRnkpMode(ParsedArgs pargs) {
        super(pargs);
    }
    
    public static Mode getInstance(ParsedArgs pargs) {
        
        Mode currentMode = new FiltRnkpMode(pargs);
        return currentMode;
    }

    @Override
    public RSASResults handle() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException {
        
        // Report
        CreateReportFileUtility.reportArgs(pargs);
        
        // Fcatory.getFactory
        FiltModeFactory factory1 = new FiltModeFactory(pargs);
        FCRFiltRnkpModeFactory factory2 = new FCRFiltRnkpModeFactory(pargs);
        
        // create results
        boolean isSameFCRFiltMode = factory1.parseRepFile();
        if(isSameFCRFiltMode && pargs.getFcr() == -1.0) {}
        else {
            factory2.calcFCR();
            factory2.calcContri();
        }
        RSASResults rsasResults = factory1.createResults();
        
        // return results
        return rsasResults;
    }
}
