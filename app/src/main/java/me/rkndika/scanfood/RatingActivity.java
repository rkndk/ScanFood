package me.rkndika.scanfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import me.rkndika.scanfood.api.Client;
import me.rkndika.scanfood.api.Service;
import me.rkndika.scanfood.model.Rating;
import me.rkndika.scanfood.model.Table;
import me.rkndika.scanfood.model.Transaction;
import me.rkndika.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {
    private CircularImageView partnerPhoto;
    private TextView partnerName, waiterName;
    private RatingBar ratingBar;
    private EditText feedback;
    private Button btnBatal, btnKirim;

    private SessionManager session;

    private Transaction transaction;
    private Table table;

    private float ratingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        session = new SessionManager(getApplicationContext());
        transaction = session.getTransaction();
        table = session.getScan();

        initViews();

    }

    private void initViews(){
        ImageButton btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });

        partnerPhoto = (CircularImageView) findViewById(R.id.partnerPhoto);
        Glide.with(this)
                .load(table.getPartner().getPhoto())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(partnerPhoto);

        partnerName = (TextView) findViewById(R.id.partnerName);
        partnerName.setText(table.getPartner().getName());

        waiterName = (TextView) findViewById(R.id.waiterName);
        waiterName.setText("Pelayan : #"+transaction.getWaiterId());

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
            }
        });

        feedback = (EditText) findViewById(R.id.feedback);

        btnBatal = (Button) findViewById(R.id.btnBatal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnKirim = (Button) findViewById(R.id.btnKirim);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimAct();
            }
        });


    }

    private void kirimAct(){
        int rating = (int) ratingValue;
        String feedbackStr = feedback.getText().toString();

        try {
            String token = session.getUserDetails().getMeta().getToken();

            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<Rating> call = apiService.review("Bearer " + token, session.getTransaction().getPartnerId(), session.getTransaction().getWaiterId(), feedbackStr, rating);
            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(Call<Rating> call, Response<Rating> response) {
                    Toast.makeText(RatingActivity.this, "Review telah terkirim", Toast.LENGTH_LONG).show();
                    session.clearScan();
                }

                @Override
                public void onFailure(Call<Rating> call, Throwable t) {
                    Toast.makeText(RatingActivity.this, "Error Sending Data!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(RatingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
