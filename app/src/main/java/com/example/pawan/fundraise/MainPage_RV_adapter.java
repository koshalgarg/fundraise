package com.example.pawan.fundraise;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by pawan on 10/27/2016.
 */

public class MainPage_RV_adapter extends RecyclerView.Adapter<Main_RV_holder> {

    ArrayList<Funds> list;
    Context context;



    public MainPage_RV_adapter(Context mainActivity, ArrayList<Funds> list) {
        this.context=mainActivity;
        this.list=list;
    }

    @Override
    public Main_RV_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fund_card, parent, false);
        //layoutView.setMinimumHeight(parent.getMeasuredHeight() / 2);
        Main_RV_holder rcv = new Main_RV_holder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final Main_RV_holder holder, final int position) {



        Glide.with(context)
                .load("http://"+context.getString(R.string.ip)+"/fundraise/images/funds/fund_"+list.get(position).getFid()+".png")
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading)
                .error(R.mipmap.ic_launcher)
                .into(holder.photo);


        holder.title.setText(list.get(position).getTitle());
        holder.cat.setText(list.get(position).getCategory());
        holder.by.setText("by "+list.get(position).getBy());
        holder.foor.setText("for "+list.get(position).getFoor());
        holder.raised.setText("raised "+list.get(position).getRaised());

        float s= Float.parseFloat(list.get(position).getPer());

        holder.per.setText((int)s+"%");
        holder.goal.setText("GOAL\n"+list.get(position).getGoal());
        holder.follow.setText("FOLLOWERS\n"+list.get(position).getFollowers());
        holder.time.setText("BEFORE\n"+list.get(position).getTime());

        holder.pb.setProgress((int) s);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(context,ShowFund.class);
                i.putExtra("id",list.get(position).getFid());
                context.startActivity(i);
            }
        });

        holder.by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(context,Profile.class);
               i.putExtra("uid",list.get(position).getUid());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
