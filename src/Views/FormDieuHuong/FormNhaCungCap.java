package Views.FormDieuHuong;

import DAO.NhaCungCapDAO;
import Models.NhaCungCap;

import static Views.UIHelper.FormUIHelper.showErrorMessage;
import static Views.UIHelper.FormUIHelper.showSuccessMessage;
import static Views.UIHelper.FormUIHelper.createStyledButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.border.TitledBorder;

public class FormNhaCungCap extends JFrame {

    private JTable tblNCC;
    private DefaultTableModel model;
    private JTextField txtMaLoc, txtTenSdtLoc, txtTen, txtDiaChi, txtSDT, txtEmail;
    private NhaCungCapDAO dao = new NhaCungCapDAO();
    Color titleColor = new Color(70, 130, 180);

    public FormNhaCungCap() {
        setTitle("Quản lý Nhà Cung Cấp");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // ===== NORTH: Thông tin + Chức năng =====
        JPanel topPanel = new JPanel(new BorderLayout(10, 0)); // bên trái: form, phải: chức năng

        // Form thông tin NCC
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        TitledBorder TtnccBorder = BorderFactory.createTitledBorder("THÔNG TIN NHÀ CUNG CẤP");
        TtnccBorder.setTitleColor(titleColor);
        formPanel.setBorder(TtnccBorder);
        txtTen = new JTextField();
        txtDiaChi = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();

        formPanel.add(new JLabel("Tên NCC:"));
        formPanel.add(txtTen);
        formPanel.add(new JLabel("Địa chỉ:"));
        formPanel.add(txtDiaChi);
        formPanel.add(new JLabel("Số điện thoại:"));
        formPanel.add(txtSDT);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        // Chức năng
        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 5, 10));
        TitledBorder CnBorder = BorderFactory.createTitledBorder("CHỨC NĂNG");
        CnBorder.setTitleColor(titleColor);
        actionPanel.setBorder(CnBorder);
        JButton btnThem = createStyledButton("Thêm", new Color(70, 130, 180));
        JButton btnSua = createStyledButton("Sửa", new Color(70, 130, 180));
        JButton btnXoa = createStyledButton("Xóa", new Color(70, 130, 180));

        actionPanel.add(btnThem);
        actionPanel.add(btnSua);
        actionPanel.add(btnXoa);

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: Danh sách NCC =====
        model = new DefaultTableModel(new String[]{"Mã NCC", "Tên NCC", "Địa chỉ", "SĐT", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNCC = new JTable(model);
        tblNCC.setRowHeight(25);
        tblNCC.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tblNCC);
        TitledBorder DsNccBorder = BorderFactory.createTitledBorder("DANH SÁCH NHÀ CUNG CẤP");
        DsNccBorder.setTitleColor(titleColor);
        scrollPane.setBorder(DsNccBorder);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== SOUTH: Lọc =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("LỌC NHÀ CUNG CẤP"));
        TitledBorder LnccBorder = BorderFactory.createTitledBorder("LỌC NHÀ CUNG CẤP");
        LnccBorder.setTitleColor(titleColor);
        filterPanel.setBorder(LnccBorder);

        txtMaLoc = new JTextField(5);
        txtTenSdtLoc = new JTextField(10);
        JComboBox<String> cmbDieuKien = new JComboBox<>(new String[]{"=", ">", "<"});
        JButton btnLoc = createStyledButton("Lọc", new Color(70, 130, 180));

        filterPanel.add(new JLabel("Mã NCC:"));
        filterPanel.add(cmbDieuKien);
        filterPanel.add(txtMaLoc);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(new JLabel("Tên/SĐT:"));
        filterPanel.add(txtTenSdtLoc);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(btnLoc);

        mainPanel.add(filterPanel, BorderLayout.SOUTH);

        // ===== LOAD & EVENT như cũ =====
        loadData("");
        // Sự kiện Thêm
        btnThem.addActionListener(e -> {
            String ten = txtTen.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();

            if (ten.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                showErrorMessage("Vui lòng nhập đầy đủ thông tin bắt buộc (Tên, Địa chỉ, SĐT)");
                return;
            }

            NhaCungCap ncc = new NhaCungCap();
            ncc.setTenNCC(ten);
            ncc.setDiaChi(diaChi);
            ncc.setSoDienThoai(sdt);
            ncc.setEmail(email);

            if (dao.themNhaCungCap(ncc)) {
                showSuccessMessage("Thêm nhà cung cấp thành công!");
                loadData("");
                clearForm();
            } else {
                showErrorMessage("Thêm thất bại!");
            }
        });

        // Sự kiện Sửa
        btnSua.addActionListener(e -> {
            int selectedRow = tblNCC.getSelectedRow();
            if (selectedRow == -1) {
                showErrorMessage("Vui lòng chọn nhà cung cấp cần sửa.");
                return;
            }

            int maNCC = (int) model.getValueAt(selectedRow, 0);
            String ten = txtTen.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();

            if (ten.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                showErrorMessage("Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            NhaCungCap ncc = new NhaCungCap(maNCC, ten, diaChi, sdt, email);
            if (dao.suaNhaCungCap(ncc)) {
                showSuccessMessage("Cập nhật thành công!");
                loadData("");
                clearForm();
            } else {
                showErrorMessage("Cập nhật thất bại!");
            }
        });

        // Sự kiện Xóa
        btnXoa.addActionListener(e -> {
            int selectedRow = tblNCC.getSelectedRow();
            if (selectedRow == -1) {
                showErrorMessage("Vui lòng chọn nhà cung cấp cần xóa.");
                return;
            }

            int maNCC = (int) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa nhà cung cấp này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.xoaNhaCungCap(maNCC)) {
                    showSuccessMessage("Xóa thành công!");
                    loadData("");
                    clearForm();
                } else {
                    showErrorMessage("Xóa thất bại! Có thể đang được sử dụng.");
                }
            }
        });

        // Sự kiện click bảng để đưa dữ liệu lên form
        tblNCC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNCC.getSelectedRow();
                if (row >= 0) {
                    txtTen.setText(model.getValueAt(row, 1).toString());
                    txtDiaChi.setText(model.getValueAt(row, 2).toString());
                    txtSDT.setText(model.getValueAt(row, 3).toString());
                    txtEmail.setText(model.getValueAt(row, 4).toString());
                }
            }
        });

        // Sự kiện Lọc
        btnLoc.addActionListener(e -> {
            String ma = txtMaLoc.getText().trim();
            String keyword = txtTenSdtLoc.getText().trim().toLowerCase();
            String condition = cmbDieuKien.getSelectedItem().toString();
            loadDataWithCondition(ma, condition, keyword);
        });

    }

    private void loadData(String ma) {
        model.setRowCount(0);
        List<NhaCungCap> list = dao.layDanhSach();
        for (NhaCungCap ncc : list) {
            if (ma.isEmpty() || String.valueOf(ncc.getMaNCC()).contains(ma)) {
                model.addRow(new Object[]{
                    ncc.getMaNCC(), ncc.getTenNCC(), ncc.getDiaChi(), ncc.getSoDienThoai(), ncc.getEmail()
                });
            }
        }
    }

    private void clearForm() {
        txtTen.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        tblNCC.clearSelection();
    }

    private void loadDataWithCondition(String ma, String condition, String keyword) {
        model.setRowCount(0);
        List<NhaCungCap> list = dao.layDanhSach();
        for (NhaCungCap ncc : list) {
            boolean matchMa = true;
            boolean matchTenOrSdt = true;

            // Lọc theo mã
            if (!ma.isEmpty()) {
                try {
                    int maInt = Integer.parseInt(ma);
                    int maNCC = ncc.getMaNCC();
                    matchMa = switch (condition) {
                        case ">" ->
                            maNCC > maInt;
                        case "<" ->
                            maNCC < maInt;
                        default ->
                            maNCC == maInt;
                    };
                } catch (NumberFormatException e) {
                    showErrorMessage("Mã NCC phải là số");
                    return;
                }
            }

            // Lọc theo tên hoặc số điện thoại
            if (!keyword.isEmpty()) {
                String ten = ncc.getTenNCC().toLowerCase();
                String sdt = ncc.getSoDienThoai().toLowerCase();
                matchTenOrSdt = ten.contains(keyword) || sdt.contains(keyword);
            }

            if (matchMa && matchTenOrSdt) {
                model.addRow(new Object[]{
                    ncc.getMaNCC(), ncc.getTenNCC(), ncc.getDiaChi(), ncc.getSoDienThoai(), ncc.getEmail()
                });
            }
        }
    }

}
