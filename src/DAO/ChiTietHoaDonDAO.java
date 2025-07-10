package DAO;

import java.sql.*;
import java.util.*;
import Models.ChiTietHoaDon;

public class ChiTietHoaDonDAO {

    public boolean themChiTietHoaDon(ChiTietHoaDon ct) {
        String sql = "INSERT INTO chitiethoadon (MaHoaDon, MaChiTiet, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaHoaDon());
            ps.setInt(2, ct.getMaChiTiet());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChiTietHoaDon(int maChiTietHD) {
        String sql = "DELETE FROM chitiethoadon WHERE MaChiTietHD = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChiTietHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDon> layChiTietTheoMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM chitiethoadon WHERE MaHoaDon = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setMaChiTietHD(rs.getInt("MaChiTietHD"));
                ct.setMaHoaDon(rs.getInt("MaHoaDon"));
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaChiTietTheoHoaDon(int maHoaDon) {
        String sql = "DELETE FROM chitiethoadon WHERE MaHoaDon = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void themChiTiet(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO chitiethoadon (MaHoaDon, MaChiTiet, SoLuong, DonGia) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cthd.getMaHoaDon());
            ps.setInt(2, cthd.getMaChiTiet());
            ps.setInt(3, cthd.getSoLuong());
            ps.setDouble(4, cthd.getDonGia());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
