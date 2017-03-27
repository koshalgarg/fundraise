package com.example.pawan.fundraise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GnssClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowFund extends AppCompatActivity {

   // SwipeRefreshLayout sp;
    SharedPreferences sp;
    FloatingActionButton fab;
    String fid;
    ImageView photo;
    String stitle,sid,sgoal,sby,sfor,sdate;
    TextView category,title,by,forr,story,raised,per,goal,followers,time,contribute,city;
    ImageView send;
    EditText  comment;
    ProgressBar pb,pb_load;

    LinearLayout commentbox;
    TextView tvcomment;


    RecyclerView rv_comments;
    LinearLayoutManager lmanager;
    Comment_Adapter adapter;
    ArrayList<Comments> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fund);
        Intent i=getIntent();
        fid=i.getStringExtra("id");
        Log.i("fid",fid);
        //Toast.makeText(this, "fid" +fid, Toast.LENGTH_SHORT).show();
        initialize();
        fetchdata();
        sp=getSharedPreferences("demo_file",MODE_PRIVATE);
    }



    private void fetchdata() {

        pb_load.setVisibility(View.VISIBLE);

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/getdata.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        Log.i("res",response);

                        try {

//                            list.clear();
                            JSONArray arr = new JSONArray(response);
                                JSONObject obj = arr.getJSONObject(0);

                             float rais= Float.parseFloat(obj.getString("raised"));
                            float goa= Float.parseFloat(obj.getString("target"));
                            float p=(rais/goa)*100;
                            Log.i("per",rais + goa+ ""+ p);
                            pb.setProgress((int) p);
                            per.setText((int)p+"%");

                            stitle=obj.getString("title");
                            Log.i("stitle",stitle);

                                title.setText(obj.getString("title"));
                                category.setText(obj.getString("category"));


                            sby=obj.getString("name");
                            sid=obj.getString("fid");
                            sfor=obj.getString("benificiary_name");
                            sgoal=obj.getString("target");
                            sdate=obj.getString("target_date");
                                 by.setText("By: "+obj.getString("name"));
                            city.setText("City: "+obj.getString("city"));

                            final String uid=obj.getString("uid");

                            by.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(ShowFund.this, uid, Toast.LENGTH_SHORT).show();
                                }
                            });


                                raised.setText("raised "+ obj.getString("raised"));
                                forr.setText("For:  "+obj.getString("benificiary_name"));
                                story.setText(obj.getString("story"));
                                time.setText("time \n"+obj.getString("target_date"));
                                followers.setText("followers \n"+obj.getString("followers"));
                                goal.setText("target\n"+obj.getString("target"));

                               Glide.with(ShowFund.this)
                                    .load("http://"+getString(R.string.ip)+"/fundraise/images/funds/fund_"+fid+".png")
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                       .placeholder(R.drawable.loading)
                                       .error(R.mipmap.ic_launcher)
                                    .into(photo);



                            pb_load.setVisibility(View.GONE);
                            //sp.setRefreshing(false);

                        } catch (Exception e) {
                            pb_load.setVisibility(View.GONE);
                            //sp.setRefreshing(false);
                            Toast.makeText(ShowFund.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());
                pb_load.setVisibility(View.GONE);
                //sp.setRefreshing(false);

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",fid);
                return params;
            }
        };

        MySingleTon.getInstance(ShowFund.this).addtoRequestQueue(stringRequest);
    }


    private void initialize() {

        comment= (EditText) findViewById(R.id.et_comment);
        send= (ImageView) findViewById(R.id.iv_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment.getText().toString().length()<1)
                {
                    return;
                }
                else if(!sp.getBoolean("login",false))
                {
                    Toast.makeText(ShowFund.this, "Login to Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                commentsend();

            }

        });

        category= (TextView) findViewById(R.id.tv_fund_category);
        title= (TextView) findViewById(R.id.tv_fund_title);
        by= (TextView) findViewById(R.id.tv_fund_name);
        forr= (TextView) findViewById(R.id.tv_fund_for);
        story= (TextView) findViewById(R.id.tv_fund_story);
        raised= (TextView) findViewById(R.id.tv_fund_raised);
        per= (TextView) findViewById(R.id.tv_fund_per);
        goal= (TextView) findViewById(R.id.tv_fund_goal);
        followers= (TextView) findViewById(R.id.tv_fund_follow);
        time= (TextView) findViewById(R.id.tv_fund_time);
        contribute= (TextView) findViewById(R.id.tv_fund_contribute);
        city= (TextView) findViewById(R.id.tv_fund_city);

        fab= (FloatingActionButton) findViewById(R.id.fab_follow);
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });

        
        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(sp.getBoolean("login",false))
                {

                    Intent i=new Intent(ShowFund.this,Contribute.class);
                    i.putExtra("fid",fid);
                    i.putExtra("uid",sp.getString("uid","a"));

                    i.putExtra("name",sp.getString("name","a"));

                    startActivityForResult(i,0);

                }
                else
                {
                    Toast.makeText(ShowFund.this, "you need to login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowFund.this,Login.class));
                    finish();
                }

            }
        });
        photo= (ImageView) findViewById(R.id.iv_fund_photo);

         pb= (ProgressBar) findViewById(R.id.pb_fund_pb);
        pb_load= (ProgressBar) findViewById(R.id.pb_show_fund_wait);


        tvcomment= (TextView) findViewById(R.id.tv_fund_comment);
        rv_comments= (RecyclerView) findViewById(R.id.rv_fund_comments);
        commentbox= (LinearLayout) findViewById(R.id.ll_fund_commentbox);
        
        tvcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rv_comments.getVisibility()==View.VISIBLE)
                {
                    rv_comments.setVisibility(View.GONE);
                    commentbox.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                }
                else 
                {
                    fab.setVisibility(View.GONE);
                    rv_comments.setVisibility(View.VISIBLE);
                    commentbox.setVisibility(View.VISIBLE);
                    rv_comments.requestFocus();
                    showComments();
                }
            }
        });

        list=new ArrayList<>();
        lmanager=new LinearLayoutManager(this);
        rv_comments.setLayoutManager(lmanager);
        adapter=new Comment_Adapter(this,list);
        rv_comments.setAdapter(adapter);

    }

    private void commentsend() {

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/comment.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        Log.i("res",response);
                        showComments();
                        comment.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());

                comment.setText("");

            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("fid",fid);
                params.put("uid",sp.getString("uid","-1"));
                params.put("name",sp.getString("name","-1"));
                params.put("comment",comment.getText().toString());
                params.put("title",stitle);

                return params;
            }
        };

        MySingleTon.getInstance(ShowFund.this).addtoRequestQueue(stringRequest);


    }

    private void showComments() {

        list.clear();

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/fetch_comments.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        Log.i("res",response);

                        try {
                            JSONArray arr=new JSONArray(response);

                            for(int i=0;i<arr.length();i++)
                            {
                                JSONObject obj=arr.getJSONObject(i);
                                Comments c=new Comments();
                                c.setUname(obj.getString("name"));
                                c.setUid(obj.getString("uid"));
                                c.setComment(obj.getString("comment"));
                                c.setTime(obj.getString("date"));

                                list.add(c);



                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Toast.makeText(ShowFund.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());
                pb_load.setVisibility(View.GONE);
                //sp.setRefreshing(false);

            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fid",fid);
                return params;
            }
        };

        MySingleTon.getInstance(ShowFund.this).addtoRequestQueue(stringRequest);

    }

    private void follow() {

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/follow.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        Log.i("res contri",response);

                        Toast.makeText(ShowFund.this,"Following this fundraiser", Toast.LENGTH_SHORT).show();
                        fetchdata();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());
                pb_load.setVisibility(View.GONE);
                //sp.setRefreshing(false);

            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fid",fid);
                params.put("uid",sp.getString("uid","sa"));
                params.put("name",sp.getString("name","sa"));
                params.put("title",stitle);
                return params;
            }
        };

        MySingleTon.getInstance(ShowFund.this).addtoRequestQueue(stringRequest);






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0)
        {
            if(resultCode>0)
            {
            //    Toast.makeText(this, resultCode+"", Toast.LENGTH_SHORT).show();

                contribute(resultCode);

            }
            else {
                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void contribute(final int resultCode) {

        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/contribute.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
  //                      Log.i("res contri",response);
//                        Toast.makeText(ShowFund.this,response, Toast.LENGTH_SHORT).show();
                        fetchdata();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("res", error.toString());
                pb_load.setVisibility(View.GONE);
                //sp.setRefreshing(false);

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fid",fid);
                params.put("uid",sp.getString("uid","sa"));
                params.put("name",sp.getString("name","sa"));
                params.put("amount",resultCode+"");
                params.put("title",stitle);

                return params;
            }
        };

        MySingleTon.getInstance(ShowFund.this).addtoRequestQueue(stringRequest);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT,"Fundraiser.com?fid="+fid+"\n"+stitle+"\nfor "+sfor+" \ngoal "+sgoal+"\nby "+sby+"\nbefore "+sdate);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Event"));
        return true;

    }
}
