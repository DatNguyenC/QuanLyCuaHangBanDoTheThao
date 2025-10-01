package Views.FormDieuHuong.TrangThaiHoaDon;

import DAO.HoaDonDAO;
import DAO.ChiTietHoaDonDAO;
import DAO.ChiTietSanPhamDAO;
import Models.HoaDon;
import Models.NguoiDung;
import Models.ChiTietHoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class HoaDonForm extends JFrame {

    private final NguoiDung nguoiDangNhap;
    private CardLayout cardLayout = null;
    private JPanel mainPanel = null;
    private HoaDonDAO hoaDonDAO = null;
    private ChiTietHoaDonDAO chiTietHoaDonDAO = null;
    private ChiTietSanPhamDAO chiTietSanPhamDAO = null;

    public HoaDonForm(NguoiDung nguoiDangNhap) {
        this.nguoiDangNhap = nguoiDangNhap;
        if (nguoiDangNhap == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập để xem hóa đơn!");
            dispose();
            return;
        }

        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        chiTietSanPhamDAO = new ChiTietSanPhamDAO();

        setTitle("Quản lý hóa đơn - Shop Thể Thao Uneti");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(110, 202, 255));

        String[] trangThais = {"ChuaThanhToan", "DaThanhToan", "DangGiao", "DaGiao", "Huy"};
        String[] trangThaiHienThi = {"Chưa Thanh Toán", "Đã Thanh Toán", "Đang Giao", "Đã Giao", "Hủy"};
        for (int i = 0; i < trangThais.length; i++) {
            JButton btn = new JButton(trangThaiHienThi[i]);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(75, 160, 255));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            final String key = trangThais[i]; // Sử dụng key để switch panel
            btn.addActionListener(e -> switchPanel(key));
            toolBar.add(btn);
        }

        add(toolBar, BorderLayout.NORTH);

        // Panel trung tâm dùng CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        for (String trangThai : trangThais) {
            JPanel panel = taoPanelTheoTrangThai(trangThai);
            mainPanel.add(panel, trangThai); // Thêm panel trực tiếp với key
        }

        add(mainPanel, BorderLayout.CENTER);

        // Hiển thị panel đầu tiên
        switchPanel("ChuaThanhToan");
    }

    private void switchPanel(String key) {
        cardLayout.show(mainPanel, key);
    }

    private JPanel taoPanelTheoTrangThai(String trangThai) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(232, 247, 255));

        // Tiêu đề
        String title = "Danh sách hóa đơn: ";
        if ("ChuaThanhToan".equals(trangThai)) title += "Chưa Thanh Toán";
        else if ("DaThanhToan".equals(trangThai)) title += "Đã Thành Toán";
        else if ("DangGiao".equals(trangThai)) title += "Đang Giao";
        else if ("DaGiao".equals(trangThai)) title += "Đã Giao";
        else if ("Huy".equals(trangThai)) title += "Hủy";

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 100, 158));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Bảng danh sách hóa đơn
        String[] columnNames = {"Mã Hóa Đơn", "Ngày Lập", "Tổng Tiền", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(164, 207, 255));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(75, 160, 255));
        table.getTableHeader().setForeground(Color.WHITE);

        // Đổ dữ liệu vào bảng
        List<HoaDon> hoaDons = hoaDonDAO.layHoaDonTheoNguoiDungVaTrangThai(nguoiDangNhap.getMaNguoiDung(), trangThai);
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        for (HoaDon hd : hoaDons) {
            Object[] row = {
                hd.getMaHoaDon(),
                hd.getNgayLap().toString(),
                df.format(hd.getTongTien()),
                hd.getTrangThai() // Hiển thị trạng thái từ DB
            };
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel nút hành động
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        JButton btnChiTiet = new JButton("Xem Chi Tiết");
        btnChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChiTiet.setBackground(new Color(75, 160, 255));
        btnChiTiet.setForeground(Color.WHITE);
        btnChiTiet.setFocusPainted(false);
        btnChiTiet.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnChiTiet.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int maHoaDon = (int) table.getValueAt(selectedRow, 0);
                InvoiceDetailPanel detailPanel = new InvoiceDetailPanel(maHoaDon);
                JOptionPane.showMessageDialog(this, detailPanel, "Chi Tiết Hóa Đơn", JOptionPane.PLAIN_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Nút Thanh Toán chỉ hiển thị khi trạng thái là "ChuaThanhToan"
        JButton btnThanhToan = null;
        if ("ChuaThanhToan".equals(trangThai)) {
            btnThanhToan = new JButton("Thanh Toán");
            btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnThanhToan.setBackground(new Color(110, 202, 255));
            btnThanhToan.setForeground(Color.WHITE);
            btnThanhToan.setFocusPainted(false);
            btnThanhToan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btnThanhToan.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int maHoaDon = (int) table.getValueAt(selectedRow, 0);
                    HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHoaDon);
                    if (hd != null) {
                        hd.setTrangThai("DaThanhToan");
                        if (hoaDonDAO.capNhatHoaDon(hd)) {
                            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                            switchPanel("DaThanhToan"); // Chuyển sang tab "DaThanhToan" sau khi thanh toán
                        } else {
                            JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            });
        }

        // Nút Hủy đơn hàng chỉ hiển thị cho các trạng thái có thể hủy
        JButton btnHuyDon = null;
        if ("ChuaThanhToan".equals(trangThai) || "DaThanhToan".equals(trangThai) || "DangGiao".equals(trangThai)) {
            btnHuyDon = new JButton("Hủy Đơn Hàng");
            btnHuyDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnHuyDon.setBackground(new Color(220, 53, 69)); // Màu đỏ
            btnHuyDon.setForeground(Color.WHITE);
            btnHuyDon.setFocusPainted(false);
            btnHuyDon.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btnHuyDon.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int maHoaDon = (int) table.getValueAt(selectedRow, 0);
                    huyDonHang(maHoaDon);
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            });
        }

        // Nút Làm mới
        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.setBackground(new Color(75, 160, 255));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLamMoi.addActionListener(e -> {
            // Làm mới panel hiện tại
            JPanel newPanel = taoPanelTheoTrangThai(trangThai);
            mainPanel.remove(panel);
            mainPanel.add(newPanel, trangThai);
            cardLayout.show(mainPanel, trangThai);
        });

        buttonPanel.add(btnChiTiet);
        if (btnThanhToan != null) {
            buttonPanel.add(btnThanhToan);
        }
        if (btnHuyDon != null) {
            buttonPanel.add(btnHuyDon);
        }
        buttonPanel.add(btnLamMoi);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Phương thức hủy đơn hàng
    private void huyDonHang(int maHoaDon) {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn hủy đơn hàng #" + maHoaDon + "?\n\n" +
            "Lưu ý: Khi hủy đơn hàng, số lượng sản phẩm sẽ được hoàn lại vào kho.",
            "Xác nhận hủy đơn hàng", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Lấy thông tin hóa đơn
                HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHoaDon);
                if (hd == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra xem hóa đơn có thuộc về người dùng hiện tại không
                if (hd.getMaNguoiDung() != nguoiDangNhap.getMaNguoiDung()) {
                    JOptionPane.showMessageDialog(this, "Bạn không có quyền hủy hóa đơn này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lấy danh sách chi tiết hóa đơn
                List<ChiTietHoaDon> dsChiTiet = chiTietHoaDonDAO.layChiTietTheoMaHoaDon(maHoaDon);
                
                // Hoàn lại số lượng sản phẩm vào kho
                for (ChiTietHoaDon ct : dsChiTiet) {
                    chiTietSanPhamDAO.congSoLuongTon(ct.getMaChiTiet(), ct.getSoLuong());
                }

                // Cập nhật trạng thái hóa đơn thành "Huy"
                hd.setTrangThai("Huy");
                if (hoaDonDAO.capNhatHoaDon(hd)) {
                    JOptionPane.showMessageDialog(this, 
                        "Hủy đơn hàng thành công!\n" +
                        "Số lượng sản phẩm đã được hoàn lại vào kho.",
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Chuyển sang tab "Huy" để xem đơn hàng đã hủy
                    switchPanel("Huy");
                } else {
                    JOptionPane.showMessageDialog(this, "Hủy đơn hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Có lỗi xảy ra khi hủy đơn hàng: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}