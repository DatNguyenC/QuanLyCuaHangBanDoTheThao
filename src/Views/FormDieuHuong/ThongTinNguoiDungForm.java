package Views.FormDieuHuong;

import DAO.NguoiDungDAO;
import Models.NguoiDung;
import Views.UIHelper.FormUIHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class ThongTinNguoiDungForm extends JFrame {
    
    private final JTextField txtHoTen;
    private final JTextField txtSoDT;
    private final JTextField txtDiaChi;
    private final JTextField txtVaiTro;
    private final JLabel lblAnh;
    private final JButton btnChonAnh;
    private final JButton btnChinhSua;
    
    private final NguoiDung nguoiDung;
    
    public ThongTinNguoiDungForm(NguoiDung nd) {
        this.nguoiDung = nd;
        setTitle("Thông tin người dùng");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 248, 255)); // Giữ màu nền hiện tại
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Thông tin người dùng", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(164, 207, 255));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        
        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(100, 100));
        lblAnh.setMaximumSize(new Dimension(100, 100));
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        hienThiAnh(lblAnh, nguoiDung.getAnhDaiDien());
        
        btnChonAnh = FormUIHelper.createRoundedButton("Chọn ảnh");
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.addActionListener(e -> chonAnh());
        btnChonAnh.setBackground(new Color(75, 160, 255));
        
        panel.add(lblAnh);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnChonAnh);
        panel.add(Box.createVerticalStrut(20));
        
        txtHoTen = addField(panel, "Họ tên:", nd.getHoTen());
        txtSoDT = addField(panel, "Số điện thoại:", nd.getSoDienThoai());
        txtDiaChi = addField(panel, "Địa chỉ:", nd.getDiaChi());
        txtVaiTro = addField(panel, "Vai trò:", nd.getVaiTro());
        txtVaiTro.setEditable(false);
        
        btnChinhSua = FormUIHelper.createRoundedButton("Lưu");
        btnChinhSua.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChinhSua.addActionListener(e -> handleLuu());
        btnChinhSua.setBackground(new Color(75, 160, 255));
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnChinhSua);
        
        JButton btnDoiMatKhau = FormUIHelper.createRoundedButton("Đổi mật khẩu");
        btnDoiMatKhau.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDoiMatKhau.addActionListener(e -> moFormDoiMatKhau());
        btnDoiMatKhau.setBackground(new Color(75, 160, 255));
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnDoiMatKhau);
        
        setContentPane(panel);
    }
    
    private JTextField addField(JPanel panel, String labelText, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setMaximumSize(new Dimension(400, 45));
        row.setBackground(new Color(240, 248, 255)); // Giữ màu nền hiện tại

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 30));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(75, 160, 255));
        
        JTextField textField = new JTextField(value);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10)); // Loại bỏ viền mặc định

        JPanel roundedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255)); // Màu nền trắng
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Bo góc 15px
                g2.setColor(new Color(164, 207, 255)); // Viền màu xanh nhạt
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        roundedPanel.setOpaque(false);
        roundedPanel.setPreferredSize(new Dimension(250, 35));
        roundedPanel.add(textField, BorderLayout.CENTER);
        
        row.add(label, BorderLayout.WEST);
        row.add(roundedPanel, BorderLayout.CENTER);
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(10));
        
        return textField;
    }
    
    private void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String extension = getFileExtension(selectedFile.getName());
            String fileName = UUID.randomUUID().toString() + "." + extension;
            Path dest = Paths.get("src/icons/anhDaiDien/" + fileName);
            try {
                Files.createDirectories(dest.getParent());
                Files.copy(selectedFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                nguoiDung.setAnhDaiDien(fileName);
                new NguoiDungDAO().capNhatAnhDaiDien(nguoiDung.getMaNguoiDung(), fileName);
                hienThiAnh(lblAnh, fileName);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Không thể lưu ảnh");
            }
        }
    }
    
    private void hienThiAnh(JLabel label, String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            String path = "src/icons/anhDaiDien/" + fileName;
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        } else {
            label.setIcon(null);
        }
    }
    
    private void handleLuu() {
        nguoiDung.setHoTen(txtHoTen.getText());
        nguoiDung.setSoDienThoai(txtSoDT.getText());
        nguoiDung.setDiaChi(txtDiaChi.getText());
        
        boolean ok = new NguoiDungDAO().capNhatThongTin(nguoiDung);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
    
    private void moFormDoiMatKhau() {
        JDialog dialog = new JDialog(this, "Đổi mật khẩu", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.getContentPane().setBackground(new Color(240, 248, 255));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Căn giữa các dòng
        JPanel pnMKCu = taoDongPassword("Mật khẩu cũ:", new JPasswordField());
        JPanel pnMKMoi = taoDongPassword("Mật khẩu mới:", new JPasswordField());
        JPanel pnXacNhan = taoDongPassword("Xác nhận:", new JPasswordField());

        // Lưu các field để sử dụng
        JPasswordField txtMKCu = (JPasswordField) pnMKCu.getClientProperty("field");
        JPasswordField txtMKMoi = (JPasswordField) pnMKMoi.getClientProperty("field");
        JPasswordField txtXN = (JPasswordField) pnXacNhan.getClientProperty("field");
        
        JButton btnXacNhan = FormUIHelper.createRoundedButton("Xác nhận");
        btnXacNhan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnXacNhan.setBackground(new Color(75, 160, 255));
        btnXacNhan.addActionListener(e -> {
            String mkCu = new String(txtMKCu.getPassword());
            String mkMoi = new String(txtMKMoi.getPassword());
            String xacNhan = new String(txtXN.getPassword());
            
            if (!nguoiDung.getMatKhau().equals(mkCu)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu cũ không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (!mkMoi.equals(xacNhan)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu mới không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean ok = new NguoiDungDAO().capNhatMatKhau(nguoiDung.getMaNguoiDung(), mkMoi);
                if (ok) {
                    nguoiDung.setMatKhau(mkMoi);
                    JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        dialog.add(pnMKCu);
        dialog.add(pnMKMoi);
        dialog.add(pnXacNhan);
        dialog.add(Box.createVerticalStrut(10));
        dialog.add(btnXacNhan);
        
        dialog.setVisible(true);
    }
    
    private JPanel taoDongPassword(String labelText, JPasswordField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        row.setBackground(new Color(240, 248, 255));
        row.setMaximumSize(new Dimension(300, 40));
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 30));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(75, 160, 255));
        
        field.setPreferredSize(new Dimension(160, 30));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8)); // Bỏ viền mặc định
        field.setBackground(new Color(255, 255, 255));
        field.setCaretColor(new Color(75, 160, 255));
        
        JPanel roundedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255)); // Màu nền trắng
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Bo góc 15px
                g2.setColor(new Color(164, 207, 255)); // Viền màu xanh nhạt
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        roundedPanel.setOpaque(false);
        roundedPanel.add(field, BorderLayout.CENTER);
        
        row.add(label);
        row.add(roundedPanel);
        row.putClientProperty("field", field);
        
        return row;
    }
}
