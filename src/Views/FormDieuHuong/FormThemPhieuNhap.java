package Views.FormDieuHuong;

import DAO.NhaCungCapDAO;
import DAO.PhieuNhapDAO;
import Models.NhaCungCap;
import Models.PhieuNhap;
import Views.MenuPanel.KhoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class FormThemPhieuNhap extends JFrame {

    private JComboBox<NhaCungCap> cboNCC;
    private JSpinner spNgayNhap;
    private KhoPanel parent;

    public FormThemPhieuNhap(KhoPanel parent) {
        this.parent = parent;
        setResizable(false);
        setTitle("Tạo Phiếu Nhập");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel titleLabel = new JLabel("THÊM PHIẾU NHẬP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setBorder(new TitledBorder("Thông tin phiếu nhập"));

        cboNCC = new JComboBox<>();
        cboNCC.setPreferredSize(new Dimension(200, 30));

        spNgayNhap = new JSpinner(new SpinnerDateModel());
        spNgayNhap.setEditor(new JSpinner.DateEditor(spNgayNhap, "yyyy-MM-dd HH:mm:ss"));
        JSpinner.DateEditor dateEditor = (JSpinner.DateEditor) spNgayNhap.getEditor();
        dateEditor.getTextField().setColumns(15);

        formPanel.add(new JLabel("Nhà cung cấp:"));
        formPanel.add(cboNCC);
        formPanel.add(new JLabel("Ngày nhập:"));
        formPanel.add(spNgayNhap);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(100, 30));
        btnLuu.setBackground(new Color(0, 153, 51));
        btnLuu.setForeground(Color.WHITE);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 30));
        btnHuy.setBackground(new Color(204, 0, 0));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.addActionListener(e -> dispose());

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnLuu);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load ComboBox
        loadComboBoxNCC();

        // ===== Xử lý nút Lưu =====
        btnLuu.addActionListener(e -> {
            NhaCungCap selected = (NhaCungCap) cboNCC.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Chưa chọn nhà cung cấp.");
                return;
            }

            PhieuNhap pn = new PhieuNhap();
            pn.setMaNCC(selected.getMaNCC());
            pn.setNgayNhap((Date) spNgayNhap.getValue());

            boolean success = new PhieuNhapDAO().themPhieuNhap(pn);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thành công!");
                parent.loadPhieuNhapTable("");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phiếu nhập thất bại!");
            }
        });
    }

    private void loadComboBoxNCC() {
        List<NhaCungCap> list = new NhaCungCapDAO().layDanhSach();
        DefaultComboBoxModel<NhaCungCap> model = new DefaultComboBoxModel<>();
        for (NhaCungCap ncc : list) {
            model.addElement(ncc);
        }
        cboNCC.setModel(model);
    }
}
