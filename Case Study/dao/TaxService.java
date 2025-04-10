package dao;

import entity.Tax;
import util.DBConnUtil;
import exception.TaxCalculationException;

import java.sql.*;
import java.util.*;

public class TaxService implements ITaxService {
    private Connection conn = DBConnUtil.getConnection("db.properties");

    @Override
    public boolean calculateTax(int employeeId, int taxYear) {
        String sql = "INSERT INTO Tax (EmployeeID, TaxYear, TaxableIncome, TaxAmount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            double income = 600000;
            double tax = income * 0.1;

            ps.setInt(1, employeeId);
            ps.setInt(2, taxYear);
            ps.setDouble(3, income);
            ps.setDouble(4, tax);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new TaxCalculationException("Tax calculation failed.");
        }
    }

    @Override
    public Tax getTaxById(int taxId) {
        String sql = "SELECT * FROM Tax WHERE TaxID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taxId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractTax(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tax> getTaxesForEmployee(int employeeId) {
        List<Tax> list = new ArrayList<>();
        String sql = "SELECT * FROM Tax WHERE EmployeeID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTax(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Tax> getTaxesForYear(int year) {
        List<Tax> list = new ArrayList<>();
        String sql = "SELECT * FROM Tax WHERE TaxYear=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTax(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Tax extractTax(ResultSet rs) throws SQLException {
        return new Tax(
            rs.getInt("TaxID"),
            rs.getInt("EmployeeID"),
            rs.getInt("TaxYear"),
            rs.getDouble("TaxableIncome"),
            rs.getDouble("TaxAmount")
        );
    }
}