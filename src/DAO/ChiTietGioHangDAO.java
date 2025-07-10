package DAO;

import Models.ChiTietGioHang;
import java.sql.*;
import java.util.*;

public class ChiTietGioHangDAO {

    public boolean themChiTietGioHang(ChiTietGioHang ct) {
        String sql = "INSERT INTO chitietgiohang (MaGioHang, MaChiTiet, SoLuong) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaGioHang());
            ps.setInt(2, ct.getMaChiTiet());
            ps.setInt(3, ct.getSoLuong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChiTietGioHang(int maChiTietGH) {
        String sql = "DELETE FROM chitietgiohang WHERE MaChiTietGH = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChiTietGH);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaTatCaTheoGioHang(int maGioHang) {
        String sql = "DELETE FROM chitietgiohang WHERE MaGioHang = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maGioHang);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietGioHang> layChiTietTheoGioHang(int maGioHang) {
        List<ChiTietGioHang> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietgiohang WHERE MaGioHang = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maGioHang);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietGioHang ct = new ChiTietGioHang();
                ct.setMaChiTietGH(rs.getInt("MaChiTietGH"));
                ct.setMaGioHang(rs.getInt("MaGioHang"));
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public long tinhTongTien(int maGioHang) {
    String sql = """
        SELECT SUM(ctgh.SoLuong * ctsp.GiaThanh) AS TongTien
        FROM chitietgiohang ctgh
        JOIN chitietsanpham ctsp ON ctgh.MaChiTiet = ctsp.MaChiTiet
        WHERE ctgh.MaGioHang = ?
    """;

    try (Connection con = DBConnect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, maGioHang);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getLong("TongTien");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

}
