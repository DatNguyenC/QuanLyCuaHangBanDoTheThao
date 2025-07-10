package Views.FormDieuHuong;

import DAO.KhuyenMaiDAO;
import DAO.SanPhamDAO;
import Models.KhuyenMai;
import Models.SanPham;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static Views.UIHelper.FormUIHelper.*;

public class FormThemKhuyenMai extends JDialog {
    private JTextField txtTenKM = new JTextField(15);
    private JTextField txtMoTa = new JTextField(15);
    private JTextField txtPhanTram = new JTextField(5);
    private JTextField txtNgayBD = new JTextField(15);
    private JTextField txtNgayKT = new JTextField(15);
    private JComboBox<SanPham> cmbSanPham = new JComboBox<>();

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    public FormThemKhuyenMai(JFrame owner, Runnable onSuccess) {
        super(owner, "Thêm Khuyến Mãi", true);
        setSize(400, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin khuyến mãi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addFormRow(form, gbc, row++, "Tên KM:", txtTenKM);
        addFormRow(form, gbc, row++, "Mô tả:", txtMoTa);
        addFormRow(form, gbc, row++, "Phần trăm giảm:", txtPhanTram);
        addFormRow(form, gbc, row++, "Ngày bắt đầu (yyyy-MM-dd HH:mm):", txtNgayBD);
        addFormRow(form, gbc, row++, "Ngày kết thúc (yyyy-MM-dd HH:mm):", txtNgayKT);
        addFormRow(form, gbc, row++, "Sản phẩm áp dụng:", cmbSanPham);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnThem = createStyledButton("Thêm", new Color(60, 179, 113));
        JButton btnHuy = createStyledButton("Hủy", new Color(220, 20, 60));
        bottom.add(btnThem);
        bottom.add(btnHuy);
        add(bottom, BorderLayout.SOUTH);

        // Load sản phẩm
        loadSanPham();

        // Sự kiện
        btnHuy.addActionListener(e -> dispose());

        btnThem.addActionListener(e -> {
            try {
                String ten = txtTenKM.getText().trim();
                String moTa = txtMoTa.getText().trim();
                double giam = Double.parseDouble(txtPhanTram.getText().trim());
                LocalDateTime bd = LocalDateTime.parse(txtNgayBD.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime kt = LocalDateTime.parse(txtNgayKT.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                SanPham sp = (SanPham) cmbSanPham.getSelectedItem();

                if (ten.isEmpty() || moTa.isEmpty() || sp == null) {
                    showErrorMessage("Vui lòng nhập đầy đủ thông tin.");
                    return;
                }

                KhuyenMai km = new KhuyenMai();
                km.setTenKM(ten);
                km.setMoTa(moTa);
                km.setPhanTramGiam(giam);
                km.setNgayBatDau(bd);
                km.setNgayKetThuc(kt);
                km.setMaSanPham(sp.getMaSanPham());

                if (khuyenMaiDAO.themKhuyenMai(km)) {
                    showSuccessMessage("Thêm thành công!");
                    if (onSuccess != null) onSuccess.run();
                    dispose();
                } else {
                    showErrorMessage("Thêm thất bại.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMessage("Dữ liệu không hợp lệ.");
            }
        });
    }

    private void loadSanPham() {
        List<SanPham> ds = sanPhamDAO.layDanhSach();
        cmbSanPham.removeAllItems();
        for (SanPham sp : ds) {
            cmbSanPham.addItem(sp);
        }
        cmbSanPham.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                label.setText(spText((SanPham) value));
            }
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });
    }

    private String spText(SanPham sp) {
        return "SP#" + sp.getMaSanPham() + " - " + sp.getTenSanPham();
    }
}
