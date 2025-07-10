package Views.MenuPanel;

import static Views.UIHelper.FormUIHelper.*;
import Views.FormDieuHuong.FormDanhMuc;
import DAO.ChiTietSanPhamDAO;
import DAO.DanhMucDAO;
import DAO.SanPhamDAO;
import Models.ChiTietSanPham;
import Models.SanPham;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.border.TitledBorder;

public class SanPhamPanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private JComboBox<Integer> cbMaSP;

    public SanPhamPanel() {
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnSanPham = new JButton("Sản phẩm");
        JButton btnChiTiet = new JButton("Chi tiết sản phẩm");

        toolBar.add(btnSanPham);
        toolBar.add(btnChiTiet);

        add(toolBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createSanPhamSubPanel(), "SANPHAM");
        contentPanel.add(createChiTietSubPanel(), "CHITIET");

        add(contentPanel, BorderLayout.CENTER);

        btnSanPham.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "SANPHAM"));
        btnChiTiet.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "CHITIET"));
    }

    private JPanel createSanPhamSubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL (Form + Image + Buttons) =====
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // === FORM PANEL ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "THÔNG TIN SẢN PHẨM",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(450, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaSP = createStyledTextField(25); // Increased columns to 25
        JTextField txtTenSP = createStyledTextField(25); // Increased columns to 25
        JTextField txtMoTa = createStyledTextField(25); // Increased columns to 25
        JTextField txtGia = createStyledTextField(25); // Increased columns to 25
        txtGia.setHorizontalAlignment(JTextField.RIGHT);
        JTextField txtHinhAnh = new JTextField(25); // Increased columns to 25
        txtHinhAnh.setVisible(false);

        // Combobox danh mục + nút '+'
        JPanel danhMucPanel = new JPanel(new BorderLayout(5, 0));
        JComboBox<String> cbDanhMuc = new JComboBox<>(getDanhMucList());
        cbDanhMuc.setPreferredSize(new Dimension(150, 30));
        JButton btnThemDanhMuc = createIconButton("+", new Color(70, 130, 180));
        btnThemDanhMuc.addActionListener(e -> {
            new FormDanhMuc(() -> {
                cbDanhMuc.setModel(new DefaultComboBoxModel<>(getDanhMucList()));
            }).setVisible(true);
        });
        danhMucPanel.add(cbDanhMuc, BorderLayout.CENTER);
        danhMucPanel.add(btnThemDanhMuc, BorderLayout.EAST);

        // Add các dòng vào form
        int row = 0;
        addFormRow(formPanel, gbc, row++, "Mã sản phẩm:", txtMaSP);
        addFormRow(formPanel, gbc, row++, "Tên sản phẩm:", txtTenSP);
        addFormRow(formPanel, gbc, row++, "Mô tả:", txtMoTa);
        addFormRow(formPanel, gbc, row++, "Giá (VND):", txtGia);
        addFormRow(formPanel, gbc, row++, "Mã danh mục:", danhMucPanel);

        // === PANEL ẢNH ===
        JPanel anhPanel = new JPanel();
        anhPanel.setLayout(new BoxLayout(anhPanel, BoxLayout.Y_AXIS));
        anhPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "ẢNH SẢN PHẨM",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        anhPanel.setBackground(Color.WHITE);
        anhPanel.setPreferredSize(new Dimension(180, 250));

        JLabel lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(150, 150));
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.WHITE);

        JButton btnChonAnh = createStyledButton("Chọn ảnh", new Color(70, 130, 180));
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                String fileName = System.currentTimeMillis() + "_" + selected.getName();
                Path dest = Paths.get("src/anhSP/" + fileName);
                try {
                    Files.createDirectories(dest.getParent());
                    Files.copy(selected.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    txtHinhAnh.setText(fileName);
                    hienThiAnh(lblAnh, fileName); // HIỆN ẢNH
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        anhPanel.add(Box.createVerticalStrut(10));
        anhPanel.add(lblAnh);
        anhPanel.add(Box.createVerticalStrut(10));
        anhPanel.add(btnChonAnh);

        // === NÚT CHỨC NĂNG ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "CHỨC NĂNG",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(100, 100));
        JButton btnThem = createStyledButton("Thêm", new Color(34, 139, 34));
        JButton btnSua = createStyledButton("Sửa", new Color(255, 140, 0));
        JButton btnXoa = createStyledButton("Xóa", new Color(220, 20, 60));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);

        JPanel formImagePanel = new JPanel(new BorderLayout(10, 0));
        formImagePanel.add(formPanel, BorderLayout.CENTER);
        formImagePanel.add(anhPanel, BorderLayout.EAST);

        topPanel.add(formImagePanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // === TABLE ===
        String[] columnNames = {"Mã SP", "Tên SP", "Mô tả", "Giá", "Ảnh", "Mã danh mục"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class; // Tất cả cột là String
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
//        table.setRowHeight(100);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "DANH SÁCH SẢN PHẨM",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));

        // === FILTER PANEL ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "LỌC SẢN PHẨM",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        filterPanel.setBackground(Color.WHITE);
        JTextField txtTimMaSP = createStyledTextField(10);
        JComboBox<String> cbLocDM = new JComboBox<>(getDanhMucListWithAll());
        JButton btnLoc = createStyledButton("Lọc", new Color(70, 130, 180));
        filterPanel.add(new JLabel("Mã SP:"));
        filterPanel.add(txtTimMaSP);
        filterPanel.add(new JLabel("Danh mục:"));
        filterPanel.add(cbLocDM);
        filterPanel.add(btnLoc);

        // === LOAD DATA BAN ĐẦU ===
        for (SanPham sp : new SanPhamDAO().layDanhSach()) {
            tableModel.addRow(new Object[]{
                sp.getMaSanPham(), sp.getTenSanPham(), sp.getMoTa(),
                String.format("%,.0f", sp.getGia()), getImageIcon(sp.getHinhAnh()), sp.getMaDanhMuc()
            });
        }

        // === SỰ KIỆN ===
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    txtMaSP.setText(table.getValueAt(row, 0).toString());
                    txtTenSP.setText(table.getValueAt(row, 1).toString());
                    txtMoTa.setText(table.getValueAt(row, 2).toString());
                    txtGia.setText(table.getValueAt(row, 3).toString().replace(",", ""));
                    cbDanhMuc.setSelectedItem(table.getValueAt(row, 5).toString());

                    String file = table.getValueAt(row, 4).toString(); // LẤY TÊN FILE
                    txtHinhAnh.setText(file);
                    hienThiAnh(lblAnh, file); // HIỆN ẢNH
                }
            }
        });

        btnLoc.addActionListener(e -> {
            String ma = txtTimMaSP.getText().trim();
            String dm = cbLocDM.getSelectedItem().toString();
            if (dm.equals("Tất cả")) {
                dm = null;
            }
            tableModel.setRowCount(0);
            for (SanPham sp : new SanPhamDAO().timKiem(ma, dm)) {
                tableModel.addRow(new Object[]{
                    sp.getMaSanPham(), sp.getTenSanPham(), sp.getMoTa(),
                    String.format("%,.0f", sp.getGia()), getImageIcon(sp.getHinhAnh()), sp.getMaDanhMuc()
                });
            }
        });

        btnThem.addActionListener(e -> {
            SanPham sp;
            boolean success;
            if (txtMaSP.getText().trim().isEmpty()) {
                sp = new SanPham(txtTenSP.getText(), txtMoTa.getText(),
                        Double.parseDouble(txtGia.getText()), txtHinhAnh.getText(), cbDanhMuc.getSelectedItem().toString());
                success = new SanPhamDAO().themSanPhamKhongMa(sp);
            } else {
                sp = new SanPham(Integer.parseInt(txtMaSP.getText()), txtTenSP.getText(), txtMoTa.getText(),
                        Double.parseDouble(txtGia.getText()), txtHinhAnh.getText(), cbDanhMuc.getSelectedItem().toString());
                success = new SanPhamDAO().themSanPham(sp);
            }

            if (success) {
                JOptionPane.showMessageDialog(panel, "Thêm thành công!");
                txtMaSP.setText(String.valueOf(sp.getMaSanPham()));
                tableModel.addRow(new Object[]{
                    sp.getMaSanPham(), sp.getTenSanPham(), sp.getMoTa(),
                    String.format("%,.0f", sp.getGia()), getImageIcon(sp.getHinhAnh()), sp.getMaDanhMuc()
                });
                capNhatComboBoxMaSP();
            } else {
                JOptionPane.showMessageDialog(panel, "Thêm thất bại hoặc trùng mã!");
            }
        });

        btnSua.addActionListener(e -> {
            int rowIdx = table.getSelectedRow();
            if (rowIdx == -1) {
                return;
            }
            SanPham sp = new SanPham(Integer.parseInt(txtMaSP.getText()), txtTenSP.getText(), txtMoTa.getText(),
                    Double.parseDouble(txtGia.getText()), txtHinhAnh.getText(), cbDanhMuc.getSelectedItem().toString());

            if (new SanPhamDAO().capNhatSanPham(sp)) {
                JOptionPane.showMessageDialog(panel, "Cập nhật thành công!");
                tableModel.setValueAt(sp.getTenSanPham(), rowIdx, 1);
                tableModel.setValueAt(sp.getMoTa(), rowIdx, 2);
                tableModel.setValueAt(String.format("%,.0f", sp.getGia()), rowIdx, 3);
                tableModel.setValueAt(getImageIcon(sp.getHinhAnh()), rowIdx, 4);
                tableModel.setValueAt(sp.getMaDanhMuc(), rowIdx, 5);
            } else {
                JOptionPane.showMessageDialog(panel, "Cập nhật thất bại!");
            }
        });

        btnXoa.addActionListener(e -> {
            int rowIdx = table.getSelectedRow();
            if (rowIdx == -1) {
                return;
            }
            String ma = txtMaSP.getText();
            if (new SanPhamDAO().xoaSanPham(ma)) {
                JOptionPane.showMessageDialog(panel, "Xóa thành công!");
                tableModel.removeRow(rowIdx);
            } else {
                JOptionPane.showMessageDialog(panel, "Xóa thất bại!");
            }
        });

        // === ASSEMBLY ===
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        // === CHỨC NĂNG + LỌC (CHIA HAI BÊN) ===
        JPanel chucNangVaLocPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 dòng, 2 cột
        chucNangVaLocPanel.setBackground(new Color(245, 245, 245));

        // Đảm bảo 2 panel này đều có nền trắng và viền đẹp
        buttonPanel.setPreferredSize(null); // bỏ giới hạn kích thước nếu có
        filterPanel.setPreferredSize(null);

        chucNangVaLocPanel.add(buttonPanel);   // bên trái
        chucNangVaLocPanel.add(filterPanel);   // bên phải

        panel.add(chucNangVaLocPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createChiTietSubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ==== FORM NHAP ====
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "THÔNG TIN CHI TIẾT SP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        formPanel.setPreferredSize(new Dimension(600, 250)); // nhỏ lại một chút nếu cần

// Các trường
        JTextField txtMaChiTiet = createStyledTextField(15);
        cbMaSP = new JComboBox<>(new SanPhamDAO().layTatCaMaSP().toArray(new Integer[0]));
        cbMaSP.setPreferredSize(new Dimension(200, 30));
        JTextField txtKichCo = createStyledTextField(15);
        JTextField txtMauSac = createStyledTextField(15);
        JTextField txtSoLuong = createStyledTextField(15);
        JTextField txtGiaThanh = createStyledTextField(15);
        JTextField txtHinhAnh = new JTextField(15);
        txtHinhAnh.setVisible(false);

// Thêm vào Grid
        formPanel.add(new JLabel("Mã chi tiết:"));
        formPanel.add(txtMaChiTiet);

        formPanel.add(new JLabel("Mã sản phẩm:"));
        formPanel.add(cbMaSP);

        formPanel.add(new JLabel("Kích cỡ:"));
        formPanel.add(txtKichCo);

        formPanel.add(new JLabel("Màu sắc:"));
        formPanel.add(txtMauSac);

        formPanel.add(new JLabel("Số lượng tồn:"));
        formPanel.add(txtSoLuong);

        formPanel.add(new JLabel("Giá thành:"));
        formPanel.add(txtGiaThanh);
        

        // ==== PANEL ẢNH ====
        JPanel anhPanel = new JPanel();
        anhPanel.setLayout(new BoxLayout(anhPanel, BoxLayout.Y_AXIS));
        anhPanel.setBackground(Color.WHITE);
        anhPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "ẢNH CHI TIẾT",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        anhPanel.setPreferredSize(new Dimension(180, 250));

        JLabel lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(150, 150));
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnh.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        lblAnh.setBackground(Color.WHITE);
        lblAnh.setOpaque(true);

        JButton btnChonAnh = createStyledButton("Chọn ảnh", new Color(70, 130, 180));
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                String fileName = System.currentTimeMillis() + "_" + selected.getName();
                Path dest = Paths.get("src/anhSP/" + fileName);
                try {
                    Files.createDirectories(dest.getParent());
                    Files.copy(selected.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    txtHinhAnh.setText(fileName);
                    hienThiAnh(lblAnh, fileName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        anhPanel.add(Box.createVerticalStrut(10));
        anhPanel.add(lblAnh);
        anhPanel.add(Box.createVerticalStrut(10));
        anhPanel.add(btnChonAnh);

        JPanel topLeft = new JPanel(new BorderLayout(10, 0));
        topLeft.add(formPanel, BorderLayout.CENTER);
        topLeft.add(anhPanel, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeft, BorderLayout.CENTER);

        // ==== TABLE ====
        String[] columnNames = {"Mã chi tiết", "Mã SP", "Kích cỡ", "Màu sắc", "Số lượng", "Giá", "Ảnh"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(220, 240, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "DANH SÁCH CHI TIẾT SP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));

        ChiTietSanPhamDAO dao = new ChiTietSanPhamDAO();
        for (ChiTietSanPham ct : dao.layDanhSach()) {
            tableModel.addRow(new Object[]{
                ct.getMaChiTiet(), ct.getMaSanPham(), ct.getKichCo(),
                ct.getMauSac(), ct.getSoLuongTon(), ct.getGiaThanh(), getImageIcon(ct.getHinhAnhChiTiet())
            });
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    txtMaChiTiet.setText(table.getValueAt(row, 0).toString());
                    cbMaSP.setSelectedItem(Integer.valueOf(table.getValueAt(row, 1).toString()));
                    txtKichCo.setText(table.getValueAt(row, 2).toString());
                    txtMauSac.setText(table.getValueAt(row, 3).toString());
                    txtSoLuong.setText(table.getValueAt(row, 4).toString());
                    txtGiaThanh.setText(table.getValueAt(row, 5).toString());
                    String file = table.getValueAt(row, 6).toString();
                    txtHinhAnh.setText(file);
                    hienThiAnh(lblAnh, file);
                }
            }
        });

        JButton btnThem = createStyledButton("Thêm", new Color(34, 139, 34));
        JButton btnSua = createStyledButton("Sửa", new Color(255, 140, 0));
        JButton btnXoa = createStyledButton("Xóa", new Color(220, 20, 60));

        btnThem.setPreferredSize(new Dimension(90, 30));
        btnSua.setPreferredSize(new Dimension(90, 30));
        btnXoa.setPreferredSize(new Dimension(90, 30));

        btnThem.addActionListener(e -> {
            int giaThanh = Integer.parseInt(txtGiaThanh.getText());
            ChiTietSanPham ct;
            boolean success;

            if (txtMaChiTiet.getText().trim().isEmpty()) {
                ct = new ChiTietSanPham(0, (int) cbMaSP.getSelectedItem(),
                        txtKichCo.getText(), txtMauSac.getText(),
                        Integer.parseInt(txtSoLuong.getText()), txtHinhAnh.getText(), giaThanh);
                success = dao.themChiTietKhongMa(ct);
            } else {
                ct = new ChiTietSanPham(Integer.parseInt(txtMaChiTiet.getText()),
                        (int) cbMaSP.getSelectedItem(), txtKichCo.getText(), txtMauSac.getText(),
                        Integer.parseInt(txtSoLuong.getText()), txtHinhAnh.getText(), giaThanh);
                success = dao.themChiTiet(ct);
            }

            if (success) {
                JOptionPane.showMessageDialog(panel, "Thêm thành công!");
                txtMaChiTiet.setText(String.valueOf(ct.getMaChiTiet()));
                tableModel.setRowCount(0);
                for (ChiTietSanPham cts : dao.layDanhSach()) {
                    tableModel.addRow(new Object[]{
                        cts.getMaChiTiet(), cts.getMaSanPham(), cts.getKichCo(),
                        cts.getMauSac(), cts.getSoLuongTon(), cts.getGiaThanh(), getImageIcon(cts.getHinhAnhChiTiet())
                    });
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Thêm thất bại!");
            }
        });

        btnSua.addActionListener(e -> {
            int rowpostion = table.getSelectedRow();
            if (rowpostion == -1) {
                return;
            }

            ChiTietSanPham ct = new ChiTietSanPham(
                    Integer.parseInt(txtMaChiTiet.getText()),
                    (int) cbMaSP.getSelectedItem(),
                    txtKichCo.getText(), txtMauSac.getText(),
                    Integer.parseInt(txtSoLuong.getText()), txtHinhAnh.getText(),
                    Integer.parseInt(txtGiaThanh.getText())
            );

            if (dao.capNhatChiTiet(ct)) {
                JOptionPane.showMessageDialog(panel, "Cập nhật thành công!");
                table.setValueAt(ct.getMaSanPham(), rowpostion, 1);
                table.setValueAt(ct.getKichCo(), rowpostion, 2);
                table.setValueAt(ct.getMauSac(), rowpostion, 3);
                table.setValueAt(ct.getSoLuongTon(), rowpostion, 4);
                table.setValueAt(ct.getGiaThanh(), rowpostion, 5);
                table.setValueAt(getImageIcon(ct.getHinhAnhChiTiet()), rowpostion, 6);
            } else {
                JOptionPane.showMessageDialog(panel, "Cập nhật thất bại!");
            }
        });

        btnXoa.addActionListener(e -> {
            int rowpostion = table.getSelectedRow();
            if (rowpostion == -1) {
                return;
            }

            int ma = Integer.parseInt(txtMaChiTiet.getText());
            if (dao.xoaChiTiet(ma)) {
                JOptionPane.showMessageDialog(panel, "Xóa thành công!");
                tableModel.removeRow(rowpostion);
            } else {
                JOptionPane.showMessageDialog(panel, "Xóa thất bại!");
            }
        });

        // ==== CHỨC NĂNG ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "CHỨC NĂNG",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);

        // ==== FILTER ====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "TÌM KIẾM / LỌC",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));

        JComboBox<Integer> cbFilterMaSP = new JComboBox<>(new SanPhamDAO().layTatCaMaSP().toArray(new Integer[0]));
        cbFilterMaSP.setPreferredSize(new Dimension(150, 30));
        JTextField txtFilterKichCo = new JTextField(15);
        JButton btnTimKiem = createStyledButton("Tìm kiếm", new Color(70, 130, 180));
        JButton btnReset = createStyledButton("Reset", new Color(255, 99, 71));

        btnTimKiem.addActionListener(e -> {
            int maSP = (int) cbFilterMaSP.getSelectedItem();
            String kichCo = txtFilterKichCo.getText().trim();

            List<ChiTietSanPham> ketQua = dao.timKiem(maSP, kichCo);
            tableModel.setRowCount(0);
            for (ChiTietSanPham ct : ketQua) {
                tableModel.addRow(new Object[]{
                    ct.getMaChiTiet(), ct.getMaSanPham(), ct.getKichCo(),
                    ct.getMauSac(), ct.getSoLuongTon(), ct.getGiaThanh(), getImageIcon(ct.getHinhAnhChiTiet())
                });
            }
        });

        btnReset.addActionListener(e -> {
            cbFilterMaSP.setSelectedIndex(0);
            txtFilterKichCo.setText("");
            tableModel.setRowCount(0);
            for (ChiTietSanPham ct : dao.layDanhSach()) {
                tableModel.addRow(new Object[]{
                    ct.getMaChiTiet(), ct.getMaSanPham(), ct.getKichCo(),
                    ct.getMauSac(), ct.getSoLuongTon(), ct.getGiaThanh(), getImageIcon(ct.getHinhAnhChiTiet())
                });
            }
        });

        filterPanel.add(new JLabel("Mã SP:"));
        filterPanel.add(cbFilterMaSP);
        filterPanel.add(new JLabel("Kích cỡ:"));
        filterPanel.add(txtFilterKichCo);
        filterPanel.add(btnTimKiem);
        filterPanel.add(btnReset);

        // GỘP BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(0, 5, 0, 5);
        gb.fill = GridBagConstraints.BOTH;
        gb.gridy = 0;

// Panel CHỨC NĂNG (40%)
        gb.gridx = 0;
        gb.weightx = 0.4;
        bottomPanel.add(buttonPanel, gb);

// Panel TÌM KIẾM / LỌC (60%)
        gb.gridx = 1;
        gb.weightx = 0.6;
        bottomPanel.add(filterPanel, gb);

        // ADD TO MAIN PANEL
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ImageIcon getImageIcon(String fileName) {
        if (fileName == null) {
            return null;
        }
        String path = "src/anhSP/" + fileName;
        ImageIcon icon = new ImageIcon(path);
        icon.setImage(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        icon.setDescription(fileName);
        return icon;
    }

    private void hienThiAnh(JLabel label, String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            String path = "src/anhSP/" + fileName;
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        } else {
            label.setIcon(null);
        }
    }

    private String[] getDanhMucList() {
        List<String> list = new DanhMucDAO().layTatCaMaDanhMuc();
        return list.toArray(new String[0]);
    }

    private String[] getDanhMucListWithAll() {
        List<String> list = new ArrayList<>();
        list.add("Tất cả");
        list.addAll(Arrays.asList(getDanhMucList()));
        return list.toArray(new String[0]);
    }

    private void capNhatComboBoxMaSP() {
        cbMaSP.setModel(new DefaultComboBoxModel<>(new SanPhamDAO().layTatCaMaSP().toArray(new Integer[0])));
    }

    private JButton createIconButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(40, 30));
        return button;
    }

}
