package Views;

import Views.MenuPanel.ThongKePanel;
import Views.MenuPanel.BanHangPanel;
import Views.MenuPanel.ThongTinTaiKhoanPanel;
import Views.MenuPanel.KhachHangPanel;
import Views.MenuPanel.HoaDonKhuyenMaiPanel;
import Views.MenuPanel.KhoPanel;
import Views.MenuPanel.SanPhamPanel;
import Views.MenuPanel.NhanVienPanel;
import Models.NguoiDung;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminForm extends JFrame {

    private final JPanel contentPanel;
    private final NguoiDung nd;
    private final JLabel lblUser;
    private final List<JButton> menuButtons = new ArrayList<>();

    public AdminForm(NguoiDung nd) {
        this.nd = nd;
        setTitle("Quản lý cửa hàng thể thao");
        setSize(1400, 800);
        ImageIcon icon = new ImageIcon("src/icons/uneti.png");
        Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // icon chuẩn thường là 32x32
        setIconImage(scaledImage);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));

        this.lblUser = new JLabel("Xin chào, " + nd.getHoTen(), JLabel.CENTER);
        lblUser.setForeground(Color.BLACK);
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        lblUser.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.add(lblUser);

        sidebar.add(createMenuButton("Thông tin tài khoản", "user.png", e -> showPanel("Tài khoản")));
        sidebar.add(createMenuButton("Bán hàng", "sell.png", e -> showPanel("Bán hàng")));
        sidebar.add(createMenuButton("Sản phẩm", "product.png", e -> showPanel("Sản phẩm")));
        sidebar.add(createMenuButton("Kho", "warehouse.png", e -> showPanel("Kho")));
        sidebar.add(createMenuButton("Hóa đơn - Khuyến mãi", "bill.png", e -> showPanel("Hóa đơn")));
        sidebar.add(createMenuButton("Thống kê", "stats.png", e -> showPanel("Thống kê")));
        sidebar.add(createMenuButton("Nhân viên", "staff.png", e -> showPanel("Nhân viên")));
        sidebar.add(createMenuButton("Khách hàng", "customer.png", e -> showPanel("Khách hàng")));

        sidebar.add(Box.createVerticalGlue());

        JButton btnThoat = createMenuButton("Thoát", "exit.png", e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
        btnThoat.setForeground(Color.RED);
        sidebar.add(btnThoat);

        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Chào mừng đến hệ thống quản trị", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Hiển thị mặc định panel "Sản phẩm"
        showPanel("Nhân viên");

// Tô sáng nút "Sản phẩm"
        for (JButton btn : menuButtons) {
            if (btn.getText().trim().equals("Nhân viên")) {
                setActiveButton(btn);
                break;
            }
        }

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, String iconName, ActionListener action) {
        JButton button = new JButton("  " + text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIcon(loadIcon(iconName));
        button.setPreferredSize(new Dimension(Short.MAX_VALUE, 40));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.DARK_GRAY);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

        // === Lưu vào danh sách để tô sáng nút đang chọn ===
        menuButtons.add(button);

        // Xử lý chọn
        button.addActionListener(e -> {
            setActiveButton(button);     // Tô sáng nút đang được chọn
            action.actionPerformed(e);  // Gọi hành động gốc
        });

        // Hover effect (nền hover chỉ nếu chưa được chọn)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(200, 230, 255))) {
                    button.setBackground(new Color(230, 230, 230));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(200, 230, 255))) {
                    button.setBackground(Color.WHITE);
                }
            }
        });

        return button;
    }

    private void setActiveButton(JButton activeButton) {
        for (JButton btn : menuButtons) {
            if (btn == activeButton) {
                btn.setBackground(new Color(200, 230, 255)); // nền xanh nhạt khi được chọn
            } else {
                btn.setBackground(Color.WHITE);              // reset về trắng
            }
        }
    }

    private ImageIcon loadIcon(String name) {
        try {
            return new ImageIcon(getClass().getResource("/icons/" + name));
        } catch (Exception e) {
            return null;
        }
    }

    private void showPanel(String name) {
        contentPanel.removeAll();
        JPanel panel = switch (name) {
            case "Tài khoản" ->
                new ThongTinTaiKhoanPanel(nd, this);
            case "Bán hàng" ->
                new BanHangPanel(nd);
            case "Sản phẩm" ->
                new SanPhamPanel();
            case "Kho" ->
                new KhoPanel();
            case "Hóa đơn" ->
                new HoaDonKhuyenMaiPanel();
            case "Thống kê" ->
                new ThongKePanel();
            case "Nhân viên" ->
                new NhanVienPanel();
            case "Khách hàng" ->
                new KhachHangPanel();
            default ->
                new JPanel();
        };
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void capNhatTenNguoiDungTrenMenu() {
        lblUser.setText("Xin chào, " + nd.getHoTen());
    }
}
