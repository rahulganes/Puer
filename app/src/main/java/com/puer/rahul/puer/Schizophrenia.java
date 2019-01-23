package com.puer.rahul.puer;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Schizophrenia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_schizophrenia);
        Button schizophrenia_explore_btn = (Button)findViewById(R.id.Schizophrenia_explore);
        schizophrenia_explore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schizophrenia_explr_int = new Intent(getApplicationContext() , Schizophrenia_explore.class);
                startActivity(schizophrenia_explr_int);
            }
        });

        Button schizophrenia_symptoms_btn = (Button)findViewById(R.id.Schizophrenia_symptoms);
        schizophrenia_symptoms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schizophrenia_symp_int = new Intent(getApplicationContext() , Schizophrenia_symptoms.class);
                startActivity(schizophrenia_symp_int);
            }
        });

        Button schizophrenia_therapy_btn = (Button)findViewById(R.id.Schizophrenia_therapy);
        schizophrenia_therapy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schizophrenia_therapy_int = new Intent(getApplicationContext() , Schizophrenia_therapy.class);
                startActivity(schizophrenia_therapy_int);
            }
        });

        Button schizophrenia_forum_btn = (Button)findViewById(R.id.Schizophrenia_forum);
        schizophrenia_forum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schizophrenia_forum_int = new Intent(getApplicationContext() , home_actiivty.class);
                startActivity(schizophrenia_forum_int);
            }
        });
        
    }
}
