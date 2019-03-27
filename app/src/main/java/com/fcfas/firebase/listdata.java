package com.fcfas.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapter.aListData;
import model.Objek;

public class listdata extends AppCompatActivity implements View.OnClickListener {

    //firebase
    private DatabaseReference database;

    //list
    private ArrayList<Objek> users;
    private aListData listData;

    //objek layout
    private RecyclerView rc_listData;
    private ProgressDialog loading;
    private FloatingActionButton tambahData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);

        database = FirebaseDatabase.getInstance().getReference();

        //memanggil objek pada layout
        rc_listData = findViewById(R.id.rc_listData);
        tambahData = findViewById(R.id.fb_tambahData);
        tambahData.setOnClickListener(this);

        //membuat objek recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rc_listData.setLayoutManager(layoutManager);
        rc_listData.setItemAnimator(new DefaultItemAnimator());

        //membuat progres loading
        loading = ProgressDialog.show(listdata.this,
                null,
                "Please Wait ...",
                true,
                false);

        //mengambil data dari firebase disertakan event
        database.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menempatkan value kedalam arraylist
                users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //mapping data pada snapshot kedalam objek data
                    Objek objek = snapshot.getValue(Objek.class);
                    objek.setKey(snapshot.getKey());

                    //memasukan objek yang sudah dimapping
                    users.add(objek);
                }

                //inisialisasi adapter dan data dalam bentuk array list
                listData = new aListData(users, listdata.this);
                rc_listData.setAdapter(listData);
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //kode ini dipanggil jika error
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                loading.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_tambahData:
                Intent tambah = new Intent(listdata.this, itemdata.class);
                tambah.putExtra("Key","");
                tambah.putExtra("Nama","");
                tambah.putExtra("Kelas","");
                tambah.putExtra("Pesan","");
                startActivity(tambah);
                break;
        }
    }

}
