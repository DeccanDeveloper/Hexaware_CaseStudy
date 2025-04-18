package util;

import dao.*;
import entity.*;

import java.util.List;

public class ReportGenerator {
    private final IEmployeeService employeeService;
    private final IPayrollService payrollService;
    private final ITaxService taxService;
    private final IFinancialRecordService financialRecordService;

    public ReportGenerator(IEmployeeService empSvc, IPayrollService payrollSvc, ITaxService taxSvc, IFinancialRecordService frSvc) {
        this.employeeService = empSvc;
        this.payrollService = payrollSvc;
        this.taxService = taxSvc;
        this.financialRecordService = frSvc;
    }

    public List<Payroll> generatePayrollReport(int employeeId) {
        return payrollService.getPayrollsForEmployee(employeeId);
    }

    public List<Tax> generateTaxReport(int employeeId) {
        return taxService.getTaxesForEmployee(employeeId);
    }

    public List<FinancialRecord> generateFinancialReport(int employeeId) {
        return financialRecordService.getFinancialRecordsForEmployee(employeeId);
    }
}
