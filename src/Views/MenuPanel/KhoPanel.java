package Views.MenuPanel;

import DAO.ChiTietNhapDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.PhieuNhapDAO;
import static Views.UIHelper.FormUIHelper.*;
import Views.FormDieuHuong.FormSuaPhieuNhap;
import Views.FormDieuHuong.FormThemPhieuNhap;
import Views.FormDieuHuong.FormThemChiTietNhap;
import Views.FormDieuHuong.FormSuaChiTietNhap;
import Models.ChiTietNhap;
import Views.FormDieuHuong.FormNhaCungCap;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.border.TitledBorder;

public class KhoPanel extends JPanel {

    private JTable tblChiTietNhap;
    private DefaultTableModel modelChiTietNhap;
    private JTextField txtTimMaChiTietPN;
    Color titleColor = new Color(70, 130, 180);

    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private JTable tblPhieuNhap;
    private DefaultTableModel modelPhieuNhap;
    private JTextField txtTimMaPhieuNhap;

    public KhoPanel() {
        setLayout(new BorderLayout());

        // ===== Nội dung chính =====
        JPanel panelQuanLyKho = KhoSubPanel();
        add(panelQuanLyKho, BorderLayout.CENTER); // Chỉ hiển thị panel Quản lý kho
    }

    private JPanel KhoSubPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // === Top Panel: PHIẾU NHẬP ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // -- Tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        TitledBorder searchBorder = BorderFactory.createTitledBorder("TÌM KIẾM");
        searchBorder.setTitleColor(titleColor);
        searchPanel.setBorder(searchBorder);
        txtTimMaPhieuNhap = new JTextField(15);
        JButton btnTim = createStyledButton("Tìm", new Color(70, 130, 180));
        searchPanel.add(new JLabel("Mã phiếu:"));
        searchPanel.add(txtTimMaPhieuNhap);
        searchPanel.add(btnTim);

        // -- Thao tác
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        TitledBorder actionBorder = BorderFactory.createTitledBorder("THAO TÁC");
        actionBorder.setTitleColor(titleColor);
        actionPanel.setBorder(actionBorder);
        JButton btnTao = createStyledButton("Tạo Phiếu Nhập", new Color(70, 130, 180));
        JButton btnSua = createStyledButton("Sửa Phiếu Nhập", new Color(70, 130, 180));
        JButton btnXoa = createStyledButton("Xóa phiếu", new Color(70, 130, 180));
        JButton btnNhapKho = createStyledButton("Nhập kho", new Color(70, 130, 180));
        actionPanel.add(btnTao);
        actionPanel.add(btnSua);
        actionPanel.add(btnXoa);
        actionPanel.add(btnNhapKho);
        // -- Nhà cung cấp
        JPanel nccPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        TitledBorder nccBorder = BorderFactory.createTitledBorder("NHÀ CUNG CHẤP");
        nccBorder.setTitleColor(titleColor);
        nccPanel.setBorder(nccBorder);
        JButton btnXemNCC = createStyledButton("Xem Nhà Cung Cấp", new Color(70, 130, 180));
        btnXemNCC.setPreferredSize(new Dimension(180, 40)); // Hoặc tăng lên 200 nếu cần
        nccPanel.add(btnXemNCC);

        // -- Add vào topPanel
        topPanel.add(searchPanel);
        topPanel.add(actionPanel);
        topPanel.add(nccPanel);

