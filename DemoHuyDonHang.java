/**
 * Demo chức năng hủy đơn hàng
 * 
 * File này minh họa cách sử dụng chức năng hủy đơn hàng mới được thêm vào
 * hệ thống quản lý cửa hàng thể thao UNETI
 */

import DAO.HoaDonDAO;
import DAO.ChiTietHoaDonDAO;
import DAO.ChiTietSanPhamDAO;
import Models.HoaDon;
import Models.NguoiDung;
import Models.ChiTietHoaDon;
import Views.FormDieuHuong.TrangThaiHoaDon.HoaDonForm;

import javax.swing.*;
import java.util.List;

public class DemoHuyDonHang {
    
    public static void main(String[] args) {
        // Demo cách sử dụng chức năng hủy đơn hàng
        
        System.out.println("=== DEMO CHỨC NĂNG HỦY ĐƠN HÀNG ===");
        System.out.println("Hệ thống quản lý cửa hàng thể thao UNETI");
        System.out.println();
        
        // 1. Khởi tạo các DAO cần thiết
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();
        
        // 2. Demo kiểm tra đơn hàng có thể hủy
        demoKiemTraDonHangCoTheHuy(hoaDonDAO);
        
        // 3. Demo quy trình hủy đơn hàng
        demoQuyTrinhHuyDonHang(hoaDonDAO, chiTietHoaDonDAO, chiTietSanPhamDAO);
        
        // 4. Demo giao diện người dùng
        demoGiaoDienNguoiDung();
        
        System.out.println("=== KẾT THÚC DEMO ===");
    }
    
    /**
     * Demo kiểm tra đơn hàng có thể hủy
     */
    private static void demoKiemTraDonHangCoTheHuy(HoaDonDAO hoaDonDAO) {
        System.out.println("1. KIỂM TRA ĐƠN HÀNG CÓ THỂ HỦY:");
        System.out.println("--------------------------------");
        
        // Lấy tất cả hóa đơn
        List<HoaDon> tatCaHoaDon = hoaDonDAO.layTatCaHoaDon();
        
        for (HoaDon hd : tatCaHoaDon) {
            String trangThai = hd.getTrangThai();
            boolean coTheHuy = "ChuaThanhToan".equals(trangThai) || 
                              "DaThanhToan".equals(trangThai) || 
                              "DangGiao".equals(trangThai);
            
            System.out.printf("Hóa đơn #%d - Trạng thái: %s - %s%n", 
                hd.getMaHoaDon(), 
                trangThai, 
                coTheHuy ? "✅ Có thể hủy" : "❌ Không thể hủy"
            );
        }
        System.out.println();
    }
    
    /**
     * Demo quy trình hủy đơn hàng
     */
    private static void demoQuyTrinhHuyDonHang(HoaDonDAO hoaDonDAO, 
                                              ChiTietHoaDonDAO chiTietHoaDonDAO,
                                              ChiTietSanPhamDAO chiTietSanPhamDAO) {
        System.out.println("2. QUY TRÌNH HỦY ĐƠN HÀNG:");
        System.out.println("----------------------------");
        
        // Giả sử có một hóa đơn cần hủy (maHoaDon = 1)
        int maHoaDonCanHuy = 1;
        
        System.out.println("Bước 1: Kiểm tra hóa đơn tồn tại");
        HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHoaDonCanHuy);
        if (hd == null) {
            System.out.println("❌ Không tìm thấy hóa đơn #" + maHoaDonCanHuy);
            return;
        }
        System.out.println("✅ Tìm thấy hóa đơn #" + maHoaDonCanHuy);
        
        System.out.println("Bước 2: Kiểm tra trạng thái có thể hủy");
        String trangThai = hd.getTrangThai();
        boolean coTheHuy = "ChuaThanhToan".equals(trangThai) || 
                          "DaThanhToan".equals(trangThai) || 
                          "DangGiao".equals(trangThai);
        
