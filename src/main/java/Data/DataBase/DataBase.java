package Data.DataBase;

import java.sql.*;

public class DataBase {
    private static Connection conn = null;

    public static Connection get() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:sqlite:Pacjenci.db");

            Statement st = conn.createStatement();
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }


    public static void setUp() throws SQLException {
        PatientsTable.createPatientsTable();
        VisitTable.createVisitsTable();
    }
}

