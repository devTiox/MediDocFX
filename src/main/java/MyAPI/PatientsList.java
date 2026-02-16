package MyAPI;

import Data.DataBase.PatientsTable;
import Data.Models.Patient;
import Data.Models.PatientPreView;
import javafx.fxml.FXML;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientsList {

    private static final List<PatientPreView> patientsList = new ArrayList<>();

    public static void loadPatientsList() throws SQLException {
        patientsList.clear();
        patientsList.addAll(PatientsTable.loadPatientsPreviews());
        patientsList.sort(PatientPreView.BY_LN_N_ID);
    }

    public static boolean addPatient(PatientPreView newPatient) {
        int index = Collections.binarySearch(patientsList, newPatient, PatientPreView.BY_LN_N_ID);
        if(index < 0 ){
            index = -(index) - 1;
            patientsList.add(index, newPatient);
            return true;
        }else return false;
    }

    public static List<PatientPreView> getPatients() {
        try {
            loadPatientsList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return List.copyOf(patientsList);
    }

    public static void deletePatient(Patient patient){
        PatientPreView preView = new PatientPreView(patient.getID(),
                                                    patient.getLastName(),
                                                    patient.getName(),
                                                    patient.getPhoneNumber());
        patientsList.remove(preView);
    }
}
