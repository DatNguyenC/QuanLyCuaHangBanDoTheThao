package Views;

import java.util.stream.Collectors;
import DAO.ChiTietSanPhamDAO;
import DAO.DanhMucDAO;
import DAO.SanPhamDAO;
import Models.NguoiDung;
import Models.SanPham;
import Models.ChiTietSanPham;
import Views.FormDieuHuong.CartForm;
import Views.FormDieuHuong.ThongTinNguoiDungForm;
import Views.PanelSanPham.TaoPanelSanPham;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import static Views.PanelSanPham.TaoPanelSanPham.taoPanelSanPham;
import Views.UIHelper.CustomScrollBarUI;
import Views.UIHelper.FormUIHelperUser;
import Views.UIHelper.WrapLayout;

public class UserForm extends JFrame {

    private List<SanPham> danhSachSanPham;

    private JPanel danhMucPanel;
    private JPanel giaCaPanel;
    private JPopupMenu popupSearch = new JPopupMenu();
    private JPanel danhSachPanel;
    private NguoiDung nguoiDungDangNhap;

    public UserForm(NguoiDung nd) {
        this.nguoiDungDangNhap = nd;
        setTitle("Quản lý cửa hàng thể thao UNETI");
        ImageIcon icon = new ImageIcon("src/icons/uneti.png");
        Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // icon chuẩn thường là 32x32
        setIconImage(scaledImage);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== LEFT: MENU trái với gradient =====
        JPanel menuPanel = FormUIHelperUser.createGradientPanel(new Color(110, 202, 255), new Color(172, 255, 255));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(240, getHeight()));

        JLabel lblMenuTitle = new JLabel("  Shop Thể Thao Uneti");
        lblMenuTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMenuTitle.setForeground(Color.WHITE);
        lblMenuTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menuPanel.add(lblMenuTitle);

// ========== Nút Danh mục ==========
        JButton btnDanhMuc = new JButton("Danh mục");
        FormUIHelperUser.styleMenuButton(btnDanhMuc);
        menuPanel.add(btnDanhMuc);

// Submenu danh mục
        JPanel subDanhMuc = new JPanel();
        subDanhMuc.setLayout(new BoxLayout(subDanhMuc, BoxLayout.Y_AXIS));
        subDanhMuc.setOpaque(false);
        subDanhMuc.setVisible(false);
        subDanhMuc.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        DanhMucDAO danhMucDAO = new DanhMucDAO();
        List<String[]> dsDanhMuc = danhMucDAO.layTatCaDanhMuc(); // [0] = mã, [1] = tên

        for (String[] dm : dsDanhMuc) {
            String maDM = dm[0];
            String tenDM = dm[1];

            JButton btn = new JButton("  • " + tenDM);
            FormUIHelperUser.styleMenuButton(btn);

            // Thêm sự kiện nếu cần xử lý khi click vào từng danh mục
            btn.addActionListener(e -> {
                locSanPhamTheoDanhMuc(maDM);
            });

            subDanhMuc.add(btn);
        }

        menuPanel.add(subDanhMuc);

// ========== Nút Giá cả ==========
        JButton btnGiaCa = new JButton("Giá cả");
        FormUIHelperUser.styleMenuButton(btnGiaCa);
        menuPanel.add(btnGiaCa);

// Submenu giá cả
        JPanel subGiaCa = new JPanel();
        subGiaCa.setLayout(new BoxLayout(subGiaCa, BoxLayout.Y_AXIS));
        subGiaCa.setOpaque(false);
        subGiaCa.setVisible(false);

        String[] giaCa = {
            "Dưới 500k", "500k - 1 triệu", "1 triệu - 2 triệu", "Trên 3 triệu"
        };

        for (String item : giaCa) {
            JButton btn = new JButton("  • " + item);
            FormUIHelperUser.styleMenuButton(btn);

            btn.addActionListener(e -> {
                switch (item) {
                    case "Dưới 500k" ->
                        locSanPhamTheoGia(0, 500_000);
                    case "500k - 1 triệu" ->
                        locSanPhamTheoGia(500_000, 1_000_000);
                    case "1 triệu - 2 triệu" ->
                        locSanPhamTheoGia(1_000_000, 2_000_000);
                    case "Trên 3 triệu" ->
                        locSanPhamTheoGia(3_000_000, Double.MAX_VALUE);
                }
            });

            subGiaCa.add(btn);
        }

        menuPanel.add(subGiaCa);

        lblMenuTitle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                danhSachPanel.removeAll(); // Xóa cũ
                SanPhamDAO sanPhamDAO = new SanPhamDAO();
                ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();

                for (SanPham sp : danhSachSanPham) {
                    String ten = sp.getTenSanPham();
                    String gia = String.format("%,.0f", sp.getGia());
                    String moTa = sp.getMoTa();
                    int soLuongTon = chiTietSanPhamDAO.tinhTongSoLuongTon(sp.getMaSanPham());
                    String soLuong = String.valueOf(soLuongTon);
                    String hinhAnh = sp.getHinhAnh();
                    danhSachPanel.add(TaoPanelSanPham.taoPanelSanPham(ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap));
                }

