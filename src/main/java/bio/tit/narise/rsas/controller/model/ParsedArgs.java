package bio.tit.narise.rsas.controller.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @author TN
 */
public class ParsedArgs {
    
    private String rpath = "";
    private String spath = "";
    private String oprefix = new File(".").getAbsolutePath().substring(0, new File(".").getAbsolutePath().length()-1) 
                        + "RSAS-Results-" + new SimpleDateFormat("yyMMddkkmmssSSS").format(System.currentTimeMillis());
    private String opath = oprefix + ".xls";
    private String oreppath = oprefix + ".rep";
    private String rppath = "";
    private String reppath = "";
    private String xlspath = "";
    
    private Double pVal = null;
    private Double fdr = null;
    private Double fcr = null;
    
    private Integer max = null;
    private Integer min = null;
    private Integer mcn = null;
    
    private boolean append = false;
    private boolean force = false;
    
    private int threadNum = -1;
    
    private boolean gseaMode = false;
    private boolean filtMode = false;
    private boolean clstMode = false;
    
    private boolean FCRRnkMode = false;
    private boolean FCRRnkpMode = false;
    
    private boolean gmt = false;
    private boolean gmx = false;
    
    private boolean les = false;
    
    private boolean jc = false;
    private boolean oc = false;
    
    private boolean wor = false;
    
    /**
     * @return the rpath
     */
    public String getRpath() {
        return rpath;
    }
    /**
     * @param rpath the rpath to set
     */
    public void setRpath(String rpath) {
        this.rpath = rpath;
    }
    
    /**
     * @return the spath
     */
    public String getSpath() {
        return spath;
    }
    /**
     * @param spath the spath to set
     */
    public void setSpath(String spath) {
        this.spath = spath;
    }
    
    /**
     * @return the opath
     */
    public String getOpath() {
        return opath;
    }
    /**
     * @return the oreppath
     */
    public String getOreppath() {
        return oreppath;
    }
    /**
     * @param oprefix the oprefix to set
     */
    public void setOprefix(String oprefix) {
        this.oprefix = oprefix;
        this.opath = this.oprefix + ".xls";
        this.oreppath = this.oprefix + ".rep";
    }
    
    /**
     * @return the rppath
     */
    public String getRppath() {
        return rppath;
    }
    /**
     * @param rppath the rppath to set
     */
    public void setRppath(String rppath) {
        this.rppath = rppath;
    }
    
    /**
     * @return the reppath
     */
    public String getReppath() {
        return reppath;
    }
    /**
     * @param reppath the reppath to set
     */
    public void setReppath(String reppath) {
        this.reppath = reppath;
    }
    
    /**
     * @return the xlspath
     */
    public String getXlspath() {
        return xlspath;
    }
    /**
     * @param xlspath the xlspath to set
     */
    public void setXlspath(String xlspath) {
        this.xlspath = xlspath;
    }
    
    /**
     * @return the pVal
     */
    public double getpVal() {
        return pVal;
    }
    
    /**
     * @param pVal the pVal to set
     */
    public void setpVal(double pVal) {
        this.pVal = pVal;
    }
    
    /**
     * @return the fdr
     */
    public double getFdr() {
        return fdr;
    }
    
    /**
     * @param fdr the fdr to set
     */
    public void setFdr(double fdr) {
        this.fdr = fdr;
    }
    
    /**
     * @return the fcr
     */
    public double getFcr() {
        return fcr;
    }
    
    /**
     * @param fcr the fcr to set
     */
    public void setFcr(double fcr) {
        this.fcr = fcr;
    }
    
    /**
     * 
     * @return the max
     */
    public int getMax() {
        return max;
    }
    
    /**
     * 
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 
     * @return the min
     */
    public int getMin() {
        return min;    
    }
    
    /**
     * 
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * 
     * @return the mcn
     */
    public int getMinConNum() {
        return mcn;
    }
    
    /**
     * 
     * @param mcn the mcn to set
     */
    public void setMinConNum(int mcn) {
        this.mcn = mcn;
    }
    
    /**
     * @return the append
     */
    public boolean isAppend() {
        return append;
    }
    
