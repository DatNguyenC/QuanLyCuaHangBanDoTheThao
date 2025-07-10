package Models;

public class ChiTietHoaDon {
    private int maChiTietHD;
    private int maHoaDon;
    private int maChiTiet;
    private int soLuong;
    private double donGia;

    // Constructors
    public ChiTietHoaDon() {}

    public ChiTietHoaDon(int maHoaDon, int maChiTiet, int soLuong, double donGia) {
        this.maHoaDon = maHoaDon;
        this.maChiTiet = maChiTiet;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters and Setters
    public int getMaChiTietHD() { return maChiTietHD; }
    public void setMaChiTietHD(int maChiTietHD) { this.maChiTietHD = maChiTietHD; }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaChiTiet() { return maChiTiet; }
    public void setMaChiTiet(int maChiTiet) { this.maChiTiet = maChiTiet; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}
