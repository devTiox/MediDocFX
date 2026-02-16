package Data.DataBase;

import Data.Models.Visit;
import Data.Models.VisitView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitTable{

    private static Connection conn;


    public static void createVisitsTable() throws SQLException {
        conn = DataBase.get();
        String createQuery = """
                CREATE TABLE IF NOT EXISTS Visits (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER OT NULL,
                    date TEXT NOT NULL,
                    treatment TEXT,
                    notes TEXT,
                    FOREIGN KEY (patient_id)
                        REFERENCES Patients(id)
                        ON DELETE CASCADE
                );
                """;
        Statement statement = conn.createStatement();
        statement.execute(createQuery);
    }

    public static int insertNewVisit(Visit v) throws SQLException{
        String insertQuery = "INSERT INTO Visits(patient_id, date, treatment, notes)\n" +
                "VALUES(?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(insertQuery);
        ps.setInt(1, v.patientID());
        ps.setString(2, String.valueOf(v.date()));
        ps.setString(3, v.treatment());
        ps.setString(4, v.notes());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public static List<VisitView> getPatientsVisits(int patientID) throws SQLException{
        List<VisitView> visits = new ArrayList<>();
        String getQuery = """
                SELECT * FROM Visits
                WHERE patient_id = ?
                ORDER BY date DESC
                """;
        PreparedStatement ps = conn.prepareStatement(getQuery);
        ps.setInt(1, patientID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String date = rs.getString("date");
            int id = rs.getInt("id");
            visits.add(new VisitView(date, id));
        }
        return visits;
    }

    public static Visit getVisit(int visitID) throws SQLException {
        String getQuery = """
                SELECT * FROM Visits
                WHERE id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(getQuery);
        ps.setInt(1, visitID);
        ResultSet rs = ps.executeQuery();
        int patientID = rs.getInt("patient_id");
        String date = rs.getString("date");
        String treatment = rs.getString("treatment");
        String notes = rs.getString("notes");
        return new Visit(visitID, date, treatment, notes, patientID);
    }

    public static void updateVisit(Visit visit) throws SQLException {
        String updateQuery = """
                UPDATE Visits
                    SET treatment = ?, notes = ?
                WHERE id = ?""";
        PreparedStatement ps = conn.prepareStatement(updateQuery);
        ps.setString(1, visit.treatment());
        ps.setString(2, "Edytowano:" + visit.date()+ "\n"
                                            + visit.notes());
        ps.setInt(3, visit.id());
        ps.executeUpdate();

    }

    public static void deleteVisits(int patientID) throws SQLException {
        String deleteQuery = "DELETE FROM Visits WHERE patient_id = ?;";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, patientID);
            ps.executeUpdate();
        }
    }

    public static void deleteVisit(int visitID) throws SQLException{
        String deleteQuery = "DELETE FROM Visits WHERE id = ?;";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, visitID);
            ps.executeUpdate();
        }
    }
}