        // === Bảng phiếu nhập ===
        modelPhieuNhap = new DefaultTableModel(new String[]{"Mã phiếu", "Nhà cung cấp", "Ngày nhập"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblPhieuNhap = new JTable(modelPhieuNhap);
        tblPhieuNhap.setRowHeight(25);
        tblPhieuNhap.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblPhieuNhap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblPhieuNhap.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        JScrollPane scrollPN = new JScrollPane(tblPhieuNhap);
        TitledBorder DsPhieuNhapBorder = BorderFactory.createTitledBorder("DANH SÁCH PHIẾU NHẬP");
        DsPhieuNhapBorder.setTitleColor(titleColor);
        scrollPN.setBorder(DsPhieuNhapBorder);

        // === Bottom Panel: CHI TIẾT PHIẾU NHẬP ===
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel bottomTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        TitledBorder CtPhieuNhapBorder = BorderFactory.createTitledBorder("CHI TIẾT PHIẾU NHẬP");
        CtPhieuNhapBorder.setTitleColor(titleColor);
        bottomPanel.setBorder(CtPhieuNhapBorder);
        JPanel searchCTPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        TitledBorder searchCTBorder = BorderFactory.createTitledBorder("TÌM KIẾM");
        searchCTBorder.setTitleColor(titleColor);
        searchCTPanel.setBorder(searchCTBorder);
        txtTimMaChiTietPN = new JTextField(10);
        JButton btnTimCT = createStyledButton("Tìm", new Color(70, 130, 180));
        searchCTPanel.add(new JLabel("Mã chi tiết phiếu nhập:"));
        searchCTPanel.add(txtTimMaChiTietPN);
        searchCTPanel.add(btnTimCT);

        JPanel actionCTPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        TitledBorder actionCTBorder = BorderFactory.createTitledBorder("THAO TÁC");
        actionCTBorder.setTitleColor(titleColor);
        actionCTPanel.setBorder(actionCTBorder);
        JButton btnTaoCT = createStyledButton("Tạo Chi Tiết", new Color(70, 130, 180));
        JButton btnSuaCT = createStyledButton("Sửa Chi Tiết", new Color(70, 130, 180));
        JButton btnXoaCT = createStyledButton("Xóa Chi Tiết", new Color(70, 130, 180));
        actionCTPanel.add(btnTaoCT);
        actionCTPanel.add(btnSuaCT);
        actionCTPanel.add(btnXoaCT);

        bottomTopPanel.add(searchCTPanel);
        bottomTopPanel.add(actionCTPanel);

        modelChiTietNhap = new DefaultTableModel(new String[]{"Mã chi tiết phiếu nhập", "Mã phiếu", "Mã chi tiết SP", "Số lượng", "Đơn giá"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblChiTietNhap = new JTable(modelChiTietNhap);
        tblChiTietNhap.setRowHeight(25);
        tblChiTietNhap.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollCT = new JScrollPane(tblChiTietNhap);

        bottomPanel.add(bottomTopPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollCT, BorderLayout.CENTER);

        // === Split Pane ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPN, bottomPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(300);

        // === Add to main panel ===
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        // === Events ===
        loadPhieuNhapTable("");

        SwingUtilities.invokeLater(() -> {
            if (modelPhieuNhap.getRowCount() > 0) {
                tblPhieuNhap.setRowSelectionInterval(0, 0);
            }
        });

        btnTim.addActionListener(e -> loadPhieuNhapTable(txtTimMaPhieuNhap.getText().trim()));
        btnTao.addActionListener(e -> new FormThemPhieuNhap(this).setVisible(true));
        btnSua.addActionListener(e -> {
            int row = tblPhieuNhap.getSelectedRow();
            if (row == -1) {
                showErrorMessage("Vui lòng chọn phiếu nhập cần sửa");
                return;
            }
            Models.PhieuNhap pn = new Models.PhieuNhap();
            pn.setMaPhieuNhap((int) modelPhieuNhap.getValueAt(row, 0));
            pn.setMaNCC((int) modelPhieuNhap.getValueAt(row, 1));
            pn.setNgayNhap((java.util.Date) modelPhieuNhap.getValueAt(row, 2));
            new FormSuaPhieuNhap(this, pn).setVisible(true);
        });
        btnXoa.addActionListener(e -> {
            int row = tblPhieuNhap.getSelectedRow();
            if (row == -1) {
                showErrorMessage("Vui lòng chọn phiếu nhập cần xóa");
                return;
            }
            int maPN = (int) modelPhieuNhap.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(panel, "Xóa phiếu nhập " + maPN + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (new DAO.PhieuNhapDAO().xoaPhieuNhap(maPN)) {
                    showSuccessMessage("Xóa phiếu nhập thành công");
                    loadPhieuNhapTable("");
                    modelChiTietNhap.setRowCount(0);
                } else {
                    showErrorMessage("Xóa thất bại");
                }
            }
        });
        btnNhapKho.addActionListener(e -> {
            int selectedRow = tblPhieuNhap.getSelectedRow();
            if (selectedRow == -1) {
                showErrorMessage("Vui lòng chọn phiếu nhập để nhập kho");
                return;
            }

            int maPhieuNhap = (int) modelPhieuNhap.getValueAt(selectedRow, 0);

            ChiTietNhapDAO chiTietNhapDAO = new ChiTietNhapDAO();
            ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();

            // Lấy danh sách chi tiết phiếu nhập
            List<ChiTietNhap> danhSachChiTiet = chiTietNhapDAO.layTheoMaPhieuNhap(maPhieuNhap);

            if (danhSachChiTiet.isEmpty()) {
                showErrorMessage("Phiếu nhập chưa có chi tiết. Không thể nhập kho.");
                return;
            }

            boolean thanhCong = true;

            for (ChiTietNhap chiTiet : danhSachChiTiet) {
                int maChiTietSP = chiTiet.getMaChiTiet();
                int soLuongNhap = chiTiet.getSoLuongNhap();

                boolean capNhat = chiTietSanPhamDAO.capNhatSoLuongTon(maChiTietSP, soLuongNhap);
                if (!capNhat) {
                    thanhCong = false;
                    break; // dừng luôn nếu có lỗi
                }
            }

            if (thanhCong) {
                // Xóa chi tiết phiếu nhập và phiếu nhập
                boolean xoaChiTiet = chiTietNhapDAO.xoaTheoMaPhieu(maPhieuNhap);
                boolean xoaPhieu = new PhieuNhapDAO().xoaPhieuNhap(maPhieuNhap);
                if (xoaChiTiet && xoaPhieu) {
                    showSuccessMessage("Nhập kho thành công và đã xóa phiếu nhập");
                    loadPhieuNhapTable("");
                    modelChiTietNhap.setRowCount(0);
                } else {
                    showErrorMessage("Lỗi khi xóa phiếu nhập");
                }
            } else {
                showErrorMessage("Lỗi khi cập nhật số lượng tồn kho");
            }
        });

        btnTimCT.addActionListener(e -> loadChiTietPhieuNhapTable(txtTimMaChiTietPN.getText().trim()));
        btnTaoCT.addActionListener(e -> new FormThemChiTietNhap(this).setVisible(true));
        btnSuaCT.addActionListener(e -> {
            int row = tblChiTietNhap.getSelectedRow();
            if (row == -1) {
                showErrorMessage("Vui lòng chọn chi tiết cần sửa");
                return;
            }
            ChiTietNhap ctn = new ChiTietNhap();
            ctn.setMaChiTietPN((int) modelChiTietNhap.getValueAt(row, 0));
            ctn.setMaPhieuNhap((int) modelChiTietNhap.getValueAt(row, 1));
            ctn.setMaChiTiet((int) modelChiTietNhap.getValueAt(row, 2));
            ctn.setSoLuongNhap((int) modelChiTietNhap.getValueAt(row, 3));
            String giaStr = modelChiTietNhap.getValueAt(row, 4).toString().replace(",", "");
            ctn.setDonGiaNhap(Double.parseDouble(giaStr));
            new FormSuaChiTietNhap(this, ctn).setVisible(true);
        });
        btnXoaCT.addActionListener(e -> {
            int row = tblChiTietNhap.getSelectedRow();
            if (row == -1) {
                showErrorMessage("Vui lòng chọn chi tiết cần xóa");
                return;
            }
            int maCTPN = (int) modelChiTietNhap.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(panel, "Xóa chi tiết phiếu nhập " + maCTPN + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (new DAO.ChiTietNhapDAO().xoaChiTietNhap(maCTPN)) {
                    showSuccessMessage("Xóa thành công");
                    loadChiTietPhieuNhapTable();
                } else {
                    showErrorMessage("Xóa thất bại");
                }
            }
        });

        btnXemNCC.addActionListener(e -> new FormNhaCungCap().setVisible(true));

        tblPhieuNhap.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblPhieuNhap.getSelectedRow();
                if (selectedRow != -1) {
                    loadChiTietPhieuNhapTable();
                }
            }
        });

        return panel;
    }

