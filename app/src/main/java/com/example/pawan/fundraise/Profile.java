package com.example.pawan.fundraise;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    RecyclerView allfunds;
    LinearLayoutManager layout;
    Profile_RV_adapter adapter;
    ArrayList<Profile_RV> list;
    TextView  name,update,my,following,logout,fundraisers;
    EditText email,phone,dob,city;
    //RecyclerView rv;
    FloatingActionButton fab;
    ImageView pic;
    String uid;
    Bitmap bitmap;
    SharedPreferences sp;
    boolean own,img_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       // Toast.makeText(this, "strting", Toast.LENGTH_SHORT).show();
        sp=getSharedPreferences("demo_file",MODE_PRIVATE);
        Intent i=getIntent();
        uid=i.getStringExtra("uid");
        if(sp.getString("uid","-1").equals(uid))
            own=true;
        else
        own=false;
        initialize();
        getPersonaldetails();

        allfunds = (RecyclerView) findViewById(R.id.rv_pro);
        layout = new LinearLayoutManager(this);
        list = new ArrayList<>();

        adapter = new Profile_RV_adapter(Profile.this, list);
        allfunds.setLayoutManager(layout);
        allfunds.setAdapter(adapter);
    }

    private void getPersonaldetails() {

        Log.i("img","downloading");

        if(!img_change) {
            Glide.with(this)
                    .load("http://" + getString(R.string.ip) + "/fundraise/images/profile/pro_" + uid + ".png")
                    .placeholder(R.drawable.loading) // can also be a drawable
                    .error(R.drawable.pro)
                   .into(pic);
        }

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/getuserinfo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        try {
                            JSONArray arr=new JSONArray(response);
                            JSONObject obj=arr.getJSONObject(0);

                            email.setText(obj.getString("email"));
                            phone.setText(obj.getString("phone"));
                            dob.setText(obj.getString("dob"));
                            city.setText(obj.getString("city"));

                        } catch (JSONException e) {
                            Toast.makeText(Profile.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",uid);
                return params;
            }
        };

        MySingleTon.getInstance(this).addtoRequestQueue(stringRequest);


    }

    private void initialize() {
        img_change=false;
        name= (TextView) findViewById(R.id.tv_pro_name);
        update= (TextView) findViewById(R.id.tv_pro_update);
        fundraisers= (TextView) findViewById(R.id.tv_pro_my);
        following= (TextView) findViewById(R.id.tv_pro_following);
        email= (EditText) findViewById(R.id.et_pro_email);
        phone= (EditText) findViewById(R.id.et_pro_number);
        dob= (EditText) findViewById(R.id.et_pro_dob);
        city= (EditText) findViewById(R.id.et_pro_city);
       // rv= (RecyclerView) findViewById(R.id.rv_pro);
        fab= (FloatingActionButton) findViewById(R.id.fab_pro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,StartFundraiser.class));
            }
        });
        pic= (ImageView) findViewById(R.id.tv_pro_pic);

        logout= (TextView) findViewById(R.id.tv_pro_logout);
        {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login.edit.putBoolean("login",false);
                    Login.edit.commit();
                    startActivity(new Intent(Profile.this,Login.class));
                    finish();

                }
            });
        }

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    datepicker();
                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });


        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

            }
        });

        if(!own)
        {
            fab.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            email.setEnabled(false);
            dob.setEnabled(false);
            city.setEnabled(false);
            phone.setEnabled(false);
            logout.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        fundraisers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updaterv(0);
            }
        });


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updaterv(1);
            }
        });
    }

    private void updaterv(final int i) {


        list.clear();
        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/profilerv.php";
        Log.i("url",bookmarkurl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        Log.i("res task", response);

                        try {
                            JSONArray arr=new JSONArray(response);
                            for(int i=0;i<arr.length();i++)
                            {
                                JSONObject obj=arr.getJSONObject(i);
                                Profile_RV a=new Profile_RV();
                                a.setFid(obj.getString("fid"));
                                a.setName(obj.getString("title"));
                                list.add(a);
                                adapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(Profile.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {


                adapter.notifyDataSetChanged();
                Log.i("res", error.toString());

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("task",i+"");
                params.put("uid",uid);
                return params;
            }
        };

        MySingleTon.getInstance(Profile.this).addtoRequestQueue(stringRequest);


        adapter.notifyDataSetChanged();
    }


    private void update()
    {

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/updateuserinfo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("res",response);
                        Toast.makeText(Profile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());
                Toast.makeText(Profile.this, "error in uploading", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",uid);
                if(img_change)
                {
                    img_change=false;
                    params.put("img","1");
                    params.put("image",getStringImage(bitmap));

                }
                else
                    params.put("img","0");
                params.put("city",city.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("dob",dob.getText().toString());

                return params;
            }
        };

        MySingleTon.getInstance(this).addtoRequestQueue(stringRequest);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pic.setImageBitmap(bitmap);
//                pic.setImageResource(R.mipmap.ic_launcher);
                Log.i("img","true");
                Log.i("img",getStringImage(bitmap));
                img_change=true;
            } catch (IOException e) {
                Log.i("img","false");
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




    private void datepicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);

                        int mYear = year;
                        int mMonth = monthOfYear;
                        int mDay = dayOfMonth;
                        String m,d;
                        mMonth+=1;
                        if(mMonth<10)
                            m="0"+mMonth;
                        else
                            m=String.valueOf(mMonth);
                        if(mDay<10)
                            d="0"+mDay;
                        else
                            d=String.valueOf(mDay);
                        dob.setText(new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mYear).append("-").append(m).append("-")
                                .append(d));

                    }
                }, mYear, mMonth, mDay);
        dialog.show();
    }
}
