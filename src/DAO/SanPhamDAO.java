package DAO;

import Models.SanPham;
import java.sql.*;
import java.util.*;

public class SanPhamDAO {

    public List<SanPham> layDanhSach() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("MaSanPham"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getDouble("Gia"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setMaDanhMuc(rs.getString("MaDanhMuc"));
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO sanpham (MaSanPham, TenSanPham, MoTa, Gia, HinhAnh, MaDanhMuc) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sp.getMaSanPham());
            stmt.setString(2, sp.getTenSanPham());
            stmt.setString(3, sp.getMoTa());
            stmt.setDouble(4, sp.getGia());
            stmt.setString(5, sp.getHinhAnh());
            stmt.setString(6, sp.getMaDanhMuc());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themSanPhamKhongMa(SanPham sp) {
        String sql = "INSERT INTO sanpham (TenSanPham, MoTa, Gia, HinhAnh, MaDanhMuc) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, sp.getTenSanPham());
            stmt.setString(2, sp.getMoTa());
            stmt.setDouble(3, sp.getGia());
            stmt.setString(4, sp.getHinhAnh());
            stmt.setString(5, sp.getMaDanhMuc());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    // Nếu cột MaSanPham là số nguyên (int)
                    sp.setMaSanPham((rs.getInt(1)));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatSanPham(SanPham sp) {
        String sql = "UPDATE sanpham SET TenSanPham=?, MoTa=?, Gia=?, HinhAnh=?, MaDanhMuc=? WHERE MaSanPham=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sp.getTenSanPham());
            stmt.setString(2, sp.getMoTa());
            stmt.setDouble(3, sp.getGia());
            stmt.setString(4, sp.getHinhAnh());
            stmt.setString(5, sp.getMaDanhMuc());
            stmt.setInt(6, sp.getMaSanPham());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaSanPham(String maSP) {
        String sql = "DELETE FROM sanpham WHERE MaSanPham=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<SanPham> timKiem(String maSP, String maDM) {
        List<SanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM sanpham WHERE 1=1");
        if (maSP != null && !maSP.isEmpty()) {
            sql.append(" AND MaSanPham LIKE ?");
        }
        if (maDM != null && !maDM.isEmpty()) {
            sql.append(" AND MaDanhMuc = ?");
        }

        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (maSP != null && !maSP.isEmpty()) {
                stmt.setString(index++, "%" + maSP + "%");
            }
            if (maDM != null && !maDM.isEmpty()) {
                stmt.setString(index++, maDM);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSanPham(rs.getInt("MaSanPham"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getDouble("Gia"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setMaDanhMuc(rs.getString("MaDanhMuc"));
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> layTatCaMaSP() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT MaSanPham FROM sanpham";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try {
                    // Chuyển String sang int (nếu MaSanPham là kiểu VARCHAR trong DB)
                    list.add(rs.getInt("MaSanPham"));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace(); // hoặc bỏ qua nếu không thể chuyển được
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double layGiaTheoMaChiTiet(int maChiTiet) {
        String sql = "SELECT sp.Gia FROM chitietsanpham ct JOIN sanpham sp ON ct.MaSanPham = sp.MaSanPham WHERE ct.MaChiTiet = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChiTiet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("Gia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public SanPham laySanPhamTheoMaChiTiet(int maChiTiet) {
        SanPham sp = null;
        String sql = "SELECT sp.* FROM sanpham sp "
                + "JOIN chitietsanpham ct ON sp.MaSanPham = ct.MaSanPham "
                + "WHERE ct.MaChiTiet = ?";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maChiTiet); // Gán giá trị cho tham số trước khi thực thi
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sp = new SanPham();
                    sp.setMaSanPham(rs.getInt("MaSanPham"));
                    sp.setTenSanPham(rs.getString("TenSanPham"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getDouble("Gia"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setMaDanhMuc(rs.getString("MaDanhMuc"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sp;
    }

    public SanPham laySanPhamTheoMa(int maSP) {
        SanPham sp = null;
        String sql = "SELECT * FROM sanpham WHERE MaSanPham = ?";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maSP); // ✅ Viết ở đây là đúng

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sp = new SanPham();
                    sp.setMaSanPham(rs.getInt("MaSanPham"));
                    sp.setTenSanPham(rs.getString("TenSanPham"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getDouble("Gia"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setMaDanhMuc(rs.getString("MaDanhMuc"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sp;
    }
    public int laySoLuongTon(int maChiTiet) {
    int soLuong = 0;
    String sql = "SELECT SoLuongTon FROM chitietsanpham WHERE MaChiTiet = ?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, maChiTiet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            soLuong = rs.getInt("SoLuongTon");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return soLuong;
}


}
