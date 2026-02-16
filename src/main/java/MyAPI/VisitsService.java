package MyAPI;

import Data.DataBase.VisitTable;
import Data.Models.Visit;
import Data.Models.VisitView;

import java.sql.SQLException;
import java.util.List;

public class VisitsService {

    public static int insertNewVisit(Visit newVisit) {
        try {
            return VisitTable.insertNewVisit(newVisit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<VisitView> getPatientsVisits(int id) {
        try {
            return VisitTable.getPatientsVisits(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Visit getVisit(int visitID){
        try {
            return VisitTable.getVisit(visitID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateVisit(Visit visit){
        try {
            VisitTable.updateVisit(visit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletePatient(int visitID) {
        try {
            VisitTable.deleteVisit(visitID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
