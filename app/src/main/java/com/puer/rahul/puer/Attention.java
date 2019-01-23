package com.puer.rahul.puer;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Attention extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_attention);

        Button add_explore_btn = (Button)findViewById(R.id.add_explore);
        add_explore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_explr_int = new Intent(getApplicationContext() , Attention_explore.class);
                startActivity(add_explr_int);
            }
        });

        Button add_therapy_btn = (Button)findViewById(R.id.add_therapy);
        add_therapy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_therapy_int = new Intent(getApplicationContext() , Attention_therapy.class);
                startActivity(add_therapy_int);
            }
        });

        Button add_symptoms_btn = (Button)findViewById(R.id.add_symptoms);
        add_symptoms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_symptoms_int = new Intent(getApplicationContext() , add_symptoms.class);
                startActivity(add_symptoms_int);
            }
        });

        Button add_forum_btn = (Button)findViewById(R.id.add_forum);
        add_forum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_forum_int = new Intent(getApplicationContext(),Attention_forum.class);
                startActivity(add_forum_int);
            }
        });
    }
}
