package id.ac.unsyiah.scanfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Table {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("available")
    @Expose
    private Integer available;
    @SerializedName("partner")
    @Expose
    private Partner partner;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    /**
     * No args constructor for use in serialization
     *
     */
    public Table() {
    }

    /**
     *
     * @param id
     * @param qrcode
     * @param partnerId
     * @param partner
     * @param number
     * @param available
     * @param meta
     */
    public Table(Integer id, Integer partnerId, Integer number, String qrcode, Integer available, Partner partner, Meta meta) {
        super();
        this.id = id;
        this.partnerId = partnerId;
        this.number = number;
        this.qrcode = qrcode;
        this.available = available;
        this.partner = partner;
        this.meta = meta;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