        if (!coTheHuy) {
            System.out.println("❌ Không thể hủy hóa đơn có trạng thái: " + trangThai);
            return;
        }
        System.out.println("✅ Có thể hủy hóa đơn có trạng thái: " + trangThai);
        
        System.out.println("Bước 3: Lấy danh sách chi tiết hóa đơn");
        List<ChiTietHoaDon> dsChiTiet = chiTietHoaDonDAO.layChiTietTheoMaHoaDon(maHoaDonCanHuy);
        System.out.println("✅ Tìm thấy " + dsChiTiet.size() + " sản phẩm trong hóa đơn");
        
        System.out.println("Bước 4: Hoàn lại số lượng sản phẩm vào kho");
        for (ChiTietHoaDon ct : dsChiTiet) {
            System.out.printf("   - Sản phẩm #%d: Hoàn lại %d sản phẩm%n", 
                ct.getMaChiTiet(), ct.getSoLuong());
            // chiTietSanPhamDAO.congSoLuongTon(ct.getMaChiTiet(), ct.getSoLuong());
        }
        
        System.out.println("Bước 5: Cập nhật trạng thái hóa đơn thành 'Huy'");
        hd.setTrangThai("Huy");
        // hoaDonDAO.capNhatHoaDon(hd);
        System.out.println("✅ Đã cập nhật trạng thái hóa đơn thành 'Huy'");
        
        System.out.println("✅ Quy trình hủy đơn hàng hoàn tất!");
        System.out.println();
    }
    
    /**
     * Demo giao diện người dùng
     */
    private static void demoGiaoDienNguoiDung() {
        System.out.println("3. GIAO DIỆN NGƯỜI DÙNG:");
        System.out.println("------------------------");
        
        System.out.println("Để sử dụng chức năng hủy đơn hàng:");
        System.out.println("1. Đăng nhập vào hệ thống");
        System.out.println("2. Click vào biểu tượng giỏ hàng");
        System.out.println("3. Click nút 'Xem Hóa Đơn'");
        System.out.println("4. Chọn tab chứa đơn hàng cần hủy");
        System.out.println("5. Click chọn đơn hàng trong danh sách");
        System.out.println("6. Click nút 'Hủy Đơn Hàng' (màu đỏ)");
        System.out.println("7. Xác nhận hủy đơn hàng");
        System.out.println();
        
        System.out.println("Các tab trạng thái đơn hàng:");
        System.out.println("- Chưa Thanh Toán: Có thể hủy");
        System.out.println("- Đã Thanh Toán: Có thể hủy");
        System.out.println("- Đang Giao: Có thể hủy");
        System.out.println("- Đã Giao: Không thể hủy");
        System.out.println("- Hủy: Đơn hàng đã bị hủy");
        System.out.println();
    }
    
    /**
     * Demo xử lý lỗi khi hủy đơn hàng
     */
    public static void demoXuLyLoi() {
        System.out.println("4. XỬ LÝ LỖI KHI HỦY ĐƠN HÀNG:");
        System.out.println("--------------------------------");
        
        System.out.println("Các trường hợp lỗi có thể xảy ra:");
        System.out.println("❌ Không tìm thấy hóa đơn");
        System.out.println("❌ Hóa đơn không thuộc về người dùng hiện tại");
        System.out.println("❌ Hóa đơn có trạng thái không thể hủy");
        System.out.println("❌ Lỗi cập nhật cơ sở dữ liệu");
        System.out.println("❌ Lỗi hoàn lại kho");
        System.out.println();
        
        System.out.println("Thông báo lỗi sẽ hiển thị cho người dùng:");
        System.out.println("- 'Không tìm thấy hóa đơn!'");
        System.out.println("- 'Bạn không có quyền hủy hóa đơn này!'");
        System.out.println("- 'Hủy đơn hàng thất bại!'");
        System.out.println();
    }
} 