package bio.tit.narise.rsas.model.factory.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bio.tit.narise.rsas.model.factory.product.ContriRes;
import bio.tit.narise.rsas.model.factory.product.FCRRes;
import bio.tit.narise.rsas.model.factory.product.ItemSetRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;
import bio.tit.narise.rsas.model.factory.product.PValRes;
import bio.tit.narise.rsas.model.factory.product.RunSumRes;

/**
 *
 * @author TN
 */
public class Storehouse {

    // this is an utility class
    private Storehouse(){ throw new UnsupportedOperationException(); }
    
    static List<OrderedItemSetRes> orderedItemSets = new ArrayList();
    static int setNum = 0;
    static Map<String, RunSumRes> IDandRunSumRes = new HashMap();
    static Map<String, PValRes> IDandPValRes = new HashMap();
    static Map<String, FCRRes> IDandFCRRes = new HashMap();
    static Map<String, ContriRes> IDandContriRes = new HashMap();
    static List<String> itemRnk = new ArrayList();
    static List<Double> itemP = new ArrayList();
    static int N = 0;
    static List<ItemSetRes> itemSets = new ArrayList();
    
    public static void clear() {
        orderedItemSets = new ArrayList();
        setNum = 0;
        IDandRunSumRes = new HashMap();
        IDandPValRes = new HashMap();
        IDandFCRRes = new HashMap();
        IDandContriRes = new HashMap();
        itemRnk = new ArrayList();
        itemP = new ArrayList();
        N = 0;
        itemSets = new ArrayList();
    }
}