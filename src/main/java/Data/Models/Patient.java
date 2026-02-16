package Data.Models;

public class Patient{

    private int ID;
    private final String PESEL;
    private final String name;
    private final String lastName;
    private final String address;
    private final String birthDate;
    private final String phoneNumber;
    private final String email;
    private String preDocumentation;

    public static Patient newPatient = null;

    public Patient(int ID, String PESEL, String name,
                   String lastName, String address,
                   String birthDate, String phoneNumber,
                   String email, String preDocumentation) {
        this(PESEL,name, lastName,address,birthDate,phoneNumber, email);
        this.ID = ID;
        this.preDocumentation = preDocumentation;
    }

    public Patient(String PESEL, String name,
                   String lastName, String address,
                   String birthDate, String phoneNumber,
                   String email) {
        this.PESEL = PESEL;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getID() {
        return ID;
    }

    public String getPESEL() {
        return PESEL;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPreDocumentation() {
        return preDocumentation;
    }

    public void setPreDocumentation(String preDocumentation){
        this.preDocumentation = preDocumentation;
    }

    @Override
    public String toString(){

        return """
                ID: %d
                Imie: %s
                Nazwisko: %s
                Adress: %s
                Data Urodzenia: %s
                PESEL: %s
                Nr Telefonu: %s
                E-mail: %s
                """.formatted(ID, name,lastName,address,birthDate,PESEL,phoneNumber, email);
    }

}
