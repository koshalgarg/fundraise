package com.example.pawan.fundraise;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.R.attr.fastScrollAlwaysVisible;

public class StartFundraiser extends AppCompatActivity {


    TextInputEditText etfor,title,target,city,date,mobile,description,story;

    AutoCompleteTextView category;



    String uid,name;
    ImageView img;
    TextView upload;
    Bitmap bitmap;
    Boolean bool_img,bool_pb;
    ProgressBar pb;
    long datemilli;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_fundraiser);

        initialize();

    }

    private void initialize() {

        //etfor,title,category,target,city,date,mobile,description,story

        sp=getSharedPreferences("demo_file",MODE_PRIVATE);

        if(!sp.getBoolean("login",false))
        {
            Toast.makeText(this, "LOGIN FIRST", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StartFundraiser.this,Login.class));
            finish();
        }
        else
        {
            uid=sp.getString("uid","-1");
            name=sp.getString("name","null");
            Toast.makeText(this,uid+" "+name, Toast.LENGTH_SHORT).show();
        }

        bool_pb=false;

        pb= (ProgressBar) findViewById(R.id.pb_start);
        etfor= (TextInputEditText) findViewById(R.id.et_start_for);
        title= (TextInputEditText) findViewById(R.id.et_start_title);
        category= (AutoCompleteTextView) findViewById(R.id.et_start_category);
        target= (TextInputEditText) findViewById(R.id.et_start_target);
        city= (TextInputEditText) findViewById(R.id.et_start_city);
        date= (TextInputEditText) findViewById(R.id.et_start_date);
        mobile= (TextInputEditText) findViewById(R.id.et_start_phone);
        description= (TextInputEditText) findViewById(R.id.et_start_description);
        story= (TextInputEditText) findViewById(R.id.et_start_story);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    datepicker();
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });


        String[] cats=getResources().getStringArray(R.array.category);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,cats);
        category.setAdapter(adapter);

        bool_img=false;
        img= (ImageView) findViewById(R.id.iv_start_image);
        upload= (TextView) findViewById(R.id.tv_start_start);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bool_pb)
                {
                    Toast.makeText(StartFundraiser.this, "Wait ..!!! UPLOADING", Toast.LENGTH_SHORT).show();
                    return;
                }
                upload();
            }
        });

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
                        datemilli = cal.getTimeInMillis();
                        Toast.makeText(StartFundraiser.this, datemilli+" ", Toast.LENGTH_SHORT).show();

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
                        date.setText(new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mYear).append("-").append(m).append("-")
                                .append(d));

                    }
                }, mYear, mMonth, mDay);
        dialog.show();
    }

    private void upload() {

        bool_pb=true;
        pb.setVisibility(View.VISIBLE);

        final String setfor,stitle,scategory,starget,scity,sdate,smobile,sdescription,sstory;
        setfor=etfor.getText().toString();
        stitle=etfor.getText().toString();
                scategory=category.getText().toString();
                starget=target.getText().toString();
                scity=city.getText().toString();;
                sdate=date.getText().toString();
                smobile=mobile.getText().toString();;
                sdescription=description.getText().toString();
                        sstory=story.getText().toString();

        if(setfor.equals("")||stitle.equals("")||scategory.equals("")||starget.equals("")||scity.equals("")||sdate.equals("")||smobile.equals("")||sdescription.equals("")||sstory.equals("") )
        {
            Toast.makeText(this, "fill all data", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            bool_pb=false;
            return;
        }


            String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/start.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                    new Response.Listener<String>() {

                        @Override

                        public void onResponse(String response) {

                            bool_pb=false;


                            pb.setVisibility(View.GONE);

                            Log.i("res",response);

                            Intent i=new Intent(StartFundraiser.this,MainActivity.class);
                           // i.putExtra("id",response);
                            startActivity(i);
                           finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                           Log.i("res", error.toString());
                    bool_pb=false;

                    pb.setVisibility(View.GONE);


                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("title",stitle);

                    params.put("name",name);
                    params.put("uid",uid);

                    params.put("target",starget);
                    params.put("category",scategory);
                    params.put("city",scity);
                    params.put("description",sdescription);
                    params.put("story",sstory);
                    params.put("mobile",smobile);
                    params.put("date",date.getText().toString());
                    params.put("for",setfor);
                    params.put("image",getStringImage(bitmap));


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
             //   Log.i("img",getStringImage(bitmap));
                bool_img=true;
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
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

}