                danhSachPanel.revalidate();
                danhSachPanel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblMenuTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lblMenuTitle.setForeground(Color.YELLOW); // Optional: Hiệu ứng hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblMenuTitle.setForeground(Color.WHITE); // Reset màu
            }
        });

// ======= Sự kiện mở submenu có animation =======
        btnDanhMuc.addActionListener(e -> {
            FormUIHelperUser.togglePanelWithAnimation(subDanhMuc, !subDanhMuc.isVisible());
        });
        btnGiaCa.addActionListener(e -> {
            FormUIHelperUser.togglePanelWithAnimation(subGiaCa, !subGiaCa.isVisible());
        });

        add(menuPanel, BorderLayout.WEST);

        // ===== RIGHT: MAIN CONTENT =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JTextField txtSearch = new JTextField("Nhập sản phẩm cần tìm...");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBackground(new Color(238, 250, 255));
        txtSearch.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        txtSearch.setPreferredSize(new Dimension(200, 35));
        txtSearch.setCaretColor(Color.BLACK);
        txtSearch.setForeground(new Color(0, 100, 158));

        txtSearch.setOpaque(false);
        JPanel searchWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(txtSearch.getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        searchWrapper.setOpaque(false);
        searchWrapper.add(txtSearch, BorderLayout.CENTER);

        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập sản phẩm cần tìm...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Nhập sản phẩm cần tìm...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        txtSearch.addActionListener((ActionEvent e) -> {
            String keyword = txtSearch.getText().trim();
            if (!keyword.isEmpty() && !keyword.equals("Nhập sản phẩm cần tìm...")) {
                locSanPhamTheoTuKhoa(keyword);  // Gọi hàm lọc
            }
        });

        // Gợi ý tên sản phẩm khi gõ
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                showSuggestions();
            }

            private void showSuggestions() {
                String input = txtSearch.getText().trim().toLowerCase();
                popupSearch.setVisible(false);
                popupSearch.removeAll();

                if (input.isEmpty() || input.equals("nhập sản phẩm cần tìm...")) {
                    return;
                }

                List<String> suggestions = danhSachSanPham.stream()
                        .map(SanPham::getTenSanPham)
                        .filter(name -> name.toLowerCase().contains(input))
                        .limit(5) // Giới hạn gợi ý
                        .collect(Collectors.toList());

                for (String suggestion : suggestions) {
                    JMenuItem item = new JMenuItem(suggestion);
                    item.setFont(txtSearch.getFont());
                    item.addActionListener(e -> {
                        txtSearch.setText(suggestion);
                        popupSearch.setVisible(false);
                        locSanPhamTheoTuKhoa(suggestion);
                    });

                    popupSearch.add(item);
                }

                if (popupSearch.getComponentCount() > 0) {
                    popupSearch.show(txtSearch, 0, txtSearch.getHeight());
                }
            }
        });

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconPanel.setOpaque(false);

        ImageIcon iconCart = new ImageIcon(getClass().getResource("/icons/shopping-cart.png"));
        Image imgCart = iconCart.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton btnCart = new JButton(new ImageIcon(imgCart));
        FormUIHelperUser.styleIconButton(btnCart);
        btnCart.addActionListener(e -> {
            // Open the CartForm, passing the logged-in user
            CartForm cartForm = new CartForm(nguoiDungDangNhap);
            cartForm.setVisible(true);
        });
        btnCart.setToolTipText("Giỏ hàng - Xem và quản lý đơn hàng");
        iconPanel.add(btnCart);

        ImageIcon iconUser = new ImageIcon(getClass().getResource("/icons/user.png"));
        Image imgUser = iconUser.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton btnUser = new JButton(new ImageIcon(imgUser));
        FormUIHelperUser.styleIconButton(btnUser);
        iconPanel.add(btnUser);

        btnUser.addActionListener(e -> {
            // Mở form thông tin người dùng
            ThongTinNguoiDungForm form = new ThongTinNguoiDungForm(nguoiDungDangNhap);
            form.setVisible(true);
        });

        // Thêm tooltip cho nút giỏ hàng để thông báo về chức năng hủy đơn hàng
        btnCart.setToolTipText("<html>Giỏ hàng<br><small>Click để xem giỏ hàng và đặt hàng</small></html>");

        searchPanel.add(searchWrapper, BorderLayout.CENTER);
        searchPanel.add(iconPanel, BorderLayout.EAST);

        rightPanel.add(searchPanel, BorderLayout.NORTH);

// === DANH SÁCH SẢN PHẨM (Scroll Panel) ===
        danhSachPanel = new JPanel(new WrapLayout(FlowLayout.CENTER, 20, 20));
        danhSachPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        danhSachPanel.setBackground(new Color(232, 247, 255)); // hoặc Color.WHITE, hoặc bất kỳ màu nào bạn thích
        danhSachPanel.setOpaque(true);

