package com.example.kaymallmanager;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Alaeddin on 5/21/2017.
 */

public class DBconnection {

    String ip,db,uname,pass,port;


    @SuppressLint("NewApi")
    public Connection connectionclasss()
    {

        // Declaring Server ip, username, database name and password
        ip = "10.0.2.2";
        port="1433";
        db = "Kay_Mall";
        uname = "sa";
        pass = "10096";

        // Declaring Server ip, username, database name and password


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ db+";user="+uname+";password="+pass+";";
            connection = DriverManager.getConnection(ConnectionURL);
        }

        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}