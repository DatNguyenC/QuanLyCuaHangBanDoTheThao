package DAO;

import Models.GioHang;
import java.sql.*;
import java.util.*;

public class GioHangDAO {

    public boolean themGioHang(GioHang gh) {
        String sql = "INSERT INTO giohang (MaNguoiDung, NgayTao) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gh.getMaNguoiDung());
            ps.setTimestamp(2, Timestamp.valueOf(gh.getNgayTao()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int layMaGioHangMoiNhat(int maNguoiDung) {
        String sql = "SELECT MaGioHang FROM giohang WHERE MaNguoiDung = ? ORDER BY NgayTao DESC LIMIT 1";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MaGioHang");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean xoaGioHang(int maGioHang) {
        String sqlChiTiet = "DELETE FROM chitietgiohang WHERE MaGioHang = ?";
        String sqlGioHang = "DELETE FROM giohang WHERE MaGioHang = ?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try (
                    PreparedStatement psChiTiet = conn.prepareStatement(sqlChiTiet); PreparedStatement psGioHang = conn.prepareStatement(sqlGioHang)) {
                psChiTiet.setInt(1, maGioHang);
                psChiTiet.executeUpdate();

                psGioHang.setInt(1, maGioHang);
                int rows = psGioHang.executeUpdate();

                conn.commit(); // Commit nếu mọi thứ ok
                return rows > 0;

            } catch (Exception ex) {
                conn.rollback(); // Rollback nếu có lỗi
                ex.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<GioHang> layTatCa() {
        List<GioHang> list = new ArrayList<>();
        String sql = "SELECT * FROM giohang";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GioHang gh = new GioHang();
                gh.setMaGioHang(rs.getInt("MaGioHang"));
                gh.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                gh.setNgayTao(rs.getTimestamp("NgayTao").toLocalDateTime());
                list.add(gh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
