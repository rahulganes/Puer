package com.puer.rahul.puer;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Autism extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_autism);

        Button autism_explore_btn = (Button)findViewById(R.id.autism_explore);
        autism_explore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autism_explr_int = new Intent(getApplicationContext() , Autism_explore.class);
                startActivity(autism_explr_int);
            }
        });

        Button autism_symptoms_btn = (Button)findViewById(R.id.autism_symptoms);
        autism_symptoms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autism_symp_int = new Intent(getApplicationContext() , Autism_symptoms.class);
                startActivity(autism_symp_int);
            }
        });

        Button autism_therapy_btn = (Button)findViewById(R.id.autism_therapy);
        autism_therapy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autism_therapy_int = new Intent(getApplicationContext() , Autism_therapy.class);
                startActivity(autism_therapy_int);
            }
        });

        Button autism_forum_btn = (Button)findViewById(R.id.autism_forum);
        autism_forum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autism_forum_int = new Intent(getApplicationContext() , Autism_forum.class);
                startActivity(autism_forum_int);
            }
        });
    }
}
