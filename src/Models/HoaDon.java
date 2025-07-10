package Models;

import java.time.LocalDateTime;

public class HoaDon {
    private int maHoaDon;
    private int maNguoiDung;
    private LocalDateTime ngayLap;
    private double tongTien;
    private String trangThai;
    private Integer maKM; // có thể null

    // Constructors
    public HoaDon() {}

    public HoaDon(int maHoaDon, int maNguoiDung, LocalDateTime ngayLap, double tongTien, String trangThai, Integer maKM) {
        this.maHoaDon = maHoaDon;
        this.maNguoiDung = maNguoiDung;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.maKM = maKM;
    }

    // Getters and Setters
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Integer getMaKM() { return maKM; }
    public void setMaKM(Integer maKM) { this.maKM = maKM; }
}
