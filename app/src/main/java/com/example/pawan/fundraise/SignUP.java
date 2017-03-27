package com.example.pawan.fundraise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUP extends AppCompatActivity  {
    EditText email,pwd,phone,name;
    TextView signup,skip,su;
    ProgressBar pb;
    boolean loading;
    String res;
    TextView login;

    LinearLayout ll;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login= (TextView) findViewById(R.id.tv_link_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUP.this,Login.class));
                finish();
            }
        });

        sp=getSharedPreferences("demo_file",MODE_PRIVATE);
        edit=sp.edit();
        if(sp.getBoolean("login",false))
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        loading=false;
        email= (EditText) findViewById(R.id.et_su_email);
        pwd= (EditText) findViewById(R.id.et_su_pwd);

        phone=(EditText) findViewById(R.id.et_su_num);

        name=(EditText) findViewById(R.id.et_su_name);

        signup= (TextView) findViewById(R.id.tv_signup);
        pb= (ProgressBar) findViewById(R.id.su_progress);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fade();

                if(email.getText().toString().equals("" ) ||name.getText().toString().equals("" )||pwd.getText().toString().equals("" )||phone.getText().toString().equals("" ) )
                {
                    Toast.makeText(SignUP.this, "fill all data", Toast.LENGTH_SHORT).show();
                    return;
                }

                fetchdata();
            }
        });



    }


    private void fade() {
        loading=true;
        signup.setAlpha((float) 0.5);
        pb.setVisibility(View.VISIBLE);
    }


    private void normal() {
        loading=false;
        pb.setVisibility(View.GONE);
        signup.setAlpha(1);
    }



    private void fetchdata() {

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/signup.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        normal();
                        Log.i("res",response);


                        JSONArray arr= null;
                        try {
                            arr = new JSONArray(response);
                            JSONObject obj=arr.getJSONObject(0);
                            edit.putBoolean("login",true);
                            edit.putString("email",email.getText().toString());
                            edit.putString("uid",obj.getString("uid"));
                            edit.putString("name",name.getText().toString());
                            edit.putString("phone",phone.getText().toString());
                            edit.commit();
                            startActivity(new Intent(SignUP.this,MainActivity.class));
                            finish();

                        } catch (JSONException e) {

                            Toast.makeText(SignUP.this, response, Toast.LENGTH_SHORT).show();

                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                Toast.makeText(SignUP.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                normal();
                Log.i("res", error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email.getText().toString());
                params.put("pwd",pwd.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("name",name.getText().toString());

                return params;
            }
        };

        MySingleTon.getInstance(this).addtoRequestQueue(stringRequest);
    }


}
