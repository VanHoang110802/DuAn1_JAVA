package com.example.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static final String URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=NhaHang;" +
            "encrypt=true;" +
            "trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Khong tim thay driver JDBC: " + e.getMessage());
            throw new RuntimeException("JDBC Driver not found: " + e.getMessage(), e);
        } catch (SQLException e) {
            System.err.println("Loi ket noi DB: " + e.getMessage());
            throw new RuntimeException("DB connection error: " + e.getMessage(), e);
        }
        if (conn == null) {
            throw new RuntimeException("Unable to obtain DB connection. Check DB server and connection settings.");
        }
        return conn;
    }
}
