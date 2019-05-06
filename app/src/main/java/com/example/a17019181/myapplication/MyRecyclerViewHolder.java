package com.example.a17019181.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;


public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView txt_comment;
    TextView txt_comment1;
    TextView txt_comment2;
    GraphView graph_comment;
    ImageView img_cal;
    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_comment = (TextView) itemView.findViewById(R.id.txt_max);
        txt_comment1 = (TextView) itemView.findViewById(R.id.txt_time);
        txt_comment2 = (TextView) itemView.findViewById(R.id.txt_degree);
        graph_comment = (GraphView) itemView.findViewById(R.id.graph);
        img_cal = (ImageView) itemView.findViewById(R.id.img_calender);


    }
}
