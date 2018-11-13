package me.rkndika.scanfood.model;

/**
 * Created by include on 03/06/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("favorite_status")
    @Expose
    private Boolean favoriteStatus;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(Boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

}