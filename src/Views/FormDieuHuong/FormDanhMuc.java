package Views.FormDieuHuong;

import DAO.DanhMucDAO;
import static Views.UIHelper.FormUIHelper.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class FormDanhMuc extends JDialog {

    private JTable table;
    private DefaultTableModel model;
    private DanhMucDAO dao = new DanhMucDAO();
    private JTextField txtMa, txtTen;

    public FormDanhMuc(Runnable reloadCallback) {
        setTitle("Quản lý danh mục");
        setModal(true);
        setSize(500, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Table
        model = new DefaultTableModel(new String[]{"Mã danh mục", "Tên danh mục"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách danh mục"));

        // Input Panel
        JPanel inputPanel = createTitledPanel("Thông tin danh mục");
        inputPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = createStyledTextField(15);
        txtTen = createStyledTextField(15);

        addFormRow(inputPanel, gbc, 0, "Mã danh mục:", txtMa);
        addFormRow(inputPanel, gbc, 1, "Tên danh mục:", txtTen);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = createStyledButton("Thêm", new Color(34, 139, 34));
        JButton btnXoa = createStyledButton("Xóa", new Color(220, 20, 60));
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);

        // Load dữ liệu
        loadDanhMuc();

        // Sự kiện click để load lên form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtMa.setText(model.getValueAt(row, 0).toString());
                    txtTen.setText(model.getValueAt(row, 1).toString());
                }
            }
        });

        // Sự kiện Thêm
        btnThem.addActionListener(e -> {
            String ma = txtMa.getText().trim();
            String ten = txtTen.getText().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }
            if (dao.themDanhMuc(ma, ten)) {
                JOptionPane.showMessageDialog(this, "Thêm danh mục thành công.");
                loadDanhMuc();
                reloadCallback.run();
                txtMa.setText("");
                txtTen.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Mã có thể đã tồn tại.");
            }
        });

        // Sự kiện Xóa
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để xóa.");
                return;
            }
            String ma = table.getValueAt(row, 0).toString();
            if (dao.coSanPhamThuocDanhMuc(ma)) {
                JOptionPane.showMessageDialog(this, "Không thể xóa danh mục đang được sử dụng.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.xoaDanhMuc(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công.");
                    loadDanhMuc();
                    reloadCallback.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại.");
                }
            }
        });

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDanhMuc() {
        model.setRowCount(0);
        List<String[]> list = dao.layTatCaDanhMuc();
        for (String[] dm : list) {
            model.addRow(dm);
        }
    }
}
