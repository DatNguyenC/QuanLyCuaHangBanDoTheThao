package Views.FormDieuHuong;

import DAO.*;
import Models.*;
import Views.UIHelper.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

public class CartForm extends JFrame {

    private NguoiDung nguoiDangNhap;
    private JPanel cartPanel;
    private JLabel lblTotalPrice;
    private GioHangDAO gioHangDAO;
    private ChiTietGioHangDAO chiTietGioHangDAO;
    private ChiTietSanPhamDAO chiTietSanPhamDAO;
    private SanPhamDAO sanPhamDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    private int maGioHang;

    public CartForm(NguoiDung nguoiDung) {
        this.nguoiDangNhap = nguoiDung;
        if (nguoiDung == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập để xem giỏ hàng!");
            dispose();
            return;
        }

        gioHangDAO = new GioHangDAO();
        chiTietGioHangDAO = new ChiTietGioHangDAO();
        chiTietSanPhamDAO = new ChiTietSanPhamDAO();
        sanPhamDAO = new SanPhamDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        maGioHang = gioHangDAO.layMaGioHangMoiNhat(nguoiDung.getMaNguoiDung());

        setTitle("Giỏ Hàng - Shop Thể Thao Uneti");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(110, 202, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Giỏ Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        lblTotalPrice = new JLabel("Tổng tiền: 0 VNĐ");
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalPrice.setForeground(Color.WHITE);
        headerPanel.add(lblTotalPrice, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        cartPanel = new JPanel(new WrapLayout(FlowLayout.CENTER, 20, 20));
        cartPanel.setBackground(new Color(232, 247, 255));
        cartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        loadCartItems();

        JScrollPane scroll = new JScrollPane(cartPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Color.WHITE);
        JButton btnCheckout = new JButton("Thanh Toán");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckout.setBackground(new Color(110, 202, 255));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCheckout.addActionListener(e -> {
            if (maGioHang == -1) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy giỏ hàng!");
                return;
            }

            List<ChiTietGioHang> dsCTGH = chiTietGioHangDAO.layChiTietTheoGioHang(maGioHang);
            if (dsCTGH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
                return;
            }

            HoaDonDAO hoaDonDAO = new HoaDonDAO();
            ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();

            double tongTien = 0;

            for (ChiTietGioHang ctgh : dsCTGH) {
                ChiTietSanPham ctsp = chiTietSanPhamDAO.layChiTietSanPhamTheoMa(ctgh.getMaChiTiet());
                if (ctsp == null) {
                    continue;
                }

                int maSP = ctsp.getMaSanPham();
                double donGia = ctsp.getGiaThanh();
                int soLuong = ctgh.getSoLuong();

                double giam = khuyenMaiDAO.layPhanTramGiamTheoMaSanPham(maSP);
                double giaSauGiam = donGia * (1 - giam / 100.0);

                tongTien += giaSauGiam * soLuong;
            }

            HoaDon hd = new HoaDon();
            hd.setMaNguoiDung(nguoiDangNhap.getMaNguoiDung());
            hd.setNgayLap(LocalDateTime.now());
            hd.setTongTien(tongTien);
            hd.setTrangThai("DangGiao");
            hd.setMaKM(null);

            int maHD = hoaDonDAO.themVaLayMa(hd);
            if (maHD == -1) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại!");
                return;
            }

            for (ChiTietGioHang ctgh : dsCTGH) {
                ChiTietSanPham ctsp = chiTietSanPhamDAO.layChiTietSanPhamTheoMa(ctgh.getMaChiTiet());
                if (ctsp == null) {
                    continue;
                }

                int maSP = ctsp.getMaSanPham();
                double giam = khuyenMaiDAO.layPhanTramGiamTheoMaSanPham(maSP);
                double giaSauGiam = ctsp.getGiaThanh() * (1 - giam / 100.0);

                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setMaHoaDon(maHD);
                cthd.setMaChiTiet(ctgh.getMaChiTiet());
                cthd.setSoLuong(ctgh.getSoLuong());
                cthd.setDonGia(giaSauGiam);

                cthdDAO.themChiTiet(cthd);

                // Cập nhật tồn kho (trừ đi)
            }

            // Xoá chi tiết giỏ hàng + giỏ hàng
            chiTietGioHangDAO.xoaChiTietGioHang(maGioHang);
            gioHangDAO.xoaGioHang(maGioHang);

            JOptionPane.showMessageDialog(this, "Đặt hàng thành công! Mã hóa đơn: " + maHD);
            dispose(); // Đóng form
        });

        JButton btnViewInvoices = new JButton("Xem Hóa Đơn");
        btnViewInvoices.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnViewInvoices.setBackground(new Color(110, 202, 255));
        btnViewInvoices.setForeground(Color.WHITE);
        btnViewInvoices.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnViewInvoices.setFocusPainted(false);
        btnViewInvoices.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnViewInvoices.addActionListener(e -> {
            // Gọi form hiển thị hóa đơn, ví dụ:
            new Views.FormDieuHuong.TrangThaiHoaDon.HoaDonForm(nguoiDangNhap).setVisible(true);
        });
        footerPanel.add(btnViewInvoices);

        footerPanel.add(btnCheckout);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        cartPanel.removeAll();
        DecimalFormat df = new DecimalFormat("#,### VNĐ");

        if (maGioHang == -1) {
            JLabel lblEmpty = new JLabel("Giỏ hàng trống!");
            lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblEmpty.setForeground(Color.GRAY);
            cartPanel.add(lblEmpty);
        } else {
            List<ChiTietGioHang> cartItems = chiTietGioHangDAO.layChiTietTheoGioHang(maGioHang);

            for (ChiTietGioHang item : cartItems) {
                ChiTietSanPham ctsp = chiTietSanPhamDAO.timTheoMa(item.getMaChiTiet());
                SanPham sp = sanPhamDAO.laySanPhamTheoMa(ctsp.getMaSanPham());
                if (sp != null && ctsp != null) {
                    JPanel itemPanel = createCartItemPanel(
                            sp.getTenSanPham(),
                            ctsp.getGiaThanh(),
                            item.getSoLuong(),
                            ctsp.getHinhAnhChiTiet(),
                            item.getMaChiTietGH(),
                            ctsp.getMaChiTiet(),
                            ctsp.getMaSanPham()
                    );
                    cartPanel.add(itemPanel);
                }
            }
        }

        capNhatTongGia();
        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private JPanel createCartItemPanel(String ten, double giaGoc, int soLuong, String hinhAnh,
            int maChiTietGH, int maChiTiet, int maSanPham) {

        DecimalFormat df = new DecimalFormat("#,### VNĐ");

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0, 122, 192));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(220, 300)); // tăng chiều cao để tránh mất chữ
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Ảnh sản phẩm ===
        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnh.setPreferredSize(new Dimension(180, 120));
        ImageIcon icon = loadProductImage(hinhAnh);
        Image img = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
        lblAnh.setIcon(new ImageIcon(img));
        panel.add(lblAnh);

        // === Tên sản phẩm ===
        JLabel lblTen = new JLabel(ten);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTen.setForeground(new Color(0, 100, 158));
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTen);

        // === Giá gốc ===
        JLabel lblGia = new JLabel("Giá: " + df.format(giaGoc));
        lblGia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblGia.setForeground(Color.GRAY);
        lblGia.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblGia);

        // === Giảm giá ===
        JCheckBox chkKhuyenMai = new JCheckBox("Áp dụng mã giảm giá");
        chkKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkKhuyenMai.setBackground(Color.WHITE);
        chkKhuyenMai.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGiaGiam = new JLabel();
        lblGiaGiam.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblGiaGiam.setForeground(Color.RED);
        lblGiaGiam.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.putClientProperty("giaGoc", giaGoc);
        panel.putClientProperty("soLuong", soLuong);
        panel.putClientProperty("maSP", maSanPham);
        panel.putClientProperty("apDungKM", false);

        KhuyenMai km = khuyenMaiDAO.layTatCa().stream()
                .filter(k -> k.getMaSanPham() == maSanPham)
                .filter(k -> {
                    LocalDateTime now = LocalDateTime.now();
                    return !now.isBefore(k.getNgayBatDau()) && !now.isAfter(k.getNgayKetThuc());
                }).findFirst().orElse(null);

        if (km != null) {
            double giam = giaGoc * (km.getPhanTramGiam() / 100.0);
            double giaSauGiam = giaGoc - giam;
            lblGiaGiam.setText("Giá sau giảm: " + df.format(giaSauGiam));
            chkKhuyenMai.setSelected(true);
            panel.putClientProperty("apDungKM", true);
        } else {
            chkKhuyenMai.setEnabled(false);
            chkKhuyenMai.setText("Không có mã KM");
        }

        chkKhuyenMai.addActionListener(e -> {
            if (chkKhuyenMai.isSelected() && km != null) {
                double giam = giaGoc * (km.getPhanTramGiam() / 100.0);
                double giaSauGiam = giaGoc - giam;
                lblGiaGiam.setText("Giá sau giảm: " + df.format(giaSauGiam));
                panel.putClientProperty("apDungKM", true);
            } else {
                lblGiaGiam.setText("");
                panel.putClientProperty("apDungKM", false);
            }
            capNhatTongGia();
        });

        panel.add(chkKhuyenMai);
        panel.add(lblGiaGiam);

        // === Số lượng + cập nhật ===
        JPanel pnlSL = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlSL.setOpaque(false);
        JLabel lblSL = new JLabel("SL:");
        JTextField txtSL = new JTextField(String.valueOf(soLuong), 3);
        txtSL.setHorizontalAlignment(JTextField.CENTER);

        JButton btnUpdate = new JButton("Cập nhật");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUpdate.setBackground(new Color(0, 123, 255));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        btnUpdate.addActionListener(e -> {
            try {
                int newSL = Integer.parseInt(txtSL.getText());
                if (newSL <= 0) {
                    throw new Exception();
                }
                int diff = newSL - soLuong;
                int ton = chiTietSanPhamDAO.laySoLuongTon(maChiTiet);
                if (diff > ton) {
                    JOptionPane.showMessageDialog(null, "Không đủ hàng! Còn " + ton);
                    return;
                }
                ChiTietGioHang ct = new ChiTietGioHang(maChiTietGH, maGioHang, maChiTiet, newSL);
                if (chiTietGioHangDAO.xoaChiTietGioHang(maChiTietGH)
                        && chiTietGioHangDAO.themChiTietGioHang(ct)) {
                    chiTietSanPhamDAO.capNhatSoLuongTon(maChiTiet, -diff);
                    loadCartItems();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "SL không hợp lệ");
                txtSL.setText(String.valueOf(soLuong));
            }
        });

        pnlSL.add(lblSL);
        pnlSL.add(txtSL);
        pnlSL.add(btnUpdate);
        panel.add(pnlSL);

        // === Nút XÓA full ngang đẹp ===
        JButton btnXoa = new JButton("XÓA SẢN PHẨM");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnXoa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnXoa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Hover hiệu ứng
        btnXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnXoa.setBackground(new Color(200, 40, 55));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnXoa.setBackground(new Color(220, 53, 69));
            }
        });

        btnXoa.addActionListener(e -> {
            int cf = JOptionPane.showConfirmDialog(null, "Xóa sản phẩm này khỏi giỏ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                if (chiTietGioHangDAO.xoaChiTietGioHang(maChiTietGH)) {
                    chiTietSanPhamDAO.capNhatSoLuongTon(maChiTiet, soLuong);
                    loadCartItems();
                }
            }
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(btnXoa);

        return panel;
    }

    private void capNhatTongGia() {
        long tong = 0;
        for (Component comp : cartPanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                Double gia = (Double) panel.getClientProperty("giaGoc");
                Integer soLuong = (Integer) panel.getClientProperty("soLuong");
                Integer maSP = (Integer) panel.getClientProperty("maSP");
                Boolean apDungKM = (Boolean) panel.getClientProperty("apDungKM");

                if (gia == null || soLuong == null || maSP == null) {
                    continue;
                }

                if (apDungKM != null && apDungKM) {
                    double giam = chiTietSanPhamDAO.layTongGiamGiaTheoMaSP(maSP);
                    gia = gia * (1 - giam / 100);
                }

                tong += gia * soLuong;
            }
        }

        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        lblTotalPrice.setText("Tổng tiền: " + df.format(tong));
    }

    private ImageIcon loadProductImage(String hinhAnh) {
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            try {
                return new ImageIcon(getClass().getResource("/anhSP/" + hinhAnh));
            } catch (Exception e) {
                System.err.println("Lỗi tải ảnh: " + hinhAnh);
            }
        }
        return new ImageIcon(getClass().getResource("/icons/default.png"));
    }

}
