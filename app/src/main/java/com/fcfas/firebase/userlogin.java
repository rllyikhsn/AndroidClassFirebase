package com.fcfas.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userlogin extends AppCompatActivity implements View.OnClickListener {

    Button btn_goSignUp, btn_userLogin;
    TextInputEditText ed_loginEmail, ed_loginPassword;
    ProgressDialog loading;

    //menggunakan class firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        mAuth = FirebaseAuth.getInstance();

        //deklarasi objek yang ada pada layout
        btn_goSignUp = findViewById(R.id.btn_goSignup);
        btn_userLogin = findViewById(R.id.btn_userLogin);
        ed_loginEmail = findViewById(R.id.ed_loginEmail);
        ed_loginPassword = findViewById(R.id.ed_loginPassword);

        //membuat button menjadi dapat di klik
        btn_goSignUp.setOnClickListener(this);
        btn_userLogin.setOnClickListener(this);
    }

    private void loginUser(){
        String Email = ed_loginEmail.getText().toString().trim();
        String Password = ed_loginPassword.getText().toString().trim();

        //cek kondisi jika edit text kosong
        if (Email.isEmpty()){
            ed_loginEmail.setError("Email tidak boleh kosong");
            ed_loginEmail.requestFocus();
            return;
            //cek kondisi kecocokan format email yang diinput jika tidak cocok
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            ed_loginEmail.setError("Masukkan Format Email dengan benar");
            ed_loginEmail.requestFocus();
            return;
            //cek kondisi password tidak boleh kosong
        } else if (Password.isEmpty()){
            ed_loginPassword.setError("Password tidak boleh kosong");
            ed_loginPassword.requestFocus();
            return;
            //cek kondisi panjang password
        } else if (Password.length() <= 6){
            ed_loginPassword.setError("Panjang password kurang dari 6 karakter");
            ed_loginPassword.requestFocus();
            return;
        } else {
            loading = ProgressDialog.show(userlogin.this,
                    null,
                    "Please Wait ...",
                    true,
                    false);

            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loading.dismiss();
                                finish();
                                Intent userInfo = new Intent(getApplicationContext(),
                                        userdetail.class);
                                userInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(userInfo);
                            } else {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Email tidak terdaftar",
                                        Toast.LENGTH_SHORT).show();
                                /*Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();*/
                            }
                        }
                    });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_goSignup:
                finish();
                Intent goSignup = new Intent(getApplicationContext(), usersingup.class);
                startActivity(goSignup);
                break;
            case R.id.btn_userLogin:
                loginUser();
                break;
        }
    }

    //methode ini dibuat untuk saat program dijalankan
    //saat ini method dibuat untuk cek kondisi apakah user sudah melakukan login atau belum

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, userdetail.class));
        }
    }
}
