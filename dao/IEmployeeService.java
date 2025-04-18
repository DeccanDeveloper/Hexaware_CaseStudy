package dao;

import entity.Employee;
import java.util.List;

public interface IEmployeeService {
    Employee getEmployeeById(int employeeId);
    List<Employee> getAllEmployees();
    boolean addEmployee(Employee employeeData);
    boolean updateEmployee(Employee employeeData);
    boolean removeEmployee(int employeeId);
}
