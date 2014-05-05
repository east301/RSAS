package bio.tit.narise.rsas.model.factory.product;

import java.util.List;

/**
 *
 * @author TN
 */
public class OrderedItemSetRes extends Result {
    
    private final String itemSetID;
    private final String description;
    private final List<String> orderedItems;
    private final int m;
    
    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public OrderedItemSetRes(String itemSetID, String description, List<String> orderedItems) {
        this.itemSetID = itemSetID;
        this.description = description;
        this.orderedItems = orderedItems;
        this.m = orderedItems.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.itemSetID).append("\t").append(this.description).append("\t");
        for(String item: orderedItems){ sb.append(item).append("\t");}
        return (sb.toString().trim());
    }
    
    /**
     * @return the itemSetID
     */
    public String getItemSetID() {
        return itemSetID;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the orderedItems
     */
    public List<String> getOrderedItems() {
        return orderedItems;
    }

    /**
     * @return the number of items
     */
    public int getNumOfItems() {
        return m;
    }

}
