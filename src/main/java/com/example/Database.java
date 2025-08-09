package com.example;

import java.sql.*;

public class Database {
    public Connection datalink;

    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/chess";
        String user = "root";
        String password = "Heng012631355";
        try {
            datalink = DriverManager.getConnection(url, user, password);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return datalink;
    }

}
