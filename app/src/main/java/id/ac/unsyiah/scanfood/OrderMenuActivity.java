package id.ac.unsyiah.scanfood;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Locale;

import id.ac.unsyiah.scanfood.model.Cart;
import id.ac.unsyiah.scanfood.model.MenuMakanan;
import id.ac.unsyiah.scanfood.util.SessionManager;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

public class OrderMenuActivity extends AppCompatActivity {

    private SessionManager session;

    private MenuMakanan menuMakanan;
    private TextView menuName, menuPromoDesc, menuPromoTime, menuPrice, menuPromoPrice, textJumlah;
    private EditText inputKeterangan;
    private FloatingActionButton btnMinus, btnPlus;
    private Button btnPilih;
    private Integer jumlahOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);

        session = new SessionManager(getApplicationContext());

        String jsonMenu = getIntent().getExtras().getString("jsonMenu");
        Gson gson = new Gson();
        menuMakanan = gson.fromJson(jsonMenu, MenuMakanan.class);

        //inisialisasi
        jumlahOrder = 1;

        initViews();

    }

    private void initViews(){
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider);
        //bannerSlider.addBanner(new DrawableBanner(R.drawable.img_front_bg));
        if(menuMakanan.getPhoto1() != null && !menuMakanan.getPhoto1().isEmpty()){
            bannerSlider.addBanner(new RemoteBanner(menuMakanan.getPhoto1()));
        }
        if(menuMakanan.getPhoto2() != null && !menuMakanan.getPhoto2().isEmpty()){
            bannerSlider.addBanner(new RemoteBanner(menuMakanan.getPhoto2()));
        }
        if(menuMakanan.getPhoto3() != null && !menuMakanan.getPhoto3().isEmpty()){
            bannerSlider.addBanner(new RemoteBanner(menuMakanan.getPhoto3()));
        }

        menuName = (TextView) findViewById(R.id.menuName);
        menuPromoDesc = (TextView) findViewById(R.id.menuPromoDesc);
        menuPromoTime = (TextView) findViewById(R.id.menuPromoTime);
        menuPrice = (TextView) findViewById(R.id.menuPrice);
        menuPromoPrice = (TextView) findViewById(R.id.menuPromoPrice);

        menuName.setText(menuMakanan.getName());
        menuPrice.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(menuMakanan.getPrice()));
        if(menuMakanan.getPromoPrice() != null){
            //membuat tulisan dicoret
            menuPrice.setPaintFlags(menuPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            //ubah warna merah
            menuPrice.setTextColor(Color.RED);


            menuPromoDesc.setText(menuMakanan.getPromoDesc());
            menuPromoDesc.setVisibility(View.VISIBLE);

            menuPromoTime.setText(menuMakanan.getPromoTime());
            menuPromoTime.setVisibility(View.VISIBLE);

            menuPromoPrice.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(menuMakanan.getPromoPrice()));
            menuPromoPrice.setVisibility(View.VISIBLE);
        }

        textJumlah = (TextView) findViewById(R.id.textJumlah);
        textJumlah.setText(""+jumlahOrder);

        inputKeterangan = (EditText) findViewById(R.id.inputKeterangan);

        btnMinus = (FloatingActionButton) findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jumlahOrder>1){
                    jumlahOrder--;
                    textJumlah.setText(""+jumlahOrder);
                }
            }
        });

        btnPlus = (FloatingActionButton) findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlahOrder++;
                textJumlah.setText(""+jumlahOrder);
            }
        });

        btnPilih = (Button) findViewById(R.id.btnPilih);
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderAct();
            }
        });
    }

    private void orderAct(){
        if(session.getTransaction() != null && session.getTransaction().getFinished()){
            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(OrderMenuActivity.this)
                    //.setImageRecourse(R.drawable.ic_cross)
                    .setTextSubTitle(R.string.title_order_error)
                    .setBody(R.string.info_order_error)
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
        else {
            String keterangan = inputKeterangan.getText().toString();
            //Toast.makeText(OrderMenuActivity.this, "Jumlah : " + jumlahOrder + " \nKeterangan : " + keterangan, Toast.LENGTH_LONG).show();

            Cart cart = new Cart(menuMakanan.getId(), menuMakanan.getName(), jumlahOrder, keterangan);
            session.addCart(cart);
            Toast.makeText(OrderMenuActivity.this, "Pesanan telah masuk ke keranjang pemesanan", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
