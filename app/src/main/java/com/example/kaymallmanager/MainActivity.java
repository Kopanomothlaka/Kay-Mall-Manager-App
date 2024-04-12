package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

CardView stores,security,cleaners,maintain,event,parking;
ImageView loutout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stores=findViewById(R.id.gotostores);
        security=findViewById(R.id.GoToSecurity);
        cleaners=findViewById(R.id.gotocleaners);
        loutout=findViewById(R.id.logout);

        loutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });


        stores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,StoresActivity.class);
                startActivity(intent);
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 =new Intent(MainActivity.this,SecurityActivity.class);
                startActivity(intent2);
            }
        });
        cleaners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 =new Intent(MainActivity.this,CleanersActivity.class);
                startActivity(intent3);
            }
        });






    }
}