package DAO;

import Models.ChiTietNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietNhapDAO {

    public List<ChiTietNhap> layDanhSach() {
        List<ChiTietNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietnhap";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ChiTietNhap ctn = new ChiTietNhap();
                ctn.setMaChiTietPN(rs.getInt("MaChiTietPN"));
                ctn.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                ctn.setMaChiTiet(rs.getInt("MaChiTiet"));
                ctn.setSoLuongNhap(rs.getInt("SoLuongNhap"));
                ctn.setDonGiaNhap(rs.getDouble("DonGiaNhap"));
                list.add(ctn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themChiTietNhap(ChiTietNhap ctn) {
        String sql = "INSERT INTO chitietnhap (MaPhieuNhap, MaChiTiet, SoLuongNhap, DonGiaNhap) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ctn.getMaPhieuNhap());
            stmt.setInt(2, ctn.getMaChiTiet());
            stmt.setInt(3, ctn.getSoLuongNhap());
            stmt.setDouble(4, ctn.getDonGiaNhap());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaChiTietNhap(int maChiTietPN) {
        String sql = "DELETE FROM chitietnhap WHERE MaChiTietPN = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maChiTietPN);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean suaChiTietNhap(ChiTietNhap ctn) {
        String sql = "UPDATE chitietnhap SET MaPhieuNhap = ?, MaChiTiet = ?, SoLuongNhap = ?, DonGiaNhap = ? WHERE MaChiTietPN = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ctn.getMaPhieuNhap());
            stmt.setInt(2, ctn.getMaChiTiet());
            stmt.setInt(3, ctn.getSoLuongNhap());
            stmt.setDouble(4, ctn.getDonGiaNhap());
            stmt.setInt(5, ctn.getMaChiTietPN());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaTheoMaPhieu(int maPhieuNhap) {
        String sql = "DELETE FROM chitietnhap WHERE MaPhieuNhap = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maPhieuNhap);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ChiTietNhap> layTheoMaPhieuNhap(int maPhieuNhap) {
        List<ChiTietNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietnhap WHERE MaPhieuNhap = ?";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhieuNhap);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietNhap ctn = new ChiTietNhap();
                ctn.setMaChiTietPN(rs.getInt("MaChiTietPN"));
                ctn.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                ctn.setMaChiTiet(rs.getInt("MaChiTiet"));
                ctn.setSoLuongNhap(rs.getInt("SoLuongNhap"));
                ctn.setDonGiaNhap(rs.getDouble("DonGiaNhap"));
                list.add(ctn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