    public void loadPhieuNhapTable(String keyword) {
        modelPhieuNhap.setRowCount(0);
        DAO.PhieuNhapDAO dao = new DAO.PhieuNhapDAO();
        List<Models.PhieuNhap> list = keyword.isEmpty()
                ? dao.layDanhSach()
                : dao.timKiemTheoMa(keyword);
        for (Models.PhieuNhap pn : list) {
            modelPhieuNhap.addRow(new Object[]{pn.getMaPhieuNhap(), pn.getMaNCC(), pn.getNgayNhap()});
        }
    }

    public void loadChiTietPhieuNhapTable() {
        loadChiTietPhieuNhapTable("");
    }

    public void loadChiTietPhieuNhapTable(String keyword) {
        modelChiTietNhap.setRowCount(0);
        DAO.ChiTietNhapDAO dao = new DAO.ChiTietNhapDAO();
        List<Models.ChiTietNhap> list = dao.layDanhSach();
        for (ChiTietNhap ctn : list) {
            if (keyword.isEmpty() || String.valueOf(ctn.getMaChiTietPN()).contains(keyword)) {
                modelChiTietNhap.addRow(new Object[]{
                    ctn.getMaChiTietPN(), ctn.getMaPhieuNhap(), ctn.getMaChiTiet(),
                    ctn.getSoLuongNhap(), moneyFormat.format(ctn.getDonGiaNhap())
                });

            }
        }
    }
}
