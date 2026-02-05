package db;

import utils.TestContext;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ScenarioRepository {

    public static void insertScenario(TestContext ctx) {

        String sql = """
            INSERT INTO scenario_execution
            (scenario_id, run_id, feature_name, scenario_name, start_time)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, ctx.getScenarioId());
            ps.setObject(2, RunContext.RUN_ID);
            ps.setString(3, ctx.getFeatureName());
            ps.setString(4, ctx.getScenarioName());
            ps.setLong(5, ctx.getStartTime());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void finishScenario(TestContext ctx, String status) {

        String sql = """
            UPDATE scenario_execution
            SET final_status = ?, end_time = ?
            WHERE scenario_id = ?
        """;

        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, System.currentTimeMillis());
            ps.setObject(3, ctx.getScenarioId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}