package DAO;

import Models.PhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    public List<PhieuNhap> layDanhSach() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM phieunhap";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                pn.setMaNCC(rs.getInt("MaNCC"));
                pn.setNgayNhap(rs.getTimestamp("NgayNhap"));
                list.add(pn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themPhieuNhap(PhieuNhap pn) {
        String sql = "INSERT INTO phieunhap (MaNCC, NgayNhap) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pn.getMaNCC());
            stmt.setTimestamp(2, new Timestamp(pn.getNgayNhap().getTime()));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaPhieuNhap(PhieuNhap pn) {
        String sql = "UPDATE phieunhap SET MaNCC = ?, NgayNhap = ? WHERE MaPhieuNhap = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pn.getMaNCC());
            stmt.setTimestamp(2, new Timestamp(pn.getNgayNhap().getTime()));
            stmt.setInt(3, pn.getMaPhieuNhap());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaPhieuNhap(int maPhieuNhap) {
        String sql = "DELETE FROM phieunhap WHERE MaPhieuNhap = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhieuNhap);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PhieuNhap> timKiemTheoMa(String keyword) {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM phieunhap WHERE MaPhieuNhap LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                pn.setMaNCC(rs.getInt("MaNCC"));
                pn.setNgayNhap(rs.getTimestamp("NgayNhap"));
                list.add(pn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
