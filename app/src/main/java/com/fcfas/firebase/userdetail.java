package com.fcfas.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class userdetail extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_userFoto;
    TextInputEditText ed_userName;
    Button btn_userSave, btn_userLogout;
    TextView tv_emailCek;
    ProgressBar progressBar;
    ProgressDialog loading;

    Uri uriuserFotoProfil;
    Uri fotoProfilUrl;
    Task<Uri> uriFotoProfilUrl;

    FirebaseAuth mAuth;
    FirebaseUser user;

    private static final int PILIH_FOTO = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);

        mAuth = FirebaseAuth.getInstance();

        //mendapatkan objek dari layout
        ed_userName = findViewById(R.id.ed_userName);
        iv_userFoto = findViewById(R.id.iv_userFoto);
        btn_userSave = findViewById(R.id.btn_userSave);
        btn_userLogout = findViewById(R.id.btn_userLogout);
        progressBar = findViewById(R.id.progressBarUpload);
        tv_emailCek = findViewById(R.id.tv_emailCek);

        //membuat button jadi dapat diklik
        iv_userFoto.setOnClickListener(this);
        btn_userSave.setOnClickListener(this);
        tv_emailCek.setOnClickListener(this);
        btn_userLogout.setOnClickListener(this);

        //menjalankan fungsi membaca user informasi
        bacaUserInformasi();

    }

    private void bacaUserInformasi(){
        user = mAuth.getCurrentUser();

        if (user != null){
            //cek kondisi userfoto tidak boleh kosong
            if (user.getPhotoUrl() != null){
                //deklarsi penempatan variabel
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(iv_userFoto);
                progressBar.setVisibility(View.GONE);
            }
            if (user.getDisplayName() != null){
                String namauser = user.getDisplayName();
                ed_userName.setText(namauser);
            }
            if (user.isEmailVerified()){
                tv_emailCek.setText("Email Sudah Terverifikasi");
            } else {
                tv_emailCek.setText("Email Belum Terverifikasi. Klik Untuk Verifikasi !");
            }
        }
    }

    private void verfiedEmail(){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(userdetail.this, "Verifikasi Email Terkirim !"
                ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //fungsi memanggil storage untuk pemilihan foto
    private void pemilihanFoto(){
        Intent pilihFoto = new Intent();
        pilihFoto.setType("image/*");
        pilihFoto.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pilihFoto, "Pilih Foto Profil")
                , PILIH_FOTO);
    }

    private void uploadFoto(){
        StorageReference fotoProfilRef =
                FirebaseStorage.getInstance().getReference("fotoUser/"+user.getUid()+"/"+
                        System.currentTimeMillis() + ".jpg");
        //cek kondisi file
        if (uriuserFotoProfil != null){
            progressBar.setVisibility(View.VISIBLE);
            fotoProfilRef.putFile(uriuserFotoProfil)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            uriFotoProfilUrl = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriFotoProfilUrl.isComplete());
                            fotoProfilUrl = uriFotoProfilUrl.getResult();



                            /*Toast.makeText(userdetail.this, "Upload Success, download URL " +
                                    fotoProfilUrl.toString(), Toast.LENGTH_LONG).show();
                            Log.i("FBApp1 URL ", fotoProfilUrl.toString());*/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(userdetail.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveInformasiUser() {
        String namaUser = ed_userName.getText().toString();

        //cek kondisi edit text tidak boleh kosong
        if (namaUser.isEmpty()){
            ed_userName.setError("Masukkan Nama");
            ed_userName.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        //cek kondisi user tidak boleh kososng
        if (user != null && fotoProfilUrl != null){
            loading = ProgressDialog.show(userdetail.this,
                    null,
                    "Please Wait ...",
                    true,
                    false);

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(namaUser)
                    .setPhotoUri(fotoProfilUrl)
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                loading.dismiss();
                                Toast.makeText(userdetail.this, "Profil Updated",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void userLogout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(userdetail.this, home.class));
    }

    //merubah tampilan foto setelah pemilihan file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PILIH_FOTO && resultCode == RESULT_OK && data != null &&
                data.getData() != null){
            uriuserFotoProfil = data.getData();
            try {
                Bitmap foto = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        uriuserFotoProfil);
                iv_userFoto.setImageBitmap(foto);

                //memanggil fungsi upload foto
                uploadFoto();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, userlogin.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userFoto:
                pemilihanFoto();
                break;
            case R.id.btn_userSave:
                saveInformasiUser();
                break;
            case R.id.tv_emailCek:
                verfiedEmail();
                break;
            case R.id.btn_userLogout:
                userLogout();
                break;
        }
    }
}
