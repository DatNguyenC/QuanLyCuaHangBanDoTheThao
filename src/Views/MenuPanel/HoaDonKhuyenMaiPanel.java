package Views.MenuPanel;

import DAO.ChiTietHoaDonDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.HoaDonDAO;
import DAO.KhuyenMaiDAO;
import Models.HoaDon;
import Models.KhuyenMai;
import Views.UIHelper.ButtonEditor;
import Views.UIHelper.ButtonRenderer;
import Views.UIHelper.FormUIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class HoaDonKhuyenMaiPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JTable tblChiTietHD;
    private DefaultTableModel modelCTHD;
    DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public HoaDonKhuyenMaiPanel() {
        setLayout(new BorderLayout());

        // ===== Toolbar =====
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // không cho kéo toolbar

        JButton btnHoaDon = new JButton("Hóa đơn");
        JButton btnKhuyenMai = new JButton("Khuyến mãi");

        toolBar.add(btnHoaDon);
        toolBar.add(btnKhuyenMai);

        add(toolBar, BorderLayout.NORTH);

        // ===== Content (CardLayout để chuyển giữa các panel) =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel panelHoaDon = taoPanelHoaDon();
        JPanel panelKhuyenMai = taoPanelKhuyenMai();

        contentPanel.add(panelHoaDon, "HOADON");
        contentPanel.add(panelKhuyenMai, "KHUYENMAI");

        add(contentPanel, BorderLayout.CENTER);

        // ===== Event nút =====
        btnHoaDon.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "HOADON"));
        btnKhuyenMai.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "KHUYENMAI"));
    }

    private JPanel taoPanelHoaDon() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Bảng hóa đơn với cột Chức năng =====
        String[] columns = {"Mã hóa đơn", "Mã người dùng", "Ngày lập", "Tổng tiền", "Trạng thái", "Khuyến mại", "Chức năng"};
        DefaultTableModel modelHoaDon = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cho cột nút xử lý có thể tương tác
            }
        };

        JTable tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(30);
        tblHoaDon.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // ===== Thêm nút "Xử lý" vào cột =====
        tblHoaDon.getColumn("Chức năng").setCellRenderer(new ButtonRenderer("Xử lý"));
        tblHoaDon.getColumn("Chức năng").setCellEditor(new ButtonEditor(new JCheckBox(), (row) -> {
            int maHD = (int) modelHoaDon.getValueAt(row, 0);
            String trangThai = (String) modelHoaDon.getValueAt(row, 4);

            if ("Huy".equals(trangThai)) {
                FormUIHelper.showErrorMessage("Hóa đơn đã bị hủy, không thể xử lý.");
                return;
            }

            String[] options = {"ChuaThanhToan", "DaThanhToan", "DaGiao", "Huy", "DangGiao"};
            String chon = (String) JOptionPane.showInputDialog(tblHoaDon, "Chọn trạng thái mới:", "Xử lý hóa đơn",
                    JOptionPane.PLAIN_MESSAGE, null, options, trangThai);

            if (chon != null && !chon.equals(trangThai)) {
                HoaDonDAO hdDAO = new HoaDonDAO();
                HoaDon hd = hdDAO.timHoaDonTheoMa(maHD);

                // Nếu chọn "Huy", cộng lại số lượng vào kho
                if ("Huy".equals(chon)) {
                    ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
                    ChiTietSanPhamDAO ctspDAO = new ChiTietSanPhamDAO();
                    var dsCT = cthdDAO.layChiTietTheoMaHoaDon(maHD);

                    for (var ct : dsCT) {
                        ctspDAO.congSoLuongTon(ct.getMaChiTiet(), ct.getSoLuong());
                    }
                }

                // Cập nhật trạng thái hóa đơn
                hd.setTrangThai(chon);
                hdDAO.capNhatHoaDon(hd);
                modelHoaDon.setValueAt(chon, row, 4);
                FormUIHelper.showSuccessMessage("Đã cập nhật trạng thái hóa đơn.");
            }
        }));

        JScrollPane scrollHD = new JScrollPane(tblHoaDon);
        scrollHD.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 1),
                "Danh sách hóa đơn",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(0, 120, 215)
        ));

        // ===== Bộ lọc trạng thái =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblLoc = new JLabel("Trạng thái:");
        String[] trangThaiOptions = {"Tất cả", "ChuaThanhToan", "DaThanhToan", "DaGiao", "Huy", "DangGiao"};
        JComboBox<String> cbTrangThai = new JComboBox<>(trangThaiOptions);
        JButton btnLoc = FormUIHelper.createStyledButton("Lọc", new Color(0, 120, 215));

        filterPanel.add(lblLoc);
        filterPanel.add(cbTrangThai);
        filterPanel.add(btnLoc);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(scrollHD, BorderLayout.CENTER);

        // ===== Chi tiết hóa đơn phía dưới =====
        JPanel bottomPanel = taoPanelChiTietHoaDon();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerLocation(350);
        panel.add(splitPane, BorderLayout.CENTER);

        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();

        // ===== Load hóa đơn =====
        Runnable loadHoaDon = () -> {
            modelHoaDon.setRowCount(0);
            String trangThaiLoc = (String) cbTrangThai.getSelectedItem();
            for (HoaDon hd : hoaDonDAO.layTatCaHoaDon()) {
                if (!"Tất cả".equals(trangThaiLoc) && !hd.getTrangThai().equals(trangThaiLoc)) {
                    continue;
                }
                modelHoaDon.addRow(new Object[]{
                    hd.getMaHoaDon(), hd.getMaNguoiDung(), hd.getNgayLap(),
                    moneyFormat.format(hd.getTongTien()), // ✅ format tiền
                    hd.getTrangThai(),
                    hd.getMaKM() == null ? "" : hd.getMaKM(),
                    "Xử lý"
                });

            }
        };
        loadHoaDon.run();

        btnLoc.addActionListener(e -> loadHoaDon.run());

        // ===== Load chi tiết khi chọn hóa đơn =====
        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (tblHoaDon.getSelectedRow() >= 0) {
                int maHD = (int) modelHoaDon.getValueAt(tblHoaDon.getSelectedRow(), 0);
                modelCTHD.setRowCount(0);
                chiTietDAO.layChiTietTheoMaHoaDon(maHD).forEach(ct -> {
                    modelCTHD.addRow(new Object[]{
                        ct.getMaChiTietHD(), ct.getMaHoaDon(), ct.getMaChiTiet(),
                        ct.getSoLuong(),
                        moneyFormat.format(ct.getDonGia()) // ✅ format đơn giá
                    });
                });
            }
        });

        return panel;
    }

    private JPanel taoPanelChiTietHoaDon() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Bảng chi tiết hóa đơn =====
        String[] columnNames = {"Mã CTHD", "Mã hóa đơn", "Mã chi tiết SP", "Số lượng", "Đơn giá"};
        modelCTHD = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblChiTietHD = new JTable(modelCTHD);
        tblChiTietHD.setRowHeight(25);
        tblChiTietHD.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblChiTietHD.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(tblChiTietHD);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 1),
                "Danh sách chi tiết hóa đơn",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(0, 120, 215)
        ));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel taoPanelKhuyenMai() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel thông tin khuyến mãi ===
        JPanel thongTinPanel = FormUIHelper.createTitledPanel("Thông tin khuyến mãi");
        thongTinPanel.setLayout(new GridLayout(4, 4, 10, 10)); // Lưới 4 dòng x 4 cột

        // Các input
        JTextField txtMaKM = FormUIHelper.createStyledTextField();
        JTextField txtTenKM = FormUIHelper.createStyledTextField();
        JTextField txtMoTa = FormUIHelper.createStyledTextField();
        JTextField txtPhanTram = FormUIHelper.createStyledTextField();
        JTextField txtNgayBD = FormUIHelper.createStyledTextField();
        JTextField txtNgayKT = FormUIHelper.createStyledTextField();
        JTextField txtMaSP = FormUIHelper.createStyledTextField();

        // Add theo thứ tự label - field
        thongTinPanel.add(FormUIHelper.createStyledLabel("Mã KM:"));
        thongTinPanel.add(txtMaKM);
        thongTinPanel.add(FormUIHelper.createStyledLabel("Tên KM:"));
        thongTinPanel.add(txtTenKM);
        thongTinPanel.add(FormUIHelper.createStyledLabel("Mô tả:"));
        thongTinPanel.add(txtMoTa);
        thongTinPanel.add(FormUIHelper.createStyledLabel("Phần trăm giảm:"));
        thongTinPanel.add(txtPhanTram);
        thongTinPanel.add(FormUIHelper.createStyledLabel("Ngày bắt đầu:"));
        thongTinPanel.add(txtNgayBD);
        thongTinPanel.add(FormUIHelper.createStyledLabel("Ngày kết thúc:"));
        thongTinPanel.add(txtNgayKT);
        thongTinPanel.add(FormUIHelper.createStyledLabel("Mã sản phẩm:"));
        thongTinPanel.add(txtMaSP);

        // Bổ sung 2 ô trống cho đủ 4x4
        thongTinPanel.add(new JLabel());
        thongTinPanel.add(new JLabel());

        // === Panel lọc và chức năng (nằm ngang) ===
        JPanel locVaChucNangPanel = new JPanel(new BorderLayout(10, 10));

        // Panel lọc
        JPanel locPanel = FormUIHelper.createTitledPanel("Lọc khuyến mãi");
        locPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField txtLocMaKM = FormUIHelper.createStyledTextField(10);
        JComboBox<String> comboHieuLuc = new JComboBox<>(new String[]{"Tất cả", "Còn hiệu lực", "Hết hiệu lực"});
        comboHieuLuc.setPreferredSize(new Dimension(150, 30));
        comboHieuLuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnLoc = FormUIHelper.createStyledButton("Lọc", new Color(52, 152, 219));
        locPanel.add(FormUIHelper.createStyledLabel("Mã KM:"));
        locPanel.add(txtLocMaKM);
        locPanel.add(comboHieuLuc);
        locPanel.add(btnLoc);

        // Panel chức năng
        JPanel chucNangPanel = FormUIHelper.createTitledPanel("Chức năng");
        chucNangPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnThem = FormUIHelper.createStyledButton("Thêm", new Color(34, 139, 34));
        JButton btnSua = FormUIHelper.createStyledButton("Sửa", new Color(242, 161, 0));
        JButton btnXoa = FormUIHelper.createStyledButton("Xóa", new Color(220, 20, 60));
        chucNangPanel.add(btnThem);
        chucNangPanel.add(btnSua);
        chucNangPanel.add(btnXoa);

        // Gắn vào panel ngang
        locVaChucNangPanel.add(locPanel, BorderLayout.CENTER);
        locVaChucNangPanel.add(chucNangPanel, BorderLayout.EAST);

        // === Bảng danh sách khuyến mãi ===
        JPanel bangPanel = FormUIHelper.createTitledPanel("Danh sách khuyến mãi");
        bangPanel.setLayout(new BorderLayout());
        String[] columnNames = {"Mã KM", "Tên KM", "Mô tả", "Giảm (%)", "Ngày BD", "Ngày KT", "Mã SP"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        bangPanel.add(scrollPane, BorderLayout.CENTER);

        // === Gắn toàn bộ vào mainPanel ===
        mainPanel.add(thongTinPanel, BorderLayout.NORTH);
        mainPanel.add(locVaChucNangPanel, BorderLayout.CENTER);
        mainPanel.add(bangPanel, BorderLayout.SOUTH);

        KhuyenMaiDAO dao = new KhuyenMaiDAO();

// Hàm load lại bảng từ DAO
        Runnable loadData = () -> {
            model.setRowCount(0);
            for (KhuyenMai km : dao.layTatCaKhuyenMai()) {
                model.addRow(new Object[]{
                    km.getMaKM(), km.getTenKM(), km.getMoTa(), km.getPhanTramGiam(),
                    km.getNgayBatDau(), km.getNgayKetThuc(), km.getMaSanPham()
                });
            }
        };
        loadData.run(); // Gọi lần đầu để hiển thị

        btnThem.addActionListener(e -> {
            try {
                String tenKM = txtTenKM.getText().trim();
                String moTa = txtMoTa.getText().trim();
                double phanTram = Double.parseDouble(txtPhanTram.getText().trim());
                LocalDateTime ngayBD = LocalDateTime.parse(txtNgayBD.getText().trim());
                LocalDateTime ngayKT = LocalDateTime.parse(txtNgayKT.getText().trim());
                int maSP = Integer.parseInt(txtMaSP.getText().trim());

                KhuyenMai km = new KhuyenMai(0, tenKM, moTa, phanTram, ngayBD, ngayKT, maSP);
                if (dao.themKhuyenMai(km)) {
                    FormUIHelper.showSuccessMessage("Thêm khuyến mãi thành công!");
                    loadData.run();
                } else {
                    FormUIHelper.showErrorMessage("Thêm thất bại!");
                }
            } catch (Exception ex) {
                FormUIHelper.showErrorMessage("Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                FormUIHelper.showErrorMessage("Vui lòng chọn một khuyến mãi để sửa.");
                return;
            }

            try {
                int maKM = Integer.parseInt(txtMaKM.getText().trim());
                String tenKM = txtTenKM.getText().trim();
                String moTa = txtMoTa.getText().trim();
                double phanTram = Double.parseDouble(txtPhanTram.getText().trim());
                LocalDateTime ngayBD = LocalDateTime.parse(txtNgayBD.getText().trim());
                LocalDateTime ngayKT = LocalDateTime.parse(txtNgayKT.getText().trim());
                int maSP = Integer.parseInt(txtMaSP.getText().trim());

                KhuyenMai km = new KhuyenMai(maKM, tenKM, moTa, phanTram, ngayBD, ngayKT, maSP);
                if (dao.capNhatKhuyenMai(km)) {
                    FormUIHelper.showSuccessMessage("Cập nhật thành công!");
                    loadData.run();
                } else {
                    FormUIHelper.showErrorMessage("Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                FormUIHelper.showErrorMessage("Lỗi: " + ex.getMessage());
            }
        });

        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                FormUIHelper.showErrorMessage("Vui lòng chọn khuyến mãi để xóa.");
                return;
            }

            int maKM = (int) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.xoaKhuyenMai(maKM)) {
                    FormUIHelper.showSuccessMessage("Xóa thành công!");
                    loadData.run();
                } else {
                    FormUIHelper.showErrorMessage("Xóa thất bại!");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaKM.setText(model.getValueAt(selectedRow, 0).toString());
                txtTenKM.setText(model.getValueAt(selectedRow, 1).toString());
                txtMoTa.setText(model.getValueAt(selectedRow, 2).toString());
                txtPhanTram.setText(model.getValueAt(selectedRow, 3).toString());
                txtNgayBD.setText(model.getValueAt(selectedRow, 4).toString());
                txtNgayKT.setText(model.getValueAt(selectedRow, 5).toString());
                txtMaSP.setText(model.getValueAt(selectedRow, 6).toString());
            }
        });
        btnLoc.addActionListener(e -> {
            String maKMStr = txtLocMaKM.getText().trim();
            String hieuLuc = (String) comboHieuLuc.getSelectedItem();

            model.setRowCount(0);
            for (KhuyenMai km : dao.layTatCaKhuyenMai()) {
                boolean matchMa = maKMStr.isEmpty() || String.valueOf(km.getMaKM()).contains(maKMStr);

                boolean matchHieuLuc = switch (hieuLuc) {
                    case "Tất cả" ->
                        true;
                    case "Còn hiệu lực" ->
                        km.getNgayBatDau().isBefore(LocalDateTime.now())
                        && km.getNgayKetThuc().isAfter(LocalDateTime.now());
                    case "Hết hiệu lực" ->
                        km.getNgayKetThuc().isBefore(LocalDateTime.now());
                    default ->
                        true;
                };

                if (matchMa && matchHieuLuc) {
                    model.addRow(new Object[]{
                        km.getMaKM(), km.getTenKM(), km.getMoTa(), km.getPhanTramGiam(),
                        km.getNgayBatDau(), km.getNgayKetThuc(), km.getMaSanPham()
                    });
                }
            }
        });

        return mainPanel;
    }

}
