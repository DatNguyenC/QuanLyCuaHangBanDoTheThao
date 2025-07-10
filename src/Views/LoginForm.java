package Views;

import DAO.NguoiDungDAO;
import Models.NguoiDung;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    // Đăng nhập
    private final JTextField txtTenDangNhap;
    private final JPasswordField txtMatKhau;

    // Đăng ký
    private final JTextField txtHoTen;
    private final JTextField txtSoDienThoai;
    private final JTextField txtDiaChi;
    private final JTextField txtDangKyTenDangNhap;
    private final JPasswordField txtDangKyMatKhau;
    private final JComboBox<String> cmbVaiTro;

    public LoginForm() {
        setTitle("Bán đồ thể thao");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Gradient nền
        keeptoo.KGradientPanel gradientPanel = new keeptoo.KGradientPanel();
        gradientPanel.setkStartColor(new Color(102, 204, 255));  // xanh nhạt
        gradientPanel.setkEndColor(new Color(0, 51, 102));       // xanh đậm
        gradientPanel.setLayout(new GridBagLayout()); // dùng để căn giữa

        // Khung chứa nội dung
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(450, 350));
        contentPanel.setBackground(new Color(255, 255, 255, 230)); // nền trắng trong suốt nhẹ
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // ==== Tab Đăng nhập ====
        JPanel loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(255, 255, 255, 0));

        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setBounds(50, 30, 120, 25);
        loginPanel.add(lblTenDangNhap);

        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setBounds(180, 30, 200, 25);
        loginPanel.add(txtTenDangNhap);

        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setBounds(50, 70, 120, 25);
        loginPanel.add(lblMatKhau);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setBounds(180, 70, 200, 25);
        loginPanel.add(txtMatKhau);

        JButton btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBounds(180, 110, 200, 30);
        btnDangNhap.setBackground(new Color(51, 153, 255));
        btnDangNhap.setForeground(Color.WHITE);
        loginPanel.add(btnDangNhap);

        tabbedPane.addTab("Đăng nhập", loginPanel);

        //btn dang nhap su kien
        btnDangNhap.addActionListener(e -> {
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

                // Phân quyền
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

        // ==== Tab Đăng ký ====
        JPanel registerPanel = new JPanel(null);
        registerPanel.setBackground(new Color(255, 255, 255, 0));

        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setBounds(50, 20, 120, 25);
        registerPanel.add(lblHoTen);

        txtHoTen = new JTextField();
        txtHoTen.setBounds(180, 20, 200, 25);
        registerPanel.add(txtHoTen);

        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setBounds(50, 60, 120, 25);
        registerPanel.add(lblSDT);

        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setBounds(180, 60, 200, 25);
        registerPanel.add(txtSoDienThoai);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(50, 100, 120, 25);
        registerPanel.add(lblDiaChi);

        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(180, 100, 200, 25);
        registerPanel.add(txtDiaChi);

        JLabel lblDangKyTenDangNhap = new JLabel("Tên đăng nhập:");
        lblDangKyTenDangNhap.setBounds(50, 140, 120, 25);
        registerPanel.add(lblDangKyTenDangNhap);

        txtDangKyTenDangNhap = new JTextField();
        txtDangKyTenDangNhap.setBounds(180, 140, 200, 25);
        registerPanel.add(txtDangKyTenDangNhap);

        JLabel lblDangKyMatKhau = new JLabel("Mật khẩu:");
        lblDangKyMatKhau.setBounds(50, 180, 120, 25);
        registerPanel.add(lblDangKyMatKhau);

        txtDangKyMatKhau = new JPasswordField();
        txtDangKyMatKhau.setBounds(180, 180, 200, 25);
        registerPanel.add(txtDangKyMatKhau);

        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setBounds(50, 220, 120, 25);
        registerPanel.add(lblVaiTro);

        cmbVaiTro = new JComboBox<>(new String[]{"User", "Admin"});
        cmbVaiTro.setBounds(180, 220, 200, 25);
        registerPanel.add(cmbVaiTro);

        JButton btnDangKy = new JButton("Tạo tài khoản");
        btnDangKy.setBounds(180, 260, 200, 30);
        btnDangKy.setBackground(new Color(0, 153, 102));
        btnDangKy.setForeground(Color.WHITE);
        registerPanel.add(btnDangKy);

        tabbedPane.addTab("Đăng ký", registerPanel);

        //su kien tao tai khoan
        btnDangKy.addActionListener(e -> {
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
                // Reset form hoặc chuyển tab
                txtHoTen.setText("");
                txtSoDienThoai.setText("");
                txtDiaChi.setText("");
                txtDangKyTenDangNhap.setText("");
                txtDangKyMatKhau.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại (có thể trùng tên đăng nhập)!");
            }
        });

        // Thêm tabs vào khung trắng giữa
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        // Thêm vào gradient panel (căn giữa)
        gradientPanel.add(contentPanel);

        // Thêm vào JFrame
        setContentPane(gradientPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
