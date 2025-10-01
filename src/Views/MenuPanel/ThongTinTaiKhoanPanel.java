package Views.MenuPanel;

import Models.NguoiDung;
import Views.AdminForm;
import Views.UIHelper.FormUIHelper;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class ThongTinTaiKhoanPanel extends JPanel {

    private final JTextField txtMaND;
    private final JTextField txtHoTen;
    private final JTextField txtSoDT;
    private final JTextField txtDiaChi;
    private final JTextField txtVaiTro;
    private final JButton btnChinhSua;
    private final JButton btnChonAnh;
    private final JLabel lblAnh;
    private final NguoiDung nguoiDung;
    private final AdminForm parentForm;

    public ThongTinTaiKhoanPanel(NguoiDung nd, AdminForm parentForm) {
        this.nguoiDung = nd;
        this.parentForm = parentForm;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Thông tin tài khoản", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));

        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(100, 100));
        lblAnh.setMaximumSize(new Dimension(100, 100));
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        hienThiAnh(lblAnh, nguoiDung.getAnhDaiDien());

        btnChonAnh = FormUIHelper.createRoundedButton("Chọn ảnh");
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.addActionListener(e -> chonAnh());

        formPanel.add(lblAnh);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(btnChonAnh);
        formPanel.add(Box.createVerticalStrut(15));

        txtMaND = addField(formPanel, "Mã người dùng:", String.valueOf(nd.getMaNguoiDung()));
        txtHoTen = addField(formPanel, "Họ tên:", nd.getHoTen());
        txtSoDT = addField(formPanel, "Số điện thoại:", nd.getSoDienThoai());
        txtDiaChi = addField(formPanel, "Địa chỉ:", nd.getDiaChi());
        txtVaiTro = addField(formPanel, "Vai trò:", nd.getVaiTro());

        setEditable(false);

        btnChinhSua = FormUIHelper.createRoundedButton("Chỉnh sửa");
        btnChinhSua.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChinhSua.setMaximumSize(new Dimension(120, 35));
        btnChinhSua.addActionListener(e -> handleEdit());

        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(btnChinhSua);

        formWrapper.add(formPanel);
        add(formWrapper, BorderLayout.CENTER);
    }

    private JTextField addField(JPanel panel, String labelText, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setMaximumSize(new Dimension(400, 50));
        row.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 30));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField textField = new JTextField(value);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        textField.setBackground(new Color(240, 240, 240));
        textField.setOpaque(false);

        JPanel roundedPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 240, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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

    private void setEditable(boolean editable) {
        txtMaND.setEditable(false);
        txtHoTen.setEditable(editable);
        txtSoDT.setEditable(editable);
        txtDiaChi.setEditable(editable);
        txtVaiTro.setEditable(false);
    }

    private void handleEdit() {
        if (btnChinhSua.getText().equals("Chỉnh sửa")) {
            setEditable(true);
            btnChinhSua.setText("Lưu");
        } else {
            nguoiDung.setHoTen(txtHoTen.getText());
            nguoiDung.setSoDienThoai(txtSoDT.getText());
            nguoiDung.setDiaChi(txtDiaChi.getText());

            boolean thanhCong = new DAO.NguoiDungDAO().capNhatThongTin(nguoiDung);
            if (thanhCong) {
                setEditable(false);
                btnChinhSua.setText("Chỉnh sửa");
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parentForm.capNhatTenNguoiDungTrenMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
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
                new DAO.NguoiDungDAO().capNhatAnhDaiDien(nguoiDung.getMaNguoiDung(), fileName);
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

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }


}
