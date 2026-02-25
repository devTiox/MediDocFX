package MyAPI;

import Data.DataBase.PatientsTable;
import Data.Models.Patient;
import Data.Models.PatientPreView;

import java.sql.SQLException;
import java.util.Collection;

public class PatientService {

    private static Patient patient;

    public static boolean loadPatient(int id) throws RuntimeException {
        try {
            patient = PatientsTable.loadPatient(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static int addPatient(Patient newPatient){
        try{
            return PatientsTable.insertPatient(newPatient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updatePatient(Patient patient){
        try{
            PatientsTable.updatePatientDocumentation(patient);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deletePatient(Patient patient){
        try {
            PatientsTable.deletePatient(patient);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Patient getPatient() {
        return patient;
    }

    public static String getPreDocumentation(int patientID) {
        try {
            return PatientsTable.getPreDocumentation(patientID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<PatientPreView> loadPatientsPreviews() {
        try {
            return PatientsTable.loadPatientsPreviews();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
