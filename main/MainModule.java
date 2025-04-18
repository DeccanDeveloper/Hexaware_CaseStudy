package main;

import dao.*;
import entity.*;
import exception.EmployeeNotFoundException;
import java.util.*;
import java.util.regex.*;

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
            System.out.println("4. Calculate Gross Salary");
            System.out.println("5. Calculate Net Salary");
            System.out.println("6. Calculate Tax for High-Income Employee");
            System.out.println("7. Process Payroll for All Employees");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            String input = sc.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("First Name: ");
                    String fname = sc.nextLine();
                    System.out.print("Last Name: ");
                    String lname = sc.nextLine();

                    String email;
                    while (true) {
                        System.out.print("Email: ");
                        email = sc.nextLine();
                        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(email);
                        if (matcher.matches()) break;
                        else System.out.println("Invalid email format. Try again.");
                    }

                    String phone;
                    while (true) {
                        System.out.print("Phone Number: ");
                        phone = sc.nextLine();
                        String phoneRegex = "^[6-9]\\d{9}$";
                        Pattern phonePattern = Pattern.compile(phoneRegex);
                        Matcher phoneMatcher = phonePattern.matcher(phone);
                        if (phoneMatcher.matches()) break;
                        else System.out.println("Invalid phone number. It should be 10 digits and start with 6-9.");
                    }

                    System.out.println("Select Gender:");
                    System.out.println("1. Male");
                    System.out.println("2. Female");
                    System.out.println("3. Other");
                    System.out.print("Enter choice (1-3): ");
                    int genderChoice;
                    try {
                        genderChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for gender. Defaulting to 'Other'.");
                        genderChoice = 3;
                    }

                    String gender = switch (genderChoice) {
                        case 1 -> "Male";
                        case 2 -> "Female";
                        default -> "Other";
                    };

                    System.out.print("Address: ");
                    String address = sc.nextLine();

                    System.out.print("Position: ");
                    String position = sc.nextLine();

                    System.out.print("Joining Date (yyyy-mm-dd): ");
                    String joinStr = sc.nextLine();
                    Date joinDate;
                    try {
                        joinDate = java.sql.Date.valueOf(joinStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid date format. Using today's date.");
                        joinDate = new Date();
                    }

                    Employee emp = new Employee();
                    emp.setFirstName(fname);
                    emp.setLastName(lname);
                    emp.setEmail(email);
                    emp.setPhoneNumber(phone);
                    emp.setGender(gender);
                    emp.setAddress(address);
                    emp.setPosition(position);
                    emp.setJoiningDate(joinDate);
                    emp.setDateOfBirth(new Date());
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
                    try {
                        System.out.print("Enter Employee ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        empService.getEmployeeById(id);
                        boolean success = payrollService.generatePayroll(id, new Date(), new Date());
                        System.out.println(success ? "Payroll generated." : "Failed to generate payroll.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> {
                    try {
                        System.out.print("Enter Employee ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        empService.getEmployeeById(id);
                        double gross = ((PayrollService) payrollService).calculateGrossSalary(id);
                        System.out.println("Gross Salary for Employee " + id + ": ₹" + gross);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> {
                    try {
                        System.out.print("Enter Employee ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        empService.getEmployeeById(id);
                        double net = ((PayrollService) payrollService).calculateNetSalary(id);
                        System.out.println("Net Salary for Employee " + id + ": ₹" + net);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 6 -> {
                    try {
                        ITaxService taxService = new TaxService();
                        System.out.print("Enter Employee ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        empService.getEmployeeById(id);
                        System.out.print("Enter Tax Year: ");
                        int year = Integer.parseInt(sc.nextLine());
                        boolean success = taxService.calculateTax(id, year);
                        System.out.println(success ? "Tax calculated and stored." : "Tax calculation failed.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter valid numbers.");
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 7 -> {
                    List<Employee> employees = empService.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees to process.");
                        break;
                    }
                    int successCount = 0;
                    for (Employee e : employees) {
                        boolean success = payrollService.generatePayroll(e.getEmployeeID(), new Date(), new Date());
                        System.out.println("Payroll for Employee ID " + e.getEmployeeID() + ": " + (success ? "Success" : "Failed"));
                        if (success) successCount++;
                    }
                    System.out.println("Payroll processed for " + successCount + " out of " + employees.size() + " employees.");
                }
                case 8 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
