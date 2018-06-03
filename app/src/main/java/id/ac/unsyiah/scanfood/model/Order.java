package id.ac.unsyiah.scanfood.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("table_id")
    @Expose
    private Integer tableId;
    @SerializedName("items")
    @Expose
    private List<Cart> items = null;


    public Order(Integer tableId, List<Cart> items) {
        this.tableId = tableId;
        this.items = items;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public List<Cart> getItems() {
        return items;
    }

    public void setItems(List<Cart> items) {
        this.items = items;
    }

}