package com.example.pawan.fundraise;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout sp;
    RecyclerView allfunds;
    LinearLayoutManager layout;
    MainPage_RV_adapter adapter;
    ArrayList<Funds> list;
    ProgressBar pb;
    Toolbar toolbar;
    SearchView searchView;
    RelativeLayout rl;
    FloatingActionButton start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        fetchdata();
        adapter.notifyDataSetChanged();
    }

    private void initialize() {


       /* int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        */
        allfunds = (RecyclerView) findViewById(R.id.allfunds);
        layout = new LinearLayoutManager(this);
        list = new ArrayList<>();

        start= (FloatingActionButton) findViewById(R.id.fab_start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StartFundraiser.class));
            }
        });

        sp= (SwipeRefreshLayout) findViewById(R.id.swipe_main);
        if(!sp.isRefreshing()) {
            sp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                        if(!searchView.getQuery().toString().equals(""))
                        {

                            searchfunds(searchView.getQuery().toString());

                        }
                    else
                    fetchdata();

                    sp.setRefreshing(true);

                }
            });
        }



        adapter = new MainPage_RV_adapter(MainActivity.this, list);
        allfunds.setLayoutManager(layout);
        allfunds.setAdapter(adapter);
        pb = (ProgressBar) findViewById(R.id.progress_main);

    }

    private void searchfunds(final String query)
    {

        pb.setVisibility(View.VISIBLE);

        list.clear();

        adapter.notifyDataSetChanged();
        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/search_funds.php";
        Log.i("url",bookmarkurl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        pb.setVisibility(View.GONE);
                        sp.setRefreshing(false);

                        if(response.length()<=10)
                        {
                            Toast.makeText(MainActivity.this, "SORRY !! No Data Found", Toast.LENGTH_SHORT).show();
                        }

                        Log.i("res", response);

                        parsejson(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {


                adapter.notifyDataSetChanged();
                sp.setRefreshing(false);
                pb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "server error", Toast.LENGTH_LONG).show();
                Log.i("res", error.toString());

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query",query);
                return params;
            }
        };

        MySingleTon.getInstance(MainActivity.this).addtoRequestQueue(stringRequest);


    }


    private void fetchdata() {
        pb.setVisibility(View.VISIBLE);
        list.clear();

        adapter.notifyDataSetChanged();
        String bookmarkurl = "http://"+getString(R.string.ip)+"/fundraise/fetch_funds.php";
        Log.i("url",bookmarkurl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmarkurl,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        pb.setVisibility(View.GONE);
                        sp.setRefreshing(false);

                        Log.i("res", response);

                        parsejson(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                adapter.notifyDataSetChanged();
                sp.setRefreshing(false);
                pb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "server error", Toast.LENGTH_LONG).show();
                Log.i("res", error.toString());

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        MySingleTon.getInstance(MainActivity.this).addtoRequestQueue(stringRequest);
    }

    private void parsejson(String response) {
        list.clear();
        adapter.notifyDataSetChanged();
        sp.setRefreshing(false);
        pb.setVisibility(View.GONE);
        try {
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                Funds a = new Funds();

                float rais= Float.parseFloat(obj.getString("raised"));
                float goa= Float.parseFloat(obj.getString("target"));
                float p=(rais/goa)*100;

                a.setUid(obj.getString("uid"));
                a.setCity(obj.getString("city"));
                a.setPer(String.valueOf(p));
                a.setFid(obj.getString("fid"));
                a.setTitle(obj.getString("title"));
                a.setCategory(obj.getString("category"));
                a.setBy(obj.getString("name"));
                a.setRaised(obj.getString("raised"));
                a.setFoor(obj.getString("benificiary_name"));
                a.setTime(obj.getString("target_date"));
                a.setFollowers(obj.getString("followers"));
                a.setGoal(obj.getString("target"));
                list.add(a);
            }

            adapter.notifyDataSetChanged();

            pb.setVisibility(View.GONE);
            sp.setRefreshing(false);

            Log.i("eeee", response.toString() + "aa");
        } catch (Exception e) {
            pb.setVisibility(View.GONE);
            sp.setRefreshing(false);
            Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
         searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!query.equals(""))
                    searchfunds(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fetchdata();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if(id==R.id.profile)
        {
            SharedPreferences sp=getSharedPreferences("demo_file",MODE_PRIVATE);

            if(!sp.getBoolean("login",false))
            {
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }

            else
            {
                Intent i= new Intent(this,Profile.class);
                i.putExtra("uid",sp.getString("uid","-1"));
                startActivity(i);

            }

        }

/*
        else if(id==R.id.filter)
        {
            Toast.makeText(this, "filter", Toast.LENGTH_SHORT).show();
        }
*/
        return super.onOptionsItemSelected(item);
    }

}
