package id.ac.unsyiah.scanfood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.Toast;

import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.skyfishjy.library.RippleBackground;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import id.ac.unsyiah.scanfood.util.SessionManager;
import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ImageButton btnMenu, btnBerita, btnFavorite, btnMap;
    private DrawerLayout drawer;
    private FloatingActionButton btnScan;

    private Context mContext;
    private ResideMenu resideMenu;
    private ResideMenuItem itemLogin, itemRegister, itemLogout, itemProfile, itemContact, itemAbout;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        session = new SessionManager(getApplicationContext());

        session.checkScan();

        setUpObject();
        setUpMenu();
        setUpOnClick();

        //ripple background tombol scan
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.rippleBackground);
        rippleBackground.startRippleAnimation();
    }

    private void setUpObject(){
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnBerita = (ImageButton) findViewById(R.id.btnBerita);
        btnFavorite = (ImageButton) findViewById(R.id.btnFavorite);
        btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnScan = (FloatingActionButton) findViewById(R.id.btnScan);
    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.gradient_greendark);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        //resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemLogin = new ResideMenuItem(this, R.drawable.ic_login, "Login");
        itemRegister = new ResideMenuItem(this, R.drawable.ic_register, "Register");
        itemLogout = new ResideMenuItem(this, R.drawable.ic_logout, "Logout");
        itemProfile = new ResideMenuItem(this, R.drawable.ic_profile, "Profile");
        itemContact = new ResideMenuItem(this, R.drawable.ic_contact, "Contact");
        itemAbout = new ResideMenuItem(this, R.drawable.ic_about, "About");

        itemLogin.setOnClickListener(this);
        itemRegister.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemContact.setOnClickListener(this);
        itemAbout.setOnClickListener(this);

        if(!session.isLoggedIn()){
            resideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_LEFT);
            resideMenu.addMenuItem(itemRegister, ResideMenu.DIRECTION_LEFT);
        }
        else {
            resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        }
        resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
        if(session.isLoggedIn()){
            resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);
        }

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    private void setUpOnClick(){
        //tombol list favorite
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        //tombol list berita
        btnBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //tombol scan
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(session.isLoggedIn()){
                    ZxingOrient integrator = new ZxingOrient(MainActivity.this);
                    integrator.setIcon(R.drawable.ic_qrcode)   // Sets the custom icon
                            .setToolbarColor("#00B774")       // Sets Tool bar Color
                            .setInfoBoxColor("#00B774")       // Sets Info box color
                            .setInfo(getResources().getString(R.string.qr_code_message))   // Sets info message in the info box
                            .setBeep(true)  // Doesn't play beep sound
                            .setVibration(true)  // Enables the vibration
                            .initiateScan();
                }
                else {
                    FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                            //.setImageRecourse(R.drawable.ic_cross)
                            .setTextSubTitle(R.string.title_scan_failed)
                            .setBody(R.string.info_scan_failed)
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

            }
        });

        //tombol menu
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZxingOrientResult scanResult =
                ZxingOrient.parseActivityResult(requestCode, resultCode, data);

        if (scanResult != null) {
            if(scanResult.getContents() != null){
                /*Toast.makeText(MainActivity.this, scanResult.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, PartnerActivity.class);
                startActivity(intent);*/

                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                i.putExtra("qrcode", scanResult.getContents());
                startActivity(i);
            }
            else{
                //Toast.makeText(MainActivity.this, "Batal Scan", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

        if (view == itemLogin){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else if (view == itemRegister){
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(i);
        }
        else if (view == itemLogout){
            session.logoutUser();
        }
        else if (view == itemProfile){
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
        }
        else if (view == itemContact){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "scanfoodapplication@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ScanFood Report");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Pesan anda...");
            startActivity(Intent.createChooser(emailIntent, "Contact Us"));
        }
        else if (view == itemAbout){
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
}
