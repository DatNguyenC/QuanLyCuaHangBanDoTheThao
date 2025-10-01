package Views.MenuPanel;

import DAO.ChiTietGioHangDAO;
import DAO.GioHangDAO;
import DAO.SanPhamDAO;
import Models.ChiTietGioHang;
import Models.GioHang;
import Models.NguoiDung;
import Models.SanPham;
import Views.FormDieuHuong.FormThemSuaChiTietGio;
import Views.FormDieuHuong.FormTaoGioHang;
import Views.FormDieuHuong.FormTaoHoaDon;
import Views.UIHelper.FormUIHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BanHangPanel extends JPanel {

    private JTable tableGioHang, tableChiTiet;
    private DefaultTableModel modelGioHang, modelChiTiet;
    private final GioHangDAO gioHangDAO = new GioHangDAO();
    private final ChiTietGioHangDAO chiTietGioHangDAO = new ChiTietGioHangDAO();

    private final NguoiDung nd;

    public BanHangPanel(NguoiDung nd) {
        this.nd = nd;

        setLayout(new GridLayout(2, 1, 10, 10));
        setBackground(Color.WHITE);

        // === PANEL GIỎ HÀNG ===
        JPanel gioHangPanel = FormUIHelper.createTitledPanel("THÔNG TIN GIỎ HÀNG");
        gioHangPanel.setLayout(new BorderLayout());

        modelGioHang = new DefaultTableModel(new String[]{"Mã Giỏ Hàng", "Mã Người Dùng", "Ngày Tạo"}, 0);
        tableGioHang = new JTable(modelGioHang);
        gioHangPanel.add(new JScrollPane(tableGioHang), BorderLayout.CENTER);

        // CHỨC NĂNG GIỎ HÀNG
        JPanel pnChucNangGio = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnChucNangGio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "CHỨC NĂNG",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        pnChucNangGio.setBackground(Color.WHITE);

        JButton btnTao = FormUIHelper.createStyledButton("Tạo", new Color(70, 130, 180));
        JButton btnXoa = FormUIHelper.createStyledButton("Xóa", new Color(70, 130, 180));
        JButton btnTaoDonHang = FormUIHelper.createStyledButton("Tạo đơn hàng", new Color(70, 130, 180));
        JButton btnLamMoi = FormUIHelper.createStyledButton("Làm mới", new Color(70, 130, 180));

        pnChucNangGio.add(btnTao);
        pnChucNangGio.add(btnXoa);
        pnChucNangGio.add(btnTaoDonHang);
        pnChucNangGio.add(btnLamMoi); // ✅
        gioHangPanel.add(pnChucNangGio, BorderLayout.SOUTH);

        // === PANEL CHI TIẾT GIỎ ===
        JPanel chiTietPanel = FormUIHelper.createTitledPanel("CHI TIẾT GIỎ HÀNG");
        chiTietPanel.setLayout(new BorderLayout());

        modelChiTiet = new DefaultTableModel(new String[]{"Mã Chi Tiết Giỏ Hàng", "Mã Giỏ Hàng", "Mã Chi Tiết", "Mã Sản Phẩm", "Số Lượng"}, 0);

        tableChiTiet = new JTable(modelChiTiet);
        chiTietPanel.add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);

        // CHỨC NĂNG CHI TIẾT
        JPanel pnChucNangCT = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnChucNangCT.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(30, 144, 255)),
                "CHỨC NĂNG",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        pnChucNangCT.setBackground(Color.WHITE);

        JButton btnThem = FormUIHelper.createStyledButton("Thêm", new Color(70, 130, 180));
        JButton btnXoaCT = FormUIHelper.createStyledButton("Xóa", new Color(70, 130, 180));
        JButton btnSua = FormUIHelper.createStyledButton("Sửa", new Color(70, 130, 180));
        pnChucNangCT.add(btnThem);
        pnChucNangCT.add(btnXoaCT);
        pnChucNangCT.add(btnSua);
        chiTietPanel.add(pnChucNangCT, BorderLayout.SOUTH);

        // === ADD TO MAIN ===
        add(gioHangPanel);
        add(chiTietPanel);

        // === SỰ KIỆN ===
        btnTao.addActionListener(e -> {
            new FormTaoGioHang((JFrame) SwingUtilities.getWindowAncestor(this), this::loadDataGio).setVisible(true);
        });

        btnXoa.addActionListener(e -> {
            int row = tableGioHang.getSelectedRow();
            if (row >= 0) {
                int maGH = (int) modelGioHang.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa giỏ hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    chiTietGioHangDAO.xoaTatCaTheoGioHang(maGH);
                    if (gioHangDAO.xoaGioHang(maGH)) {
                        FormUIHelper.showSuccessMessage("Xóa thành công.");
                        loadDataGio();
                    } else {
                        FormUIHelper.showErrorMessage("Xóa thất bại.");
                    }
                }
            }
        });

        tableGioHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableGioHang.getSelectedRow();
                if (row >= 0) {
                    int maGH = (int) modelGioHang.getValueAt(row, 0);
                    loadDataChiTiet(maGH);
                }
            }
        });

        btnTaoDonHang.addActionListener(e -> {
            int row = tableGioHang.getSelectedRow();
            if (row < 0) {
                FormUIHelper.showErrorMessage("Vui lòng chọn giỏ hàng để tạo đơn hàng.");
                return;
            }
            int maGH = (int) modelGioHang.getValueAt(row, 0);
            new FormTaoHoaDon((JFrame) SwingUtilities.getWindowAncestor(this), maGH, nd).setVisible(true);
        });
        btnLamMoi.addActionListener(e -> loadDataGio()); // ✅

        btnThem.addActionListener(e -> {
            int row = tableGioHang.getSelectedRow();
            if (row < 0) {
                FormUIHelper.showErrorMessage("Chọn giỏ hàng trước khi thêm.");
                return;
            }
            int maGH = (int) modelGioHang.getValueAt(row, 0);
            new FormThemSuaChiTietGio((JFrame) SwingUtilities.getWindowAncestor(this), maGH, null, this::reloadChiTietCurrentGio).setVisible(true);
        });

        btnSua.addActionListener(e -> {
            int row = tableChiTiet.getSelectedRow();
            if (row < 0) {
                FormUIHelper.showErrorMessage("Chọn dòng cần sửa.");
                return;
            }
            ChiTietGioHang ct = new ChiTietGioHang();
            ct.setMaChiTietGH((int) modelChiTiet.getValueAt(row, 0));
            ct.setMaGioHang((int) modelChiTiet.getValueAt(row, 1));
            ct.setMaChiTiet((int) modelChiTiet.getValueAt(row, 2));
            ct.setSoLuong((int) modelChiTiet.getValueAt(row, 3));
            new FormThemSuaChiTietGio((JFrame) SwingUtilities.getWindowAncestor(this), ct.getMaGioHang(), ct, this::reloadChiTietCurrentGio).setVisible(true);

        });

        btnXoaCT.addActionListener(e -> {
            int row = tableChiTiet.getSelectedRow();
            if (row < 0) {
                FormUIHelper.showErrorMessage("Chọn dòng cần xóa.");
                return;
            }
            int maCTGH = (int) modelChiTiet.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (chiTietGioHangDAO.xoaChiTietGioHang(maCTGH)) {
                    FormUIHelper.showSuccessMessage("Đã xóa chi tiết.");
                    reloadChiTietCurrentGio();
                } else {
                    FormUIHelper.showErrorMessage("Xóa thất bại.");
                }
            }
        });

        loadDataGio();
    }

    private void loadDataGio() {
        modelGioHang.setRowCount(0);
        List<GioHang> ds = gioHangDAO.layTatCa();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (GioHang gh : ds) {
            modelGioHang.addRow(new Object[]{
                gh.getMaGioHang(),
                gh.getMaNguoiDung(),
                gh.getNgayTao().format(fmt)
            });
        }
        modelChiTiet.setRowCount(0);
    }

    private void loadDataChiTiet(int maGH) {
        modelChiTiet.setRowCount(0);
        List<ChiTietGioHang> ds = chiTietGioHangDAO.layChiTietTheoGioHang(maGH);
        for (ChiTietGioHang ct : ds) {
            SanPham sp = new SanPhamDAO().laySanPhamTheoMaChiTiet(ct.getMaChiTiet());
            int maSanPham = (sp != null) ? sp.getMaSanPham() : -1;

            modelChiTiet.addRow(new Object[]{
                ct.getMaChiTietGH(),
                ct.getMaGioHang(),
                ct.getMaChiTiet(),
                maSanPham,
                ct.getSoLuong()
            });
        }
    }

    private void reloadChiTietCurrentGio() {
        int row = tableGioHang.getSelectedRow();
        if (row >= 0) {
            int maGH = (int) modelGioHang.getValueAt(row, 0);
            loadDataChiTiet(maGH);
        }
    }
}
