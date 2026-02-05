package db;

import utils.TestContext;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestResultRepository {
    public static void save(TestContext context, String status) {

        String sql = """
            INSERT INTO scenario_execution
            (run_id, feature_name, scenario_name, status, duration_ms, api_name)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, RunContext.RUN_ID);
            ps.setString(2, context.getFeatureName());
            ps.setString(3, context.getScenarioName());
            ps.setString(4, status);
            ps.setLong(5, context.getEndTime() - context.getStartTime());
            ps.setString(6, context.getApiName());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
