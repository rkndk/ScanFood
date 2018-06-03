package id.ac.unsyiah.scanfood.api;

import java.util.ArrayList;
import java.util.List;

import id.ac.unsyiah.scanfood.model.Cart;
import id.ac.unsyiah.scanfood.model.Favorite;
import id.ac.unsyiah.scanfood.model.MenuMakanan;
import id.ac.unsyiah.scanfood.model.News;
import id.ac.unsyiah.scanfood.model.Order;
import id.ac.unsyiah.scanfood.model.Partner;
import id.ac.unsyiah.scanfood.model.Rating;
import id.ac.unsyiah.scanfood.model.Review;
import id.ac.unsyiah.scanfood.model.Table;
import id.ac.unsyiah.scanfood.model.Transaction;
import id.ac.unsyiah.scanfood.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by include on 02/06/17.
 */

public interface Service {
    @GET("api/news")
    Call<List<News>> getNews();

    @FormUrlEncoded
    @POST("api/auth/login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @GET("api/favorites")
    Call<List<Favorite>> getFavorites(@Header("Authorization") String authHeader);

    @GET("api/scan/{qrcode}")
    Call<Table> scan(@Header("Authorization") String authHeader, @Path("qrcode") String qrcode);

    @FormUrlEncoded
    @POST("api/favorite")
    Call<Favorite> favoriteToggle(@Header("Authorization") String authHeader, @Field("partner_id") int partnerId);

    @GET("api/menu/{partner_id}")
    Call<List<MenuMakanan>> getMenu(@Header("Authorization") String authHeader, @Path("partner_id") Integer partnerId);

    @POST("api/order")
    Call<Transaction> sendOrder(@Header("Authorization") String authHeader,  @Body Order order);

    @POST("api/payorder/{transaction_id}")
    Call<Transaction> payOrder(@Header("Authorization") String authHeader,  @Path("transaction_id") Integer transactionId);

    @FormUrlEncoded
    @POST("api/review")
    Call<Rating> review(@Header("Authorization") String authHeader,
                        @Field("partner_id") Integer partnerId,
                        @Field("waiter_id") Integer waiterId,
                        @Field("content") String content,
                        @Field("rating") Integer rating);

    @GET("api/review/{partner_id}")
    Call<List<Review>> getReview(@Header("Authorization") String authHeader, @Path("partner_id") Integer partnerId);

    @Multipart
    @POST("api/auth/register")
    Call<User> register(@Part MultipartBody.Part photo,
                        @Part("name") RequestBody name,
                        @Part("email") RequestBody email,
                        @Part("password") RequestBody password,
                        @Part("address") RequestBody address);

    @GET("api/partners")
    Call<List<Partner>> getPartners();
}
