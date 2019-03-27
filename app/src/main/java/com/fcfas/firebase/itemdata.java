package com.fcfas.firebase;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Objek;

public class itemdata extends AppCompatActivity implements View.OnClickListener{

    Button btn_pushObjek,btn_popObjek;
    EditText ed_Nama,ed_Kelas,ed_Pesan;
    ProgressDialog loading;

    String pKey,pNama,pKelas,pPesan;

    //firebase
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdata);

        database = FirebaseDatabase.getInstance().getReference();

        //mengambil data yang sudah diparsing pada listdata
        pKey = getIntent().getStringExtra("Key");
        pNama = getIntent().getStringExtra("Nama");
        pKelas = getIntent().getStringExtra("Kelas");
        pPesan = getIntent().getStringExtra("Pesan");

        //mendapatkan id dari layout
        ed_Nama = findViewById(R.id.ed_Nama);
        ed_Kelas = findViewById(R.id.ed_Kelas);
        ed_Pesan = findViewById(R.id.ed_Pesan);
        btn_pushObjek = findViewById(R.id.btn_pushObjek);
        btn_popObjek = findViewById(R.id.btn_popObjek);

        //memberikan nilai hasil parsing dari data list
        ed_Nama.setText(pNama);
        ed_Kelas.setText(pKelas);
        ed_Pesan.setText(pPesan);

        //button action
        btn_pushObjek.setOnClickListener(this);
        btn_popObjek.setOnClickListener(this);

        //cek kondisi untuk kepemilikan key supaya button dinamis
        if (pKey.equals("")){
            btn_pushObjek.setText("Save");
            btn_popObjek.setText("Cancel");
        } else {
            btn_pushObjek.setText("Update");
            btn_popObjek.setText("Delete");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pushObjek:
                //get value from edit text dijadikan string
                String Nama = ed_Nama.getText().toString();
                String Kelas = ed_Kelas.getText().toString();
                String Pesan = ed_Pesan.getText().toString();

                //cek kondisi button sesuai dengan fungsi yang didapatkan dari kondisi pendapatakn key
                if (btn_pushObjek.getText().equals("Save")){
                    //perintah save
                    //Cek kondisi jika push nilai edit text kosong
                    if (ed_Nama.equals("")){
                        ed_Nama.setError("Silahkan Masukkan Nama");
                        //menggerakan cursor pada edit text yang dituju
                        ed_Nama.requestFocus();
                    } else if (ed_Kelas.equals("")){
                        ed_Kelas.setError("Silahkan Masukkan Kelas");
                        ed_Kelas.requestFocus();
                    } else if (ed_Pesan.equals("")){
                        ed_Pesan.setError("Silahkan Masukkan Pesan");
                        ed_Pesan.requestFocus();
                        //eksekusi push jika nilai semua tidak kosong
                    } else {
                        //membuat progress dialog
                        loading = ProgressDialog.show(itemdata.this,
                                null,
                                "Please Wait ...",
                                true,
                                false);

                        //menjalankan fungsi pushObjek dengan memanggil objek
                        pushObjek(new Objek(
                                Nama,
                                Kelas,
                                Pesan)
                        );
                    }
                } else {
                    //perintah edit
                    //Cek kondisi jika push nilai edit text kosong
                    if (ed_Nama.equals("")){
                        ed_Nama.setError("Silahkan Masukkan Nama");
                        //menggerakan cursor pada edit text yang dituju
                        ed_Nama.requestFocus();
                    } else if (ed_Kelas.equals("")){
                        ed_Kelas.setError("Silahkan Masukkan Kelas");
                        ed_Kelas.requestFocus();
                    } else if (ed_Pesan.equals("")){
                        ed_Pesan.setError("Silahkan Masukkan Pesan");
                        ed_Pesan.requestFocus();
                        //eksekusi push jika nilai semua tidak kosong
                    } else {
                        //membuat progress dialog
                        loading = ProgressDialog.show(itemdata.this,
                                null,
                                "Please Wait ...",
                                true,
                                false);

                        //menjalankan fungsi pushObjek dengan memanggil objek
                        updateObjek(new Objek(
                                Nama,
                                Kelas,
                                Pesan),pKey);
                    }
                }
                break;
            case R.id.btn_popObjek:
                //cek kondisi button apakah untuk delete atau cancel
                if (btn_popObjek.getText().equals("Cancel")){
                    finish();
                } else {
                    deleteObjek(pKey);
                }

        }
    }

    private void pushObjek(Objek objek) {
        database.child("Users")
                .push()
                .setValue(objek)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();
                        finish();

                        ed_Nama.setText("");
                        ed_Kelas.setText("");
                        ed_Pesan.setText("");

                        //membuat pesan di layar
                        Toast.makeText(itemdata.this,
                                "Data Berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateObjek(Objek objek, String key){
        database.child("Users").child(key)
                .setValue(objek)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();
                        finish();

                        //membuat pesan di layar
                        Toast.makeText(itemdata.this,
                                "Data Berhasil diubah",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void deleteObjek(String key){
        database.child("Users").child(key)
                .removeValue()
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();

                        //membuat pesan di layar
                        Toast.makeText(itemdata.this,
                                "Data Berhasil dihapus",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
