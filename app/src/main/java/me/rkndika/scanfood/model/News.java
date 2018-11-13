package me.rkndika.scanfood.model;

/**
 * Created by include on 02/06/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("partner")
    @Expose
    private Partner partner;

    /**
     * No args constructor for use in serialization
     */
    public News() {
    }

    /**
     * @param content
     * @param id
     * @param createdAt
     * @param partnerId
     * @param partner
     */
    public News(Integer id, Integer partnerId, String content, String createdAt, Partner partner) {
        super();
        this.id = id;
        this.partnerId = partnerId;
        this.content = content;
        this.createdAt = createdAt;
        this.partner = partner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

}