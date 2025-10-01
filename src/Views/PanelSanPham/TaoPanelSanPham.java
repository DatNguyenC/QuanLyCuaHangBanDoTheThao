package Views.PanelSanPham;

import Models.NguoiDung;
import Views.PanelSanPham.FormThongTinSanPham;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaoPanelSanPham {

    public static JPanel taoPanelSanPham(String ten, String gia, String moTa, String soLuong, String hinhAnh, NguoiDung nguoiDungDangNhap) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Nền trắng
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Viền xanh bo góc
                g2.setColor(new Color(0, 122, 192));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(200, 280));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Ảnh sản phẩm =====
        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnh.setPreferredSize(new Dimension(180, 140));
        try {
            ImageIcon icon = new ImageIcon("src/anhSP/" + hinhAnh);
            Image img = icon.getImage().getScaledInstance(180, 140, Image.SCALE_SMOOTH);
            lblAnh.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblAnh.setText("Không có ảnh");
        }
        panel.add(lblAnh);

        lblAnh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openThongTinForm(panel, ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblAnh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });

        // ===== Tên sản phẩm =====
        JLabel lblTen = new JLabel(ten, JLabel.CENTER);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTen.setForeground(new Color(0, 100, 158));
        panel.add(lblTen);

        // ===== Giá =====
        JLabel lblGia = new JLabel("Giá: " + gia + " VNĐ", JLabel.CENTER);
        lblGia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGia.setForeground(Color.RED);
        lblGia.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblGia);

        // ===== Mô tả =====
        JTextArea txtMoTa = new JTextArea(moTa);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setEditable(false);
        txtMoTa.setOpaque(false);
        txtMoTa.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtMoTa.setMaximumSize(new Dimension(180, 50));
        txtMoTa.setForeground(new Color(0, 73, 115));
        panel.add(txtMoTa);

        // ===== Số lượng =====
        JLabel lblSoLuong = new JLabel("Còn: " + soLuong + " sản phẩm", JLabel.CENTER);
        lblSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSoLuong.setForeground(Color.GRAY);
        lblSoLuong.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSoLuong);

        // ===== Nút thêm vào giỏ hàng =====
        JButton btnThem = new JButton("Thêm vào giỏ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(0, 123, 255));
        btnThem.setFocusPainted(false);
        btnThem.setBorderPainted(false);
        btnThem.setContentAreaFilled(false);
        btnThem.setOpaque(false);
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThem.setMaximumSize(new Dimension(130, 35));

        // 👉 Gọi sự kiện giống click ảnh
        btnThem.addActionListener(e -> {
            openThongTinForm(panel, ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap);
        });

        panel.add(Box.createVerticalStrut(5));
        panel.add(btnThem);

        return panel;
    }

    private static void openThongTinForm(JPanel panel, String ten, String gia, String moTa, String soLuong, String hinhAnh, NguoiDung nguoiDungDangNhap) {
        SwingUtilities.invokeLater(() -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            new FormThongTinSanPham(topFrame, ten, gia, moTa, soLuong, hinhAnh, nguoiDungDangNhap).setVisible(true);
        });
    }
}
