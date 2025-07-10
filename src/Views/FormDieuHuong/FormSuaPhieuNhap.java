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

public class FormSuaPhieuNhap extends JFrame {
    private JComboBox<NhaCungCap> cboNCC;
    private JSpinner spNgayNhap;
    private KhoPanel parent;
    private PhieuNhap pnSua;

    public FormSuaPhieuNhap(KhoPanel parent, PhieuNhap pn) {
        this.parent = parent;
        this.pnSua = pn;
        setResizable(false);
        setTitle("Sửa Phiếu Nhập");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel with border and padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel titleLabel = new JLabel("SỬA PHIẾU NHẬP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel with titled border
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setBorder(new TitledBorder("Thông tin phiếu nhập"));
        
        // Customize combo box
        cboNCC = new JComboBox<>();
        cboNCC.setPreferredSize(new Dimension(200, 30));
        
        // Customize date spinner
        spNgayNhap = new JSpinner(new SpinnerDateModel());
        spNgayNhap.setEditor(new JSpinner.DateEditor(spNgayNhap, "yyyy-MM-dd HH:mm:ss"));
        JSpinner.DateEditor dateEditor = (JSpinner.DateEditor)spNgayNhap.getEditor();
        dateEditor.getTextField().setColumns(15);
        
        // Load data
        loadComboBoxNCC(pn.getMaNCC());
        spNgayNhap.setValue(pn.getNgayNhap());
        
        // Add components to form panel
        formPanel.add(new JLabel("Nhà cung cấp:"));
        formPanel.add(cboNCC);
        formPanel.add(new JLabel("Ngày nhập:"));
        formPanel.add(spNgayNhap);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnLuu = new JButton("Cập nhật");
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

        btnLuu.addActionListener(e -> {
            NhaCungCap selected = (NhaCungCap) cboNCC.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pnSua.setMaNCC(selected.getMaNCC());
            pnSua.setNgayNhap((Date) spNgayNhap.getValue());

            if (new PhieuNhapDAO().suaPhieuNhap(pnSua)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                parent.loadPhieuNhapTable("");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadComboBoxNCC(int maNCCChon) {
        List<NhaCungCap> list = new NhaCungCapDAO().layDanhSach();
        DefaultComboBoxModel<NhaCungCap> model = new DefaultComboBoxModel<>();
        NhaCungCap selectedNCC = null;
        
        for (NhaCungCap ncc : list) {
            model.addElement(ncc);
            if (ncc.getMaNCC() == maNCCChon) {
                selectedNCC = ncc;
            }
        }
        
        cboNCC.setModel(model);
        if (selectedNCC != null) {
            cboNCC.setSelectedItem(selectedNCC);
        }
    }
}