package com.example.a17019181.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SimpleData extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private double compareVar;
    ImageView pinky;
    ArrayList<Post> postArrayList;
    double highest;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_data);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        pinky= (ImageView) findViewById(R.id.pinky);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(uid).child("finger");



        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI

                //storing data from db to a list, making easier to calculate
                updateData(dataSnapshot);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("UserListActivity", databaseError.getDetails());

            }
        };


        databaseReference.addValueEventListener(postListener);






    }

  public void updateData(DataSnapshot dataSnapshot){

      postArrayList = new ArrayList<Post>();


      for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

          Post child = childSnapshot.getValue(Post.class);
          postArrayList.add(child);

      }



      for (Post x: postArrayList){
          double compare = Double.parseDouble(x.getMiddleMax());
          if (highest< compare){
              highest = compare;
          }

      }


      if (highest < 180){
          pinky.setVisibility(View.INVISIBLE);
      } else{
          pinky.setVisibility(View.VISIBLE);
      }

      //reset all
      postArrayList.clear();
      highest=0;

    }



}
