package interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import data.FinancialManager;
import data.FinancialManagerDbHelper;
import entities.Account;

public interface DatabaseHelperFunctions{

    void writeToDatabase(FinancialManagerDbHelper dbHelper);
    void updateFromDatabase(FinancialManagerDbHelper dbHelper);
    void readFromDatabase(FinancialManagerDbHelper dbHelper, int rowId);
}