package MyAPI;

import Data.Models.PatientPreView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientsList {

    private static List<PatientPreView> patientsList;

    public static void loadPatientsList(){
        patientsList = new ArrayList<>();

        patientsList.addAll(PatientService.loadPatientsPreviews());
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
        loadPatientsList();
        return List.copyOf(patientsList);
    }
    public static void deletePatient(PatientPreView patient){
        patientsList.remove(patient);
    }

}
