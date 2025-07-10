/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author PC
 */
public class SanPham {

    private int maSanPham;
    private String tenSanPham;
    private String moTa;
    private double gia;
    private String hinhAnh;
    private String maDanhMuc;

    // Getter và Setter...
    // (Tương tự như mẫu đã đưa trước)
    public SanPham() {
    }

    public SanPham(int maSanPham, String tenSanPham, String moTa, double gia, String hinhAnh, String maDanhMuc) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.maDanhMuc = maDanhMuc;
    }

    public SanPham(String ten, String moTa, double gia, String hinhAnh, String maDanhMuc) {
        this.tenSanPham = ten;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.maDanhMuc = maDanhMuc;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

}
