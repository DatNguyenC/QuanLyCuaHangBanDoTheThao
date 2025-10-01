package DAO;

import java.sql.*;
import java.util.*;
import Models.HoaDon;

public class HoaDonDAO {

    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO hoadon (MaNguoiDung, NgayLap, TongTien, TrangThai, MaKM) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hd.getMaNguoiDung());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            ps.setDouble(3, hd.getTongTien());
            ps.setString(4, hd.getTrangThai());
            if (hd.getMaKM() != null) {
                ps.setInt(5, hd.getMaKM());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatHoaDon(HoaDon hd) {
        String sql = "UPDATE hoadon SET MaNguoiDung = ?, NgayLap = ?, TongTien = ?, TrangThai = ?, MaKM = ? WHERE MaHoaDon = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hd.getMaNguoiDung());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            ps.setDouble(3, hd.getTongTien());
            ps.setString(4, hd.getTrangThai());
            if (hd.getMaKM() != null) {
                ps.setInt(5, hd.getMaKM());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, hd.getMaHoaDon());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHoaDon(int maHoaDon) {
        String sql = "DELETE FROM hoadon WHERE MaHoaDon = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HoaDon> layTatCaHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getInt("MaHoaDon"));
                hd.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getString("TrangThai"));
                int maKM = rs.getInt("MaKM");
                hd.setMaKM(rs.wasNull() ? null : maKM);
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HoaDon timHoaDonTheoMa(int maHoaDon) {
        String sql = "SELECT * FROM hoadon WHERE MaHoaDon = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getInt("MaHoaDon"));
                hd.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getString("TrangThai"));
                int maKM = rs.getInt("MaKM");
                hd.setMaKM(rs.wasNull() ? null : maKM);
                return hd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int themVaLayMa(HoaDon hd) {
        int maHD = -1;
        String sql = "INSERT INTO hoadon (MaNguoiDung, NgayLap, TongTien, TrangThai, MaKM) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, hd.getMaNguoiDung());

            // Chuyển LocalDateTime sang Timestamp
            Timestamp ngayLap = Timestamp.valueOf(hd.getNgayLap());
            ps.setTimestamp(2, ngayLap);

            ps.setDouble(3, hd.getTongTien());
            ps.setString(4, hd.getTrangThai());

            if (hd.getMaKM() != null) {
                ps.setInt(5, hd.getMaKM());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                maHD = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maHD;
    }

    public Map<String, Double> layDoanhThuTheoThang() {
        Map<String, Double> doanhThuTheoThang = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(NgayLap, '%Y-%m') AS Thang, SUM(TongTien) AS Tong "
                + "FROM hoadon WHERE TrangThai = 'DaThanhToan' "
                + "GROUP BY Thang ORDER BY Thang";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String thang = rs.getString("Thang");
                double tong = rs.getDouble("Tong");
                doanhThuTheoThang.put(thang, tong);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doanhThuTheoThang;
    }

    public List<HoaDon> layHoaDonTheoNguoiDungVaTrangThai(int maNguoiDung, String trangThai) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE MaNguoiDung = ? AND TrangThai = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNguoiDung);
            ps.setString(2, trangThai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getInt("MaHoaDon"));
                hd.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getString("TrangThai"));
                int maKM = rs.getInt("MaKM");
                hd.setMaKM(rs.wasNull() ? null : maKM);
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
