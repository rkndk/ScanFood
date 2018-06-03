package id.ac.unsyiah.scanfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import id.ac.unsyiah.scanfood.util.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private CircularImageView userPhoto;
    private TextView userName, userEmail, userAddress;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(getApplicationContext());

        userPhoto = (CircularImageView) findViewById(R.id.userPhoto);
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userAddress = (TextView) findViewById(R.id.userAddress);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(ProfileActivity.this)
                .load(session.getUserDetails().getPhoto())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(userPhoto);

        userName.setText(session.getUserDetails().getName());
        userEmail.setText(session.getUserDetails().getEmail());
        userAddress.setText(session.getUserDetails().getAddress());
    }
}
