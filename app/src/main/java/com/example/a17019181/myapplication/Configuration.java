package com.example.a17019181.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


//database
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Configuration extends AppCompatActivity {
    public int counter;
    Button config_Start;
    Button config_Stop;
    TextView config_Count;
    ProgressBar progress_Bar_Config;
    ConfigurationData post = new ConfigurationData(0,1);
    String validation;
    CountDownTimer countdown;
    NumberPicker second;
    NumberPicker minute;
    Spinner dropdown;

    //database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        config_Start = (Button) findViewById(R.id.start_config);
        config_Stop = (Button) findViewById((R.id.stop_config));
        config_Count = (TextView) findViewById(R.id.config_countdown);
        progress_Bar_Config = (ProgressBar) findViewById(R.id.progressBarConfig);

        dropdown = (Spinner) findViewById(R.id.dropdownlist);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdown.setAdapter(dropdownAdapter);


        minute = (NumberPicker) findViewById(R.id.minute_config);
        minute.setMinValue(0);
        minute.setMaxValue(59);

        second = (NumberPicker) findViewById(R.id.seconds_config);
        second.setMinValue(0);
        second.setMaxValue(59);

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



        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(uid).child("configuration");




        config_Start.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                int minutes = minute.getValue();
                int seconds = second.getValue();


                counter = 60 * minutes + seconds;

                progress_Bar_Config.setMax(counter);

                start();
                try {


                    countdown = new countDown(counter*1000,1000);

                    countdown.start();
                }catch(NumberFormatException e){

                }

            }
        });

        config_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });



    }


    private void start(){


        if(!(minute.getValue()==0 && second.getValue()==0)){
        int diff = Integer.parseInt(dropdown.getSelectedItem().toString());
        progress_Bar_Config.setProgress(progress_Bar_Config.getMax());
        config_Stop.setEnabled(true);
        post.setOnConfig(1);
        post.setDifficulty(diff);
        databaseReference.setValue(post);
        minute.setVisibility(View.INVISIBLE);
        second.setVisibility(View.INVISIBLE);}
    }

    private void stop(){
        config_Stop.setEnabled(false);
        countdown.cancel();
        config_Count.setText(null);
        post.setOnConfig(0);
        databaseReference.setValue(post);
        progress_Bar_Config.setProgress(0);
        minute.setVisibility(View.VISIBLE);
        second.setVisibility(View.VISIBLE);
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
            progress_Bar_Config.setProgress(progress);

            config_Count.setText(formatter.format(min) + ":" + formatter.format(sec));

            counter--;

        }

        @Override
        public void onFinish() {
            config_Count.setText(null);
            post.setOnConfig(0);
            databaseReference.setValue(post);
            progress_Bar_Config.setProgress(0);
            minute.setVisibility(View.VISIBLE);
            second.setVisibility(View.VISIBLE);

        }

    }



}