// === Lấy dữ liệu từ CSDL để hiển thị sản phẩm ===
        SanPhamDAO sanPhamDAO = new SanPhamDAO();
        ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();
        danhSachSanPham = sanPhamDAO.layDanhSach();
        for (SanPham sp : danhSachSanPham) {
            String ten = sp.getTenSanPham();
            String gia = String.format("%,.0f", sp.getGia()); // format đẹp
            String moTa = sp.getMoTa();
            int soLuongTon = chiTietSanPhamDAO.tinhTongSoLuongTon(sp.getMaSanPham());
            String soLuong = String.valueOf(soLuongTon);
            String hinhAnh = sp.getHinhAnh(); // là tên file ảnh

            // Thêm panel sản phẩm vào danh sách
            danhSachPanel.add(
                    TaoPanelSanPham.taoPanelSanPham(ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap)
            );
        }

// Bạn có thể lặp danh sách sản phẩm từ CSDL
// ScrollPane
        JScrollPane scroll = new JScrollPane(danhSachPanel);
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(20); // cuộn mượt

// === Custom scroll UI bo tròn và màu đẹp ===
        scroll.getVerticalScrollBar().setUI(new CustomScrollBarUI(
                new Color(134, 219, 255), // màu thumb (phần kéo)
                new Color(230, 230, 230) // màu track (phần nền)
        ));

        // ======= Nút Thoát =======
        JButton btnThoat = new JButton("Thoát");
        btnThoat.setFocusPainted(false);
        btnThoat.setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        btnThoat.setBorderPainted(false);
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnThoat.setHorizontalAlignment(SwingConstants.LEFT);
        FormUIHelperUser.styleMenuButton(btnThoat);
        menuPanel.add(Box.createVerticalGlue()); // đẩy các nút phía trên lên
        menuPanel.add(btnThoat);
        btnThoat.setForeground(Color.WHITE);
        btnThoat.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose(); // Đóng UserForm hiện tại
                new LoginForm().setVisible(true); // Mở lại form đăng nhập
            }
        });
        menuPanel.add(Box.createVerticalGlue()); // Đẩy nút thoát xuống cuối
        menuPanel.add(btnThoat);

// === Thêm vào giao diện bên phải ===
        rightPanel.add(scroll, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);
    }

    private void locSanPhamTheoTuKhoa(String tuKhoa) {
        danhSachPanel.removeAll();  // Xóa các sản phẩm hiện tại
        tuKhoa = tuKhoa.toLowerCase();
        ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();

        for (SanPham sp : danhSachSanPham) {
            if (sp.getTenSanPham().toLowerCase().contains(tuKhoa)) {
                String ten = sp.getTenSanPham();
                String gia = String.format("%,.0f", sp.getGia());
                String moTa = sp.getMoTa();
                int soLuongTon = chiTietSanPhamDAO.tinhTongSoLuongTon(sp.getMaSanPham());
                String soLuong = String.valueOf(soLuongTon);
                String hinhAnh = sp.getHinhAnh();

                danhSachPanel.add(TaoPanelSanPham.taoPanelSanPham(ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap));
            }
        }

        danhSachPanel.revalidate();  // Cập nhật lại layout
        danhSachPanel.repaint();
    }

    private void locSanPhamTheoDanhMuc(String maDanhMuc) {
        danhSachPanel.removeAll();  // Xóa hết sản phẩm đang hiển thị
        ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();

        for (SanPham sp : danhSachSanPham) {
            if (String.valueOf(sp.getMaDanhMuc()).equals(maDanhMuc)) {
                String ten = sp.getTenSanPham();
                String gia = String.format("%,.0f", sp.getGia());
                String moTa = sp.getMoTa();
                int soLuongTon = chiTietSanPhamDAO.tinhTongSoLuongTon(sp.getMaSanPham());
                String soLuong = String.valueOf(soLuongTon);
                String hinhAnh = sp.getHinhAnh();

                danhSachPanel.add(TaoPanelSanPham.taoPanelSanPham(ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap));
            }
        }

        danhSachPanel.revalidate();
        danhSachPanel.repaint();
    }

    private void locSanPhamTheoGia(double min, double max) {
        danhSachPanel.removeAll();  // Xóa sản phẩm cũ
        ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();

        for (SanPham sp : danhSachSanPham) {
            double giaSP = sp.getGia();
            if (giaSP >= min && giaSP <= max) {
                String ten = sp.getTenSanPham();
                String gia = String.format("%,.0f", sp.getGia());
                String moTa = sp.getMoTa();
                int soLuongTon = chiTietSanPhamDAO.tinhTongSoLuongTon(sp.getMaSanPham());
                String soLuong = String.valueOf(soLuongTon);
                String hinhAnh = sp.getHinhAnh();

                danhSachPanel.add(TaoPanelSanPham.taoPanelSanPham(ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap));
            }
        }

        danhSachPanel.revalidate();
        danhSachPanel.repaint();
    }

}
