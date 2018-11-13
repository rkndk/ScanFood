package me.rkndika.scanfood;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;

import me.rkndika.scanfood.api.Client;
import me.rkndika.scanfood.api.Service;
import me.rkndika.scanfood.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private CircularImageView uploadPhoto;
    private EditText inputName, inputEmail, inputAddress, inputPass, inputPassConf;
    private Button btnRegister;

    private List<Uri> mSelected;

    public static final int REQUEST_CODE_CHOOSE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        uploadPhoto = (CircularImageView) findViewById(R.id.uploadPhoto);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputAddress = (EditText) findViewById(R.id.inputAddress);
        inputPass = (EditText) findViewById(R.id.inputPass);
        inputPassConf = (EditText) findViewById(R.id.inputPassConf);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(RegisterActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.Matisse_Zhihu)
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAct();
            }
        });
    }

    private void registerAct() {
        String nameStr = inputName.getText().toString();
        String emailStr = inputEmail.getText().toString();
        String addressStr = inputAddress.getText().toString();
        String passwordStr = inputPass.getText().toString();
        String passwordConfStr = inputPassConf.getText().toString();

        if(TextUtils.isEmpty(nameStr)) {
            inputName.setError("Nama tidak boleh kosong !");
            return;
        }

        if(TextUtils.isEmpty(emailStr)) {
            inputEmail.setError("Email tidak boleh kosong !");
            return;
        }

        if(TextUtils.isEmpty(addressStr)) {
            inputAddress.setError("Alamat tidak boleh kosong !");
            return;
        }

        if(TextUtils.isEmpty(passwordStr)) {
            inputPass.setError("Password tidak boleh kosong !");
            return;
        }

        if(TextUtils.isEmpty(passwordConfStr)) {
            inputPassConf.setError("Konfirmasi password tidak boleh kosong !");
            return;
        }

        if(!passwordStr.equals(passwordConfStr)) {
            inputPass.setError("Password dan konfirmasi password tidak sama !");
            inputPassConf.setError("Password dan konfirmasi password tidak sama !");
            return;
        }


        if(mSelected == null || mSelected.size() == 0){
            Toast.makeText(RegisterActivity.this, "Foto tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }

        File file = new File(getRealPathFromURI(mSelected.get(0)));

        if( !file.exists()){
            Toast.makeText(RegisterActivity.this, "Foto tidak ditemukan !", Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody foto = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), foto);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameStr);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailStr);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), passwordStr);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), addressStr);

        try {
            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<User> call = apiService.register(photo, name, email, password, address);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        //Toast.makeText(RegisterActivity.this, "Berhasil Register", Toast.LENGTH_LONG).show();
                        LoginActivity.start(RegisterActivity.this);
                        RegisterActivity.this.finish();
                    }else {

                        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(RegisterActivity.this)
                                //.setImageRecourse(R.drawable.ic_cross)
                                .setTextSubTitle(R.string.title_register_failed)
                                .setBody(R.string.info_register_failed)
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
                    FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(RegisterActivity.this)
                            //.setImageRecourse(R.drawable.ic_cross)
                            .setTextSubTitle(R.string.title_register_failed)
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
            Log.d("Error", e.getMessage());
            //Toast.makeText(LoginActivity.this, "Catch: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);

            Glide.with(RegisterActivity.this)
                    .load(mSelected.get(0))
                    .placeholder(R.drawable.rec_gray)
                    .error(R.drawable.rec_gray)
                    .dontAnimate()
                    .into(uploadPhoto);
        }
    }

    private String getRealPathFromURI(Uri contentURI){
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null , null);
        if(cursor == null){
            result = contentURI.getPath();
        }
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
