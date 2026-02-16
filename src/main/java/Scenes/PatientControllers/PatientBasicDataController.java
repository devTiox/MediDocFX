package Scenes.PatientControllers;

import Data.Models.Patient;
import MyAPI.PatientService;
import Scenes.EntryMode;
import Scenes.OnShowAware;
import Scenes.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


@SuppressWarnings("SpellCheckingInspection")
public class PatientBasicDataController implements OnShowAware {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField birthDayField;
    @FXML private TextField birthMonthField;
    @FXML private TextField birthYearField;
    @FXML private TextField peselField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;


    private EntryMode mode;
    private int patientID;

    @Override
    public void onShow(EntryMode mode, Integer patientID) {
        this.mode = mode;

        if (mode == EntryMode.NEW) {
            clearFields();
        } else if (mode == EntryMode.EDIT) {
            if (patientID == null) {
                throw new IllegalArgumentException("EDIT wymaga patientID");
            }
            this.patientID = patientID;
            fillFields(loadPatient(patientID));
        } else {
            fillFields(Patient.newPatient);
        }
    }

    @FXML
    private void acceptBasicData() {
        if(!validateAll()) return;
        Patient patient = getPatient();

        if (mode == EntryMode.NEW || mode == EntryMode.RE_EDIT) {
            Patient.newPatient = patient;
            SceneManager.switchTo("CHECK_BOXES", EntryMode.NEW, null);
        } else {
            if(!PatientService.updatePatient(patient)) {
                return;
            }
            SceneManager.openPatientView(patientID);
        }
    }

    private Patient getPatient() {
        String birthDay = birthDayField.getText();
        if(birthDay.length()==1) birthDay = "0"+birthDay;
        String birthDate = birthDay +
                "/" + birthMonthField.getText() +
                "/" + birthYearField.getText();
        return new Patient(
                patientID,
                peselField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                addressField.getText(),
                birthDate,
                phoneField.getText(),
                emailField.getText(),
                null
        );
    }

    private Patient loadPatient(int patientID) {
        if (!PatientService.loadPatient(patientID)) {
            throw new RuntimeException("Nie można załadować pacjenta");
        }
        return PatientService.getPatient();
    }

    private void fillFields(Patient patient) {
        String[] birthDate = patient.getBirthDate().split("/");
        peselField.setText(patient.getPESEL());
        firstNameField.setText(patient.getName());
        lastNameField.setText(patient.getLastName());
        addressField.setText(patient.getAddress());
        birthDayField.setText(birthDate[0]);
        birthMonthField.setText(birthDate[1]);
        birthYearField.setText(birthDate[2]);
        phoneField.setText(patient.getPhoneNumber());
        emailField.setText(patient.getEmail());
    }

    private void clearFields() {
        peselField.clear();
        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        birthDayField.clear();
        birthMonthField.clear();
        birthYearField.clear();
        phoneField.clear();
        emailField.clear();
    }

    @FXML
    public void goBack(){
        if(mode == EntryMode.NEW)
            SceneManager.switchTo("MENU", null, null);
        else if(mode == EntryMode.EDIT)
            SceneManager.switchTo("PATIENT_VIEW", null, patientID);
    }

// ----------------------CHECKING_FIELDS---------------------------------
    private static final String ERROR_STYLE = "-fx-border-color: red; -fx-border-width: 2;";
    private static final String OK_STYLE = "";

    private void mark(TextField f, boolean ok) {
        f.setStyle(ok ? OK_STYLE : ERROR_STYLE);
    }

    private boolean isDigits(TextField f, int len) {
        String s = f.getText().trim();
        return s.matches("\\d{" + len + "}");
    }

    private boolean isInRangeInt(TextField f, int min, int max) {
        String s = f.getText().trim();
        if (!s.matches("\\d+")) return false;
        int v = Integer.parseInt(s);
        return v >= min && v <= max;
    }

    private boolean isEmail(TextField f) {
        String s = f.getText().trim();
        return s.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    /** Zwraca true jeśli wszystko OK i ustawia style na polach */
    private boolean validateAll() {
        boolean ok = true;

        // wymagane
        boolean firstOk = !firstNameField.getText().isBlank();
        mark(firstNameField, firstOk); ok &= firstOk;

        boolean lastOk = !lastNameField.getText().isBlank();
        mark(lastNameField, lastOk); ok &= lastOk;

        boolean addrOk = !addressField.getText().isBlank(); // jeśli adres ma być opcjonalny -> daj true
        mark(addressField, addrOk); ok &= addrOk;

        // PESEL: 11 cyfr
        boolean peselOk = isDigits(peselField, 11);
        mark(peselField, peselOk); ok &= peselOk;

        // Telefon: 9 cyfr (jak chcesz dopuścić +48, spacje itd. powiem jak)
        boolean phoneOk = isDigits(phoneField, 9);
        mark(phoneField, phoneOk); ok &= phoneOk;

        boolean emailOk = isEmail(emailField);
        mark(emailField, emailOk); ok &= emailOk;

        // Data urodzenia z 3 pól (dd / mm / yyyy)
        boolean dayOk = isInRangeInt(birthDayField, 1, 31);
        mark(birthDayField, dayOk); ok &= dayOk;

        boolean monthOk = isInRangeInt(birthMonthField, 1, 12);
        mark(birthMonthField, monthOk); ok &= monthOk;

        boolean yearOk = isInRangeInt(birthYearField, 1900, 2100);
        mark(birthYearField, yearOk); ok &= yearOk;


        // (opcjonalnie) dokładniejsza walidacja: czy dzień pasuje do miesiąca (np. 31/02 odpada)
        if (dayOk && monthOk && yearOk) {
            int d = Integer.parseInt(birthDayField.getText().trim());
            int m = Integer.parseInt(birthMonthField.getText().trim());
            int y = Integer.parseInt(birthYearField.getText().trim());

            boolean realDate = isRealDate(d, m, y);
            // jeśli data nieprawidłowa, zaznacz wszystkie 3 na czerwono
            if (!realDate) {
                mark(birthDayField, false);
                mark(birthMonthField, false);
                mark(birthYearField, false);
                ok = false;
            }
        }

        return ok;
    }

    private boolean isRealDate(int d, int m, int y) {
        // prosta walidacja dni w miesiącu + luty i przestępne
        int[] days = {31, (isLeap(y) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (m < 1 || m > 12) return false;
        return d >= 1 && d <= days[m - 1];
    }

    private boolean isLeap(int y) {
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
    }

}
