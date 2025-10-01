package Views.MenuPanel;

import DAO.NguoiDungDAO;
import Models.NguoiDung;
import Views.UIHelper.FormUIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class KhachHangPanel extends JPanel {

    private JTextField txtMaKH, txtHoTen, txtSoDienThoai, txtDiaChi, txtTenDangNhap, txtMatKhau;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblAnh;
    private String tenAnh = "";
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    public KhachHangPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        JPanel formPanel = FormUIHelper.createTitledPanel("THÔNG TIN KHÁCH HÀNG");
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(420, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaKH = FormUIHelper.createStyledTextField();
        txtMaKH.setEditable(false);
        FormUIHelper.addFormRow(formPanel, gbc, 0, "Mã KH:", txtMaKH);

        txtHoTen = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 1, "Họ tên:", txtHoTen);

        txtSoDienThoai = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 2, "SĐT:", txtSoDienThoai);

        txtDiaChi = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 3, "Địa chỉ:", txtDiaChi);

        txtTenDangNhap = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 4, "Tên đăng nhập:", txtTenDangNhap);

        txtMatKhau = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 5, "Mật khẩu:", txtMatKhau);

        lblAnh = new JLabel("Ảnh", JLabel.CENTER);
        lblAnh.setPreferredSize(new Dimension(120, 140));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        FormUIHelper.addFormRow(formPanel, gbc, 6, "Ảnh đại diện:", lblAnh);

        JButton btnChonAnh = FormUIHelper.createStyledButton("Chọn ảnh", new Color(70, 130, 180));
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(btnChonAnh, gbc);

        contentPanel.add(formPanel, BorderLayout.WEST);

        JPanel tablePanel = FormUIHelper.createTitledPanel("DANH SÁCH KHÁCH HÀNG");
        tablePanel.setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Mã", "Họ tên", "SĐT", "Tên ĐN", "Địa chỉ", "Ảnh"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel locPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locPanel.setBackground(Color.WHITE);
        locPanel.setBorder(BorderFactory.createTitledBorder("Lọc khách hàng"));

        JTextField txtLocMa = FormUIHelper.createStyledTextField(10);
        JTextField txtLocTen = FormUIHelper.createStyledTextField(10);
        JButton btnLoc = FormUIHelper.createStyledButton("Lọc", new Color(70, 130, 180));

        locPanel.add(new JLabel("Mã KH:"));
        locPanel.add(txtLocMa);
        locPanel.add(new JLabel("Tên KH:"));
        locPanel.add(txtLocTen);
        locPanel.add(btnLoc);

        tablePanel.add(locPanel, BorderLayout.SOUTH);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createTitledBorder("CHỨC NĂNG"));
        btnPanel.setBackground(Color.WHITE);

        JButton btnThem = FormUIHelper.createStyledButton("Thêm", new Color(70, 130, 180));
        JButton btnSua = FormUIHelper.createStyledButton("Sửa", new Color(70, 130, 180));
        JButton btnXoa = FormUIHelper.createStyledButton("Xóa", new Color(70, 130, 180));
        JButton btnClear = FormUIHelper.createStyledButton("Làm mới", new Color(70, 130, 180));

        for (JButton btn : new JButton[]{btnThem, btnSua, btnXoa, btnClear}) {
            btn.setMaximumSize(new Dimension(150, 40));
            btnPanel.add(Box.createVerticalStrut(10));
            btnPanel.add(btn);
        }

        contentPanel.add(btnPanel, BorderLayout.EAST);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtMaKH.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                txtHoTen.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtSoDienThoai.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtTenDangNhap.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtDiaChi.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                txtMatKhau.setText("");

                Object objAnh = tableModel.getValueAt(row, 5);
                tenAnh = objAnh != null ? objAnh.toString() : "";
                hienThiAnh(tenAnh);

            }
        });

        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("images");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                tenAnh = selectedFile.getName();
                File destDir = new File("src/icons/anhDaiDien");
                destDir.mkdirs();
                File destFile = new File(destDir, tenAnh);
                try {
                    if (!destFile.exists()) {
                        Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    hienThiAnh(tenAnh);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    FormUIHelper.showErrorMessage("Lỗi sao chép ảnh: " + ex.getMessage());
                }
            }
        });

        btnThem.addActionListener(e -> {
            String hoTen = txtHoTen.getText();
            String sdt = txtSoDienThoai.getText();
            String diaChi = txtDiaChi.getText();
            String tenDangNhap = txtTenDangNhap.getText();
            String matKhau = txtMatKhau.getText();

            if (hoTen.isEmpty() || sdt.isEmpty() || tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                FormUIHelper.showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc.");
                return;
            }

            NguoiDung kh = new NguoiDung();
            kh.setHoTen(hoTen);
            kh.setSoDienThoai(sdt);
            kh.setDiaChi(diaChi);
            kh.setTenDangNhap(tenDangNhap);
            kh.setMatKhau(matKhau);
            kh.setVaiTro("User");
            kh.setAnhDaiDien(tenAnh);

            if (nguoiDungDAO.taoTaiKhoan(kh)) {
                FormUIHelper.showSuccessMessage("Thêm khách hàng thành công.");
                loadTableData();
                clearForm();
            } else {
                FormUIHelper.showErrorMessage("Thêm thất bại. Có thể tên đăng nhập đã tồn tại.");
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaKH.getText().isEmpty()) {
                return;
            }

            NguoiDung kh = new NguoiDung();
            kh.setMaNguoiDung(Integer.parseInt(txtMaKH.getText()));
            kh.setHoTen(txtHoTen.getText());
            kh.setSoDienThoai(txtSoDienThoai.getText());
            kh.setDiaChi(txtDiaChi.getText());
            kh.setAnhDaiDien(tenAnh);
            kh.setTenDangNhap(txtTenDangNhap.getText());
            String matKhau = txtMatKhau.getText();

            boolean success;
            if (!matKhau.isEmpty()) {
                kh.setMatKhau(matKhau);
                success = nguoiDungDAO.capNhatThongTinFull(kh);
            } else {
                success = nguoiDungDAO.capNhatThongTin(kh);
            }

            if (success) {
                FormUIHelper.showSuccessMessage("Cập nhật thành công.");
                loadTableData();
            } else {
                FormUIHelper.showErrorMessage("Cập nhật thất bại.");
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaKH.getText().isEmpty()) {
                return;
            }
            int maKH = Integer.parseInt(txtMaKH.getText());
            if (nguoiDungDAO.xoaNguoiDung(maKH)) {
                FormUIHelper.showSuccessMessage("Xoá thành công.");
                loadTableData();
                clearForm();
            } else {
                FormUIHelper.showErrorMessage("Xoá thất bại.");
            }
        });

        btnLoc.addActionListener(e -> {
            tableModel.setRowCount(0);
            String maText = txtLocMa.getText().trim();
            String tenText = txtLocTen.getText().trim().toLowerCase();
            for (NguoiDung kh : nguoiDungDAO.getAllKhachHang()) {
                boolean match = true;
                if (!maText.isEmpty()) {
                    try {
                        int ma = Integer.parseInt(maText);
                        match &= kh.getMaNguoiDung() == ma;
                    } catch (Exception ex) {
                        FormUIHelper.showErrorMessage("Mã KH phải là số.");
                        return;
                    }
                }
                if (!tenText.isEmpty()) {
                    match &= kh.getHoTen().toLowerCase().contains(tenText);
                }
                if (match) {
                    tableModel.addRow(new Object[]{
                        kh.getMaNguoiDung(), kh.getHoTen(), kh.getSoDienThoai(), kh.getTenDangNhap(), kh.getDiaChi(), kh.getAnhDaiDien()
                    });
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (NguoiDung kh : nguoiDungDAO.getAllKhachHang()) {
            tableModel.addRow(new Object[]{
                kh.getMaNguoiDung(), kh.getHoTen(), kh.getSoDienThoai(), kh.getTenDangNhap(), kh.getDiaChi(), kh.getAnhDaiDien()
            });
        }
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtSoDienThoai.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        txtDiaChi.setText("");
        tenAnh = "";
        lblAnh.setIcon(null);
        lblAnh.setText("Ảnh");
    }

    private void hienThiAnh(String tenFile) {
        try {
            ImageIcon icon = new ImageIcon("src/icons/anhDaiDien/" + tenFile);
            Image img = icon.getImage().getScaledInstance(120, 140, Image.SCALE_SMOOTH);
            lblAnh.setIcon(new ImageIcon(img));
            lblAnh.setText(null);
        } catch (Exception e) {
            lblAnh.setIcon(null);
            lblAnh.setText("Ảnh");
        }
    }
}
