package Views.FormDieuHuong;


import DAO.ChiTietNhapDAO;
import Models.ChiTietNhap;
import Views.MenuPanel.KhoPanel;
import static Views.UIHelper.FormUIHelper.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FormSuaChiTietNhap extends JDialog {
    private final JComboBox<Integer> cbMaPhieuNhap;
    private final JComboBox<Integer> cbMaChiTiet;
    private final JTextField txtSoLuongNhap;
    private final JTextField txtDonGiaNhap;
    private final int maChiTietPN;
    private final KhoPanel parent;

    public FormSuaChiTietNhap(KhoPanel parent, ChiTietNhap ctn) {
        this.parent = parent;
        this.maChiTietPN = ctn.getMaChiTietPN();

        setTitle("CẬP NHẬT CHI TIẾT PHIẾU NHẬP");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel titleLabel = new JLabel("CẬP NHẬT CHI TIẾT PHIẾU NHẬP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 130, 180));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel with styled border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180)),
            "Thông tin chi tiết",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(70, 130, 180)
        ));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Create styled components
        cbMaPhieuNhap = new JComboBox<>(getMaPhieuNhapList());
        cbMaPhieuNhap.setPreferredSize(new Dimension(200, 30));
        cbMaPhieuNhap.setSelectedItem(ctn.getMaPhieuNhap());
        
        cbMaChiTiet = new JComboBox<>(getMaChiTietList());
        cbMaChiTiet.setPreferredSize(new Dimension(200, 30));
        cbMaChiTiet.setSelectedItem(ctn.getMaChiTiet());
        
        txtSoLuongNhap = createStyledTextField(10);
        txtSoLuongNhap.setText(String.valueOf(ctn.getSoLuongNhap()));
        
        txtDonGiaNhap = createStyledTextField(10);
        txtDonGiaNhap.setText(String.valueOf(ctn.getDonGiaNhap()));
        txtDonGiaNhap.setHorizontalAlignment(JTextField.RIGHT);

        // Add form components
        int gridRow = 0;
        addFormRow(formPanel, gbc, gridRow++, "Mã phiếu nhập:", cbMaPhieuNhap);
        addFormRow(formPanel, gbc, gridRow++, "Mã chi tiết SP:", cbMaChiTiet);
        addFormRow(formPanel, gbc, gridRow++, "Số lượng nhập:", txtSoLuongNhap);
        addFormRow(formPanel, gbc, gridRow++, "Đơn giá nhập (VND):", txtDonGiaNhap);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton btnSua = createStyledButton("Cập nhật", new Color(255, 140, 0)); // Orange for update
        JButton btnHuy = createStyledButton("Hủy", new Color(220, 20, 60)); // Red for cancel
        
        btnSua.addActionListener(this::handleSua);
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnHuy);
        buttonPanel.add(btnSua);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSua(ActionEvent e) {
        try {
            int maPhieuNhap = (int) cbMaPhieuNhap.getSelectedItem();
            int maChiTiet = (int) cbMaChiTiet.getSelectedItem();
            int soLuong = Integer.parseInt(txtSoLuongNhap.getText());
            double donGia = Double.parseDouble(txtDonGiaNhap.getText());

            ChiTietNhap ctn = new ChiTietNhap(maChiTietPN, maPhieuNhap, maChiTiet, soLuong, donGia);
            if (new ChiTietNhapDAO().suaChiTietNhap(ctn)) {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật chi tiết phiếu nhập thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                parent.loadChiTietPhieuNhapTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật chi tiết phiếu nhập thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đúng định dạng số!", 
                "Lỗi nhập liệu", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Đã xảy ra lỗi khi cập nhật chi tiết!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper methods (same as in FormThemChiTietNhap)





    private Integer[] getMaPhieuNhapList() {
        return new DAO.PhieuNhapDAO().layDanhSach().stream()
            .map(pn -> pn.getMaPhieuNhap())
            .toArray(Integer[]::new);
    }

    private Integer[] getMaChiTietList() {
        return new DAO.ChiTietSanPhamDAO().layDanhSach().stream()
            .map(ct -> ct.getMaChiTiet())
            .toArray(Integer[]::new);
    }
}