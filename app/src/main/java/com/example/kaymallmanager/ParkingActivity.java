package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ParkingActivity extends AppCompatActivity {
    Connection con;
    DBconnection dBconnection;
    ResultSet rs;
    EditText parkingid,MALL,ParkingArea,Availability,find;
    Button SearchBnt,AddBtn,Update,Delete;
    private ProgressDialog progressDialog;
    TextView viewname,viewava;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        parkingid=findViewById(R.id.parkingID);
        ParkingArea=findViewById(R.id.parkingnameEdittext);
        Availability=findViewById(R.id.availableEdittext);

        find=findViewById(R.id.Searchidparking);
        SearchBnt=findViewById(R.id.searchBtnE);
        AddBtn=findViewById(R.id.AddE);
        Update=findViewById(R.id.UpdateE);
        Delete=findViewById(R.id.DeleteE);
        progressDialog = new ProgressDialog(this);

        viewava=findViewById(R.id.shiftIdshow);
        viewname=findViewById(R.id.parkingnamme);



        SearchBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchMethod();

            }
        });

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddParking();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEvent();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteEvent();
            }
        });


    }

    private void searchMethod() {
        try {
            progressDialog.setMessage("Searching parking...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String parkingId = find.getText().toString().trim();

                if (parkingId.isEmpty()) {
                    Toast.makeText(this, "Please enter Parking ID for search", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!parkingId.matches("[0-9]+")) {
                    Toast.makeText(this, "Enter a numeric Parking ID", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    String sqlSearch = "SELECT ParkingArea, Availability FROM Parking WHERE ParkingID = '" + parkingId + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlSearch);

                    if (rs.next()) {
                        // Parking information found
                        String parkingArea = rs.getString("ParkingArea");
                        String availability = rs.getString("Availability");

                        // Display the information in the corresponding TextViews
                        viewname.setText(parkingArea);
                        viewava.setText(availability);

                        Toast.makeText(this, "Parking information retrieved", Toast.LENGTH_SHORT).show();
                    } else {
                        // Parking not found
                        viewname.setText("");
                        viewava.setText("");
                        Toast.makeText(this, "Parking not found", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }


    private void DeleteEvent() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String parkingId = parkingid.getText().toString().trim();

                if (parkingId.isEmpty()) {
                    Toast.makeText(this, "Please enter Parking ID for deletion", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!parkingId.matches("[0-9]+")) {
                    Toast.makeText(this, "Enter a numeric Parking ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this parking?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            progressDialog.setMessage("Deleting parking...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            String sqlDelete = "DELETE FROM Parking WHERE ParkingID = '" + parkingId + "'";
                            Statement st = con.createStatement();
                            int rowsAffected = st.executeUpdate(sqlDelete);

                            if (rowsAffected > 0) {
                                // Deletion successful
                                Toast.makeText(ParkingActivity.this, "Parking deleted successfully", Toast.LENGTH_SHORT).show();
                                // Clear the Parking ID field after deletion
                                parkingid.setText("");
                                ParkingArea.setText("");
                                Availability.setText("");
                            } else {
                                // Deletion failed, parking not found
                                Toast.makeText(ParkingActivity.this, "Parking not found or deletion failed", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        } catch (SQLException e) {
                            Log.e("SQL Error: ", e.getMessage());
                            progressDialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }


    private void UpdateEvent() {
        try {
            progressDialog.setMessage("Updating parking...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String parkingId = parkingid.getText().toString().trim();
                String parkingArea = ParkingArea.getText().toString().trim();
                String availability = Availability.getText().toString().trim();

                if (parkingId.isEmpty()) {
                    Toast.makeText(this, "Please enter Parking ID for update", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!parkingId.matches("[0-9]+")) {
                    Toast.makeText(this, "Enter a numeric Parking ID", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Update");
                    builder.setMessage("Are you sure you want to update this parking?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                StringBuilder sqlUpdate = new StringBuilder("UPDATE Parking SET ");
                                List<String> updates = new ArrayList<>();

                                if (!parkingArea.isEmpty()) {
                                    updates.add("ParkingArea = '" + parkingArea + "'");
                                }
                                if (!availability.isEmpty()) {
                                    updates.add("Availability = '" + availability + "'");
                                }

                                if (updates.isEmpty()) {
                                    Toast.makeText(ParkingActivity.this, "No valid updates provided", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return; // Exit method if no valid updates
                                }

                                sqlUpdate.append(String.join(", ", updates));
                                sqlUpdate.append(" WHERE ParkingID = '").append(parkingId).append("'");

                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlUpdate.toString());

                                if (rowsAffected > 0) {
                                    // Update successful
                                    Toast.makeText(ParkingActivity.this, "Parking updated successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after update if needed
                                    parkingid.setText("");
                                    ParkingArea.setText("");
                                    Availability.setText("");
                                } else {
                                    // Update failed
                                    Toast.makeText(ParkingActivity.this, "Failed to update parking", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            } catch (SQLException e) {
                                Log.e("SQL Error: ", e.getMessage());
                                progressDialog.dismiss();
                            }
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }



    private void AddParking() {
        try {
            progressDialog.setMessage("Adding parking...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String parkingId = parkingid.getText().toString().trim();
                String parkingArea = ParkingArea.getText().toString().trim();
                String availability = Availability.getText().toString().trim();

                if (parkingId.isEmpty() || parkingArea.isEmpty() || availability.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!parkingId.matches("[0-9]+")) {
                    Toast.makeText(this, "Enter a numeric Parking ID", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    String sqlInsert = "INSERT INTO Parking (ParkingID, ParkingArea, Availability) " +
                            "VALUES ('" + parkingId + "', '" + parkingArea + "', '" + availability + "')";
                    Statement st = con.createStatement();
                    int rowsAffected = ((Statement) st).executeUpdate(sqlInsert);

                    if (rowsAffected > 0) {
                        // Insertion successful
                        Toast.makeText(this, "Parking added successfully", Toast.LENGTH_SHORT).show();
                        // Clear the fields after insertion if needed
                        parkingid.setText("");
                        ParkingArea.setText("");
                        Availability.setText("");
                    } else {
                        // Insertion failed
                        Toast.makeText(this, "Failed to add parking", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }

}