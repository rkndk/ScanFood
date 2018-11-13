package me.rkndika.scanfood;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tuyenmonkey.mkloader.MKLoader;

import me.rkndika.scanfood.api.Client;
import me.rkndika.scanfood.api.Service;
import me.rkndika.scanfood.model.Table;
import me.rkndika.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {
    private MKLoader loader;
    private TextView txtWait;
    private Button btnCancel;
    private String qrcode;

    private SessionManager session;

    private Call<Table> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        session = new SessionManager(getApplicationContext());

        qrcode = getIntent().getExtras().getString("qrcode");

        initViews();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadJSON();
            }
        }, 2000); //2 seconds


    }

    private void initViews() {
        loader = (MKLoader) findViewById(R.id.loading);
        txtWait = (TextView) findViewById(R.id.txtWait);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(call != null && call.isExecuted()){
                    call.cancel();
                }
                finish();
            }
        });
    }


    private void loadJSON(){

        try {
            String token = session.getUserDetails().getMeta().getToken();

            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            call = apiService.scan("Bearer " + token, qrcode);
            call.enqueue(new Callback<Table>() {
                @Override
                public void onResponse(Call<Table> call, Response<Table> response) {
                    if(response.isSuccessful()){
                        session.setScan(response.body());
                    }
                    else{
                        Toast.makeText(ScanActivity.this, "QR Code Tidak Dikenal", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Table> call, Throwable t) {
                    if(!call.isCanceled()){
                        Toast.makeText(ScanActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(ScanActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
