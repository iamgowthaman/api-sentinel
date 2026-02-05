package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection get() throws Exception {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/qa",
                "qa_user",
                ""
        );
    }
}
