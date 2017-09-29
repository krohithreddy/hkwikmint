package com.example.rohithreddy.hkwikmint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MobileNumber extends AppCompatActivity {
    public static int button =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);

        Button back = (Button) findViewById(R.id.back);
        Button Proceed = (Button) findViewById(R.id.submit);
        Proceed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                button=1;
                Intent myIntent = new Intent(MobileNumber.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
