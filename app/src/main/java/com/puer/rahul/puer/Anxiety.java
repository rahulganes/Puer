package com.puer.rahul.puer;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Anxiety extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_anxiety);

        Button anxiety_explore_btn = (Button)findViewById(R.id.anxiety_explore);
        anxiety_explore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxiety_explr_int = new Intent(getApplicationContext() , Anxiety_explore.class);
                startActivity(anxiety_explr_int);
            }
        });

        Button anxiety_symptoms_btn = (Button)findViewById(R.id.anxiety_symptoms);
        anxiety_symptoms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxiety_symp_int = new Intent(getApplicationContext() , Anxiety_symptoms.class);
                startActivity(anxiety_symp_int);
            }
        });

        Button anxiety_therapy_btn = (Button)findViewById(R.id.anxiety_therapy);
        anxiety_therapy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxiety_therapy_int = new Intent(getApplicationContext() , Anxiety_therapy.class);
                startActivity(anxiety_therapy_int);
            }
        });

        Button anxiety_forum_btn = (Button)findViewById(R.id.anxiety_forum);
        anxiety_forum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxiety_forum_int = new Intent(getApplicationContext() , Anxiety_forum.class);
                startActivity(anxiety_forum_int);
            }
        });


    }
}
