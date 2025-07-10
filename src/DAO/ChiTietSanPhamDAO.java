package DAO;

import Models.ChiTietSanPham;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietSanPhamDAO {

    // Lấy danh sách tất cả chi tiết sản phẩm
    public List<ChiTietSanPham> layDanhSach() {
        List<ChiTietSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietSanPham";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ChiTietSanPham ct = new ChiTietSanPham();
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setMaSanPham(rs.getInt("MaSanPham"));
                ct.setKichCo(rs.getString("KichCo"));
                ct.setMauSac(rs.getString("MauSac"));
                ct.setSoLuongTon(rs.getInt("SoLuongTon"));
                ct.setHinhAnhChiTiet(rs.getString("HinhAnhChiTiet"));
                ct.setGiaThanh(rs.getInt("GiaThanh"));
                list.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm chi tiết sản phẩm (KHÔNG nhập mã chi tiết - dùng AUTO_INCREMENT)
    public boolean themChiTietKhongMa(ChiTietSanPham ct) {
        String sql = "INSERT INTO ChiTietSanPham (MaSanPham, KichCo, MauSac, SoLuongTon, HinhAnhChiTiet, GiaThanh) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ct.getMaSanPham());
            stmt.setString(2, ct.getKichCo());
            stmt.setString(3, ct.getMauSac());
            stmt.setInt(4, ct.getSoLuongTon());
            stmt.setString(5, ct.getHinhAnhChiTiet());
            stmt.setInt(6, ct.getGiaThanh());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ct.setMaChiTiet(rs.getInt(1)); // gán lại mã mới vào đối tượng
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm chi tiết sản phẩm (CÓ mã chi tiết - trường hợp đặc biệt)
    public boolean themChiTiet(ChiTietSanPham ct) {
        String sql = "INSERT INTO ChiTietSanPham (MaChiTiet, MaSanPham, KichCo, MauSac, SoLuongTon, HinhAnhChiTiet, GiaThanh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ct.getMaChiTiet());
            stmt.setInt(2, ct.getMaSanPham());
            stmt.setString(3, ct.getKichCo());
            stmt.setString(4, ct.getMauSac());
            stmt.setInt(5, ct.getSoLuongTon());
            stmt.setString(6, ct.getHinhAnhChiTiet());
            stmt.setInt(7, ct.getGiaThanh());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật chi tiết sản phẩm
    public boolean capNhatChiTiet(ChiTietSanPham ct) {
        String sql = "UPDATE ChiTietSanPham SET MaSanPham=?, KichCo=?, MauSac=?, SoLuongTon=?, HinhAnhChiTiet=?, GiaThanh=? WHERE MaChiTiet=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ct.getMaSanPham());
            stmt.setString(2, ct.getKichCo());
            stmt.setString(3, ct.getMauSac());
            stmt.setInt(4, ct.getSoLuongTon());
            stmt.setString(5, ct.getHinhAnhChiTiet());
            stmt.setInt(6, ct.getGiaThanh());
            stmt.setInt(7, ct.getMaChiTiet());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa chi tiết sản phẩm
    public boolean xoaChiTiet(int maChiTiet) {
        String sql = "DELETE FROM ChiTietSanPham WHERE MaChiTiet=?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maChiTiet);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm kiếm chi tiết theo mã sản phẩm và kích cỡ
    public List<ChiTietSanPham> timKiem(int maSP, String kichCo) {
        List<ChiTietSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietSanPham WHERE MaSanPham=? AND KichCo LIKE ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maSP);
            stmt.setString(2, "%" + kichCo + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChiTietSanPham ct = new ChiTietSanPham();
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setMaSanPham(rs.getInt("MaSanPham"));
                ct.setKichCo(rs.getString("KichCo"));
                ct.setMauSac(rs.getString("MauSac"));
                ct.setSoLuongTon(rs.getInt("SoLuongTon"));
                ct.setHinhAnhChiTiet(rs.getString("HinhAnhChiTiet"));
                ct.setGiaThanh(rs.getInt("GiaThanh"));
                list.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ChiTietSanPham> layChiTietVaTenSanPham() {
        List<ChiTietSanPham> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSanPham FROM ChiTietSanPham ct JOIN SanPham sp ON ct.MaSanPham = sp.MaSanPham";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ChiTietSanPham ct = new ChiTietSanPham();
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setMaSanPham(rs.getInt("MaSanPham"));
                ct.setKichCo(rs.getString("KichCo"));
                ct.setMauSac(rs.getString("MauSac"));
                ct.setSoLuongTon(rs.getInt("SoLuongTon"));
                ct.setHinhAnhChiTiet(rs.getString("HinhAnhChiTiet"));
                ct.setGiaThanh(rs.getInt("GiaThanh"));
                ct.setTenSanPham(rs.getString("TenSanPham")); // Cần có setter trong Model
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BigDecimal layGiaThanhTheoMaChiTiet(int maChiTiet) {
        String sql = "SELECT GiaThanh FROM chitietsanpham WHERE MaChiTiet = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChiTiet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("GiaThanh");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public double layTongGiamGiaTheoMaSP(int maSP) {
        String sql = "SELECT SUM(km.PhanTramGiam) as TongGiam FROM khuyenmai km WHERE km.MaSanPham = ? AND NOW() BETWEEN km.NgayBatDau AND km.NgayKetThuc";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TongGiam");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int layMaSPTheoChiTiet(int maCT) {
        String sql = "SELECT MaSanPham FROM chitietsanpham WHERE MaChiTiet = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maCT);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MaSanPham");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ChiTietSanPham timTheoMa(int maChiTiet) {
        String sql = "SELECT * FROM ChiTietSanPham WHERE MaChiTiet = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maChiTiet);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ChiTietSanPham ct = new ChiTietSanPham();
                ct.setMaChiTiet(rs.getInt("MaChiTiet"));
                ct.setMaSanPham(rs.getInt("MaSanPham"));
                ct.setKichCo(rs.getString("KichCo"));
                ct.setMauSac(rs.getString("MauSac"));
                ct.setSoLuongTon(rs.getInt("SoLuongTon"));
                ct.setHinhAnhChiTiet(rs.getString("HinhAnhChiTiet"));
                ct.setGiaThanh(rs.getInt("GiaThanh"));
                return ct;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capNhatSoLuongTon(int maChiTietSP, int soLuongThem) {
        String sql = "UPDATE chitietsanpham SET SoLuongTon = SoLuongTon + ? WHERE MaChiTiet = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, soLuongThem);
            stmt.setInt(2, maChiTietSP);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

   

        public void congSoLuongTon(int maChiTiet, int soLuong) {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "UPDATE chitietsanpham SET SoLuongTon = SoLuongTon + ? WHERE MaChiTiet = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, soLuong);
                stmt.setInt(2, maChiTiet);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    

}
