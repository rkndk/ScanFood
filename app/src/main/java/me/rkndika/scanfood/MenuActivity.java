package me.rkndika.scanfood;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.rkndika.scanfood.adapter.MenuAdapter;
import me.rkndika.scanfood.api.Client;
import me.rkndika.scanfood.api.Service;
import me.rkndika.scanfood.model.MenuMakanan;
import me.rkndika.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private SessionManager session;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar pb;
    private ImageView infoImg;
    private TextView infoText;
    private LinearLayout infoContainer;
    private ImageButton btnPesan;
    private Button btnMinuman, btnMakanan;

    private Boolean isMakanan;

    private List<MenuMakanan> listMakanan, listMinuman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        session = new SessionManager(getApplicationContext());

        //inisial tampilan untuk makanan dulu
        isMakanan = true;

        listMakanan = new ArrayList<MenuMakanan>();
        listMinuman = new ArrayList<MenuMakanan>();

        initView();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
                Toast.makeText(MenuActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
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

        btnPesan = (ImageButton) findViewById(R.id.btnPesan);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesanAct();
            }
        });

        btnMinuman = (Button) findViewById(R.id.btnMinuman);
        btnMinuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMakanan = false;
                showList();
            }
        });

        btnMakanan = (Button) findViewById(R.id.btnMakanan);
        btnMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMakanan = true;
                showList();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        infoContainer.setVisibility(View.GONE);

        try {
            String token = session.getUserDetails().getMeta().getToken();
            Integer partnerId = session.getScan().getPartnerId();

            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<List<MenuMakanan>> call = apiService.getMenu("Bearer " + token, partnerId);
            call.enqueue(new Callback<List<MenuMakanan>>() {
                @Override
                public void onResponse(Call<List<MenuMakanan>> call, Response<List<MenuMakanan>> response) {
                    if(response == null || response.body().isEmpty()){
                        infoContainer.setVisibility(View.VISIBLE);
                    }
                    else {

                        for (int i=0; i<response.body().size(); i++){
                            if(response.body().get(i).getCatName().equalsIgnoreCase("Makanan")){
                                listMakanan.add(response.body().get(i));
                            }
                            else {
                                listMinuman.add(response.body().get(i));
                            }
                        }

                        showList();
                    }

                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<MenuMakanan>> call, Throwable t) {
                    Toast.makeText(MenuActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    infoContainer.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(MenuActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            infoContainer.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }

    private void pesanAct(){
        /*ArrayList<Cart> listCart = session.getCart();
        if(listCart != null){
            //Toast.makeText(MenuActivity.this, "Jumlah Cart : " + listCart.size(), Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(MenuActivity.this, "Tidak ada data", Toast.LENGTH_LONG).show();
        }*/
        Intent i = new Intent(MenuActivity.this, ListCartActivity.class);
        startActivity(i);
    }

    private void showList(){
        infoContainer.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);

        recyclerView.setVisibility(View.VISIBLE);

        if(isMakanan){
            if(listMakanan != null && listMakanan.size()>0){
                recyclerView.setAdapter(new MenuAdapter(getApplicationContext(), listMakanan));
                recyclerView.smoothScrollToPosition(0);
                swipeContainer.setRefreshing(false);
            }
            else {
                infoContainer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        else {
            if(listMinuman != null && listMinuman.size()>0){
                recyclerView.setAdapter(new MenuAdapter(getApplicationContext(), listMinuman));
                recyclerView.smoothScrollToPosition(0);
                swipeContainer.setRefreshing(false);

            }
            else {
                infoContainer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }


    }
}
