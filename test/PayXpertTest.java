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

        Employee emp = new Employee();
        emp.setFirstName("Manovishaal");
        emp.setLastName("D");
        emp.setEmail("manovishaal@gmail.com");
        emp.setDateOfBirth(new Date());
        emp.setGender("male");
        emp.setPhoneNumber("9080327359");
        emp.setAddress("India");
        emp.setPosition("Engineer");
        emp.setJoiningDate(new Date());
        emp.setTerminationDate(null);
        empService.addEmployee(emp);

        List<Employee> list = empService.getAllEmployees();
        System.out.println("Total Employees: " + list.size());

        boolean payrollGenerated = payrollService.generatePayroll(list.get(0).getEmployeeID(), new Date(), new Date());
        System.out.println("Payroll generated: " + payrollGenerated);
    }
}