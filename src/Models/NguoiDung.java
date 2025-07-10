package Models;

public class NguoiDung {

    private int maNguoiDung;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;
    private String vaiTro;
    private String anhDaiDien;

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    // Constructors
    public NguoiDung() {
    }

    public NguoiDung(int maNguoiDung, String tenDangNhap, String matKhau, String hoTen, String soDienThoai, String diaChi, String vaiTro) {
        this.maNguoiDung = maNguoiDung;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
    }

    // Getters & Setters
    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

//    @Override
//    public String toString() {
//        return hoTen + " (" + vaiTro + ")";
//    }

    @Override
    public String toString() {
        return "ID: " + maNguoiDung + " - " + hoTen;
    }

}
