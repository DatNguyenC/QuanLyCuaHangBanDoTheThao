package Views;

import DAO.NguoiDungDAO;
import Models.NguoiDung;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private final JTextField txtTenDangNhap;
    private final JPasswordField txtMatKhau;
    private final JTextField txtHoTen;
    private final JTextField txtSoDienThoai;
    private final JTextField txtDiaChi;
    private final JTextField txtDangKyTenDangNhap;
    private final JPasswordField txtDangKyMatKhau;
    private final JComboBox<String> cmbVaiTro;
    private JPanel currentPanel;
    private final JPanel loginPanel;
    private final JPanel registerPanel;
    private JPanel contentPanel;

    public LoginForm() {
        setTitle("Bán đồ thể thao");
        ImageIcon icon = new ImageIcon("src/icons/uneti.png");
        Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(scaledImage);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel authPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(102, 204, 255);
                Color color2 = new Color(0, 51, 102);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        authPanel.setPreferredSize(new Dimension(400, 600));
        authPanel.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgImage = new ImageIcon("src/icons/anhdaidien/background.jpg");
                Image image = bgImage.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setPreferredSize(new Dimension(600, 600));

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(350, 400));
        contentPanel.setBackground(new Color(195, 236, 255));
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(255, 255, 255, 0));

        JLabel lblWelcome = new JLabel("Chào mừng trở lại");
        lblWelcome.setBounds(45, 20, 200, 30);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(lblWelcome);

        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập");
        lblTenDangNhap.setBounds(45, 50, 280, 20);
        loginPanel.add(lblTenDangNhap);

        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setBounds(45, 70, 280, 30);
        loginPanel.add(txtTenDangNhap);

        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setBounds(45, 110, 280, 30);
        loginPanel.add(lblMatKhau);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setBounds(45, 140, 280, 30);
        loginPanel.add(txtMatKhau);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(45, 180, 280, 35);
        btnLogin.setBackground(new Color(255, 137, 57)); // xanh dương đậm
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        loginPanel.add(btnLogin);

        JLabel lblRegister = new JLabel("<html><a href='#'>Chưa có tài khoản? Đăng ký</a></html>");
        lblRegister.setBounds(45, 230, 280, 30);
        lblRegister.setForeground(Color.WHITE);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginPanel.add(lblRegister);

        registerPanel = new JPanel(null);
        registerPanel.setBackground(new Color(255, 255, 255, 0));

        JLabel lblRegisterTitle = new JLabel("Tạo tài khoản");
        lblRegisterTitle.setBounds(45, 20, 200, 30);
        lblRegisterTitle.setFont(new Font("Arial", Font.BOLD, 20));
        registerPanel.add(lblRegisterTitle);

        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setBounds(45, 70, 120, 25);
        registerPanel.add(lblHoTen);

        txtHoTen = new JTextField();
        txtHoTen.setBounds(165, 70, 160, 30);
        registerPanel.add(txtHoTen);

        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setBounds(45, 110, 120, 25);
        registerPanel.add(lblSDT);

        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setBounds(165, 110, 160, 30);
        registerPanel.add(txtSoDienThoai);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(45, 150, 120, 25);
        registerPanel.add(lblDiaChi);

        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(165, 150, 160, 30);
        registerPanel.add(txtDiaChi);

        JLabel lblDangKyTenDangNhap = new JLabel("Tên đăng nhập:");
        lblDangKyTenDangNhap.setBounds(45, 190, 120, 25);
        registerPanel.add(lblDangKyTenDangNhap);

        txtDangKyTenDangNhap = new JTextField();
        txtDangKyTenDangNhap.setBounds(165, 190, 160, 30);
        registerPanel.add(txtDangKyTenDangNhap);

        JLabel lblDangKyMatKhau = new JLabel("Mật khẩu:");
        lblDangKyMatKhau.setBounds(45, 230, 120, 25);
        registerPanel.add(lblDangKyMatKhau);

        txtDangKyMatKhau = new JPasswordField();
        txtDangKyMatKhau.setBounds(165, 230, 160, 30);
        registerPanel.add(txtDangKyMatKhau);

        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setBounds(45, 270, 120, 25);
        registerPanel.add(lblVaiTro);

        cmbVaiTro = new JComboBox<>(new String[]{"User", "Admin"});
        cmbVaiTro.setBounds(165, 270, 160, 30);
        registerPanel.add(cmbVaiTro);

        JButton btnRegister = new JButton("Tạo tài khoản");
        btnRegister.setBounds(45, 320, 280, 35);
        btnRegister.setBackground(new Color(255, 137, 57));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        registerPanel.add(btnRegister);

        JLabel lblBackToLogin = new JLabel("<html><a href='#'>Quay lại đăng nhập</a></html>");
        lblBackToLogin.setBounds(45, 370, 280, 30);
        lblBackToLogin.setForeground(Color.BLUE);
        lblBackToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerPanel.add(lblBackToLogin);

        // Action listeners
        btnLogin.addActionListener(e -> {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword());

            if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            NguoiDungDAO dao = new NguoiDungDAO();
            NguoiDung nd = dao.dangNhap(tenDangNhap, matKhau);

            if (nd != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                if ("Admin".equalsIgnoreCase(nd.getVaiTro())) {
                    new AdminForm(nd).setVisible(true);
                } else {
                    new UserForm(nd).setVisible(true);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu");
            }
        });

        btnRegister.addActionListener(e -> {
            String hoTen = txtHoTen.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String tenDN = txtDangKyTenDangNhap.getText().trim();
            String matKhau = new String(txtDangKyMatKhau.getPassword()).trim();
            String vaiTro = (String) cmbVaiTro.getSelectedItem();

            if (hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || tenDN.isEmpty() || matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            NguoiDung nd = new NguoiDung();
            nd.setHoTen(hoTen);
            nd.setSoDienThoai(sdt);
            nd.setDiaChi(diaChi);
            nd.setTenDangNhap(tenDN);
            nd.setMatKhau(matKhau);
            nd.setVaiTro(vaiTro);

            NguoiDungDAO dao = new NguoiDungDAO();
            if (dao.taoTaiKhoan(nd)) {
                JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công!");
                txtHoTen.setText("");
                txtSoDienThoai.setText("");
                txtDiaChi.setText("");
                txtDangKyTenDangNhap.setText("");
                txtDangKyMatKhau.setText("");
                showPanel(loginPanel);
            } else {
                JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại (có thể trùng tên đăng nhập)!");
            }
        });

        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel(registerPanel);
            }
        });

        lblBackToLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel(loginPanel);
            }
        });

        currentPanel = loginPanel;
        contentPanel.add(currentPanel, BorderLayout.CENTER);

        authPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(authPanel, BorderLayout.WEST);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void showPanel(JPanel panel) {
        contentPanel.remove(currentPanel);
        currentPanel = panel;
        contentPanel.add(currentPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
