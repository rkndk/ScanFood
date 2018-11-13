package me.rkndika.scanfood.model;

/**
 * Created by include on 08/06/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    @SerializedName("menu_name")
    @Expose
    private String menuName;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("desc")
    @Expose
    private String desc;

    public Cart(Integer menuId, String menuName, Integer quantity, String desc) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.desc = desc;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
