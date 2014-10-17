package bio.tit.narise.rsas.model.factory.clstmode.product;

import java.util.List;

/**
 *
 * @author tn
 */
public class ClstModeResults {
    private final Cluster topClstRowTop;
    private final Cluster topClstColTop;
    private final Cluster topClstRowBottom;
    private final Cluster topClstColBottom;
    
    private final HeatmapMatrix heatmap;
    
    private final List<Cluster> ctClustersColTop;
    private final List<Cluster> ctClustersColBottom;
    private final List<SubHeatmapMatrix> subHeatmapsTop;
    private final List<SubHeatmapMatrix> subHeatmapsBottom;

    public ClstModeResults(HeatmapMatrix heatmap, Cluster topClstRowTop, Cluster topClstColTop, Cluster topClstRowBottom, Cluster topClstColBottom, List<SubHeatmapMatrix> subHeatmapsTop, List<SubHeatmapMatrix> subHeatmapsBottom, List<Cluster> ctClustersColTop, List<Cluster> ctClustersColBottom) {
        this.topClstRowTop = topClstRowTop;
        this.topClstColTop = topClstColTop;
        this.topClstRowBottom = topClstRowBottom;
        this.topClstColBottom = topClstColBottom;
        
        this.heatmap = heatmap;
        
        this.ctClustersColTop = ctClustersColTop;
        this.ctClustersColBottom = ctClustersColBottom;
        this.subHeatmapsTop = subHeatmapsTop;
        this.subHeatmapsBottom = subHeatmapsBottom;
    }

    /**
     * @return the topClstRowTop
     */
    public Cluster getTopClstRowTop() {
        return topClstRowTop;
    }

    /**
     * @return the topClstColTop
     */
    public Cluster getTopClstColTop() {
        return topClstColTop;
    }

    /**
     * @return the topClstRowBottom
     */
    public Cluster getTopClstRowBottom() {
        return topClstRowBottom;
    }

    /**
     * @return the topClstColBottom
     */
    public Cluster getTopClstColBottom() {
        return topClstColBottom;
    }

    /**
     * @return the heatmap
     */
    public HeatmapMatrix getHeatmap() {
        return heatmap;
    }

    /**
     * @return the ctClustersColTop
     */
    public List<Cluster> getCtClustersColTop() {
        return ctClustersColTop;
    }

    /**
     * @return the ctClustersColBottom
     */
    public List<Cluster> getCtClustersColBottom() {
        return ctClustersColBottom;
    }

    /**
     * @return the subHeatmapsTop
     */
    public List<SubHeatmapMatrix> getSubHeatmapsTop() {
        return subHeatmapsTop;
    }

    /**
     * @return the subHeatmapsBottom
     */
    public List<SubHeatmapMatrix> getSubHeatmapsBottom() {
        return subHeatmapsBottom;
    }
}
