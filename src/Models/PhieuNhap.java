/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author PC
 */
import java.util.Date;

public class PhieuNhap {
    private int maPhieuNhap;
    private int maNCC;
    private Date ngayNhap;

    // Getters & Setters

    public PhieuNhap(int maPhieuNhap, int maNCC, Date ngayNhap) {
        this.maPhieuNhap = maPhieuNhap;
        this.maNCC = maNCC;
        this.ngayNhap = ngayNhap;
    }

    public PhieuNhap() {
    }

    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

 
    
}
