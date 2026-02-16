package Data.DataBase;

import Data.Models.Patient;
import Data.Models.PatientPreView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientsTable {

    private static Connection conn;

    public static void createPatientsTable() throws SQLException {
        conn = DataBase.get();
        String createQuery = """
                CREATE TABLE IF NOT EXISTS Patients (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      PESEL TEXT UNIQUE NOT NULL,
                      name TEXT NOT NULL,
                      lastName TEXT NOT NULL,
                      address TEXT,
                      birthDate TEXT,
                      phoneNumber TEXT,
                      email TEXT,
                      preDocumentation TEXT
                  );
                
                """;
        Statement statement = conn.createStatement();
        statement.execute(createQuery);
    }

    public static int insertPatient(Patient p) throws SQLException {
        String insertQuery = "INSERT INTO Patients(PESEL, name, lastName, address, birthDate, phoneNumber, email, preDocumentation)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(insertQuery);
        ps.setString( 1, p.getPESEL());
        ps.setString(2, p.getName());
        ps.setString(3, p.getLastName());
        ps.setString(4, p.getAddress());
        ps.setString(5, p.getBirthDate());
        ps.setString(6, p.getPhoneNumber());
        ps.setString(7, p.getEmail());
        ps.setString(8, p.getPreDocumentation());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Nie udało się pobrać ID pacjenta:%s %s %s%n".formatted( p.getPESEL(), p.getName(), p.getLastName()));
    }

    public static void updatePatientDocumentation(Patient p) throws SQLException {
        String updateQuery = """
                 UPDATE Patients
                     SET PESEL = ?, name = ?, lastName = ?, address = ?,
                     phoneNumber = ?, email = ?, birthDate = ?
                 WHERE id = ?""";

        PreparedStatement ps = conn.prepareStatement(updateQuery);

        ps.setString(1, p.getPESEL());
        ps.setString(2, p.getName());
        ps.setString(3, p.getLastName());
        ps.setString(4, p.getAddress());
        ps.setString(5, p.getPhoneNumber());
        ps.setString(6, p.getEmail());
        ps.setString( 7, p.getBirthDate());
        ps.setInt(8, p.getID());

        ps.executeUpdate();
        System.out.printf("Zaaktualizowano Dokumentacje %s, %d%n", p.getPESEL(), p.getID());
    }

    public static List<PatientPreView> loadPatientsPreviews() throws SQLException {
        List<PatientPreView> patientsPreViewList = new ArrayList<>();
        String query = "SELECT id, name, lastName, phoneNumber FROM Patients";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String lastName = rs.getString("lastName");
            String phoneNumber = rs.getString("phoneNumber");
            patientsPreViewList.add(new PatientPreView(id, lastName, name, phoneNumber));
        }
        return patientsPreViewList;
    }

    public static Patient loadPatient(int ID) throws SQLException {
        String loadQuery = "SELECT * FROM Patients WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(loadQuery);
        ps.setInt(1, ID);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new SQLException("Patient not found, id=" + ID);
        }
        return new Patient(
                rs.getInt("ID"),
                rs.getString("PESEL"),
                rs.getString("name"),
                rs.getString("lastName"),
                rs.getString("address"),
                rs.getString("birthDate"),
                rs.getString("phoneNumber"),
                rs.getString("email"),
                rs.getString("preDocumentation"));
    }

    public static void deletePatient(Patient p) throws SQLException {
        VisitTable.deleteVisits(p.getID());
        String deleteQuery = "DELETE FROM Patients WHERE id = ?;";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, p.getID());
            ps.executeUpdate();
        }
    }

    public static String getPreDocumentation(int patientID) throws SQLException {
        String getQuery = "SELECT preDocumentation FROM Patients WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(getQuery);
        ps.setInt(1, patientID);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) throw new SQLException("Patient not found, id=" + patientID);
        return rs.getString("preDocumentation");
    }
}
