package com.example.a17019181.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;


//database
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Sensor extends AppCompatActivity {
    public int counter;
    Button sensor_Start;
    Button sensor_Stop;
    TextView sensor_Count;
    ProgressBar progress_Bar;
    Measure post = new Measure(0);
    CountDownTimer countdown;
    NumberPicker minute;
    NumberPicker second;
    AlertDialog alert;

    //database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        sensor_Start = (Button) findViewById(R.id.start_sensor);
        sensor_Stop = (Button) findViewById((R.id.stop_sensor));
        sensor_Count = (TextView) findViewById(R.id.sensor_countdown);
        progress_Bar = (ProgressBar) findViewById(R.id.progressBar);
        minute = (NumberPicker) findViewById(R.id.minute_sensor);
        second = (NumberPicker) findViewById(R.id.seconds_sensor);

        minute.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        second.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        minute.setMinValue(0);
        minute.setMaxValue(59);

        second.setMinValue(0);
        second.setMaxValue(59);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(uid).child("measure");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Your data has been measured and uploaded, go to data page to check")
                .setNegativeButton("Confirm",null);

        alert = builder.create();


        sensor_Start.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                int minutes = minute.getValue();
                int seconds = second.getValue();
                counter = 60 * minutes + seconds;


                progress_Bar.setMax(counter);

                start();
               try {


                   countdown = new countDown(counter*1000,1000);

                   countdown.start();
               }catch(NumberFormatException e){

               }

            }
        });

        sensor_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stop();
            }
        });



    }


    private void start(){
        if(!(minute.getValue()==0 && second.getValue()==0)) {
            progress_Bar.setProgress(progress_Bar.getMax());
            sensor_Stop.setEnabled(true);
            post.setOn(1);
            databaseReference.setValue(post);
            minute.setVisibility(View.INVISIBLE);
            second.setVisibility(View.INVISIBLE);
        }
    }

    private void stop(){
        sensor_Stop.setEnabled(false);
        countdown.cancel();
        sensor_Count.setText(null);
        post.setOn(0);
        databaseReference.setValue(post);
        progress_Bar.setProgress(0);
        minute.setVisibility(View.VISIBLE);
        second.setVisibility(View.VISIBLE);
        alert.show();




    }



    public class countDown extends CountDownTimer {

        public countDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            NumberFormat formatter = new DecimalFormat("00");
            long min = (millisUntilFinished / 60000) ;
            long sec = (millisUntilFinished % 60000 / 1000) ;
            int progress = (int) (millisUntilFinished/1000);
            progress_Bar.setProgress(progress);

            sensor_Count.setText(formatter.format(min) + ":" + formatter.format(sec));

            counter--;

        }

        @Override
        public void onFinish() {
            sensor_Count.setText(null);
            post.setOn(0);
            databaseReference.setValue(post);
            progress_Bar.setProgress(0);
            minute.setVisibility(View.VISIBLE);
            second.setVisibility(View.VISIBLE);
            alert.show();
        }

    }



}
