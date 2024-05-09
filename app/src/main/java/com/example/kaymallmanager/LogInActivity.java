package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogInActivity extends AppCompatActivity {

    Button login;

    Connection con;
    DBconnection dBconnection;
    ResultSet rs;
    String name , str;
    EditText email , password;
    ProgressBar progressBar;
    TextView text,resetpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        login=findViewById(R.id.login);
        email=findViewById(R.id.editTextUsername);
        password=findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        text=findViewById(R.id.reisterTextView);
        resetpass=findViewById(R.id.resetTextView);



        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ResetPass=new Intent(LogInActivity.this, ResetPassActivity.class);
                startActivity(ResetPass);
            }
        });


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent5=new Intent(LogInActivity.this, CreateAccountActivity.class);
                startActivity(intent5);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (emailText.isEmpty()) {
                    email.setError("Please enter email");
                } else if (passwordText.isEmpty()) {
                    password.setError("Please enter password");
                } else {
                    connect(emailText, passwordText);
                }


            }
        });
    }

    private void connect(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        try {
                dBconnection =new DBconnection();
                con = dBconnection.connectionclasss();
                if (con != null) {

                        //Toast.makeText(LogInActivity.this, "Connection Established", Toast.LENGTH_SHORT).show();



                    String sql = "SELECT * FROM AdminUser WHERE email = '" + email + "' AND password = '" + password + "' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(LogInActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LogInActivity.this, "Enter correct email or password", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        });


                    }
















                } else {
                    Toast.makeText(LogInActivity.this, "Connection not Established", Toast.LENGTH_SHORT).show();

                    // Perform actions for a successful connection
                }
            } catch (Exception e) {
                Log.e("Error : ", e.getMessage());

            }



    }

}
