package bio.tit.narise.rsas.controller;

import bio.tit.narise.rsas.controller.mode.ClstMode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import bio.tit.narise.rsas.controller.model.ParsedArgs;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import bio.tit.narise.rsas.model.factory.product.RSASResults;
import bio.tit.narise.rsas.controller.mode.Mode;
import bio.tit.narise.rsas.model.factory.clstmode.product.ClstModeResults;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author TN
 */
public class RSAS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, ExecutionException {

        // start time
        long start = System.currentTimeMillis();
        boolean help = false;
        
        // create ParsedArgs instance
        ParsedArgs pargs = new ParsedArgs();
        
        // create the command line parser
        CommandLineParser parser = new BasicParser();
        
        // create the Options
        Options options = new Options();
        options.addOption( "h", "help", false, "print this message" );
        
        options.addOption( OptionBuilder.withLongOpt( "rnk" )
                                .withArgName("file")
                                .withDescription( "specify the path of the rnk file (rnk file format)" )
                                .hasArg(true)
                                .create("r") );
        options.addOption( OptionBuilder.withLongOpt( "rnkp" )
                                .withArgName("file")
                                .withDescription( "specify the path of the rnkp file (rnkp file format)" )
                                .hasArg(true)
                                .create("rp") );
        options.addOption( OptionBuilder.withLongOpt( "set" )
                                .withArgName("file")
                                .withDescription( "specify the path of the set file (gmt or gmx file format)" )
                                .hasArg(true)
                                .create("s") );
        options.addOption( OptionBuilder.withLongOpt( "out" )
                                .withArgName("file")
                                .withDescription( "name output files (file extensions are added automatically)" )
                                .hasArg(true)
                                .create("o") );
        options.addOption( OptionBuilder.withLongOpt( "rep" )
                                .withArgName("file")
                                .withDescription( "specify the path of the rep file (rep file format)" )
                                .hasArg(true)
                                .create("rep") );
        options.addOption( OptionBuilder.withLongOpt( "xls" )
                                .withArgName("file")
                                .withDescription( "specify the path of the xls file (xls file format)" )
                                .hasArg(true)
                                .create("xls") );
        
        options.addOption( OptionBuilder.withLongOpt( "p-value" )
                                .withArgName("percentage")
                                .withDescription( "specify the p-value threshold" )
                                .hasArg(true)
                                .create("p") );
        options.addOption( OptionBuilder.withLongOpt( "FDR" )
                                .withArgName("percentage")
                                .withDescription( "specify the FDR threshold value" )
                                .hasArg(true)
                                .create("d") );
        options.addOption( OptionBuilder.withLongOpt( "FCR" )
                                .withArgName("percentage")
                                .withDescription( "specify the FCR threshold value" )
                                .hasArg(true)
                                .create("c") );
        
        options.addOption( OptionBuilder.withLongOpt( "max" )
                                .withArgName("number")
                                .withDescription( "specify the maximum number of items in an item set" )
                                .hasArg(true)
                                .create("ma") );
        options.addOption( OptionBuilder.withLongOpt( "min" )
                                .withArgName("number")
                                .withDescription( "specify the minimum number of items in an item set" )
                                .hasArg(true)
                                .create("mi") );
        
        options.addOption( OptionBuilder.withLongOpt( "set_min_contri" )
                                .withArgName("number")
                                .withDescription( "item sets with less than the specified number of contributors are removed from the heatmap" )
                                .hasArg(true)
                                .create("smc") );
        options.addOption( OptionBuilder.withLongOpt( "clst_min_contri" )
                                .withArgName("number")
                                .withDescription( "clusters with less than the specified number of contributors are removed from the heatmap" )
                                .hasArg(true)
                                .create("cmc") );
        options.addOption( OptionBuilder.withLongOpt( "cut_dist" )
                                .withArgName("distance")
                                .withDescription( "cut a tree from hierarchical clustering of items at the specified distance (in the range 0 to 1)" )
                                .hasArg(true)
                                .create("cutD") );
        options.addOption( OptionBuilder.withLongOpt( "cut_into_k_clst" )
                                .withArgName("number")
                                .withDescription( "cut a tree from hierarchical clustering of items into the specified number of clusters" )
                                .hasArg(true)
                                .create("cutK") );
        options.addOption( OptionBuilder.withLongOpt( "clst_num_to_save" )
                                .withArgName("number")
                                .withDescription( "specify the maximum number of clusters to save" )
                                .hasArg(true)
                                .create("cns") );
        
