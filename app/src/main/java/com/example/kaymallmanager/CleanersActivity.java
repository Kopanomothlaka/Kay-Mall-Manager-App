package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CleanersActivity extends AppCompatActivity {

    Connection con;
    DBconnection dBconnection;
    ResultSet rs;

    TextView cal,cleaners,shift;
    EditText enter,CleanerId,CleanerName,Mall,Email,ShiftSchedule,Area;
    Button SearchBnt,AddBtn,Update,Delete;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaners);
        cal=findViewById(R.id.numberclaners);
        enter=findViewById(R.id.Searchidcleaner);
        cleaners=findViewById(R.id.cleanerName);
        shift=findViewById(R.id.shiftIdshow);
        SearchBnt=findViewById(R.id.searchBtnCleaners);

        AddBtn=findViewById(R.id.AddCleaner);
        Update=findViewById(R.id.UpdateCleaner);
        Delete=findViewById(R.id.DeleteCleaner);

        CleanerId=findViewById(R.id.CleanerID);
        CleanerName=findViewById(R.id.cleanerEdittext);
        Email=findViewById(R.id.EmailEdittext);
        ShiftSchedule=findViewById(R.id.ShirtEdittext);
        Area=findViewById(R.id.AreaEdittext);
        progressDialog = new ProgressDialog(this);



        SearchBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchMethod();

            }
        });
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCleaner();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCleaner();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCleaner();
            }
        });

        calculateSecurity();
        Pie();

    }


    private void Pie() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT * FROM Cleaners";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                ArrayList<PieEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();

                while (rs.next()) {
                    int storeID = rs.getInt("CleanerID"); // Replace "StoreID" with the actual column name from the database
                    String storeName = rs.getString("CleanerName"); // Replace "StoreName" with the actual column name from the database
                    // Replace "FloorSection" with the actual column name from the database

                    entries.add(new PieEntry(storeID, storeName + " "));
                    labels.add(String.valueOf(storeID));
                }
                rs.close();
                st.close();

                PieChart pieChart = findViewById(R.id.lineChartCleaners);

                PieDataSet dataSet = new PieDataSet(entries, "Label");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueTextSize(12f);

                PieData pieData = new PieData(dataSet);

                pieChart.setData(pieData);
                pieChart.setEntryLabelColor(Color.WHITE);
                pieChart.setEntryLabelTextSize(12f);
                pieChart.setUsePercentValues(false);
                pieChart.getDescription().setEnabled(false);
                pieChart.getLegend().setEnabled(false);
                pieChart.invalidate();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(CleanersActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void DeleteCleaner() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String cleanerID = CleanerId.getText().toString().trim();

                if (cleanerID.isEmpty()) {
                    Toast.makeText(this, "Please enter a Cleaner ID", Toast.LENGTH_SHORT).show();
                } else {
                    // Show a confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this cleaner?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String sqlDelete = "DELETE FROM Cleaners WHERE CleanerID = '" + cleanerID + "'";
                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlDelete);

                                if (rowsAffected > 0) {
                                    // Deletion successful
                                    Toast.makeText(getApplicationContext(), "Cleaner deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after deletion if needed
                                    CleanerId.setText("");
                                    CleanerName.setText("");
                                    Mall.setText("");
                                    Email.setText("");
                                    ShiftSchedule.setText("");
                                    Area.setText("");
                                } else {
                                    // Deletion failed, cleaner not found
                                    Toast.makeText(getApplicationContext(), "Cleaner not found or deletion failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("Error : ", e.getMessage());
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancelled deletion
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
        }
    }

    private void UpdateCleaner() {
        try {
            progressDialog.setMessage("Updating cleaner...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String cleanerId = CleanerId.getText().toString().trim();
                String cleanerName = CleanerName.getText().toString().trim();
                String contactInformation = Email.getText().toString().trim(); // Assuming Email field corresponds to ContactInformation
                String shift = ShiftSchedule.getText().toString().trim();
                String area = Area.getText().toString().trim();

                if (cleanerId.isEmpty()) {
                    Toast.makeText(this, "Please enter Cleaner ID for update", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!cleanerId.matches("[0-9]+")) {
                    Toast.makeText(this, "Enter a numeric Cleaner ID", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    // Show a confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Update");
                    builder.setMessage("Are you sure you want to update this cleaner?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                StringBuilder sqlUpdate = new StringBuilder("UPDATE Cleaners SET ");
                                List<String> updates = new ArrayList<>();

                                if (!cleanerName.isEmpty()) {
                                    updates.add("CleanerName = '" + cleanerName + "'");
                                }
                                if (!contactInformation.isEmpty()) {
                                    updates.add("ContactInformation = '" + contactInformation + "'");
                                }
                                if (!shift.isEmpty()) {
                                    updates.add("ShiftSchedule = '" + shift + "'");
                                }
                                if (!area.isEmpty()) {
                                    updates.add("CleaningAreas = '" + area + "'");
                                }

                                if (updates.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "No valid updates provided", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return; // Exit method if no valid updates
                                }

                                sqlUpdate.append(String.join(", ", updates));
                                sqlUpdate.append(" WHERE CleanerID = '").append(cleanerId).append("'");

                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlUpdate.toString());

                                if (rowsAffected > 0) {
                                    // Update successful
                                    Toast.makeText(getApplicationContext(), "Cleaner updated successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after update if needed
                                    CleanerId.setText("");
                                    CleanerName.setText("");
                                    Email.setText("");
                                    ShiftSchedule.setText("");
                                    Area.setText("");
                                } else {
                                    // Update failed
                                    Toast.makeText(getApplicationContext(), "Failed to update cleaner", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                Log.e("Error : ", e.getMessage());
                                progressDialog.dismiss();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancelled update
                            progressDialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }





    private void calculateSecurity() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sqldisplay = "SELECT COUNT(*) AS CleanerID FROM Cleaners";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqldisplay);
                if (rs.next()) {
                    int CleanerID = rs.getInt("CleanerID");
                    cal.setText(" " + CleanerID);
                }
                rs.close(); // Close ResultSet
                st.close(); // Close statement
                con.close(); // Close connection
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(CleanersActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    private void searchMethod() {

        try {


            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String CleanerName = enter.getText().toString().trim();

                if (CleanerName.isEmpty()) {
                    Toast.makeText(this, "Please enter a Store ID", Toast.LENGTH_SHORT).show();


                } else {
                    String sqlsearch = "SELECT * FROM Cleaners WHERE CleanerName = '" + CleanerName + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlsearch);

                    if (rs.next()) {
                        // Store found, update UI with store information
                        cleaners.setText(rs.getString("CleanerName"));
                        shift.setText(rs.getString("ShiftSchedule"));


                    } else {
                        // Store not found
                        Toast.makeText(this, "Security Name not found", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("Error : ", e.getMessage());

        }




    }
    private void AddCleaner() {
        try {
            progressDialog.setMessage("Adding cleaner...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String cleanerId = CleanerId.getText().toString().trim();
                String cleanerName = CleanerName.getText().toString().trim();
                String contactInformation = Email.getText().toString().trim();
                String shift = ShiftSchedule.getText().toString().trim();
                String area = Area.getText().toString().trim();

                if (cleanerId.isEmpty() || cleanerName.isEmpty() || contactInformation.isEmpty() || shift.isEmpty() || area.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!cleanerId.matches("[0-9]+")) {
                    Toast.makeText(this, "Enter a numeric Cleaner ID", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(contactInformation).matches()) {
                    Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    // Show a confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Addition");
                    builder.setMessage("Are you sure you want to add this cleaner?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String sqlInsert = "INSERT INTO Cleaners (CleanerID, CleanerName, ContactInformation, ShiftSchedule, CleaningAreas) " +
                                        "VALUES ('" + cleanerId + "', '" + cleanerName + "', '" + contactInformation + "', '" + shift + "', '" + area + "')";
                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlInsert);

                                if (rowsAffected > 0) {
                                    // Insertion successful
                                    Toast.makeText(getApplicationContext(), "Cleaner added successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after insertion if needed
                                    CleanerId.setText("");
                                    CleanerName.setText("");
                                    Email.setText("");
                                    ShiftSchedule.setText("");
                                    Area.setText("");
                                } else {
                                    // Insertion failed
                                    Toast.makeText(getApplicationContext(), "Failed to add cleaner", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                Log.e("Error : ", e.getMessage());
                                progressDialog.dismiss();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancelled addition
                            progressDialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }



}