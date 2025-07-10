package Models;

import java.time.LocalDateTime;

public class GioHang {
    private int maGioHang;
    private int maNguoiDung;
    private LocalDateTime ngayTao;

    public GioHang() {}

    public GioHang(int maNguoiDung, LocalDateTime ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.ngayTao = ngayTao;
    }

    public int getMaGioHang() { return maGioHang; }
    public void setMaGioHang(int maGioHang) { this.maGioHang = maGioHang; }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
