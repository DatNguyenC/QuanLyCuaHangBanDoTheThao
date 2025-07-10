package Models;

import java.time.LocalDateTime;

public class KhuyenMai {
    private int maKM;
    private String tenKM;
    private String moTa;
    private double phanTramGiam;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private int maSanPham;

    public KhuyenMai() {}

    public KhuyenMai(int maKM, String tenKM, String moTa, double phanTramGiam, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, int maSanPham) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.moTa = moTa;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.maSanPham = maSanPham;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPHam) {
        this.maSanPham = maSanPHam;
    }

 

    // Getters và Setters
    public int getMaKM() { return maKM; }
    public void setMaKM(int maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(double phanTramGiam) { this.phanTramGiam = phanTramGiam; }

    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDateTime getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDateTime ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
}
