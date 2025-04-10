package test;

import dao.EmployeeService;
import dao.PayrollService;
import entity.Employee;
import entity.Payroll;

import java.util.Date;
import java.util.List;

public class PayXpertTest {
    public static void main(String[] args) {
        EmployeeService empService = new EmployeeService();
        PayrollService payrollService = new PayrollService();

        // Test 1: Add and Fetch Employee
        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("test@example.com");
        emp.setDateOfBirth(new Date());
        emp.setGender("Other");
        emp.setPhoneNumber("0000000000");
        emp.setAddress("India");
        emp.setPosition("Engineer");
        emp.setJoiningDate(new Date());
        emp.setTerminationDate(null);
        empService.addEmployee(emp);

        List<Employee> list = empService.getAllEmployees();
        System.out.println("Total Employees: " + list.size());

        // Test 2: Generate Payroll
        boolean payrollGenerated = payrollService.generatePayroll(list.get(0).getEmployeeID(), new Date(), new Date());
        System.out.println("Payroll generated: " + payrollGenerated);
    }
}