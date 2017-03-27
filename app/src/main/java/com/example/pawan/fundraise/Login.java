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

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText email,pwd;
    TextView login,skip,su;
    ProgressBar pb;
    boolean loading;
    String res;
   LinearLayout ll;


   public static SharedPreferences sp;
  public static   SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp=getSharedPreferences("demo_file",MODE_PRIVATE);
        edit=sp.edit();
        if(sp.getBoolean("login",false))
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }




        loading=false;
        email= (EditText) findViewById(R.id.et_login_email);
        pwd= (EditText) findViewById(R.id.et_login_pwd);


        login= (TextView) findViewById(R.id.tv_login);
        skip= (TextView) findViewById(R.id.tv_skip);
        su= (TextView) findViewById(R.id.tv_link_su);

        pb= (ProgressBar) findViewById(R.id.login_progress);
        ll= (LinearLayout) findViewById(R.id.ll_login);
        login.setOnClickListener(this);
        su.setOnClickListener(this);
        skip.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(loading)
            return;

        int id=v.getId();
        switch (id)
        {
            case R.id.tv_login:
           login();
                 break;

            case  R.id.tv_skip:
                startActivity(new Intent(Login.this,MainActivity.class));
                finish();
                break;
            case R.id.tv_link_su: {
                startActivity(new Intent(Login.this, SignUP.class));
                finish();
            }


        }

    }

    private void login() {


        fade();
        fetchdata();
    }

    private void fade() {
        loading=true;
        login.setAlpha((float) 0.5);
        pb.setVisibility(View.VISIBLE);
        //ll.setAlpha((float) 0.3);
     //   su.setAlpha((float) 0.3);
    }


    private void normal() {
        loading=false;
        pb.setVisibility(View.GONE);
        login.setAlpha(1);
     //   ll.setAlpha((float)1);
   //     su.setAlpha((float)1);
    }



    private void fetchdata() {

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/login.php";
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
                                edit.putString("email",obj.getString("email"));
                                edit.putString("uid",obj.getString("uid"));
                                edit.putString("name",obj.getString("name"));
                                edit.putString("phone",obj.getString("phone"));

                                edit.commit();

                                startActivity(new Intent(Login.this,MainActivity.class));
                                finish();


                            } catch (JSONException e) {
                                Toast.makeText(Login.this, "Incorrect Combination", Toast.LENGTH_SHORT).show();

                            }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                Toast.makeText(Login.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                normal();
                Log.i("res", error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email.getText().toString());
                params.put("pwd",pwd.getText().toString());
                return params;
            }
        };

        MySingleTon.getInstance(this).addtoRequestQueue(stringRequest);
    }


}
