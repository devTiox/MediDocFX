package Data.Models;

public record Visit(int id,String date, String treatment, String notes, int patientID){

    @Override
    public String toString(){
        return String.format("%s%n" +
                "--------------------%n" +
                "%s%n" +
                "--------------------%n" +
                "%s%n" +
                "--------------------%n", date, treatment, notes);
    }
}
