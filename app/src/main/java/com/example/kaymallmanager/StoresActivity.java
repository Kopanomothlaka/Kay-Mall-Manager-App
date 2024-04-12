package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
        StoreCategory=findViewById(R.id.categoryEdittext);
        MallID=findViewById(R.id.mallEdittext);
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
                        StoreCategory.setText(rs.getString("StoreCategoryID"));
                        MallID.setText(rs.getString("MallID"));
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
                    String sqlDelete = "DELETE FROM Store WHERE StoreID = '" + storeID + "'";
                    Statement st = con.createStatement();
                    int rowsAffected = st.executeUpdate(sqlDelete);

                    if (rowsAffected > 0) {
                        // Deletion successful
                        Toast.makeText(this, "Store deleted successfully", Toast.LENGTH_SHORT).show();
                        // Clear the fields after deletion if needed
                        StoreID.setText("");
                        StoreName.setText("");
                        StoreCategory.setText("");
                        MallID.setText("");
                        FloorSection.setText("");
                        ContactInformation.setText("");
                    } else {
                        // Deletion failed, store not found
                        Toast.makeText(this, "Store not found or deletion failed", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());
            progressDialog.dismiss();
        }
    }


    private void UpdateStore() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String storeID = StoreID.getText().toString().trim();
                String storeName = StoreName.getText().toString().trim();
                String storeCategory = StoreCategory.getText().toString().trim();
                String mallID = MallID.getText().toString().trim();
                String floorSection = FloorSection.getText().toString().trim();
                String contactInformation = ContactInformation.getText().toString().trim();

                if (storeID.isEmpty()) {
                    Toast.makeText(StoresActivity.this, "Please enter Store ID", Toast.LENGTH_SHORT).show();
                }
                 else if(storeName.isEmpty()){
                    Toast.makeText(StoresActivity.this, "Please enter Store Name", Toast.LENGTH_SHORT).show();


                }
                else if(!storeCategory.matches("[0-9]")){
                    Toast.makeText(StoresActivity.this, "Please enter correct Category Id e.g from 1-7", Toast.LENGTH_SHORT).show();

                }
                else if(!mallID.matches("[0-9]{1}")){
                    Toast.makeText(StoresActivity.this, "Please enter correct Mall ID e.g 1", Toast.LENGTH_SHORT).show();

                }



                else {
                    String sql = "UPDATE Store SET StoreName = ?, StoreCategoryID = ?, MallID = ?, FloorSection = ?, ContactInformation = ? WHERE StoreID = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, storeName);
                    preparedStatement.setString(2, storeCategory);
                    preparedStatement.setString(3, mallID);
                    preparedStatement.setString(4, floorSection);
                    preparedStatement.setString(5, contactInformation);
                    preparedStatement.setString(6, storeID);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(StoresActivity.this, "Store Updated", Toast.LENGTH_LONG).show();
                        StoreID.setText("");
                        StoreName.setText("");
                        StoreCategory.setText("");
                        MallID.setText("");
                        FloorSection.setText("");
                        ContactInformation.setText("");
                    } else {
                        Toast.makeText(StoresActivity.this, "Failed to update store", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("Error: ", e.getMessage());
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
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String storeID = StoreID.getText().toString().trim();
                String storeName = StoreName.getText().toString().trim();
                String storeCategory = StoreCategory.getText().toString().trim();
                String mallID = MallID.getText().toString().trim();
                String floorSection = FloorSection.getText().toString().trim();
                String contactInformation = ContactInformation.getText().toString().trim();

                if (storeID.isEmpty() || storeName.isEmpty() || storeCategory.isEmpty() || mallID.isEmpty() || floorSection.isEmpty() || contactInformation.isEmpty()) {
                    Toast.makeText(StoresActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

                else if (!storeCategory.matches("[0-9]")){
                    Toast.makeText(StoresActivity.this, "Enter correct storeCategory e.g 1-7", Toast.LENGTH_SHORT).show();


                }
                else if (!mallID.matches("[0-9]{1}")){
                    Toast.makeText(StoresActivity.this, "Enter correct MallID e.g 1-2", Toast.LENGTH_SHORT).show();


                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(contactInformation).matches()){
                    Toast.makeText(StoresActivity.this, "Enter correct MallID e.g 1-2", Toast.LENGTH_SHORT).show();


                }

                    else {
                    String sql = "INSERT INTO Store (StoreID, StoreName, StoreCategoryID, MallID, FloorSection, ContactInformation) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, storeID);
                    preparedStatement.setString(2, storeName);
                    preparedStatement.setString(3, storeCategory);
                    preparedStatement.setString(4, mallID);
                    preparedStatement.setString(5, floorSection);
                    preparedStatement.setString(6, contactInformation);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(StoresActivity.this, "New Store Added", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful addition
                        StoreID.setText("");
                        StoreName.setText("");
                        StoreCategory.setText("");
                        MallID.setText("");
                        FloorSection.setText("");
                        ContactInformation.setText("");
                    } else {
                        Toast.makeText(StoresActivity.this, "Failed to add store", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }


}