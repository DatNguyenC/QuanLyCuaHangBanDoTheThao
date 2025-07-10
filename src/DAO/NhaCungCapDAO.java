package DAO;

import Models.NhaCungCap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {

    public List<NhaCungCap> layDanhSach() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM nhacungcap";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getInt("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSoDienThoai(rs.getString("SoDienThoai"));
                ncc.setEmail(rs.getString("Email"));
                list.add(ncc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themNhaCungCap(NhaCungCap ncc) {
        String sql = "INSERT INTO nhacungcap (TenNCC, DiaChi, SoDienThoai, Email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ncc.getTenNCC());
            stmt.setString(2, ncc.getDiaChi());
            stmt.setString(3, ncc.getSoDienThoai());
            stmt.setString(4, ncc.getEmail());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE nhacungcap SET TenNCC = ?, DiaChi = ?, SoDienThoai = ?, Email = ? WHERE MaNCC = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ncc.getTenNCC());
            stmt.setString(2, ncc.getDiaChi());
            stmt.setString(3, ncc.getSoDienThoai());
            stmt.setString(4, ncc.getEmail());
            stmt.setInt(5, ncc.getMaNCC());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaNhaCungCap(int maNCC) {
        String sql = "DELETE FROM nhacungcap WHERE MaNCC = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maNCC);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