	options.addOption( "a", "append", false, "append output" );
	options.addOption( "f", "force", false, "force overwrite" );
        
        options.addOption( OptionBuilder.withLongOpt( "thread" )
                                .withArgName("number")
                                .withDescription( "specify the number of threads to use" )
                                .hasArg(true)
                                .create("t") );
        
        options.addOption( "gsea", "gsea_mode", false, "use gsea mode (rnk or rnkp and set files are required)" );
        options.addOption( "filt", "filt_mode", false, "use filtering mode (rep file is required)" );
        options.addOption( "clst", "clst_mode", false, "use clustering mode (xls file is required)" );
        
        options.addOption( "cr", "rank-based_FCR", false, "use rank-based FCR" );
        options.addOption( "cp", "p-value-based_FCR", false, "use p-value-based FCR" );
        
        options.addOption( "gmt", "gmt_format", false, "read set file as gmt file format (default)" );
        options.addOption( "gmx", "gmx_format", false, "read set file as gmx file format" );
        
        options.addOption( "les", "leading-edge_subset", false, "apply filtering to leading-edge subsets" );
        
        options.addOption( "jc", "jaccard_coeff", false, "use jaccard coefficient for the clustering" );
        options.addOption( "oc", "overlap_coeff", false, "use overlap coefficient for the clustering" );
        
        options.addOption( "wor", "without_report", false, "do not create rep file" );
        
