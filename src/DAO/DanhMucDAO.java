package DAO;

import java.sql.*;
import java.util.*;

public class DanhMucDAO {

    public List<String> layTatCaMaDanhMuc() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaDanhMuc FROM danhmuc";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("MaDanhMuc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themDanhMuc(String ma, String ten) {
        String sql = "INSERT INTO danhmuc(MaDanhMuc, TenDanhMuc) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            ps.setString(2, ten);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaDanhMuc(String ma) {
        String sql = "DELETE FROM danhmuc WHERE MaDanhMuc = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean coSanPhamThuocDanhMuc(String ma) {
        String sql = "SELECT COUNT(*) FROM sanpham WHERE MaDanhMuc = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String[]> layTatCaDanhMuc() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT MaDanhMuc, TenDanhMuc FROM danhmuc";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{rs.getString(1), rs.getString(2)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