    /**
     * @param append the append to set
     */
    public void setAppend(boolean append) {
        this.append = append;
    }
    
    /**
     * @return the force
     */
    public boolean isForce() {
        return force;
    }
    
    /**
     * @param force the force to set
     */
    public void setForce(boolean force) {
        this.force = force;
    }
    
    /**
     * @return the threadNum
     */
    public int getThreadNum() {
        return threadNum;
    }

    /**
     * @param threadNum the threadNum to set
     */
    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
    
    /**
     * @return the gseaMode
     */
    public boolean isGseaMode() {
        return gseaMode;
    }

    /**
     * @param gseaMode the gseaMode to set
     */
    public void setGseaMode(boolean gseaMode) {
        this.gseaMode = gseaMode;
    }

    /**
     * @return the filtMode
     */
    public boolean isFiltMode() {
        return filtMode;
    }

    /**
     * @param filtMode the filtMode to set
     */
    public void setFiltMode(boolean filtMode) {
        this.filtMode = filtMode;
    }

    /**
     * @return the clstMode
     */
    public boolean isClstMode() {
        return clstMode;
    }

    /**
     * @param clstMode the clstMode to set
     */
    public void setClstMode(boolean clstMode) {
        this.clstMode = clstMode;
    }
    
    /**
     * @return the FCRRnkMode
     */
    public boolean isFCRRnkMode() {
        return FCRRnkMode;
    }

    /**
     * @param FCRRnkMode the FCRRnkMode to set
     */
    public void setFCRRnkMode(boolean FCRRnkMode) {
        this.FCRRnkMode = FCRRnkMode;
    }

    /**
     * @return the FCRRnkpMode
     */
    public boolean isFCRRnkpMode() {
        return FCRRnkpMode;
    }

    /**
     * @param FCRRnkpMode the FCRRnkpMode to set
     */
    public void setFCRRnkpMode(boolean FCRRnkpMode) {
        this.FCRRnkpMode = FCRRnkpMode;
    }
    
    /**
     * @return the gmt
     */
    public boolean isGmt() {
        return gmt;
    }

    /**
     * @param gmt the gmt to set
     */
    public void setGmt(boolean gmt) {
        this.gmt = gmt;
    }

    /**
     * @return the gmx
     */
    public boolean isGmx() {
        return gmx;
    }

    /**
     * @param gmx the gmx to set
     */
    public void setGmx(boolean gmx) {
        this.gmx = gmx;
    }
    
    /**
     * 
     * @return the les
     */
    public boolean isLes() {
        return les;
    }
    
    /**
     * 
     * @param les the les to set
     */
    public void setLes(boolean les) {
        this.les = les;
    }
    
    /**
     * 
     * @return the jc
     */
    public boolean isJc() {
        return jc;
    }
    
    /**
     * 
     * @param jc the jc to set
     */
    public void setJc(boolean jc) {
        this.jc = jc;
    }
    
    /**
     * 
     * @return the oc
     */
    public boolean isOc() {
        return oc;
    }
    
    /**
     * 
     * @param oc the oc to set
     */
    public void setOc(boolean oc) {
        this.oc = oc;
    }
    
    /**
     * @return the wor
     */
    public boolean isWor() {
        return wor;
    }

    /**
     * @param wor the wor to set
     */
    public void setWor(boolean wor) {
        this.wor = wor;
    }
    
    // methods for checkArgs()
    public void checkArgs() throws FileNotFoundException, IOException {
        
        checkIntOptions();
        checkDoubleOptions();
        File ofile = new File(opath);
        File orepfile = new File(oreppath);
        checkOutputFile(ofile);
        checkOutputRepFile(orepfile);
        checkModeOptions();
        checkInputFileOptions();
        if(filtMode){
            if(!FCRRnkMode && !FCRRnkpMode){
                setFCRFiltMode();
            }
            checkRepFileArgs();
        }
    }
    
