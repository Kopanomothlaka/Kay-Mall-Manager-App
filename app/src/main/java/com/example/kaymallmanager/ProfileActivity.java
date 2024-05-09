package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileActivity extends AppCompatActivity {

    ImageView loutout,home;
    EditText email,emailResetNew,newPassword,confirmPassword;
    Button ResetPass ,updateBtn;
    Connection con;
    DBconnection dBconnection;
    ResultSet rs;
    TextView view_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        home=findViewById(R.id.backhome);
        email=findViewById(R.id.adminemail);
        loutout=findViewById(R.id.logout);


        view_data=findViewById(R.id.view_data);
        updateBtn=findViewById(R.id.updatebtn);

        emailResetNew=findViewById(R.id.adminEmailSec);
        newPassword=findViewById(R.id.adminEmailreset);
        confirmPassword=findViewById(R.id.adminemailconform);
        ResetPass=findViewById(R.id.resetbtn);

        loutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out=new Intent(ProfileActivity.this,LogInActivity.class);
                startActivity(out);
            }
        });





        DisplayDetails();


         ResetPass.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ResetPassword();
             }
         });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateEmail();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ResetPassword() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String enterEmail = emailResetNew.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();


                if (enterEmail.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    emailResetNew.setError("Enter all fields !!");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(enterEmail).matches()) {
                    emailResetNew.setError("Please Enter a Valid Email Address");
                }
                else if(newPass.length()<6){
                    newPassword.setError("Password must be at least 6 characters long");
                } else if (!newPass.equals(confirmPass)) {
                    confirmPassword.setError("Passwords do not match");

                } else {

                    String sql = "UPDATE AdminUser SET password = ? WHERE email = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, newPass);
                    preparedStatement.setString(2, enterEmail);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(ProfileActivity.this, "Email Reset Successfully", Toast.LENGTH_LONG).show();
                        emailResetNew.setText("");
                        newPassword.setText("");
                        confirmPassword.setText("");
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
        catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

    }

    private void UpdateEmail() {




            try {
                dBconnection = new DBconnection();
                con = dBconnection.connectionclasss();
                if (con != null) {
                    String AdminEmail = email.getText().toString().trim();


                    if (AdminEmail.isEmpty()) {
                        Toast.makeText(ProfileActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(AdminEmail).matches()) {
                        Toast.makeText(ProfileActivity.this, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                    }


                    else {

                        String sql = "UPDATE AdminUser SET email = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, AdminEmail);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            Toast.makeText(ProfileActivity.this, "Email Reset Successfully", Toast.LENGTH_LONG).show();
                            email.setText("");
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
            catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }


    }

    private void DisplayDetails() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {

                String query = "SELECT * FROM AdminUser "; // Replace YourTableName with your actual table name and userID with the appropriate column name for identifying the user
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    String userEmail = rs.getString("email");
                    view_data.setText(userEmail); // Set the fetched email to the EditText
                }

            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}