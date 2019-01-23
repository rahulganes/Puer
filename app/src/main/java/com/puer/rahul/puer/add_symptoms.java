package com.puer.rahul.puer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class add_symptoms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_add_symptoms);
        TextView textView = (TextView)findViewById(R.id.textView13);
        textView.setMovementMethod(new ScrollingMovementMethod());
        TextView textView2 = (TextView)findViewById(R.id.textView15);
        textView2.setMovementMethod(new ScrollingMovementMethod());

    }
}
