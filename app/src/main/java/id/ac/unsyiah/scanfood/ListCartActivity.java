package id.ac.unsyiah.scanfood;

import android.app.Dialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import id.ac.unsyiah.scanfood.adapter.CartAdapter;
import id.ac.unsyiah.scanfood.adapter.OrderAdapter;
import id.ac.unsyiah.scanfood.api.Client;
import id.ac.unsyiah.scanfood.api.Service;
import id.ac.unsyiah.scanfood.model.Cart;
import id.ac.unsyiah.scanfood.model.Order;
import id.ac.unsyiah.scanfood.model.Transaction;
import id.ac.unsyiah.scanfood.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView infoImg;
    private TextView infoText, hargaTotal;
    private LinearLayout infoContainer;
    private Button btnSend, btnPay, btnCart, btnOrder;
    private Button btnCancelSend, btnCancelPay;
    private RelativeLayout loadingView;
    private LinearLayout infoPrice;

    private SessionManager session;

    private Boolean isCart;

    Call<Transaction> callSend;
    Call<Transaction> callPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cart);

        session = new SessionManager(getApplicationContext());

        //inisialisasi tampilan cart, jika tidak diintent maka isCart true
        if(getIntent().hasExtra("isCart")){
            isCart = getIntent().getExtras().getBoolean("isCart", true);
        }
        else{
            isCart = true;
        }

        initView();
    }

    private void initView(){
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingView = (RelativeLayout) findViewById(R.id.loadingView);

        btnCancelSend = (Button) findViewById(R.id.btnCancelSend);
        btnCancelSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSend();
            }
        });

        btnCancelPay = (Button) findViewById(R.id.btnCancelPay);
        btnCancelPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPay();
            }
        });

        infoPrice = (LinearLayout) findViewById(R.id.infoPrice);

        infoContainer = (LinearLayout) findViewById(R.id.infoContainer);
        infoImg = (ImageView) findViewById(R.id.infoImg);
        infoText = (TextView) findViewById(R.id.infoText);
        hargaTotal = (TextView) findViewById(R.id.hargaTotal);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAct();
            }
        });

        btnPay = (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payAct();
            }
        });

        btnCart = (Button) findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartListAct();
            }
        });

        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListAct();
            }
        });





        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);*/

        loadData();
    }

    private void loadData(){
        btnCancelSend.setVisibility(View.GONE);
        btnCancelPay.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);

        btnSend.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
        infoPrice.setVisibility(View.GONE);
        infoContainer.setVisibility(View.GONE);

        hargaTotal.setText("Rp. 0");

        initRecyclerView();
        recyclerView.setVisibility(View.VISIBLE);

        if(isCart){
            btnSend.setVisibility(View.VISIBLE);

            ArrayList<Cart> listCart = session.getCart();
            if(listCart != null && listCart.size()>0){
                //Toast.makeText(ListCartActivity.this, "Jumlah Cart : " + listCart.size(), Toast.LENGTH_LONG).show();

                recyclerView.setAdapter(new CartAdapter(getApplicationContext(), listCart));
                recyclerView.smoothScrollToPosition(0);

            }
            else {
                //Toast.makeText(ListCartActivity.this, "Tidak ada data", Toast.LENGTH_LONG).show();
                infoContainer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        else {
            btnPay.setVisibility(View.VISIBLE);

            Transaction transaction = session.getTransaction();
            if(transaction != null && transaction.getItems().size()>0){
                //Toast.makeText(ListCartActivity.this, "Jumlah Cart : " + listCart.size(), Toast.LENGTH_LONG).show();

                recyclerView.setAdapter(new OrderAdapter(getApplicationContext(), transaction.getItems()));
                recyclerView.smoothScrollToPosition(0);

            }
            else {
                //Toast.makeText(ListCartActivity.this, "Tidak ada data", Toast.LENGTH_LONG).show();
                infoContainer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }


    }

    private void sendAct(){
        if(session.getCart() == null || session.getCart().size() == 0){
            Toast.makeText(ListCartActivity.this, "Keranjang Kosong!", Toast.LENGTH_LONG).show();
            loadData();
        }
        else {
            //tampilin loading
            loadingView.setVisibility(View.VISIBLE);

            //tampilin btnCancelSend
            btnCancelSend.setVisibility(View.VISIBLE);

            String token = session.getUserDetails().getMeta().getToken();
            Order order = new Order(session.getScan().getId(), session.getCart());
            try {
                Client client = new Client();
                Service apiService = client.getClient().create(Service.class);
                callSend = apiService.sendOrder("Bearer " + token, order);
                callSend.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        if(response.isSuccessful()){
                            session.setTransaction(response.body());
                            session.clearCart();
                            loadData();
                        }
                        else{
                            loadData();
                            Toast.makeText(ListCartActivity.this, "Kesalahan Server!", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(ListCartActivity.this, "Kode : " + response.code(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        if(!callSend.isCanceled()){
                            loadData();
                            //Log.d("Error", t.getMessage());
                            Toast.makeText(ListCartActivity.this, "Error Connect to Server! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception e){
                Log.d("Error", e.getMessage());
                loadData();
                Toast.makeText(ListCartActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void payAct(){
        Transaction transaction = session.getTransaction();

        if(transaction == null || transaction.getItems().size() == 0){
            Toast.makeText(ListCartActivity.this, "Pesanan Kosong!", Toast.LENGTH_LONG).show();
            loadData();
        }
        else {
            //tampilin loading
            loadingView.setVisibility(View.VISIBLE);

            //tampilin btnCancelSend
            btnCancelPay.setVisibility(View.VISIBLE);

            String token = session.getUserDetails().getMeta().getToken();
            int transactionId = session.getTransaction().getId();

            try {
                Client client = new Client();
                Service apiService = client.getClient().create(Service.class);
                callPay = apiService.payOrder("Bearer " + token, transactionId);
                callPay.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        if(response.isSuccessful()){
                            session.setTransaction(response.body());
                            loadData();

                            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(ListCartActivity.this)
                                    //.setImageRecourse(R.drawable.ic_cross)
                                    .setTextSubTitle(R.string.title_pay_success)
                                    .setBody(R.string.info_pay_success)
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
                        else{
                            loadData();
                            Toast.makeText(ListCartActivity.this, "Kesalahan Server!"+response.code(), Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(ListCartActivity.this, "Kode : " + response.code(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        if(!callPay.isCanceled()){
                            loadData();
                            //Log.d("Error", t.getMessage());
                            Toast.makeText(ListCartActivity.this, "Error Connect to Server! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception e){
                Log.d("Error", e.getMessage());
                loadData();
                Toast.makeText(ListCartActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cartListAct(){
        isCart = true;
        loadData();
    }

    private void orderListAct(){
        isCart = false;
        loadData();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
    }

    private void cancelSend(){
        if(callSend != null && callSend.isExecuted()){
            callSend.cancel();
        }
        loadData();
    }

    private void cancelPay(){
        if(callPay != null && callPay.isExecuted()){
            callPay.cancel();
        }
        loadData();
    }
}
