package Views.PanelSanPham;

import DAO.ChiTietGioHangDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.DanhGiaDAO;
import DAO.GioHangDAO;
import DAO.NguoiDungDAO;
import DAO.SanPhamDAO;
import Models.ChiTietGioHang;
import Models.ChiTietSanPham;
import Models.DanhGia;
import Models.GioHang;
import Models.NguoiDung;
import Models.SanPham;
import Views.FormDieuHuong.FormSuaDanhGia;
import Views.FormDieuHuong.FormThemSuaDanhGia;
import Views.UIHelper.CustomScrollBarUI;
import Views.UIHelper.RoundedTitledBorder;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class FormThongTinSanPham extends JDialog {

    private ChiTietSanPham chiTietDaChon = null;

    public FormThongTinSanPham(JFrame parent, String ten, String gia, String moTa, String soLuong, String hinhAnh, NguoiDung nguoiDungDangNhap) {
        super(parent, "Thông tin sản phẩm", true);
        setSize(600, 800);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(232, 247, 255));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Ảnh sản phẩm ===
        JLabel lblImage = new JLabel();
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImage.setPreferredSize(new Dimension(320, 200));
        setAnh(lblImage, hinhAnh);
        content.add(lblImage);
        content.add(Box.createVerticalStrut(10));

        // === Thông tin sản phẩm ===
        JPanel panelThongTin = new JPanel();
        panelThongTin.setLayout(new BoxLayout(panelThongTin, BoxLayout.Y_AXIS));
        panelThongTin.setBackground(Color.WHITE);
        panelThongTin.setBorder(new RoundedTitledBorder("Thông tin sản phẩm", new Color(0, 102, 204)));
        panelThongTin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelThongTin.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        JLabel lblTen = new JLabel("Tên: " + ten);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblGia = new JLabel("Giá: " + gia + " VNĐ");
        lblGia.setForeground(Color.RED);
        lblGia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSL = new JLabel("Tồn kho: " + soLuong + " sản phẩm");
        lblSL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSL.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtMoTa = new JTextArea(moTa);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setEditable(false);
        txtMoTa.setBackground(panelThongTin.getBackground());
        txtMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelThongTin.add(lblTen);
        panelThongTin.add(lblGia);
        panelThongTin.add(lblSL);
        panelThongTin.add(txtMoTa);

        content.add(panelThongTin);
        content.add(Box.createVerticalStrut(10));

        // === Chi tiết sản phẩm ===
        JPanel panelChiTiet = new JPanel();
        panelChiTiet.setLayout(new BoxLayout(panelChiTiet, BoxLayout.Y_AXIS));
        panelChiTiet.setBackground(Color.WHITE);
        panelChiTiet.setBorder(new RoundedTitledBorder("Chọn chi tiết sản phẩm", new Color(0, 102, 204)));

        ChiTietSanPhamDAO ctspDAO = new ChiTietSanPhamDAO();
        List<ChiTietSanPham> listCT = ctspDAO.layDanhSach();
        ButtonGroup group = new ButtonGroup();

        JLabel lblSoLuongTon = new JLabel("Tồn kho: ");
        lblSoLuongTon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSoLuongTon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtSoLuongMua = new JTextField(5);
        txtSoLuongMua.setMaximumSize(new Dimension(60, 25));
        txtSoLuongMua.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (ChiTietSanPham ct : listCT) {
            SanPham sp = new SanPhamDAO().laySanPhamTheoMa(ct.getMaSanPham());
            if (sp.getTenSanPham().equals(ten)) {
                JRadioButton rb = new JRadioButton("Size: " + ct.getKichCo() + " | Màu: " + ct.getMauSac());
                rb.setOpaque(false);
                rb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                rb.addActionListener(e -> {
                    chiTietDaChon = ct;
                    setAnh(lblImage, ct.getHinhAnhChiTiet());
                    lblSoLuongTon.setText("Tồn kho: " + ct.getSoLuongTon() + " sản phẩm");
                    txtSoLuongMua.setText("1");
                });
                panelChiTiet.add(rb);
                group.add(rb);
            }
        }

        panelChiTiet.add(lblSoLuongTon);

        JPanel soLuongPanel = new JPanel();
        soLuongPanel.setLayout(new BoxLayout(soLuongPanel, BoxLayout.X_AXIS));
        soLuongPanel.setBackground(Color.WHITE);
        soLuongPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        soLuongPanel.add(new JLabel("Số lượng mua: "));
        soLuongPanel.add(Box.createHorizontalStrut(5));
        soLuongPanel.add(txtSoLuongMua);

        panelChiTiet.add(Box.createVerticalStrut(5));
        panelChiTiet.add(soLuongPanel);

        JScrollPane scrollChiTiet = new JScrollPane(panelChiTiet);
        scrollChiTiet.setPreferredSize(new Dimension(500, 120));
        scrollChiTiet.setBorder(null);
        scrollChiTiet.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChiTiet.getVerticalScrollBar().setUnitIncrement(20); // tốc độ cuộn
        scrollChiTiet.getVerticalScrollBar().setUI(new CustomScrollBarUI(
                new Color(134, 219, 255),
                new Color(230, 230, 230)
        ));

        content.add(scrollChiTiet);
        content.add(Box.createVerticalStrut(10));

        // === Đánh giá ===
        JPanel panelDanhGia = new JPanel();
        panelDanhGia.setLayout(new BoxLayout(panelDanhGia, BoxLayout.Y_AXIS));
        panelDanhGia.setBackground(Color.WHITE);
        panelDanhGia.setBorder(new RoundedTitledBorder("Đánh giá người dùng", new Color(0, 102, 204)));

        DanhGiaDAO dgDAO = new DanhGiaDAO();
        NguoiDungDAO ndDAO = new NguoiDungDAO();
        List<DanhGia> danhGias = dgDAO.layTatCaDanhGia();
        SanPham sp = new SanPhamDAO().laySanPhamTheoTen(ten);
        int maSP = sp.getMaSanPham();
        boolean daDanhGia = false;

        for (DanhGia dg : danhGias) {
            try {
                if (Integer.parseInt(dg.getMaSanPham()) == maSP) {
                    NguoiDung nd = ndDAO.getAllKhachHang().stream()
                            .filter(n -> n.getMaNguoiDung() == dg.getMaNguoiDung())
                            .findFirst().orElse(null);
                    if (nd == null) {
                        continue;
                    }

                    JLabel lbl = new JLabel("<html><b>" + nd.getHoTen() + "</b> - " + getStarString(dg.getSoSao())
                            + "<br><i>\"" + dg.getBinhLuan() + "\"</i></html>");
                    lbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    panelDanhGia.add(lbl);
                    panelDanhGia.add(Box.createVerticalStrut(5));

                    if (dg.getMaNguoiDung() == nguoiDungDangNhap.getMaNguoiDung()) {
                        daDanhGia = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        JScrollPane scrollDG = new JScrollPane(panelDanhGia);
        scrollDG.setPreferredSize(new Dimension(500, 100));
        scrollDG.setBorder(null);
        scrollDG.getVerticalScrollBar().setUnitIncrement(20);
        scrollDG.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDG.getVerticalScrollBar().setUI(new CustomScrollBarUI(
                new Color(134, 219, 255),
                new Color(230, 230, 230)
        ));

        content.add(scrollDG);
        content.add(Box.createVerticalStrut(5));

        // === Nút đánh giá ===
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelNut.setOpaque(false);

        JButton btnThemDG = taoButtonXanh("Thêm đánh giá");
        btnThemDG.addActionListener(e -> {
            new FormThemSuaDanhGia((JFrame) SwingUtilities.getWindowAncestor(this), maSP, nguoiDungDangNhap).setVisible(true);
            dispose();
        });

        JButton btnSuaDG = taoButtonXanh("Sửa đánh giá của bạn");
        btnSuaDG.addActionListener(e -> {
            new FormSuaDanhGia((JFrame) SwingUtilities.getWindowAncestor(this), maSP, nguoiDungDangNhap).setVisible(true);
            dispose();
        });

        panelNut.add(daDanhGia ? btnSuaDG : btnThemDG);
        content.add(panelNut);

        // === Nút thêm vào giỏ ===
        JButton btnThem = taoButtonXanh("Thêm vào giỏ hàng");
        btnThem.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThem.setPreferredSize(new Dimension(200, 40));
        btnThem.addActionListener(e -> {
            if (chiTietDaChon == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm!");
                return;
            }

            int soLuongMua;
            try {
                soLuongMua = Integer.parseInt(txtSoLuongMua.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ!");
                return;
            }

            if (soLuongMua <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return;
            }

            if (soLuongMua > chiTietDaChon.getSoLuongTon()) {
                JOptionPane.showMessageDialog(this, "Số lượng mua vượt quá tồn kho!");
                return;
            }

            try {
                GioHangDAO ghDAO = new GioHangDAO();
                ChiTietGioHangDAO ctghDAO = new ChiTietGioHangDAO();

                int maNguoiDung = nguoiDungDangNhap.getMaNguoiDung();
                int maGioHang = ghDAO.layMaGioHangMoiNhat(maNguoiDung);

                // Nếu chưa có giỏ hàng hôm nay → tạo mới
                if (maGioHang == -1) {
                    GioHang gh = new GioHang();
                    gh.setMaNguoiDung(maNguoiDung);
                    gh.setNgayTao(LocalDateTime.now());
                    boolean taoMoi = ghDAO.themGioHang(gh);
                    if (!taoMoi) {
                        JOptionPane.showMessageDialog(this, "Không thể tạo giỏ hàng mới!");
                        return;
                    }
                    maGioHang = ghDAO.layMaGioHangMoiNhat(maNguoiDung);
                }

                // Tạo chi tiết giỏ hàng
                ChiTietGioHang ctgh = new ChiTietGioHang();
                ctgh.setMaGioHang(maGioHang);
                ctgh.setMaChiTiet(chiTietDaChon.getMaChiTiet());
                ctgh.setSoLuong(soLuongMua);

                boolean them = ctghDAO.themChiTietGioHang(ctgh);
                if (them) {
                    JOptionPane.showMessageDialog(this, "Đã thêm vào giỏ: "
                            + chiTietDaChon.getKichCo() + " - " + chiTietDaChon.getMauSac()
                            + " | Số lượng: " + soLuongMua);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể thêm vào giỏ hàng!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi!");
            }
        });

        content.add(Box.createVerticalStrut(10));
        content.add(btnThem);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setAnh(JLabel lbl, String fileAnh) {
        try {
            ImageIcon icon = new ImageIcon("src/anhSP/" + fileAnh);
            Image img = icon.getImage().getScaledInstance(320, 200, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
            lbl.setText("");
        } catch (Exception e) {
            lbl.setText("Không có ảnh");
            lbl.setIcon(null);
        }
    }

    private String getStarString(int soSao) {
        return "⭐".repeat(soSao) + "☆".repeat(5 - soSao);
    }

    private JButton taoButtonXanh(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 123, 255));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = c.getWidth();
                int height = c.getHeight();
                g2.setColor(new Color(0, 123, 255));
                g2.fillRoundRect(0, 0, width, height, 25, 25);
                super.paint(g2, c);
                g2.dispose();
            }
        });
        return btn;
    }
}
