package dao;

import entity.Payroll;
import util.DBConnUtil;
import exception.PayrollGenerationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayrollService implements IPayrollService {
    private Connection conn = DBConnUtil.getConnection("db.properties");

    @Override
    public boolean generatePayroll(int employeeId, java.util.Date start, java.util.Date end) {
        String sql = "INSERT INTO Payroll (EmployeeID, PayPeriodStartDate, PayPeriodEndDate, BasicSalary, OvertimePay, Deductions, NetSalary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            double basic = 50000;
            double overtime = 5000;
            double deductions = 7000;
            double net = basic + overtime - deductions;

            ps.setInt(1, employeeId);
            ps.setDate(2, new java.sql.Date(start.getTime()));
            ps.setDate(3, new java.sql.Date(end.getTime()));
            ps.setDouble(4, basic);
            ps.setDouble(5, overtime);
            ps.setDouble(6, deductions);
            ps.setDouble(7, net);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PayrollGenerationException("Error generating payroll.");
        }
    }

    @Override
    public Payroll getPayrollById(int payrollId) {
        String sql = "SELECT * FROM Payroll WHERE PayrollID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payrollId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractPayroll(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payroll> getPayrollsForEmployee(int employeeId) {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM Payroll WHERE EmployeeID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractPayroll(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Payroll> getPayrollsForPeriod(java.util.Date start, java.util.Date end) {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM Payroll WHERE PayPeriodStartDate >= ? AND PayPeriodEndDate <= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(start.getTime()));
            ps.setDate(2, new java.sql.Date(end.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractPayroll(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Payroll extractPayroll(ResultSet rs) throws SQLException {
        return new Payroll(
                rs.getInt("PayrollID"),
                rs.getInt("EmployeeID"),
                rs.getDate("PayPeriodStartDate"),
                rs.getDate("PayPeriodEndDate"),
                rs.getDouble("BasicSalary"),
                rs.getDouble("OvertimePay"),
                rs.getDouble("Deductions"),
                rs.getDouble("NetSalary")
        );
    }
}
