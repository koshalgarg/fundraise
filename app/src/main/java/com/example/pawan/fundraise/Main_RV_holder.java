package com.example.pawan.fundraise;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Main_RV_holder extends RecyclerView.ViewHolder {

    ImageView photo;
    TextView cat,title,by,foor,raised,per,goal,follow,time;

    CardView cv;
    ProgressBar pb;

    public Main_RV_holder(View itemView) {
        super(itemView);

        photo= (ImageView) itemView.findViewById(R.id.iv_photo);
        cat= (TextView) itemView.findViewById(R.id.tv_category);
        title= (TextView) itemView.findViewById(R.id.tv_title);
        by= (TextView) itemView.findViewById(R.id.tv_by);
        foor= (TextView) itemView.findViewById(R.id.tv_for);
        raised= (TextView) itemView.findViewById(R.id.tv_raised);
        per= (TextView) itemView.findViewById(R.id.tv_per);
        goal= (TextView) itemView.findViewById(R.id.tv_goal);
        follow= (TextView) itemView.findViewById(R.id.tv_follow);
        time= (TextView) itemView.findViewById(R.id.tv_time);
        pb= (ProgressBar) itemView.findViewById(R.id.pb_per);
cv= (CardView) itemView.findViewById(R.id.fund_card_view);

    }
}
