package com.fcfas.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class usersingup extends AppCompatActivity implements View.OnClickListener {

    Button btn_userSignup, btn_goLogin;
    TextInputEditText ed_signupPassword,ed_signupEmail;
    ProgressDialog loading;

    //mengambil class firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersingup);

        //inisialisasi firebase auth
        mAuth = FirebaseAuth.getInstance();

        //memanggil objek pada layout
        btn_userSignup = findViewById(R.id.btn_userSignup);
        btn_goLogin = findViewById(R.id.btn_goLogin);
        ed_signupEmail = findViewById(R.id.ed_signupEmail);
        ed_signupPassword = findViewById(R.id.ed_signupPassword);

        //membuat fungsi objek button menjadi dapat di klik
        btn_userSignup.setOnClickListener(this);
        btn_goLogin.setOnClickListener(this);
    }

    private void registerUser(){
        //membuat variabel untuk mendapatkan value dari edit text
        String email = ed_signupEmail.getText().toString().trim();
        String password = ed_signupPassword.getText().toString().trim();

        //cek kondisi jika edit text kosong
        if (email.isEmpty()){
            ed_signupEmail.setError("Email tidak boleh kosong");
            ed_signupEmail.requestFocus();
            return;
            //cek kondisi kecocokan format email yang diinput jika tidak cocok
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ed_signupEmail.setError("Masukkan Format Email dengan benar");
            ed_signupEmail.requestFocus();
            return;
            //cek kondisi password tidak boleh kosong
        } else if (password.isEmpty()){
            ed_signupPassword.setError("Password tidak boleh kosong");
            ed_signupPassword.requestFocus();
            return;
            //cek kondisi panjang password
        } else if (password.length() <= 5){
            ed_signupPassword.setError("Panjang password kurang dari 6 karakter");
            ed_signupPassword.requestFocus();
            return;
        } else {
            loading = ProgressDialog.show(usersingup.this,
                    null,
                    "Please Wait ...",
                    true,
                    false);
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Pendaftaran Berhasil",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                Intent login = new Intent(getApplicationContext(),
                                        userdetail.class);
                                startActivity(login);
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    loading.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Email sudah terdaftar",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        //membuat perintah pemilihan button
        switch (v.getId()){
            case R.id.btn_userSignup:
                registerUser();
                break;
            case R.id.btn_goLogin:
                finish();
                Intent goLogin = new Intent(getApplicationContext(), userlogin.class);
                startActivity(goLogin);
                break;
        }
    }
}
