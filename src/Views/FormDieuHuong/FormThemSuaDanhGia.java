package Views.FormDieuHuong;

import DAO.DanhGiaDAO;
import Models.DanhGia;
import Models.NguoiDung;

import javax.swing.*;
import java.awt.*;

public class FormThemSuaDanhGia extends JDialog {

    public FormThemSuaDanhGia(JFrame parent, int maSP, NguoiDung nguoiDung) {
        super(parent, "Thêm đánh giá", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblSao = new JLabel("Chọn số sao (1-5):");
        JComboBox<Integer> cbSao = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        JLabel lblBinhLuan = new JLabel("Bình luận:");
        JTextArea txtBinhLuan = new JTextArea(4, 20);
        txtBinhLuan.setLineWrap(true);
        txtBinhLuan.setWrapStyleWord(true);

        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setBackground(new Color(0, 123, 255));
        btnXacNhan.setForeground(Color.WHITE);

        btnXacNhan.addActionListener(e -> {
            DanhGia dg = new DanhGia();
            dg.setMaSanPham(String.valueOf(maSP));
            dg.setMaNguoiDung(nguoiDung.getMaNguoiDung());
            dg.setSoSao((int) cbSao.getSelectedItem());
            dg.setBinhLuan(txtBinhLuan.getText());
            dg.setNgayDanhGia(new java.util.Date()); // Gán ngày hiện tại
            if (new DanhGiaDAO().themDanhGia(dg)) {
                JOptionPane.showMessageDialog(this, "Đánh giá đã được thêm.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm đánh giá thất bại.");
            }
        });

        panel.add(lblSao);
        panel.add(cbSao);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblBinhLuan);
        panel.add(new JScrollPane(txtBinhLuan));
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnXacNhan);

        add(panel, BorderLayout.CENTER);
    }
}
