package main;

import dao.*;
import entity.*;
import java.util.*;

public class MainModule {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        IEmployeeService empService = new EmployeeService();
        IPayrollService payrollService = new PayrollService();

        while (true) {
            System.out.println("\n==== PayXpert Payroll Management ====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Generate Payroll");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("First Name: ");
                    String fname = sc.nextLine();
                    System.out.print("Last Name: ");
                    String lname = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.println("Select Gender:");
                    System.out.println("1. Male");
                    System.out.println("2. Female");
                    System.out.println("3. Other");
                    System.out.print("Enter choice (1-3): ");
                    int genderChoice = sc.nextInt();
                    sc.nextLine(); // consume newline
                    String gender = switch (genderChoice) {
                        case 1 -> "Male";
                        case 2 -> "Female";
                        default -> "Other";
                    };

                    Employee emp = new Employee();
                    emp.setFirstName(fname);
                    emp.setLastName(lname);
                    emp.setEmail(email);
                    emp.setDateOfBirth(new Date());
                    emp.setJoiningDate(new Date());
                    emp.setGender(gender);
                    emp.setPhoneNumber("1234567890");
                    emp.setAddress("N/A");
                    emp.setPosition("Intern");
                    emp.setTerminationDate(null);

                    boolean added = empService.addEmployee(emp);
                    System.out.println(added ? "Employee added." : "Failed to add.");
                }
                case 2 -> {
                    List<Employee> all = empService.getAllEmployees();
                    for (Employee e : all) {
                        System.out.println(e.getEmployeeID() + " - " + e.getFirstName() + " " + e.getLastName());
                    }
                }
                case 3 -> {
                    System.out.print("Enter Employee ID: ");
                    int id = sc.nextInt();
                    boolean success = payrollService.generatePayroll(id, new Date(), new Date());
                    System.out.println(success ? "Payroll generated." : "Failed to generate payroll.");
                }
                case 4 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
