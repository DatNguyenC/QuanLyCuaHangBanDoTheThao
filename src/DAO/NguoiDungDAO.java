package DAO;

import Models.NguoiDung;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {

    public NguoiDung dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM nguoidung WHERE TenDangNhap = ? AND MatKhau = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToNguoiDung(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean taoTaiKhoan(NguoiDung nd) {
        String sql = "INSERT INTO nguoidung (TenDangNhap, MatKhau, HoTen, SoDienThoai, DiaChi, VaiTro, AnhDaiDien) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nd.getTenDangNhap());
            stmt.setString(2, nd.getMatKhau());
            stmt.setString(3, nd.getHoTen());
            stmt.setString(4, nd.getSoDienThoai());
            stmt.setString(5, nd.getDiaChi());
            stmt.setString(6, nd.getVaiTro());
            stmt.setString(7, nd.getAnhDaiDien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatThongTin(NguoiDung nd) {
        String sql = "UPDATE nguoidung SET HoTen = ?, SoDienThoai = ?, DiaChi = ?, AnhDaiDien = ? WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nd.getHoTen());
            stmt.setString(2, nd.getSoDienThoai());
            stmt.setString(3, nd.getDiaChi());
            stmt.setString(4, nd.getAnhDaiDien());
            stmt.setInt(5, nd.getMaNguoiDung());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatAnhDaiDien(int maND, String tenFileAnh) {
        String sql = "UPDATE nguoidung SET AnhDaiDien = ? WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenFileAnh);
            stmt.setInt(2, maND);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<NguoiDung> getAllNhanVien() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung WHERE VaiTro = 'Admin'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNguoiDung(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NguoiDung> getAllKhachHang() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung WHERE VaiTro = 'User'";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNguoiDung(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaNguoiDung(int maND) {
        String sql = "DELETE FROM nguoidung WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maND);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getAnhDaiDienByMaND(int maND) {
        String sql = "SELECT AnhDaiDien FROM nguoidung WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maND);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("AnhDaiDien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int demNguoiDungDungAnh(String tenAnh) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE AnhDaiDien = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenAnh);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private NguoiDung mapResultSetToNguoiDung(ResultSet rs) throws SQLException {
        NguoiDung nd = new NguoiDung();
        nd.setMaNguoiDung(rs.getInt("MaNguoiDung"));
        nd.setTenDangNhap(rs.getString("TenDangNhap"));
        nd.setMatKhau(rs.getString("MatKhau"));
        nd.setHoTen(rs.getString("HoTen"));
        nd.setSoDienThoai(rs.getString("SoDienThoai"));
        nd.setDiaChi(rs.getString("DiaChi"));
        nd.setVaiTro(rs.getString("VaiTro"));
        nd.setAnhDaiDien(rs.getString("AnhDaiDien"));
        return nd;
    }

    public boolean capNhatThongTinFull(NguoiDung nd) {
        String sql = "UPDATE nguoidung SET HoTen = ?, SoDienThoai = ?, DiaChi = ?, TenDangNhap = ?, MatKhau = ?, AnhDaiDien = ? WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nd.getHoTen());
            stmt.setString(2, nd.getSoDienThoai());
            stmt.setString(3, nd.getDiaChi());
            stmt.setString(4, nd.getTenDangNhap());
            stmt.setString(5, nd.getMatKhau());
            stmt.setString(6, nd.getAnhDaiDien());
            stmt.setInt(7, nd.getMaNguoiDung());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
