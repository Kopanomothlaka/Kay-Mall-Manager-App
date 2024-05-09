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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class StoresActivity extends AppCompatActivity {

    Connection con;
    DBconnection dBconnection;
    ResultSet rs;
    EditText StoreID,StoreName,StoreCategory,MallID,FloorSection,ContactInformation,searchEditText ;
    Button add ,update,delete, search;
    TextView numberofstore , storeName,Floor;
    private ProgressDialog progressDialog;

    private ListView storeListView;
    private ArrayAdapter<String> storeAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        StoreID=findViewById(R.id.idid);
        StoreName=findViewById(R.id.nameEdittext);
        FloorSection=findViewById(R.id.floorEdittext);
        ContactInformation=findViewById(R.id.emailEdittext);


        add=findViewById(R.id.addstore);

        numberofstore=findViewById(R.id.numberofstores);

        update=findViewById(R.id.updatestore);
        delete=findViewById(R.id.DELETEstore);
        storeName=findViewById(R.id.storename);
        Floor=findViewById(R.id.FloorSection);
        search=findViewById(R.id.searchBtn);
        searchEditText=findViewById(R.id.Searchid);


        progressDialog = new ProgressDialog(this);


        storeListView = findViewById(R.id.storeListView);
        storeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        storeListView.setAdapter(storeAdapter);












        storeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStore();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStore();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteStore();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMethod();
            }
        });
        calculateStoreCount();
        DisplayStore();
        Graph();
        Pie();


    }

    private void Pie() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT * FROM Store";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                ArrayList<PieEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();

                while (rs.next()) {
                    int storeID = rs.getInt("StoreID"); // Replace "StoreID" with the actual column name from the database
                    String storeName = rs.getString("StoreName"); // Replace "StoreName" with the actual column name from the database
                    // Replace "FloorSection" with the actual column name from the database

                    entries.add(new PieEntry(storeID, storeName + " "));
                    labels.add(String.valueOf(storeID));
                }
                rs.close();
                st.close();

                PieChart pieChart = findViewById(R.id.lineChart2);

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
            Toast.makeText(StoresActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void Graph() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT * FROM Store";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();

                int index = 0;
                while (rs.next()) {
                    int storeID = rs.getInt("StoreID"); // Replace "StoreID" with the actual column name from the database
                    String storeName = rs.getString("StoreName"); // Replace "StoreName" with the actual column name from the database
                    String floorSection = rs.getString("FloorSection"); // Replace "FloorSection" with the actual column name from the database

                    float xValue = index; // Use the index as the x-value
                    float yValue = storeID; // StoreID as the y-value

                    entries.add(new BarEntry(xValue, yValue));
                    labels.add(storeName + " - " + floorSection);

                    index++;
                }
                rs.close();
                st.close();

                BarChart barChart = findViewById(R.id.lineChart);

                BarDataSet dataSet = new BarDataSet(entries, "Label");
                BarData barData = new BarData(dataSet);

                barChart.setData(barData);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setLabelRotationAngle(-45);
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setEnabled(false);
                barChart.invalidate();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(StoresActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void searchMethod() {
        try {
            progressDialog.setMessage("Searching store...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String storeID = searchEditText.getText().toString().trim();

                if (storeID.isEmpty()) {
                    Toast.makeText(this, "Please enter a Store ID", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                } else {
                    String sqlsearch = "SELECT * FROM Store WHERE StoreID = '" + storeID + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlsearch);

                    if (rs.next()) {
                        // Store found, update UI with store information
                        StoreID.setText(rs.getString("StoreID"));
                        StoreName.setText(rs.getString("StoreName"));
                        FloorSection.setText(rs.getString("FloorSection"));
                        ContactInformation.setText(rs.getString("ContactInformation"));
                        progressDialog.dismiss();
                    } else {
                        // Store not found
                        Toast.makeText(this, "Store not found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }


    private void DisplayStore() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT * FROM Store";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder floorSectionBuilder = new StringBuilder();


                while (rs.next()){
                    String storeName = rs.getString("StoreName");
                    String FloorSection=rs.getString("FloorSection");
                    storeAdapter.add(storeName);
                    storeAdapter.add(FloorSection);
                    System.out.println(storeName);
                    System.out.println(FloorSection);


                    stringBuilder.append(storeName).append("\n");
                    floorSectionBuilder.append(FloorSection).append("\n");

                }

                storeName.setText(stringBuilder.toString());
                Floor.setText(floorSectionBuilder.toString());

                rs.close(); // Close ResultSet
                st.close(); // Close statement
                con.close(); // Close connection
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(StoresActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void DeleteStore() {
        try {
            progressDialog.setMessage("Deleting store...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String storeID = StoreID.getText().toString().trim();

                if (storeID.isEmpty()) {
                    Toast.makeText(this, "Please enter a Store ID", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    // Show a confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this store?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String sqlDelete = "DELETE FROM Store WHERE StoreID = '" + storeID + "'";
                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlDelete);

                                if (rowsAffected > 0) {
                                    // Deletion successful
                                    Toast.makeText(getApplicationContext(), "Store deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after deletion if needed
                                    StoreID.setText("");
                                    StoreName.setText("");
                                    FloorSection.setText("");
                                    ContactInformation.setText("");
                                } else {
                                    // Deletion failed, store not found
                                    Toast.makeText(getApplicationContext(), "Store not found or deletion failed", Toast.LENGTH_SHORT).show();
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
                            // Cancelled deletion
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


    private void UpdateStore() {
        try {
            progressDialog.setMessage("Updating store...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String storeId = StoreID.getText().toString().trim();
                String storeName = StoreName.getText().toString().trim();
                String floorSection = FloorSection.getText().toString().trim();
                String contactInformation = ContactInformation.getText().toString().trim();

                if (storeId.isEmpty()) {
                    Toast.makeText(this, "Please enter Store ID for update", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!storeId.matches("\\d{2}")) {
                    Toast.makeText(this, "Store ID should contain exactly two digits", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    StringBuilder sqlUpdate = new StringBuilder("UPDATE Store SET ");
                    List<String> updates = new ArrayList<>();

                    if (!storeName.isEmpty() && storeName.matches("[a-zA-Z]+")) {
                        updates.add("StoreName = '" + storeName + "'");
                    }
                    if (!floorSection.isEmpty() && floorSection.matches("[a-zA-Z]+")) {
                        updates.add("FloorSection = '" + floorSection + "'");
                    }
                    if (!contactInformation.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(contactInformation).matches()) {
                        updates.add("ContactInformation = '" + contactInformation + "'");
                    }

                    if (updates.isEmpty()) {
                        Toast.makeText(this, "No valid updates provided", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return; // Exit method if no valid updates
                    }

                    sqlUpdate.append(String.join(", ", updates));
                    sqlUpdate.append(" WHERE StoreId = '").append(storeId).append("'");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Update");
                    builder.setMessage("Are you sure you want to update this store?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlUpdate.toString());

                                if (rowsAffected > 0) {
                                    // Update successful
                                    Toast.makeText(StoresActivity.this, "Store updated successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after update if needed
                                    StoreID.setText("");
                                    StoreName.setText("");
                                    FloorSection.setText("");
                                    ContactInformation.setText("");
                                } else {
                                    // Update failed
                                    Toast.makeText(StoresActivity.this, "Failed to update store", Toast.LENGTH_SHORT).show();
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




    private void calculateStoreCount() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT COUNT(*) AS StoreID FROM Store";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    int StoreID = rs.getInt("StoreID");
                    numberofstore.setText(" " + StoreID);
                }
                rs.close(); // Close ResultSet
                st.close(); // Close statement
                con.close(); // Close connection
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(StoresActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void AddStore() {
        try {
            progressDialog.setMessage("Adding store...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String storeId = StoreID.getText().toString().trim();
                String storeName = StoreName.getText().toString().trim();
                String floorSection = FloorSection.getText().toString().trim();
                String contactInformation = ContactInformation.getText().toString().trim();

                if (storeId.isEmpty() || storeName.isEmpty() || floorSection.isEmpty() || contactInformation.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!storeId.matches("\\d{2}")) {
                    Toast.makeText(this, "Store ID should contain exactly two digits", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!storeName.matches("[a-zA-Z]+")) {
                    StoreName.setError("Store name should contain only letters");
                    progressDialog.dismiss();
                } else if (!floorSection.matches("[a-zA-Z]+")) {
                    FloorSection.setError("Floor name should contain only letters");
                    progressDialog.dismiss();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(contactInformation).matches()) {
                    ContactInformation.setError("Enter valid email address");
                    progressDialog.dismiss();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirm Add");
                    builder.setMessage("Are you sure you want to add this store?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                String sqlInsert = "INSERT INTO Store (StoreId, StoreName, FloorSection, ContactInformation) " +
                                        "VALUES ('" + storeId + "','" + storeName + "', '" + floorSection + "', '" + contactInformation + "')";
                                Statement st = con.createStatement();
                                int rowsAffected = st.executeUpdate(sqlInsert);

                                if (rowsAffected > 0) {
                                    // Insertion successful
                                    Toast.makeText(StoresActivity.this, "Store added successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the fields after insertion if needed
                                    StoreID.setText("");
                                    StoreName.setText("");
                                    FloorSection.setText("");
                                    ContactInformation.setText("");
                                } else {
                                    // Insertion failed
                                    Toast.makeText(StoresActivity.this, "Failed to add store", Toast.LENGTH_SHORT).show();
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



}