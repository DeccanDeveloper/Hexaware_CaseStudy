package dao;

import entity.FinancialRecord;
import util.DBConnUtil;
import exception.FinancialRecordException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinancialRecordService implements IFinancialRecordService {
    private Connection conn = DBConnUtil.getConnection("db.properties");

    @Override
    public boolean addFinancialRecord(int employeeId, String description, double amount, String recordType) {
        String sql = "INSERT INTO FinancialRecord (EmployeeID, RecordDate, Description, Amount, RecordType) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            ps.setString(3, description);
            ps.setDouble(4, amount);
            ps.setString(5, recordType);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FinancialRecordException("Error adding financial record.");
        }
    }

    @Override
    public FinancialRecord getFinancialRecordById(int recordId) {
        String sql = "SELECT * FROM FinancialRecord WHERE RecordID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractRecord(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForEmployee(int employeeId) {
        List<FinancialRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM FinancialRecord WHERE EmployeeID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractRecord(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForDate(java.util.Date recordDate) {
        List<FinancialRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM FinancialRecord WHERE RecordDate=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(recordDate.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractRecord(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private FinancialRecord extractRecord(ResultSet rs) throws SQLException {
        return new FinancialRecord(
                rs.getInt("RecordID"),
                rs.getInt("EmployeeID"),
                rs.getDate("RecordDate"),
                rs.getString("Description"),
                rs.getDouble("Amount"),
                rs.getString("RecordType")
        );
    }
}
