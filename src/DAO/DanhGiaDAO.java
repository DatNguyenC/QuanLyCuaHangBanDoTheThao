package DAO;

import Models.DanhGia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DanhGiaDAO {

    // Lấy danh sách tất cả đánh giá
    public List<DanhGia> layTatCaDanhGia() {
        List<DanhGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhGia";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DanhGia dg = new DanhGia();
                dg.setMaDanhGia(rs.getInt("MaDanhGia"));
                dg.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                dg.setMaSanPham(rs.getString("MaSanPham"));
                dg.setSoSao(rs.getInt("SoSao"));
                dg.setBinhLuan(rs.getString("BinhLuan"));
                dg.setNgayDanhGia(rs.getTimestamp("NgayDanhGia"));
                list.add(dg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm đánh giá
    public boolean themDanhGia(DanhGia dg) {
        String sql = "INSERT INTO DanhGia (MaDanhGia, MaNguoiDung, MaSanPham, SoSao, BinhLuan, NgayDanhGia) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dg.getMaDanhGia());
            stmt.setInt(2, dg.getMaNguoiDung());
            stmt.setString(3, dg.getMaSanPham());
            stmt.setInt(4, dg.getSoSao());
            stmt.setString(5, dg.getBinhLuan());
            stmt.setTimestamp(6, new Timestamp(dg.getNgayDanhGia().getTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật đánh giá
    public boolean capNhatDanhGia(DanhGia dg) {
        String sql = "UPDATE DanhGia SET MaNguoiDung=?, MaSanPham=?, SoSao=?, BinhLuan=?, NgayDanhGia=? WHERE MaDanhGia=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dg.getMaNguoiDung());
            stmt.setString(2, dg.getMaSanPham());
            stmt.setInt(3, dg.getSoSao());
            stmt.setString(4, dg.getBinhLuan());
            stmt.setTimestamp(5, new Timestamp(dg.getNgayDanhGia().getTime()));
            stmt.setInt(6, dg.getMaDanhGia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xoá đánh giá
    public boolean xoaDanhGia(String maDanhGia) {
        String sql = "DELETE FROM DanhGia WHERE MaDanhGia=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDanhGia);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm kiếm đánh giá theo mã sản phẩm
    public List<DanhGia> layDanhGiaTheoSanPham(String maSanPham) {
        List<DanhGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhGia WHERE MaSanPham = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DanhGia dg = new DanhGia();
                dg.setMaDanhGia(rs.getInt("MaDanhGia"));
                dg.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                dg.setMaSanPham(rs.getString("MaSanPham"));
                dg.setSoSao(rs.getInt("SoSao"));
                dg.setBinhLuan(rs.getString("BinhLuan"));
                dg.setNgayDanhGia(rs.getTimestamp("NgayDanhGia"));
                list.add(dg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy đánh giá theo mã đánh giá
    public DanhGia timTheoMa(String maDanhGia) {
        String sql = "SELECT * FROM DanhGia WHERE MaDanhGia = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDanhGia);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DanhGia dg = new DanhGia();
                dg.setMaDanhGia(rs.getInt("MaDanhGia"));
                dg.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                dg.setMaSanPham(rs.getString("MaSanPham"));
                dg.setSoSao(rs.getInt("SoSao"));
                dg.setBinhLuan(rs.getString("BinhLuan"));
                dg.setNgayDanhGia(rs.getTimestamp("NgayDanhGia"));
                return dg;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DanhGia layDanhGiaTheoNguoiDung(int maSP, int maNguoiDung) {
        String sql = "SELECT * FROM danhgia WHERE MaSanPham = ? AND MaNguoiDung = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(maSP));
            ps.setInt(2, maNguoiDung);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DanhGia dg = new DanhGia();
                dg.setMaDanhGia(rs.getInt("MaDanhGia"));
                dg.setMaSanPham(rs.getString("MaSanPham"));
                dg.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                dg.setSoSao(rs.getInt("SoSao"));
                dg.setBinhLuan(rs.getString("BinhLuan"));
                return dg;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
