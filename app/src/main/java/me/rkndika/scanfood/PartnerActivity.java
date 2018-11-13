package me.rkndika.scanfood;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import me.rkndika.scanfood.api.Client;
import me.rkndika.scanfood.api.Service;
import me.rkndika.scanfood.model.Favorite;
import me.rkndika.scanfood.model.Partner;
import me.rkndika.scanfood.model.Table;
import me.rkndika.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerActivity extends AppCompatActivity implements View.OnClickListener{

    private Table table;
    private Partner partner;
    private SessionManager session;

    private ImageButton btnHome, btnReview, btnPartner;
    private ImageView partnerCover;
    private SimpleRatingBar partnerRating;
    private TextView partnerName, partnerTime;
    private FloatingActionButton btnMenu, btnPay, btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        session = new SessionManager(getApplicationContext());
        table = session.getScan();
        partner = table.getPartner();

        setUpObject();

        initViews();
    }

    private void setUpObject() {
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnReview = (ImageButton) findViewById(R.id.btnReview);
        btnPartner = (ImageButton) findViewById(R.id.btnPartnerDetail);

        btnHome.setOnClickListener(this);
        btnReview.setOnClickListener(this);
        btnPartner.setOnClickListener(this);

        partnerCover = (ImageView) findViewById(R.id.partnerCover);
        partnerRating = (SimpleRatingBar) findViewById(R.id.partnerRating);
        partnerName = (TextView) findViewById(R.id.partnerName);
        partnerTime = (TextView) findViewById(R.id.partnerTime);

        btnPay = (FloatingActionButton) findViewById(R.id.btnPay);
        btnMenu = (FloatingActionButton) findViewById(R.id.btnMenu);
        btnFavorite = (FloatingActionButton) findViewById(R.id.btnFavorite);
    }

    private void initViews(){
        Glide.with(this)
                .load(partner.getCover())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .into(partnerCover);

        partnerRating.setRating(partner.getRating());
        partnerName.setText(partner.getName());
        partnerTime.setText("Buka : " + partner.getOpenTime() + " - " + partner.getCloseTime());

        if(session.getScan().getMeta().getFavoriteStatus()){
            btnFavorite.setImageResource(R.drawable.ic_heart_full);
        }

        btnFavorite.setOnClickListener(this);

        btnMenu.setOnClickListener(this);

        btnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnHome){

            if(session.getTransaction() == null){
                session.clearScan();
            }
            else if(session.getTransaction().getFinished()){

                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(PartnerActivity.this)
                        //.setImageRecourse(R.drawable.ic_cross)
                        .setTextSubTitle(R.string.title_review)
                        .setBody(R.string.info_review)
                        .setNegativeButtonText(R.string.btn_tidak)
                        .setNegativeColor(R.color.colorRed)
                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                                session.clearScan();
                            }
                        })
                        .setPositiveButtonText(R.string.btn_ya)
                        .setPositiveColor(R.color.colorPrimary)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                                Intent i = new Intent(PartnerActivity.this, RatingActivity.class);
                                startActivity(i);
                            }
                        })
                        .build();
                alert.show();
            }
            else{
                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(PartnerActivity.this)
                        //.setImageRecourse(R.drawable.ic_cross)
                        .setTextSubTitle(R.string.title_logout_scan)
                        .setBody(R.string.info_logout_scan)
                        .setPositiveButtonText(R.string.btn_ok)
                        .setPositiveColor(R.color.colorPrimary)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                alert.show();
            }


        }
        else if (v == btnReview){
            Intent i = new Intent(PartnerActivity.this, ReviewActivity.class);
            startActivity(i);
        }
        else if (v == btnPartner){
            partnerDetailAct();
        }
        else if (v == btnFavorite){
            if(table.getMeta().getFavoriteStatus()){
                btnFavorite.setImageResource(R.drawable.ic_heart);
            }
            else {
                btnFavorite.setImageResource(R.drawable.ic_heart_full);
            }
            favoriteAct();
        }
        else if (v == btnMenu){
            Intent i = new Intent(PartnerActivity.this, MenuActivity.class);
            startActivity(i);
        }
        else if (v == btnPay){
            Intent i = new Intent(PartnerActivity.this, ListCartActivity.class);
            i.putExtra("isCart", false);
            startActivity(i);
        }
    }

    private void favoriteAct(){
        try {
            String token = session.getUserDetails().getMeta().getToken();

            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<Favorite> call = apiService.favoriteToggle("Bearer " + token, partner.getId());
            call.enqueue(new Callback<Favorite>() {
                @Override
                public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                    if(response.code() == 201){
                        //favorite
                        table.getMeta().setFavoriteStatus(true);
                        session.updateScan(table);
                    }
                    else if(response.code() == 200){
                        //unfavorite
                        table.getMeta().setFavoriteStatus(false);
                        session.updateScan(table);
                    }
                }

                @Override
                public void onFailure(Call<Favorite> call, Throwable t) {
                    //Toast.makeText(PartnerActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            //Toast.makeText(PartnerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void partnerDetailAct(){
        Intent i = new Intent(PartnerActivity.this, DetailPartnerActivity.class);
        i.putExtra("id", partner.getId());
        i.putExtra("name", partner.getName());
        i.putExtra("photo", partner.getPhoto());
        i.putExtra("address", partner.getAddress());
        i.putExtra("cover", partner.getCover());
        i.putExtra("desc", partner.getDesc());
        i.putExtra("email", partner.getEmail());
        i.putExtra("phone", partner.getPhone());
        i.putExtra("rating", partner.getRating());
        i.putExtra("open_time", partner.getOpenTime());
        i.putExtra("close_time", partner.getCloseTime());
        i.putExtra("latitude", partner.getLatitude());
        i.putExtra("longitude", partner.getLongitude());
        startActivity(i);
    }
}