    private void checkIntOptions() {
        
        if(max != null && max < 0){ throw new IllegalArgumentException("Illegal maximum size of items in a set"); }
	if(min != null && min < 0){ throw new IllegalArgumentException("Illegal minimum size of items in a set"); }
        if(mcn != null && mcn < 0){ throw new IllegalArgumentException("Illegal minimum contributors number"); }
        
        if(min == null){ min = 0; }
        if(max == null){ max = -1; }
        if(mcn == null){ mcn = 1; }
        
        if(threadNum == -1){ 
            if( Runtime.getRuntime().availableProcessors() == 1 ){ threadNum = 1; }
            else if( Runtime.getRuntime().availableProcessors() > 1 ){ threadNum = Runtime.getRuntime().availableProcessors() - 1; }
        }
        if(threadNum < 1 || threadNum > Runtime.getRuntime().availableProcessors()){ throw new IllegalArgumentException("Illegal thread number"); }
    }
    
    private void checkDoubleOptions() {
        
        if(pVal != null && pVal < 0.0){ throw new IllegalArgumentException("Illegal p-value"); }
	if(fdr != null && fdr < 0.0){ throw new IllegalArgumentException("Illegal FDR value"); }
        if(fcr != null && fcr < 0.0){ throw new IllegalArgumentException("Illegal FCR value"); }
        
        if(pVal == null){ pVal = 110.0; }
        if(fdr == null){ fdr = -1.0; }
        if(fcr == null){ fcr = -1.0; }        
    }
    
    private void checkOutputFile(File file) {
        
        if(append && force){ // append = true, force = true
            throw new IllegalArgumentException("Unable to output: both -f and -a options are set TRUE");
        }
        else if(!append && !force){ // append = false, force = false
            if( file.exists() ){ throw new IllegalArgumentException("Unable to output: *.xls file exists, -f option is required to force overwrite"); }
        }
        else if(append) { // append = true, force = false
            if( file.exists() ){
                if( file.canWrite() ){
                    //O.K.
                }
                else {
                    throw new IllegalArgumentException("Unable to append output to *.xls file: unable to write (permission denied)");
                }
            }
            else {
                throw new IllegalArgumentException("Unable to append output to *.xls file: file does not exist");
            }
        }
        else if(force) { // append = false, force = true
            if( file.exists() ) {
                if ( file.canWrite() ) {
                    //O.K.
                }
                else {
                    throw new IllegalArgumentException("Unable to overwrite *.xls file: unable to write (permission denied)");
                }
            }
        }
    }
    private void checkOutputRepFile(File file) {
        
        if(!append && !force){ // append = false, force = false
            if( file.exists() ){ throw new IllegalArgumentException("Unable to output: *.rep file exists, -f option is required to force overwrite"); }
        }
        else if(append) { // append = true, force = false
            if( file.exists() ){
                if( file.canWrite() ){
                    //O.K.
                }
                else {
                    throw new IllegalArgumentException("Unable to append output to *.rep file: unable to write (permission denied)");
                }
            }
            else {
                throw new IllegalArgumentException("Unable to append output to *.rep file: file does not exist");
            }
        }
        else if(force) { // append = false, force = true
            if( file.exists() ) {
                if ( file.canWrite() ) {
                    //O.K.
                }
                else {
                    throw new IllegalArgumentException("Unable to overwrite *.rep file: unable to write (permission denied)");
                }
            }
        }
    }
    
    private void checkModeOptions() {
        
        if(gseaMode && filtMode | gseaMode && clstMode | filtMode && clstMode) { 
            throw new IllegalArgumentException("Illegal mode selection: more than one mode is selected");
        }
        else if(!gseaMode && !filtMode && !clstMode) { 
            if(!xlspath.isEmpty()){
                System.out.println("Use clustering mode");
                setClstMode(true);
            }
            if(!reppath.isEmpty()){
                System.out.println("Use filtering mode");
                setFiltMode(true);
            }
            else {
                System.out.println("Use gsea mode");
                setGseaMode(true);
            }
        }
        
        if(!clstMode) {
            if(FCRRnkMode && FCRRnkpMode){ throw new IllegalArgumentException("both -cr and -cp options are selected"); }
            else if(!FCRRnkMode && !FCRRnkpMode){
                if(!rpath.isEmpty()){
                    System.out.println("Use rank-based FCR");
                    setFCRRnkMode(true);
                }
                else if(!rppath.isEmpty()){
                    System.out.println("Use p-value-based FCR");
                    setFCRRnkpMode(true);
                }
            }
        }
        else {
            if(jc && oc) { throw new IllegalArgumentException("both -jc and -oc options are selected"); }
            else if (!jc && !oc) {
                setJc(true); // default is jaccard coefficient
            }
        }
    }
    
