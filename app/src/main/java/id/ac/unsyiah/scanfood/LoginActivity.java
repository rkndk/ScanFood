package id.ac.unsyiah.scanfood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.tuyenmonkey.mkloader.MKLoader;

import id.ac.unsyiah.scanfood.api.Client;
import id.ac.unsyiah.scanfood.api.Service;
import id.ac.unsyiah.scanfood.util.SessionManager;
import id.ac.unsyiah.scanfood.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPass;
    private Button btnLogin, btnRegister;
    private MKLoader loader;
    private ImageView logo;

    // Session Manager Class
    SessionManager session;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPass = (EditText) findViewById(R.id.inputPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loginAction();
            }
        });

        btnRegister  = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        loader = (MKLoader) findViewById(R.id.loading);
        logo = (ImageView) findViewById(R.id.logo);

        processViews(false);
    }

    private void processViews(Boolean process){
        if(process){
            loader.setVisibility(View.VISIBLE);
            logo.setVisibility(View.GONE);
        }
        else {
            loader.setVisibility(View.GONE);
            logo.setVisibility(View.VISIBLE);
        }
    }

    private void loginAction(){

        String email = inputEmail.getText().toString();
        String password = inputPass.getText().toString();

        // Check if username, password is filled
        if(email.trim().length() > 0 && password.trim().length() > 0){

            processViews(true);

            try {
                Client client = new Client();
                Service apiService = client.getClient().create(Service.class);
                Call<User> call = apiService.login(email,password);
                call.enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            User user = response.body();
                            session.createLoginSession(user);
                            //Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                            processViews(false);
                        }else {

                            processViews(false);

                            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(LoginActivity.this)
                                    //.setImageRecourse(R.drawable.ic_cross)
                                    .setTextSubTitle(R.string.title_login_failed)
                                    .setBody(R.string.info_wrong_login)
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

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        processViews(false);

                        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(LoginActivity.this)
                                //.setImageRecourse(R.drawable.ic_cross)
                                .setTextSubTitle(R.string.title_login_failed)
                                .setBody(R.string.info_connect_failed)
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
                });
            }catch (Exception e){
                processViews(false);
                Log.d("Error", e.getMessage());
                //Toast.makeText(LoginActivity.this, "Catch: "+e.toString(), Toast.LENGTH_SHORT).show();
            }

        }else{
            FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(LoginActivity.this)
                    //.setImageRecourse(R.drawable.ic_cross)
                    .setTextSubTitle(R.string.title_login_failed)
                    .setBody(R.string.info_empty_login)
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
}