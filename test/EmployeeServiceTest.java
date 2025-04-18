package test;

import dao.EmployeeService;
import entity.Employee;
import exception.EmployeeNotFoundException;
import org.junit.jupiter.api.*;
import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceTest {

    static EmployeeService empService;
    static int testEmployeeId;

    @BeforeAll
    public static void setup() {
        empService = new EmployeeService();
    }

    @Test
    @Order(1)
    public void testAddEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setEmail("john.doe@example.com");
        emp.setGender("Male");
        emp.setPhoneNumber("9876543210");
        emp.setAddress("Chennai");
        emp.setPosition("Developer");
        emp.setDateOfBirth(new Date(90, 0, 1)); // Jan 1, 1990
        emp.setJoiningDate(new Date());
        emp.setTerminationDate(null);

        boolean added = empService.addEmployee(emp);
        Assertions.assertTrue(added);
        testEmployeeId = emp.getEmployeeID();
    }

    @Test
    @Order(2)
    public void testGetEmployeeById() {
        Employee emp = empService.getEmployeeById(testEmployeeId);
        Assertions.assertNotNull(emp);
        Assertions.assertEquals("John", emp.getFirstName());
    }

    @Test
    @Order(3)
    public void testUpdateEmployee() {
        Employee emp = empService.getEmployeeById(testEmployeeId);
        emp.setPosition("Senior Developer");
        boolean updated = empService.updateEmployee(emp);
        Assertions.assertTrue(updated);

        Employee updatedEmp = empService.getEmployeeById(testEmployeeId);
        Assertions.assertEquals("Senior Developer", updatedEmp.getPosition());
    }

    @Test
    @Order(4)
    public void testGetAllEmployees() {
        List<Employee> list = empService.getAllEmployees();
        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    @Order(5)
    public void testRemoveEmployee() {
        boolean removed = empService.removeEmployee(testEmployeeId);
        Assertions.assertTrue(removed);
    }

    @Test
    @Order(6)
    public void testGetNonExistentEmployeeThrowsException() {
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            empService.getEmployeeById(999999); // random non-existing ID
        });
    }
}
