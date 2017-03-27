package com.example.pawan.fundraise;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VolleyFetch  {

    public static String res;

    public void fetchdata(Context context, String url, final ArrayList<String > key, final ArrayList<String > value) {

        Log.i("tins",url);
String  res="errorrrrr";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        Log.i("tins", response);
       //                 res=response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                Log.i("eeee", error.toString());

         //       res="error";
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                for (int i = 0; i < value.size(); i++) {
                    params.put(key.get(i), value.get(i));
                }

                return params;
            }
        };

        MySingleTon.getInstance(context).addtoRequestQueue(stringRequest);

        Log.i("tins", res);

       // return res;
    }

}


