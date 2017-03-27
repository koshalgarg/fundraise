package com.example.pawan.fundraise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Contribute extends AppCompatActivity {


    EditText et;
    TextView con,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);

        Intent i=getIntent();
        Log.i("contribute uid",i.getStringExtra("uid"));
        Log.i("contribute name",i.getStringExtra("name"));
        Log.i("contribute fid",i.getStringExtra("fid"));

        et= (EditText) findViewById(R.id.et_con_amount);
        con= (TextView) findViewById(R.id.tv_con_go);

        cancel= (TextView) findViewById(R.id.tv_con_cancel);

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().equals(""))
                {
                    Toast.makeText(Contribute.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent resultIntent = new Intent();

                setResult(Integer.parseInt(et.getText().toString()), resultIntent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent resultIntent = new Intent();
                setResult(-1, resultIntent);
                finish();

            }
        });

    }
}
