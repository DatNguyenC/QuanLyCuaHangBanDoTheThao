package Views.FormDieuHuong;

import DAO.ChiTietGioHangDAO;
import DAO.ChiTietSanPhamDAO;
import Models.ChiTietGioHang;
import Models.ChiTietSanPham;
import Views.UIHelper.FormUIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormThemSuaChiTietGio extends JDialog {

    private final JComboBox<ChiTietSanPham> cboMaChiTiet;
    private final JTextField txtSoLuong;
    private final ChiTietGioHangDAO gioHangDAO = new ChiTietGioHangDAO();
    private final ChiTietSanPhamDAO ctspDAO = new ChiTietSanPhamDAO();
    private final boolean isEdit;
    private final ChiTietGioHang ctgh;
    private final int maGioHang;
    private final Runnable onSuccess;

    public FormThemSuaChiTietGio(JFrame parent, int maGioHang, ChiTietGioHang ctgh, Runnable onSuccess) {
        super(parent, (ctgh == null ? "Thêm" : "Sửa") + " Chi Tiết Giỏ Hàng", true);
        this.maGioHang = maGioHang;
        this.ctgh = ctgh;
        this.onSuccess = onSuccess;
        this.isEdit = ctgh != null;

        setSize(800, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // === FORM ===
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        form.add(new JLabel("Chi tiết sản phẩm:"));
        cboMaChiTiet = new JComboBox<>();
        loadChiTietSanPham();
        form.add(cboMaChiTiet);

        form.add(new JLabel("Số lượng:"));
        txtSoLuong = new JTextField();
        form.add(txtSoLuong);

        add(form, BorderLayout.CENTER);

        // === BUTTONS ===
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        JButton btnLuu = FormUIHelper.createStyledButton("Lưu", new Color(116, 166, 205));
        JButton btnHuy = FormUIHelper.createStyledButton("Hủy", new Color(169, 169, 169));
        actionPanel.add(btnLuu);
        actionPanel.add(btnHuy);
        add(actionPanel, BorderLayout.SOUTH);

        // === DỮ LIỆU CŨ ===
        if (isEdit) {
            txtSoLuong.setText(String.valueOf(ctgh.getSoLuong()));
            selectComboBoxItem(ctgh.getMaChiTiet());
        }

        // === SỰ KIỆN ===
        btnLuu.addActionListener(e -> xuLyLuu());
        btnHuy.addActionListener(e -> dispose());
    }

    private void loadChiTietSanPham() {
        List<ChiTietSanPham> list = ctspDAO.layChiTietVaTenSanPham();
        for (ChiTietSanPham ct : list) {
            cboMaChiTiet.addItem(ct);
        }
        cboMaChiTiet.setRenderer((list1, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                ChiTietSanPham ctsp = (ChiTietSanPham) value;
                label.setText(ctsp.getMaChiTiet() + " - " + ctsp.getTenSanPham() + " - " +
                        ctsp.getKichCo() + ", " + ctsp.getMauSac());
            }
            return label;
        });
    }

    private void selectComboBoxItem(int maChiTiet) {
        for (int i = 0; i < cboMaChiTiet.getItemCount(); i++) {
            ChiTietSanPham ct = cboMaChiTiet.getItemAt(i);
            if (ct.getMaChiTiet() == maChiTiet) {
                cboMaChiTiet.setSelectedIndex(i);
                break;
            }
        }
    }

    private void xuLyLuu() {
        try {
            ChiTietSanPham selected = (ChiTietSanPham) cboMaChiTiet.getSelectedItem();
            if (selected == null) {
                FormUIHelper.showErrorMessage("Chưa chọn chi tiết sản phẩm.");
                return;
            }

            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) throw new NumberFormatException();

            ChiTietGioHang newCT = new ChiTietGioHang();
            newCT.setMaChiTiet(selected.getMaChiTiet());
            newCT.setSoLuong(soLuong);
            newCT.setMaGioHang(maGioHang);

            boolean success = false;
            if (isEdit) {
                newCT.setMaChiTietGH(ctgh.getMaChiTietGH());
                success = gioHangDAO.xoaChiTietGioHang(ctgh.getMaChiTietGH()) &&
                          gioHangDAO.themChiTietGioHang(newCT);
            } else {
                success = gioHangDAO.themChiTietGioHang(newCT);
            }

            if (success) {
                FormUIHelper.showSuccessMessage((isEdit ? "Cập nhật" : "Thêm") + " thành công!");
                if (onSuccess != null) onSuccess.run();
                dispose();
            } else {
                FormUIHelper.showErrorMessage((isEdit ? "Cập nhật" : "Thêm") + " thất bại!");
            }

        } catch (NumberFormatException e) {
            FormUIHelper.showErrorMessage("Số lượng phải là số nguyên dương!");
        }
    }
}
