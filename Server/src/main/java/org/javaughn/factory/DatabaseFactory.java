package org.javaughn.factory;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseFactory {
    private static Connection connection = null;

    public static Connection getConnection(){

        if(connection != null) return connection;

        try{
            String url = "jdbc:postgresql://localhost/ChatApp?user=postgres&password=ApOsTrOpHy@1";
            connection = DriverManager.getConnection(url);

            if(connection == null) System.out.println("Error Connecting");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

}
