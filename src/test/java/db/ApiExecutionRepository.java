package db;

import utils.TestContext;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ApiExecutionRepository {

    public static void saveApi(
            TestContext ctx,
            String apiName,
            String method,
            String endpoint,
            String status,
            long duration) {

        String sql = """
            INSERT INTO api_execution
            (scenario_id, api_name, method, endpoint, status, duration_ms)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, ctx.getScenarioId());
            ps.setString(2, apiName);
            ps.setString(3, method);
            ps.setString(4, endpoint);
            ps.setString(5, status);
            ps.setLong(6, duration);

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}