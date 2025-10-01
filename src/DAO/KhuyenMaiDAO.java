package DAO;

import Models.KhuyenMai;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class KhuyenMaiDAO {

    public List<KhuyenMai> layTatCaKhuyenMai() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM khuyenmai";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getInt("MaKM"));
                km.setTenKM(rs.getString("TenKM"));
                km.setMoTa(rs.getString("MoTa"));
                km.setPhanTramGiam(rs.getDouble("PhanTramGiam"));
                km.setNgayBatDau(rs.getTimestamp("NgayBatDau").toLocalDateTime());
                km.setNgayKetThuc(rs.getTimestamp("NgayKetThuc").toLocalDateTime());
                km.setMaSanPham(rs.getInt("MaSanPham")); // thêm dòng này
                list.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaKhuyenMai(int maKM) {
        String sql = "DELETE FROM khuyenmai WHERE MaKM = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double layPhanTramGiamTheoMaSanPham(int maSP) {
        String sql = "SELECT PhanTramGiam FROM khuyenmai WHERE MaSanPham = ? AND CURRENT_TIMESTAMP BETWEEN NgayBatDau AND NgayKetThuc";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("PhanTramGiam");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO khuyenmai (TenKM, MoTa, PhanTramGiam, NgayBatDau, NgayKetThuc, MaSanPham) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getMoTa());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setTimestamp(4, Timestamp.valueOf(km.getNgayBatDau()));
            ps.setTimestamp(5, Timestamp.valueOf(km.getNgayKetThuc()));
            ps.setInt(6, km.getMaSanPham());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KhuyenMai> layTatCa() {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM khuyenmai";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getInt("MaKM"));
                km.setTenKM(rs.getString("TenKM"));
                km.setMoTa(rs.getString("MoTa"));
                km.setPhanTramGiam(rs.getDouble("PhanTramGiam"));
                km.setNgayBatDau(rs.getTimestamp("NgayBatDau").toLocalDateTime());
                km.setNgayKetThuc(rs.getTimestamp("NgayKetThuc").toLocalDateTime());
                km.setMaSanPham(rs.getInt("MaSanPham"));
                ds.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE khuyenmai SET TenKM = ?, MoTa = ?, PhanTramGiam = ?, NgayBatDau = ?, NgayKetThuc = ?, MaSanPham = ? WHERE MaKM = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getMoTa());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setTimestamp(4, Timestamp.valueOf(km.getNgayBatDau()));
            ps.setTimestamp(5, Timestamp.valueOf(km.getNgayKetThuc()));
            ps.setInt(6, km.getMaSanPham());
            ps.setInt(7, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhuyenMai layKhuyenMaiApDung(int maSP) {
        LocalDateTime now = LocalDateTime.now();
        return layTatCa().stream()
                .filter(k -> k.getMaSanPham() == maSP)
                .filter(k -> !now.isBefore(k.getNgayBatDau()) && !now.isAfter(k.getNgayKetThuc()))
                .findFirst().orElse(null);
    }

}
