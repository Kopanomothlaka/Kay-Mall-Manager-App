package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateAccountActivity extends AppCompatActivity {

    EditText createEmail, createPass, createConfirmPass;
    Button createAcc;

    Connection con;
    DBconnection dBconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createEmail = findViewById(R.id.registerEmail);
        createPass = findViewById(R.id.registerPass);
        createConfirmPass = findViewById(R.id.registerPassConfrim);
        createAcc = findViewById(R.id.REGISTER);

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = createEmail.getText().toString().trim();
                String passwordText = createPass.getText().toString().trim();
                String confirmPasswordText = createConfirmPass.getText().toString().trim();

                if (emailText.isEmpty()) {
                    createEmail.setError("Please enter email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    createEmail.setError("Please enter a valid email address");
                } else if (passwordText.isEmpty()) {
                    createPass.setError("Please enter password");
                }
                else if (passwordText.length()<6) {
                    createPass.setError("Password must contain more than 6 characters");
                }

                else if (!passwordText.equals(confirmPasswordText)) {
                    createConfirmPass.setError("Passwords do not match");
                } else {
                    registerUser(emailText, passwordText);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                // Check if the email already exists in the database
                String checkEmailQuery = "SELECT * FROM AdminUser WHERE email = ?";
                PreparedStatement checkEmailStmt = con.prepareStatement(checkEmailQuery);
                checkEmailStmt.setString(1, email);
                ResultSet emailResultSet = checkEmailStmt.executeQuery();

                if (emailResultSet.next()) {
                    // Email already exists
                    Toast.makeText(CreateAccountActivity.this, "Email already exists", Toast.LENGTH_LONG).show();
                } else {
                    // Email does not exist, proceed with registration
                    String insertQuery = "INSERT INTO AdminUser (email, password) VALUES (?, ?)";
                    PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                    insertStmt.setString(1, email);
                    insertStmt.setString(2, password);
                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(CreateAccountActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateAccountActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(CreateAccountActivity.this, "Connection not Established", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
    }
}