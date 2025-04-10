package dao;

import entity.FinancialRecord;
import java.util.Date;
import java.util.List;

public interface IFinancialRecordService {
    boolean addFinancialRecord(int employeeId, String description, double amount, String recordType);
    FinancialRecord getFinancialRecordById(int recordId);
    List<FinancialRecord> getFinancialRecordsForEmployee(int employeeId);
    List<FinancialRecord> getFinancialRecordsForDate(Date recordDate);
}
