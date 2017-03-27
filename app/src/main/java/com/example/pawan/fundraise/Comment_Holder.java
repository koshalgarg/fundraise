package com.example.pawan.fundraise;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Comment_Holder extends RecyclerView.ViewHolder {


    TextView uname,time,comment;
    CardView cv;
    ProgressBar pb;

    public Comment_Holder(View itemView) {
        super(itemView);

        uname= (TextView) itemView.findViewById(R.id.tv_cmt_name);
        time= (TextView) itemView.findViewById(R.id.tv_cmt_time);
        comment= (TextView) itemView.findViewById(R.id.tv_cmt_comment);



    }
}
