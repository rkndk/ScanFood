package me.rkndika.scanfood;

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

import me.rkndika.scanfood.adapter.NewsAdapter;
import me.rkndika.scanfood.api.Client;
import me.rkndika.scanfood.api.Service;
import me.rkndika.scanfood.model.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar pb;
    private ImageView infoImg;
    private TextView infoText;
    private LinearLayout infoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initView();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
                Toast.makeText(NewsActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
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
        loadJSON();
    }

    private void loadJSON(){
        infoContainer.setVisibility(View.GONE);

        try {
            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);

            Call<List<News>> call = apiService.getNews();

            call.enqueue(new Callback<List<News>>() {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    if(response == null || response.body().isEmpty()){
                        infoContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setAdapter(new NewsAdapter(getApplicationContext(), response.body()));
                        recyclerView.smoothScrollToPosition(0);
                        swipeContainer.setRefreshing(false);
                    }

                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<News>> call, Throwable t) {
                    Toast.makeText(NewsActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    infoContainer.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(NewsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            infoContainer.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }
}
