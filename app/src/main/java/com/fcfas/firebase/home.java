package com.fcfas.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity implements View.OnClickListener{

    Button btn_auth, btn_realtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_auth = findViewById(R.id.btn_auth);
        btn_realtime = findViewById(R.id.btn_realtime);

        btn_auth.setOnClickListener(this);
        btn_realtime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_realtime:
                Intent realtime = new Intent(getApplicationContext(), listdata.class);
                startActivity(realtime);
                break;
            case R.id.btn_auth:
                Intent auth = new Intent(getApplicationContext(), userlogin.class);
                startActivity(auth);
                break;
        }
    }
}