        if(args.length == 0) {
            
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "RSAS", options );
        } else { try {
            
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );
            
            // for help
            if( line.hasOption( "h" ) | line.hasOption( "help" ) ) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "RSAS", options );
                help = true;
            }
            
            // for rnk file
            if( line.hasOption( "r" ) | line.hasOption( "rnk" ) ) {
                pargs.setRpath( line.getOptionValue( "r" ) );
            }
            // for rnkp file
            if( line.hasOption( "rp" ) | line.hasOption( "rnkp" ) ) {
                pargs.setRppath( line.getOptionValue( "rp" ) );
            }
            // for set file
            if( line.hasOption( "s" ) | line.hasOption( "set" ) ) {
                pargs.setSpath( line.getOptionValue( "s" ) );
            }
            // for output file
            if( line.hasOption( "o" ) | line.hasOption( "out" ) ) {
                pargs.setOprefix( line.getOptionValue( "o" ) );
            }
            // for rep file
            if( line.hasOption( "rep" ) | line.hasOption( "rep" )  ) {
                pargs.setReppath( line.getOptionValue( "rep" ) );
            }
            // for xls file
            if( line.hasOption( "xls" ) | line.hasOption( "xls" )  ) {
                pargs.setXlspath( line.getOptionValue( "xls" ) );
            }
            
            // for p-value
            if( line.hasOption( "p" ) | line.hasOption( "p-value" ) ) {
                pargs.setpVal( Double.parseDouble(line.getOptionValue( "p" )) );
            }
            // for FDR
            if( line.hasOption( "d" ) | line.hasOption( "FDR" ) ) {
                pargs.setFdr( Double.parseDouble(line.getOptionValue( "d" )) );
            }
            // for FCR
            if( line.hasOption( "c" ) | line.hasOption( "FCR" ) ) {
                pargs.setFcr( Double.parseDouble(line.getOptionValue( "c" )) );
            }
            
            // for max
            if( line.hasOption( "ma" ) | line.hasOption( "max" ) ) {
                pargs.setMax( Integer.parseInt(line.getOptionValue( "ma" )) );
            }
            // for min
            if( line.hasOption( "mi" ) | line.hasOption( "min" ) ) {
                pargs.setMin( Integer.parseInt(line.getOptionValue( "mi" )) );
            }
            
            // for smc
            if( line.hasOption( "smc" ) | line.hasOption( "set_min_contri" ) ) {
                pargs.setSetMinContri( Integer.parseInt(line.getOptionValue( "smc" )) );
            }
            
            // for cmc
            if( line.hasOption( "cmc" ) | line.hasOption( "clst_min_contri" ) ) {
                pargs.setClstMinContri( Integer.parseInt(line.getOptionValue( "cmc" )) );
            }
            
            // for cutD
            if( line.hasOption( "cutD" ) | line.hasOption( "cut_dist" ) ) {
                pargs.setCutD( Double.parseDouble(line.getOptionValue( "cutD" )) );
            }
            
            // for cutK
            if( line.hasOption( "cutK" ) | line.hasOption( "cut_into_k_clst" ) ) {
                pargs.setCutK( Integer.parseInt(line.getOptionValue( "cutK" )) );
            }
            
            // for cns
            if( line.hasOption( "cns" ) | line.hasOption( "clst_num_to_save" ) ) {
                pargs.setCns( Integer.parseInt(line.getOptionValue( "cns" )) );
            }
            
            // for append
            if( line.hasOption( "a" ) | line.hasOption( "append" ) ) {
                pargs.setAppend(true);
            }
            // for force overwrite
            if( line.hasOption( "f" ) | line.hasOption( "force" ) ) {
                pargs.setForce(true);
            }
            
            // for thread number
            if( line.hasOption( "t" ) | line.hasOption( "thread" ) ) {
                pargs.setThreadNum( Integer.parseInt(line.getOptionValue( "t" )) );
            }
            
            // for gsea mode
            if( line.hasOption( "gsea" ) | line.hasOption( "gsea_mode" ) ) {
                pargs.setGseaMode(true);
            }
            // for filtering mode
            if( line.hasOption( "filt" ) | line.hasOption( "filt_mode" ) ) {
                pargs.setFiltMode(true);
            }
            // for clustering mode
            if( line.hasOption( "clst" ) | line.hasOption( "clst_mode" ) ) {
                pargs.setClstMode(true);
            }
            
            // for rank-based FCR
            if( line.hasOption( "cr" ) | line.hasOption( "rank-based_FCR" ) ) {
                pargs.setFCRRnkMode(true);
            }
            // for p-value-based FCR
            if( line.hasOption( "cp" ) | line.hasOption( "p-value-based_FCR" ) ) {
                pargs.setFCRRnkpMode(true);
            }
            
            // for gmt
            if( line.hasOption( "gmt" ) | line.hasOption( "gmt_format" ) ) {
                pargs.setGmt(true);
            }
            // for gmx
            if( line.hasOption( "gmx" ) | line.hasOption( "gmx_format" ) ) {
                pargs.setGmx(true);
            }
            
            // for les
            if( line.hasOption( "les" ) | line.hasOption( "leading-edge_subset" ) ) {
                pargs.setLes(true);
            }
            
            // for jc
            if( line.hasOption( "jc" ) | line.hasOption( "jaccard_coeff" ) ) {
                pargs.setJc(true);
            }
            // for oc
            if( line.hasOption( "oc" ) | line.hasOption( "overlap_coeff" ) ) {
                pargs.setOc(true);
            }
            
            // for without report
            if( line.hasOption( "wor" ) | line.hasOption( "without_report" ) ) {
                pargs.setWor(true);
            }
        }
        catch( ParseException exp ) {
            
            throw new IllegalArgumentException(exp.getMessage());
        }
        
        if(!help) {
            
            pargs.checkArgs();
            
            if(pargs.isGseaMode() || pargs.isFiltMode()) {
                CreateXlsFileUtility.createSaveFile(pargs);
                CreateReportFileUtility.createReportFile(pargs);
                Mode currentMode = Mode.getInstance(pargs);
                RSASResults rsasResults = currentMode.handle();
                CreateXlsFileUtility.saveResults(rsasResults, pargs);
                CreateReportFileUtility.closeReportFile();
            }
            
            if(pargs.isClstMode()){
                ClstMode currentMode = new ClstMode(pargs);
                ClstModeResults res = currentMode.clst();
                SaveHeatmapUtility shutil = new SaveHeatmapUtility();
                shutil.saveHeatmap(res.getHeatmap());
                
                if(pargs.getCutD() < 1 || pargs.getCutK() > 1) {
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("clst_mode_results_index.html"))))) {
                        pw.println("<!DOCTYPE html>");
                        pw.println("<html><head>");
                        pw.println("</head><body>");
                    }
                    
                    if(res.getSubHeatmapsTop().size() > 0) {
                        shutil.saveSubHeatmaps(res.getSubHeatmapsTop(), pargs.getCns(), "Top");
                    }
                    if(res.getSubHeatmapsBottom().size() > 0) {
                        shutil.saveSubHeatmaps(res.getSubHeatmapsBottom(), pargs.getCns(), "Bottom");
                    }
                    
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("clst_mode_results_index.html"), true)))) {
                        pw.println("</body></html>");
                    }
                }
            }
            
            if(pargs.isGseaMode() || pargs.isFiltMode() || pargs.isClstMode()) {
                // calculation time
                System.out.println("[ Info ] Completed!");
                long stop = System.currentTimeMillis();
                System.out.println( (stop - start)/1000 + " sec" );
            }
            else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "RSAS", options );
            }
        }
    }}
}
