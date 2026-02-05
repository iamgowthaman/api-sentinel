package excel;
import db.DBConnection;
import db.RunContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;
public class ExcelReportGenerator {
    public static void generate() {

        try (Workbook wb = new XSSFWorkbook();
             Connection con = DBConnection.get()) {

            Map<String, List<UUID>> featureScenarioMap = new LinkedHashMap<>();

            // INDEX DATA
            String indexSql = """
                SELECT feature_name, scenario_id, final_status
                FROM scenario_execution
                WHERE run_id = ?
            """;

            try (PreparedStatement ps = con.prepareStatement(indexSql)) {
                ps.setObject(1, RunContext.RUN_ID);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    featureScenarioMap
                            .computeIfAbsent(rs.getString("feature_name"), k -> new ArrayList<>())
                            .add((UUID) rs.getObject("scenario_id"));
                }
            }

            // INDEX SHEET
            Sheet index = wb.createSheet("Index");
            int ir = 0;
            Row h = index.createRow(ir++);
            h.createCell(0).setCellValue("Feature");
            h.createCell(1).setCellValue("Total");
            h.createCell(2).setCellValue("Pass");
            h.createCell(3).setCellValue("Fail");

            for (String feature : featureScenarioMap.keySet()) {

                int pass = 0, fail = 0;

                String cntSql = """
                    SELECT final_status FROM scenario_execution
                    WHERE feature_name = ? AND run_id = ?
                """;

                try (PreparedStatement ps = con.prepareStatement(cntSql)) {
                    ps.setString(1, feature);
                    ps.setObject(2, RunContext.RUN_ID);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        if ("PASS".equals(rs.getString(1))) pass++;
                        else fail++;
                    }
                }

                Row r = index.createRow(ir++);
                r.createCell(0).setCellValue(feature);
                r.createCell(1).setCellValue(pass + fail);
                r.createCell(2).setCellValue(pass);
                r.createCell(3).setCellValue(fail);

                generateFeatureSheet(wb, con, feature);
            }

            try (FileOutputStream fos =
                         new FileOutputStream("target/Test-Execution-Report.xlsx")) {
                wb.write(fos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateFeatureSheet(
            Workbook wb, Connection con, String feature) throws Exception {

        Sheet sheet = wb.createSheet(feature);
        int row = 0;

        String scenarioSql = """
            SELECT scenario_id, scenario_name, final_status
            FROM scenario_execution
            WHERE feature_name = ? AND run_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(scenarioSql)) {
            ps.setString(1, feature);
            ps.setObject(2, RunContext.RUN_ID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID scenarioId = (UUID) rs.getObject(1);

                Row sr = sheet.createRow(row++);
                sr.createCell(0).setCellValue(
                        "Scenario: " + rs.getString(2)
                                + " | STATUS: " + rs.getString(3)
                );

                Row hr = sheet.createRow(row++);
                hr.createCell(1).setCellValue("API");
                hr.createCell(2).setCellValue("Method");
                hr.createCell(3).setCellValue("Endpoint");
                hr.createCell(4).setCellValue("Status");
                hr.createCell(5).setCellValue("Duration(ms)");

                String apiSql = """
                    SELECT api_name, method, endpoint, status, duration_ms
                    FROM api_execution
                    WHERE scenario_id = ?
                """;

                try (PreparedStatement aps = con.prepareStatement(apiSql)) {
                    aps.setObject(1, scenarioId);
                    ResultSet ars = aps.executeQuery();

                    while (ars.next()) {
                        Row ar = sheet.createRow(row++);
                        ar.createCell(1).setCellValue(ars.getString(1));
                        ar.createCell(2).setCellValue(ars.getString(2));
                        ar.createCell(3).setCellValue(ars.getString(3));
                        ar.createCell(4).setCellValue(ars.getString(4));
                        ar.createCell(5).setCellValue(ars.getLong(5));
                    }
                }
                row++;
            }
        }
    }
}
