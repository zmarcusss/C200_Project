package com.example.a17019181.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Data extends AppCompatActivity {


    RecyclerView recyclerView;
    GraphView graph;
    String uid;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference postConfiguration;
    FirebaseRecyclerOptions<Post> option;
    FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layout = new LinearLayoutManager(this ); //make new layout so that I can reverse it, as I want the latest data first
        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);
        recyclerView.setLayoutManager(layout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(uid).child("finger");
        //        databaseReference = firebaseDatabase.getReference("index"); //if got another 5 fingers
        //        databaseReference = firebaseDatabase.getReference("C200");
        //        databaseReference = firebaseDatabase.getReference("C200");
        //        databaseReference = firebaseDatabase.getReference("C200");


        postConfiguration = firebaseDatabase.getReference().child(uid).child("configuration");


        displayData();


    }



//
//    private void postComment(){
//        String content = edt_content.getText().toString();
//        ConfigurationData post = new ConfigurationData(content);
//
//
//        postConfiguration.setValue(post);
//
//
//
//        adapter.notifyDataSetChanged();
//
//    }

    private void displayData() {
        option =
                new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(databaseReference,Post.class
                ).build();

        adapter=
                new FirebaseRecyclerAdapter<Post, MyRecyclerViewHolder>(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position, @NonNull Post model) {



                        //set calender image to the specific, for example Jan 5, set Pic to cal5

                        try {


                            String input = model.getTime();
                            DateFormat df = new SimpleDateFormat("E MMM  d HH:mm:ss yyyy");

                            System.out.println(df.parseObject(input));
                            DateFormat formatter = new SimpleDateFormat("d");
                            String day=formatter.format(df.parseObject(input));
                            int insert = getResources().getIdentifier("cal"+day , "drawable", getPackageName());
                            holder.img_cal.setImageResource(insert);

                        } catch (ParseException e) {
                        }







                        String degrees = model.getMiddleDegree().replaceAll("\\s","");
                        degrees = degrees.replaceAll("[()]"," ");

                        String[] statisticList = degrees.split(",");

                        DataPoint[] dp = new DataPoint[statisticList.length];

                        for (int i =0; i<dp.length;i++){
                           dp[i] = new DataPoint(i, Double.parseDouble(statisticList[i]));


                        }

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                        series.setColor(Color.RED);

                        holder.txt_comment.setText("Max: " + model.getMiddleMax());
                        holder.txt_comment1.setText("Date / Time: " + model.getTime());
                        holder.txt_comment2.setText("Statistics: " + model.getMiddleDegree());
                        holder.graph_comment.addSeries(series);


                    }

                    @NonNull
                    @Override
                    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                       View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.post_item,viewGroup,false);
                        return new MyRecyclerViewHolder(itemView);
                    }
                };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simpledata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_hand:
                startActivity(new Intent(Data.this,SimpleData.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
