/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author PC
 */
public class ChiTietNhap {
    private int MaChiTietPN;
    private int MaPhieuNhap;
    private int MaChiTiet;
    private int SoLuongNhap;
    private double DonGiaNhap;

    public ChiTietNhap() {
    }

    public ChiTietNhap(int MaChiTietPN, int MaPhieuNhap, int MaChiTiet, int SoLuongNhap, double DonGiaNhap) {
        this.MaChiTietPN = MaChiTietPN;
        this.MaPhieuNhap = MaPhieuNhap;
        this.MaChiTiet = MaChiTiet;
        this.SoLuongNhap = SoLuongNhap;
        this.DonGiaNhap = DonGiaNhap;
    }

    
    public int getMaChiTietPN() {
        return MaChiTietPN;
    }

    public void setMaChiTietPN(int MaChiTietPN) {
        this.MaChiTietPN = MaChiTietPN;
    }

    public int getMaPhieuNhap() {
        return MaPhieuNhap;
    }

    public void setMaPhieuNhap(int MaPhieuNhap) {
        this.MaPhieuNhap = MaPhieuNhap;
    }

    public int getMaChiTiet() {
        return MaChiTiet;
    }

    public void setMaChiTiet(int MaChiTiet) {
        this.MaChiTiet = MaChiTiet;
    }

    public int getSoLuongNhap() {
        return SoLuongNhap;
    }

    public void setSoLuongNhap(int SoLuongNhap) {
        this.SoLuongNhap = SoLuongNhap;
    }

    public double getDonGiaNhap() {
        return DonGiaNhap;
    }

    public void setDonGiaNhap(double DonGiaNhap) {
        this.DonGiaNhap = DonGiaNhap;
    }
    
}