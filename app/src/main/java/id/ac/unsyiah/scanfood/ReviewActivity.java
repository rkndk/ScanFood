package id.ac.unsyiah.scanfood;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.ac.unsyiah.scanfood.adapter.ReviewAdapter;
import id.ac.unsyiah.scanfood.api.Client;
import id.ac.unsyiah.scanfood.api.Service;
import id.ac.unsyiah.scanfood.model.Review;
import id.ac.unsyiah.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar pb;
    private ImageView infoImg;
    private TextView infoText;
    private LinearLayout infoContainer;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        session = new SessionManager(getApplicationContext());

        initView();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
                Toast.makeText(ReviewActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        infoContainer = (LinearLayout) findViewById(R.id.infoContainer);
        infoImg = (ImageView) findViewById(R.id.infoImg);
        infoText = (TextView) findViewById(R.id.infoText);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        loadJSON();
    }

    private void loadJSON(){
        infoContainer.setVisibility(View.GONE);

        try {
            String token = session.getUserDetails().getMeta().getToken();
            Integer partnerId = session.getScan().getPartnerId();

            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);

            Call<List<Review>> call = apiService.getReview("Bearer " + token, partnerId);

            call.enqueue(new Callback<List<Review>>() {
                @Override
                public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                    if(response == null || response.body().isEmpty()){
                        infoContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setAdapter(new ReviewAdapter(getApplicationContext(), response.body()));
                        recyclerView.smoothScrollToPosition(0);
                        swipeContainer.setRefreshing(false);
                    }

                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<Review>> call, Throwable t) {
                    Toast.makeText(ReviewActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    infoContainer.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(ReviewActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            infoContainer.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }
}
