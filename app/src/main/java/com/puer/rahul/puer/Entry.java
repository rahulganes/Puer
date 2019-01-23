package com.puer.rahul.puer;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Entry extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_entry);

        Button add_btn = (Button)findViewById(R.id.attention);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_int = new Intent(getApplicationContext() , Attention.class);
                startActivity(add_int);
            }
        });

        Button anxiety_btn = (Button)findViewById(R.id.anxiety);
        anxiety_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anxiety_int = new Intent(getApplicationContext() , Anxiety.class);
                startActivity(anxiety_int);
            }
        });

        Button autism_btn = (Button)findViewById(R.id.autism);
        autism_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autism_int = new Intent(getApplicationContext() , Autism.class);
                startActivity(autism_int);
            }
        });

        Button schizophrenia_btn = (Button)findViewById(R.id.schizophrenia);
        schizophrenia_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schizophrenia_int = new Intent(getApplicationContext() , Schizophrenia.class);
                startActivity(schizophrenia_int);
            }
        });

    }
}