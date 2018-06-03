package id.ac.unsyiah.scanfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.mikhaellopez.circularimageview.CircularImageView;

public class DetailPartnerActivity extends AppCompatActivity {
    private TextView partnerName, partnerAddress, partnerPhone, partnerEmail;
    private ImageView partnerCover;
    private CircularImageView partnerPhoto;
    private SimpleRatingBar partnerRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_partner);

        initView();

        Integer id = getIntent().getExtras().getInt("id");
        String name = getIntent().getExtras().getString("name");
        String photo = getIntent().getExtras().getString("photo");
        String address = getIntent().getExtras().getString("address");
        String cover = getIntent().getExtras().getString("cover");
        String desc = getIntent().getExtras().getString("desc");
        String email = getIntent().getExtras().getString("email");
        String phone = getIntent().getExtras().getString("phone");
        Float rating = getIntent().getExtras().getFloat("rating");
        String open_time = getIntent().getExtras().getString("open_time");
        String close_time = getIntent().getExtras().getString("close_time");
        Double latitude = getIntent().getExtras().getDouble("latitude");
        Double longitude = getIntent().getExtras().getDouble("longitude");

        partnerName.setText(name);
        partnerAddress.setText(address);
        partnerPhone.setText(phone);
        partnerEmail.setText(email);

        partnerRating.setRating(rating);

        Glide.with(this)
                .load(cover)
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(partnerCover);

        Glide.with(this)
                .load(photo)
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(partnerPhoto);


    }

    private void initView(){
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        partnerName = (TextView) findViewById(R.id.partnerName);
        partnerAddress = (TextView) findViewById(R.id.partnerAddress);
        partnerPhone = (TextView) findViewById(R.id.partnerPhone);
        partnerEmail = (TextView) findViewById(R.id.partnerEmail);
        partnerCover = (ImageView) findViewById(R.id.partnerCover);
        partnerPhoto = (CircularImageView) findViewById(R.id.partnerPhoto);
        partnerRating = (SimpleRatingBar) findViewById(R.id.partnerRating);
    }
}