    private void checkInputFileOptions() {
        
        if(gseaMode){
            
            if( spath.isEmpty() ){
                throw new IllegalArgumentException("-s option is required");
            }
            if(!gmt && !gmx) {
                if(spath.endsWith(".gmx")) {
                    System.out.println("Read set file as gmx file format");
                    gmx = true;
                }
                else {
                    System.out.println("Read set file as gmt file format");
                    gmt = true;
                }
            }
            if( gmt && gmx ){
                throw new IllegalArgumentException("both -gmt and -gmx options are selected");
            }
            
            if( rpath.isEmpty() && rppath.isEmpty() ){
                throw new IllegalArgumentException("-r option or -rp option is required");
            }
            else if( !rpath.isEmpty() && rppath.isEmpty() ){
                if( FCRRnkpMode ) {
                    throw new IllegalArgumentException("rnkp file is required for calculation of p-value-based FCR");
                }
            }
            else if( !rpath.isEmpty() && !rppath.isEmpty() ){
                throw new IllegalArgumentException("both -r and -rp options are selected");
            }
        }
        else if(filtMode){
            
            if( reppath.isEmpty() ){
                throw new IllegalArgumentException("-rep option is required");
            }
        }
        else if(clstMode){
            
            if( xlspath.isEmpty() ){
                throw new IllegalArgumentException("-xls option is required");
            }
        }
    }
    
    private void setFCRFiltMode() throws FileNotFoundException, IOException {
        
        File repfile = new File(reppath);
        String filtModeString = "";
        
        if(checkBeforeReadfile(repfile)){
            
            BufferedReader repbr = new BufferedReader(new FileReader(repfile));
            String line;
            while( (line = repbr.readLine()) != null ){
                String[] lineList = line.trim().split("\t");
                if(line.startsWith("<FCR_FILTMODE>") && lineList.length == 1) {
                    filtModeString = line.replaceFirst("<FCR_FILTMODE>", "").replaceFirst("</FCR_FILTMODE>", "");
                    break;
                }
            }
            
            switch (filtModeString) {
                case "FCR_RNK":
                    System.out.println("Use rank-based FCR");
                    FCRRnkMode = true;
                    break;
                case "FCR_RNKP":
                    System.out.println("Use p-value-based FCR");
                    FCRRnkpMode = true;
                    break;
            }
        }
        else {
            
            { throw new IllegalArgumentException("Error in reading rep file: file does not exist or cannot be opened"); }
        }
    }
    private void checkRepFileArgs() throws FileNotFoundException, IOException {
        
        File repfile = new File(reppath);
        
        if(FCRRnkpMode){
        
            if(checkBeforeReadfile(repfile)){
                
                BufferedReader repbr = new BufferedReader(new FileReader(repfile));
                String line;
                while( (line = repbr.readLine()) != null ){
                    String[] lineList = line.trim().split("\t");
                    if(line.startsWith("<ITEMRNK>") && lineList.length == 1) {
                        throw new IllegalArgumentException("<ITEMRNKP> tag is required for calculation of p-value-based FCR");
                    }
                    if(line.startsWith("<ITEMRNKP>") && lineList.length == 1) {
                        break;
                    }
                }
            }
            else {
                { throw new IllegalArgumentException("Error in reading rep file: file does not exist or cannot be opened"); }
            }
        }
    }
    // private methods for setFCRFiltMode and checkRepFileArgs()
    private boolean checkBeforeReadfile(File file) {
        
        if ( file.exists() ){
            if ( file.isFile() && file.canRead() ){
                return true;
            }
        }
        return false;
    }
    
}
