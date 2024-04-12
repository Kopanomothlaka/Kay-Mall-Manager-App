package com.example.kaymallmanager;

import androidx.appcompat.app.AppCompatActivity;

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

public class CleanersActivity extends AppCompatActivity {

    Connection con;
    DBconnection dBconnection;
    ResultSet rs;

    TextView cal,cleaners,shift;
    EditText enter,CleanerId,CleanerName,Mall,Email,ShiftSchedule,Area;
    Button SearchBnt,AddBtn,Update,Delete;

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
        Mall=findViewById(R.id.CleanerMallEdittext);
        Email=findViewById(R.id.EmailEdittext);
        ShiftSchedule=findViewById(R.id.ShirtEdittext);
        Area=findViewById(R.id.AreaEdittext);



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
                String CleanerID = CleanerId.getText().toString().trim();

                if (CleanerID.isEmpty()) {
                    Toast.makeText(this, "Please enter a Store ID", Toast.LENGTH_SHORT).show();

                } else {
                    String sqlDelete = "DELETE FROM Cleaners WHERE CleanerID = '" + CleanerID + "'";
                    Statement st = con.createStatement();
                    int rowsAffected = st.executeUpdate(sqlDelete);

                    if (rowsAffected > 0) {
                        // Deletion successful
                        Toast.makeText(this, "Cleaner deleted successfully", Toast.LENGTH_SHORT).show();
                        // Clear the fields after deletion if needed
                        CleanerId.setText("");
                        CleanerName.setText("");
                        Mall.setText("");
                        Email.setText("");
                        ShiftSchedule.setText("");
                        Area.setText("");
                    } else {
                        // Deletion failed, store not found
                        Toast.makeText(this, "Cleaner not found or deletion failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } catch (Exception e) {
            Log.e("Error : ", e.getMessage());

        }



    }

    private void UpdateCleaner() {
        try {
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String cleanerid= CleanerId.getText().toString().trim();
                String cleanername = CleanerName.getText().toString().trim();
                String mall = Mall.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String shift = ShiftSchedule.getText().toString().trim();
                String area = Area.getText().toString().trim();

                if (cleanerid.isEmpty() || mall.isEmpty() || cleanername.isEmpty() || email.isEmpty() || shift.isEmpty()) {
                    Toast.makeText(CleanersActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!cleanerid.matches("[0-9]+")) {
                    Toast.makeText(CleanersActivity.this, "Enter a numeric Cleaner ID", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(CleanersActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    String sql = "UPDATE Cleaners SET CleanerName = ?, MallID = ?, ContactInformation = ?, ShiftSchedule = ?, CleaningAreas = ? WHERE CleanerID = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, cleanername);
                    preparedStatement.setString(2, mall);
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, shift);
                    preparedStatement.setString(5, area);
                    preparedStatement.setString(6, cleanerid);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(CleanersActivity.this, "Cleaner Updated", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful update
                        CleanerId.setText("");
                        CleanerName.setText("");
                        Mall.setText("");
                        Email.setText("");
                        ShiftSchedule.setText("");
                        Area.setText("");
                    } else {
                        Toast.makeText(CleanersActivity.this, "Failed to update cleaner", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
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
                String CleanerID = enter.getText().toString().trim();

                if (CleanerID.isEmpty()) {
                    Toast.makeText(this, "Please enter a Store ID", Toast.LENGTH_SHORT).show();


                } else {
                    String sqlsearch = "SELECT * FROM Cleaners WHERE CleanerID = '" + CleanerID + "'";
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
            dBconnection = new DBconnection();
            con = dBconnection.connectionclasss();
            if (con != null) {
                String cleanerid= CleanerId.getText().toString().trim();
                String cleanername = CleanerName.getText().toString().trim();
                String mall = Mall.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String shift = ShiftSchedule.getText().toString().trim();
                String area = Area.getText().toString().trim();

                if (cleanerid.isEmpty() || mall.isEmpty() || cleanername.isEmpty() || email.isEmpty() || shift.isEmpty()) {
                    Toast.makeText(CleanersActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!cleanerid.matches("[0-9]+")) {
                    Toast.makeText(CleanersActivity.this, "Enter a numeric Security ID", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(CleanersActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    String sql = "INSERT INTO Cleaners (CleanerID, CleanerName, MallID, ContactInformation, ShiftSchedule,CleaningAreas) VALUES (?, ?, ?, ?, ?,?)";
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, cleanerid);
                    preparedStatement.setString(2, cleanername);
                    preparedStatement.setString(3, mall);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, shift);
                    preparedStatement.setString(6, area);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        Toast.makeText(CleanersActivity.this, "New Security Guard Added", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful addition
                        CleanerId.setText("");
                        CleanerName.setText("");
                        Mall.setText("");
                        Email.setText("");
                        ShiftSchedule.setText("");
                        Area.setText("");
                    } else {
                        Toast.makeText(CleanersActivity.this, "Failed to add security guard", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }




    }

}