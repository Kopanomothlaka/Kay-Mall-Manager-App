package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

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

public class ResetPassActivity extends AppCompatActivity {

    EditText emailEdit, passEdit, confirmEdit;
    Button btn;
    Connection con;
    DBconnection dBconnection;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        emailEdit = findViewById(R.id.resetEmaill);
        passEdit = findViewById(R.id.resetPassPass);
        confirmEdit = findViewById(R.id.resetPassConfrim);

        btn = findViewById(R.id.resetPassBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });
    }

    private void ResetPassword() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String enterEmail = emailEdit.getText().toString().trim();
                String newPass = passEdit.getText().toString().trim();
                String confirmPass = confirmEdit.getText().toString().trim();

                if (enterEmail.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    emailEdit.setError("Enter all fields!!");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(enterEmail).matches()) {
                    emailEdit.setError("Please enter a valid email address");
                } else if (newPass.length() < 6) {
                    passEdit.setError("Password must be at least 6 characters long");
                } else if (!newPass.equals(confirmPass)) {
                    confirmEdit.setError("Passwords do not match");
                } else {
                    String sql = "UPDATE AdminUser SET password = ? WHERE email = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, newPass);
                    preparedStatement.setString(2, enterEmail);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(ResetPassActivity.this, "Email Reset Successfully", Toast.LENGTH_LONG).show();
                        emailEdit.setText("");
                        passEdit.setText("");
                        confirmEdit.setText("");
                    } else {
                        Toast.makeText(ResetPassActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(ResetPassActivity.this, "Connection not Established", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }
}