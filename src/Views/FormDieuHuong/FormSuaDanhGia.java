package Views.FormDieuHuong;

import DAO.DanhGiaDAO;
import Models.DanhGia;
import Models.NguoiDung;

import javax.swing.*;
import java.awt.*;

public class FormSuaDanhGia extends JDialog {
    public FormSuaDanhGia(JFrame parent, int maSP, NguoiDung nguoiDung) {
        super(parent, "Sửa đánh giá", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        DanhGiaDAO dao = new DanhGiaDAO();
        DanhGia danhGia = dao.layDanhGiaTheoNguoiDung(maSP, nguoiDung.getMaNguoiDung());

        if (danhGia == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy đánh giá để sửa.");
            dispose();
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblSao = new JLabel("Chỉnh số sao (1-5):");
        JComboBox<Integer> cbSao = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        cbSao.setSelectedItem(danhGia.getSoSao());

        JLabel lblBinhLuan = new JLabel("Chỉnh bình luận:");
        JTextArea txtBinhLuan = new JTextArea(4, 20);
        txtBinhLuan.setText(danhGia.getBinhLuan());
        txtBinhLuan.setLineWrap(true);
        txtBinhLuan.setWrapStyleWord(true);

        JButton btnCapNhat = new JButton("Cập nhật");
        btnCapNhat.setBackground(new Color(255, 193, 7));
        btnCapNhat.setForeground(Color.BLACK);
        btnCapNhat.addActionListener(e -> {
            danhGia.setSoSao((int) cbSao.getSelectedItem());
            danhGia.setBinhLuan(txtBinhLuan.getText());
            danhGia.setNgayDanhGia(new java.util.Date()); // Gán ngày hiện tại
            if (dao.capNhatDanhGia(danhGia)) {
                JOptionPane.showMessageDialog(this, "Cập nhật đánh giá thành công.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
            }
        });

        panel.add(lblSao);
        panel.add(cbSao);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblBinhLuan);
        panel.add(new JScrollPane(txtBinhLuan));
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnCapNhat);

        add(panel, BorderLayout.CENTER);
    }
}
