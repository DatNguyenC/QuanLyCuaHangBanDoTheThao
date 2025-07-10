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
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class NhanVienPanel extends JPanel {

    private JTextField txtMaND, txtTenDangNhap, txtMatKhau, txtHoTen, txtSoDienThoai, txtDiaChi;
    private JTable table;
    private DefaultTableModel tableModel;
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private JLabel lblAnh;
    private String tenAnh = "";

    public NhanVienPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // ==== FORM THÔNG TIN NHÂN VIÊN ====
        JPanel formPanel = FormUIHelper.createTitledPanel("THÔNG TIN NHÂN VIÊN");
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(420, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaND = FormUIHelper.createStyledTextField();
        txtMaND.setEditable(false);
        FormUIHelper.addFormRow(formPanel, gbc, 0, "Mã người dùng:", txtMaND);

        txtTenDangNhap = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 1, "Tên đăng nhập:", txtTenDangNhap);

        txtMatKhau = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 2, "Mật khẩu:", txtMatKhau);

        txtHoTen = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 3, "Họ tên:", txtHoTen);

        txtSoDienThoai = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 4, "Số điện thoại:", txtSoDienThoai);

        txtDiaChi = FormUIHelper.createStyledTextField();
        FormUIHelper.addFormRow(formPanel, gbc, 5, "Địa chỉ:", txtDiaChi);

        lblAnh = new JLabel("Ảnh", JLabel.CENTER);
        lblAnh.setPreferredSize(new Dimension(120, 140));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        FormUIHelper.addFormRow(formPanel, gbc, 6, "Ảnh đại diện:", lblAnh);

        JButton btnChonAnh = FormUIHelper.createStyledButton("Chọn ảnh", new Color(100, 149, 237));
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(btnChonAnh, gbc);

        contentPanel.add(formPanel, BorderLayout.WEST);

        // ==== DANH SÁCH + LỌC ====
        JPanel centerPanel = new JPanel(new BorderLayout());

        // ==== LỌC NHÂN VIÊN ====
        JPanel locPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locPanel.setBackground(Color.WHITE);
        locPanel.setBorder(BorderFactory.createTitledBorder("Lọc nhân viên"));

        JTextField txtLocMa = FormUIHelper.createStyledTextField(10);
        JTextField txtLocTenDangNhap = FormUIHelper.createStyledTextField(10);
        JButton btnLoc = FormUIHelper.createStyledButton("Lọc", new Color(100, 149, 237));

        locPanel.add(new JLabel("Mã ND:"));
        locPanel.add(txtLocMa);
        locPanel.add(new JLabel("Tên đăng nhập:"));
        locPanel.add(txtLocTenDangNhap);
        locPanel.add(btnLoc);


        // ==== DANH SÁCH ====
        JPanel tablePanel = FormUIHelper.createTitledPanel("DANH SÁCH NHÂN VIÊN");
        tablePanel.setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Mã", "Tên đăng nhập", "Họ tên", "SĐT", "Địa chỉ", "Ảnh"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(locPanel, BorderLayout.SOUTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // ==== NÚT CHỨC NĂNG ====
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createTitledBorder("CHỨC NĂNG"));
        btnPanel.setBackground(Color.WHITE);

        JButton btnThem = FormUIHelper.createStyledButton("Thêm", new Color(60, 179, 113));
        JButton btnSua = FormUIHelper.createStyledButton("Sửa", new Color(255, 165, 0));
        JButton btnXoa = FormUIHelper.createStyledButton("Xóa", new Color(220, 53, 69));
        JButton btnClear = FormUIHelper.createStyledButton("Làm mới", new Color(100, 149, 237));

        for (JButton btn : new JButton[]{btnThem, btnSua, btnXoa, btnClear}) {
            btn.setMaximumSize(new Dimension(150, 40));
            btnPanel.add(Box.createVerticalStrut(10));
            btnPanel.add(btn);
        }

        contentPanel.add(btnPanel, BorderLayout.EAST);

        // ==== SỰ KIỆN ====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtMaND.setText(tableModel.getValueAt(row, 0).toString());
                txtTenDangNhap.setText(tableModel.getValueAt(row, 1).toString());
                txtHoTen.setText(tableModel.getValueAt(row, 2).toString());
                txtSoDienThoai.setText(tableModel.getValueAt(row, 3).toString());
                txtDiaChi.setText(tableModel.getValueAt(row, 4).toString());
                tenAnh = tableModel.getValueAt(row, 5).toString();
                hienThiAnh(tenAnh);
            }
        });

        btnThem.addActionListener(e -> {
            if (txtTenDangNhap.getText().isEmpty() || txtMatKhau.getText().isEmpty()) {
                FormUIHelper.showErrorMessage("Vui lòng nhập tên đăng nhập và mật khẩu.");
                return;
            }
            NguoiDung nd = new NguoiDung();
            nd.setTenDangNhap(txtTenDangNhap.getText());
            nd.setMatKhau(txtMatKhau.getText());
            nd.setHoTen(txtHoTen.getText());
            nd.setSoDienThoai(txtSoDienThoai.getText());
            nd.setDiaChi(txtDiaChi.getText());
            nd.setVaiTro("Admin");
            nd.setAnhDaiDien(tenAnh);

            if (nguoiDungDAO.taoTaiKhoan(nd)) {
                FormUIHelper.showSuccessMessage("Thêm tài khoản thành công.");
                loadTableData();
                clearForm();
            } else {
                FormUIHelper.showErrorMessage("Thêm tài khoản thất bại.");
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaND.getText().isEmpty()) {
                FormUIHelper.showErrorMessage("Chọn nhân viên để sửa.");
                return;
            }
            NguoiDung nd = new NguoiDung();
            nd.setMaNguoiDung(Integer.parseInt(txtMaND.getText()));
            nd.setHoTen(txtHoTen.getText());
            nd.setSoDienThoai(txtSoDienThoai.getText());
            nd.setDiaChi(txtDiaChi.getText());
            nd.setAnhDaiDien(tenAnh);

            if (nguoiDungDAO.capNhatThongTin(nd)) {
                FormUIHelper.showSuccessMessage("Cập nhật thành công.");
                loadTableData();
            } else {
                FormUIHelper.showErrorMessage("Cập nhật thất bại.");
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaND.getText().isEmpty()) {
                FormUIHelper.showErrorMessage("Chọn nhân viên để xóa.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int maND = Integer.parseInt(txtMaND.getText());
                if (nguoiDungDAO.xoaNguoiDung(maND)) {
                    FormUIHelper.showSuccessMessage("Xóa thành công.");
                    loadTableData();
                    clearForm();
                } else {
                    FormUIHelper.showErrorMessage("Xóa thất bại.");
                }
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
                    FormUIHelper.showErrorMessage("Lỗi khi sao chép ảnh: " + ex.getMessage());
                }
            }
        });

        btnLoc.addActionListener(e -> {
            String maText = txtLocMa.getText().trim();
            String tenDangNhap = txtLocTenDangNhap.getText().trim();

            tableModel.setRowCount(0);
            for (NguoiDung nd : nguoiDungDAO.getAllNhanVien()) {
                boolean match = true;

                if (!maText.isEmpty()) {
                    try {
                        int maFilter = Integer.parseInt(maText);
                        match = match && nd.getMaNguoiDung() == maFilter;
                    } catch (NumberFormatException ex) {
                        FormUIHelper.showErrorMessage("Mã người dùng phải là số.");
                        return;
                    }
                }

                if (!tenDangNhap.isEmpty()) {
                    match = match && nd.getTenDangNhap().toLowerCase().contains(tenDangNhap.toLowerCase());
                }

                if (match) {
                    tableModel.addRow(new Object[]{
                        nd.getMaNguoiDung(),
                        nd.getTenDangNhap(),
                        nd.getHoTen(),
                        nd.getSoDienThoai(),
                        nd.getDiaChi(),
                        nd.getAnhDaiDien() == null ? "" : nd.getAnhDaiDien()
                    });
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<NguoiDung> list = nguoiDungDAO.getAllNhanVien();
        for (NguoiDung nd : list) {
            tableModel.addRow(new Object[]{
                nd.getMaNguoiDung(),
                nd.getTenDangNhap(),
                nd.getHoTen(),
                nd.getSoDienThoai(),
                nd.getDiaChi(),
                nd.getAnhDaiDien() == null ? "" : nd.getAnhDaiDien()
            });
        }
    }

    private void clearForm() {
        txtMaND.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        txtHoTen.setText("");
        txtSoDienThoai.setText("");
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
