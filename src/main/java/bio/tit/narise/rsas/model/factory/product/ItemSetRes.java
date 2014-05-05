package bio.tit.narise.rsas.model.factory.product;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author TN
 */
public class ItemSetRes extends Result {
    private String itemSetID;
    private String description;
    private Set<String> items;
    
    @Override
    public Result add(Result res) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ItemSetRes(String itemSetID, String description, Set<String> items) {
        this.itemSetID = itemSetID;
        this.description = description;
        this.items = items;
    }

    public ItemSetRes() {
        this.itemSetID = "";
        this.description = "";
        this.items =new HashSet();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.itemSetID).append("\t").append(this.description).append("\t");
        for(String item: items){ sb.append(item).append("\t");}
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
     * @return the items
     */
    public Set<String> getItems() {
        return items;
    }

    /**
     * @param itemSetID the itemSetID to set
     */
    public void setItemSetID(String itemSetID) {
        this.itemSetID = itemSetID;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Set<String> items) {
        this.items = items;
    }
}
