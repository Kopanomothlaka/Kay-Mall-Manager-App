package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

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

public class SecurityActivity extends AppCompatActivity {

    Connection con;
    DBconnection dBconnection;
    ResultSet rs;

    private ListView storeListView;
    private ArrayAdapter<String> storeAdapter;
    TextView display,Dname,Dshift;
    EditText SecurityId,MallID,Name,Email,Shift ,searchEdit;
    Button search ,add,update,delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        display=findViewById(R.id.displaySecurityNo);

        SecurityId=findViewById(R.id.SecID);
        MallID=findViewById(R.id.MallIdEdittext);
        Name=findViewById(R.id.SecNameEdittext);
        Email=findViewById(R.id.EmailEdittext);
        Shift=findViewById(R.id.ShirtEdittext);
        Dname=findViewById(R.id.securityName);
        Dshift=findViewById(R.id.shiftId);

        search=findViewById(R.id.searchSecBtn);
        searchEdit=findViewById(R.id.SearchSecid);
        add=findViewById(R.id.AddSec);
        update=findViewById(R.id.UpdateSec);
        delete=findViewById(R.id.DeleteSec);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchMethod();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSecurity();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSecurity();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSecurity();
            }
        });

        storeListView = findViewById(R.id.storeListView);
        storeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        storeListView.setAdapter(storeAdapter);









        calculateSecurity();
        DisplaySecurity();
        Graph();


        
    }

    private void DisplaySecurity() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT * FROM Security";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder shiftBuilder = new StringBuilder();


                while (rs.next()){
                    String storeName = rs.getString("SecurityPersonnelName");
                    String shiftType=rs.getString("ShiftSchedule");

                    storeAdapter.add(storeName);
                    storeAdapter.add(shiftType);


                    System.out.println(storeName);
                    System.out.println(shiftType);


                    stringBuilder.append(storeName).append("\n");
                    shiftBuilder.append(shiftType).append("\n");

                }

                Dname.setText(stringBuilder.toString());
                Dshift.setText(shiftBuilder.toString());

                rs.close(); // Close ResultSet
                st.close(); // Close statement
                con.close(); // Close connection
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(SecurityActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }



    private void Graph() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sql = "SELECT * FROM Security";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                ArrayList<PieEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();

                while (rs.next()) {
                    int storeID = rs.getInt("SecurityID"); // Replace "StoreID" with the actual column name from the database
                    String storeName = rs.getString("SecurityPersonnelName"); // Replace "StoreName" with the actual column name from the database
                    String floorSection = rs.getString("ShiftSchedule"); // Replace "FloorSection" with the actual column name from the database

                    entries.add(new PieEntry(storeID, storeName + " - " + floorSection));
                    labels.add(String.valueOf(storeID));
                }
                rs.close();
                st.close();

                PieChart pieChart = findViewById(R.id.lineChart);

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
            Toast.makeText(SecurityActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void DeleteSecurity() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String securityID = SecurityId.getText().toString().trim();

                if (securityID.isEmpty()) {
                    Toast.makeText(SecurityActivity.this, "Please enter Security ID to delete", Toast.LENGTH_SHORT).show();
                } else if (!securityID.matches("[0-9]+")) {
                    Toast.makeText(SecurityActivity.this, "Enter a numeric Security ID", Toast.LENGTH_SHORT).show();
                } else {
                    String sql = "DELETE FROM Security WHERE SecurityID = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, securityID);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(SecurityActivity.this, "Security Guard Deleted", Toast.LENGTH_LONG).show();
                        // Clear EditText field after successful deletion
                        SecurityId.setText("");
                    } else {
                        Toast.makeText(SecurityActivity.this, "No security guard found with that ID", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }



    }

    private void UpdateSecurity() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String securityID = SecurityId.getText().toString().trim();
                String mallID = MallID.getText().toString().trim();
                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String shift = Shift.getText().toString().trim();

                if (securityID.isEmpty() || mallID.isEmpty() || name.isEmpty() || email.isEmpty() || shift.isEmpty()) {
                    Toast.makeText(SecurityActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!securityID.matches("[0-9]+")) {
                    Toast.makeText(SecurityActivity.this, "Enter a numeric Security ID", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SecurityActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    String sql = "UPDATE Security SET MallID = ?, SecurityPersonnelName = ?, ContactInformation = ?, ShiftSchedule = ? WHERE SecurityID = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, mallID);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, shift);
                    preparedStatement.setString(5, securityID);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(SecurityActivity.this, "Security Guard Updated", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful update
                        SecurityId.setText("");
                        MallID.setText("");
                        Name.setText("");
                        Email.setText("");
                        Shift.setText("");
                    } else {
                        Toast.makeText(SecurityActivity.this, "Failed to update security guard", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }








    }

    private void AddSecurity() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String Security = SecurityId.getText().toString().trim();
                String mallid = MallID.getText().toString().trim();
                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String shift = Shift.getText().toString().trim();

                if (Security.isEmpty() || mallid.isEmpty() || name.isEmpty() || email.isEmpty() || shift.isEmpty()) {
                    Toast.makeText(SecurityActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!Security.matches("[0-9]+")) {
                    Toast.makeText(SecurityActivity.this, "Enter a numeric Security ID", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SecurityActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    String sql = "INSERT INTO Security (SecurityID, MallID, SecurityPersonnelName, ContactInformation, ShiftSchedule) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, Security);
                    preparedStatement.setString(2, mallid);
                    preparedStatement.setString(3, name);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, shift);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(SecurityActivity.this, "New Security Guard Added", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful addition
                        SecurityId.setText("");
                        MallID.setText("");
                        Name.setText("");
                        Email.setText("");
                        Shift.setText("");
                    } else {
                        Toast.makeText(SecurityActivity.this, "Failed to add security guard", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }




    }

    private void searchMethod() {

        try {


            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String SecurityID = searchEdit.getText().toString().trim();

                if (SecurityID.isEmpty()) {
                    Toast.makeText(this, "Please enter a Store ID", Toast.LENGTH_SHORT).show();


                } else {
                    String sqlsearch = "SELECT * FROM Security WHERE SecurityID = '" + SecurityID + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlsearch);

                    if (rs.next()) {
                        // Store found, update UI with store information
                        SecurityId.setText(rs.getString("SecurityID"));
                        MallID.setText(rs.getString("MallID"));
                        Name.setText(rs.getString("SecurityPersonnelName"));
                        Email.setText(rs.getString("ContactInformation"));
                        Shift.setText(rs.getString("ShiftSchedule"));

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

    private void calculateSecurity() {

        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String sqldisplay = "SELECT COUNT(*) AS ParkingID FROM Parking";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqldisplay);
                if (rs.next()) {
                    int ParkingID = rs.getInt("ParkingID");
                    display.setText(" " + ParkingID);
                }
                rs.close(); // Close ResultSet
                st.close(); // Close statement
                con.close(); // Close connection
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(SecurityActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }


}