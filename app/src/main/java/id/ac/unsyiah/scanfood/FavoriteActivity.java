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

import id.ac.unsyiah.scanfood.adapter.FavoriteAdapter;
import id.ac.unsyiah.scanfood.api.Client;
import id.ac.unsyiah.scanfood.api.Service;
import id.ac.unsyiah.scanfood.model.Favorite;
import id.ac.unsyiah.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_favorite);

        session = new SessionManager(getApplicationContext());

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

        infoContainer = (LinearLayout) findViewById(R.id.infoContainer);
        infoImg = (ImageView) findViewById(R.id.infoImg);
        infoText = (TextView) findViewById(R.id.infoText);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);


        if(session.isLoggedIn()){
            loadJSON();
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadJSON();
                    Toast.makeText(FavoriteActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            infoImg.setImageResource(R.drawable.ic_user);
            infoText.setText("Please Login");
            infoContainer.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }

    }

    private void loadJSON(){
        infoContainer.setVisibility(View.GONE);

        try {
            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            String token = session.getUserDetails().getMeta().getToken();
            Call<List<Favorite>> call = apiService.getFavorites("Bearer " + token);
            call.enqueue(new Callback<List<Favorite>>() {
                @Override
                public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {

                    if(response == null || response.body().isEmpty()){
                        infoContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setAdapter(new FavoriteAdapter(getApplicationContext(), response.body()));
                        recyclerView.smoothScrollToPosition(0);
                        swipeContainer.setRefreshing(false);
                    }

                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<Favorite>> call, Throwable t) {
                    //Log.d("Error", t.getMessage());
                    Toast.makeText(FavoriteActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    infoContainer.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(FavoriteActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            infoContainer.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }
}
