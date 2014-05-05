package bio.tit.narise.rsas.model.factory.tool;

import java.util.ArrayList;
import java.util.List;
import bio.tit.narise.rsas.model.factory.product.ItemSetRes;
import bio.tit.narise.rsas.model.factory.product.OrderedItemSetRes;

/**
 *
 * @author TN
 */
public class ToolOrderedItemSetCalculator implements Tool {
    
    private final ItemSetRes itemSet;
    private final List<String> itemRnk;
    private final int N;
    
    public ToolOrderedItemSetCalculator(List<String> itemRnk, ItemSetRes itemSet, int N) {
        this.itemRnk = itemRnk;
        this.itemSet = itemSet;
        this.N = N;
    }
    
    @Override
    public OrderedItemSetRes calc(){
        List<String> orderedItems = new ArrayList();
        for(int i = 0; i < N; i++) {
            if( itemSet.getItems().contains(itemRnk.get(i)) ) {
                orderedItems.add(itemRnk.get(i));
            }
        }
        OrderedItemSetRes orderedItemSet = new OrderedItemSetRes(itemSet.getItemSetID(), itemSet.getDescription(), orderedItems);
        return orderedItemSet;
    }
    
}
