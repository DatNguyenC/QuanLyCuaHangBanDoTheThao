package Models;

public class ChiTietSanPham {

    private int maChiTiet;
    private int maSanPham;
    private String kichCo;
    private String mauSac;
    private int soLuongTon;
    private String hinhAnhChiTiet;
    private int GiaThanh;

    private String tenSanPham;

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(int maChiTiet, int maSanPham, String kichCo, String mauSac, int soLuongTon, String hinhAnhChiTiet) {
        this.maChiTiet = maChiTiet;
        this.maSanPham = maSanPham;
        this.kichCo = kichCo;
        this.mauSac = mauSac;
        this.soLuongTon = soLuongTon;
        this.hinhAnhChiTiet = hinhAnhChiTiet;
    }

    public ChiTietSanPham(int maChiTiet, int maSanPham, String kichCo, String mauSac, int soLuongTon, String hinhAnhChiTiet, int GiaThanh) {
        this.maChiTiet = maChiTiet;
        this.maSanPham = maSanPham;
        this.kichCo = kichCo;
        this.mauSac = mauSac;
        this.soLuongTon = soLuongTon;
        this.hinhAnhChiTiet = hinhAnhChiTiet;
        this.GiaThanh = GiaThanh;
    }

    public int getGiaThanh() {
        return GiaThanh;
    }

    public void setGiaThanh(int GiaThanh) {
        this.GiaThanh = GiaThanh;
    }

    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getKichCo() {
        return kichCo;
    }

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getHinhAnhChiTiet() {
        return hinhAnhChiTiet;
    }

    public void setHinhAnhChiTiet(String hinhAnhChiTiet) {
        this.hinhAnhChiTiet = hinhAnhChiTiet;
    }
//=================================================//

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
//=================================================//
}
