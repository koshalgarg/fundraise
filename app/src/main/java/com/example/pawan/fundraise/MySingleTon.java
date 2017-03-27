package com.example.pawan.fundraise;

import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/**
 * Created by surya on 19/10/16.
 */

public class MySingleTon  {

    private  static MySingleTon instance;
    private RequestQueue requestQueue;
    private static Context mctx;
    private MySingleTon(Context ctx)
    {
        mctx=ctx;
        requestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue=Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleTon getInstance(Context ctx){
        if(instance==null)
        {
            instance=new MySingleTon(ctx);
        }
        return instance;
    }
    public <T>void addtoRequestQueue(Request<T>request)
    {

        requestQueue.add(request);
    }
}