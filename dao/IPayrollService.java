package dao;

import entity.Payroll;
import java.util.List;

public interface IPayrollService {
    boolean generatePayroll(int employeeId, java.util.Date startDate, java.util.Date endDate);
    Payroll getPayrollById(int payrollId);
    List<Payroll> getPayrollsForEmployee(int employeeId);
    List<Payroll> getPayrollsForPeriod(java.util.Date startDate, java.util.Date endDate);
}
