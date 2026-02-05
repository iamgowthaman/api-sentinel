package com.sentinel;

import java.sql.Connection;
import java.sql.DriverManager;

public class App {

    public static void main(String[] args) {

        System.out.println("Testing DB connection...");

        try {
            Connection con = get();
            System.out.println("Connected successfully!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection get() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/qa",
                    "qa_user",
                    ""   // no password (local)
            );
        } catch (Exception e) {
            throw new RuntimeException("DB connection failed", e);
        }
    }
}