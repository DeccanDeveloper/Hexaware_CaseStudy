package dao;

import entity.Employee;
import exception.EmployeeNotFoundException;
import util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeService implements IEmployeeService {
    private Connection conn = DBConnUtil.getConnection("db.properties");

    @Override
    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO Employee (FirstName, LastName, DateOfBirth, Gender, Email, PhoneNumber, Address, Position, JoiningDate, TerminationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setDate(3, new java.sql.Date(emp.getDateOfBirth().getTime()));
            ps.setString(4, emp.getGender());
            ps.setString(5, emp.getEmail());
            ps.setString(6, emp.getPhoneNumber());
            ps.setString(7, emp.getAddress());
            ps.setString(8, emp.getPosition());
            ps.setDate(9, new java.sql.Date(emp.getJoiningDate().getTime()));
            if (emp.getTerminationDate() != null) {
                ps.setDate(10, new java.sql.Date(emp.getTerminationDate().getTime()));
            } else {
                ps.setNull(10, Types.DATE);
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    emp.setEmployeeID(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM Employee WHERE EmployeeID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractEmployee(rs);
            } else {
                throw new EmployeeNotFoundException("Employee not found: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE Employee SET FirstName=?, LastName=?, DateOfBirth=?, Gender=?, Email=?, PhoneNumber=?, Address=?, Position=?, JoiningDate=?, TerminationDate=? WHERE EmployeeID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setDate(3, new java.sql.Date(emp.getDateOfBirth().getTime()));
            ps.setString(4, emp.getGender());
            ps.setString(5, emp.getEmail());
            ps.setString(6, emp.getPhoneNumber());
            ps.setString(7, emp.getAddress());
            ps.setString(8, emp.getPosition());
            ps.setDate(9, new java.sql.Date(emp.getJoiningDate().getTime()));
            if (emp.getTerminationDate() != null) {
                ps.setDate(10, new java.sql.Date(emp.getTerminationDate().getTime()));
            } else {
                ps.setNull(10, Types.DATE);
            }
            ps.setInt(11, emp.getEmployeeID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeEmployee(int id) {
        String sql = "DELETE FROM Employee WHERE EmployeeID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("EmployeeID"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getDate("DateOfBirth"),
                rs.getString("Gender"),
                rs.getString("Email"),
                rs.getString("PhoneNumber"),
                rs.getString("Address"),
                rs.getString("Position"),
                rs.getDate("JoiningDate"),
                rs.getDate("TerminationDate")
        );
    }
}
