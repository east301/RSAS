package bio.tit.narise.rsas.controller.mode;

import bio.tit.narise.rsas.controller.model.ParsedArgs;
import bio.tit.narise.rsas.model.factory.clstmode.ClstModeFactory;
import bio.tit.narise.rsas.model.factory.clstmode.product.HeatmapMatrix;
import bio.tit.narise.rsas.model.factory.product.RSASResults;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author tn
 */
public class ClstMode {
    private final ParsedArgs pargs;
    
    public ClstMode(ParsedArgs pargs) {
        this.pargs = pargs;
    }
    
    public HeatmapMatrix clst() throws IOException, InterruptedException, ExecutionException {
        
        ClstModeFactory factory = new ClstModeFactory(this.pargs);
        factory.parseXlsFile();
        factory.mkClusters();
        factory.mkHClusters();
        factory.orderHeatmap();
        return factory.getHeatmap();
    }

}
