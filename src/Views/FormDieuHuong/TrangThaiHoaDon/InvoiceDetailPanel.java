package Views.FormDieuHuong.TrangThaiHoaDon;

import DAO.ChiTietHoaDonDAO;
import DAO.ChiTietSanPhamDAO;
import Models.ChiTietHoaDon;
import Models.ChiTietSanPham;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InvoiceDetailPanel extends JPanel {

    private final ChiTietHoaDonDAO chiTietHoaDonDAO;
    private final ChiTietSanPhamDAO chiTietSanPhamDAO;
    private final int maHoaDon;
    private JLabel lblDetail;
    private JPanel imagePanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;

    public InvoiceDetailPanel(int maHoaDon) {
        this.maHoaDon = maHoaDon;
        this.chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        this.chiTietSanPhamDAO = new ChiTietSanPhamDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(232, 247, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Chi Tiết Hóa Đơn #" + maHoaDon);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 100, 158));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.PAGE_START);

        // Panel danh sách từng dòng hóa đơn
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        updateListPanel();
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateDetail() {
        List<ChiTietHoaDon> details = chiTietHoaDonDAO.layChiTietTheoMaHoaDon(maHoaDon);
        StringBuilder detailText = new StringBuilder("<html><body>");
        boolean hasDetails = false;

        for (ChiTietHoaDon cthd : details) {
            ChiTietSanPham ctsp = chiTietSanPhamDAO.layChiTietSanPhamTheoMa(cthd.getMaChiTiet());
            if (ctsp != null) {
                hasDetails = true;
                detailText.append("<div style='margin-bottom: 5px;'>")
                        .append("Kích cỡ: ").append(ctsp.getKichCo())
                        .append(", Màu sắc: ").append(ctsp.getMauSac())
                        .append(", Số lượng: ").append(cthd.getSoLuong())
                        .append("</div>");
            }
        }
        detailText.append("</body></html>");

        if (!hasDetails) {
            lblDetail.setText("<html><body>Không có chi tiết hóa đơn nào.</body></html>");
        } else {
            lblDetail.setText(detailText.toString());
        }
    }

    private void updateListPanel() {
        listPanel.removeAll();
        List<ChiTietHoaDon> details = chiTietHoaDonDAO.layChiTietTheoMaHoaDon(maHoaDon);
        ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();
        boolean hasDetails = false;
        int rowHeight = 140;
        int maxHeight = 350;
        int rowCount = 0;
        for (ChiTietHoaDon cthd : details) {
            ChiTietSanPham ctsp = chiTietSanPhamDAO.layChiTietSanPhamTheoMa(cthd.getMaChiTiet());
            if (ctsp != null) {
                hasDetails = true;
                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new BorderLayout(10, 0));
                rowPanel.setOpaque(false);
                String fileName = ctsp.getHinhAnhChiTiet();
                JLabel imageLabel = new JLabel();
                imageLabel.setPreferredSize(new Dimension(120, 120));
                if (fileName != null && !fileName.trim().isEmpty()) {
                    Path dest = Paths.get("src/anhSP/" + fileName);
                    try {
                        if (Files.exists(dest)) {
                            ImageIcon imageIcon = new ImageIcon(dest.toAbsolutePath().toString());
                            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                                Image image = imageIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                                imageLabel.setIcon(new ImageIcon(image));
                            } else {
                                imageLabel.setText("<html><center>Ảnh lỗi<br>(Không tải được)</center></html>");
                            }
                        } else {
                            imageLabel.setText("<html><center>Không tìm thấy<br>hình ảnh</center></html>");
                        }
                    } catch (Exception e) {
                        imageLabel.setText("<html><center>Lỗi tải<br>hình ảnh</center></html>");
                    }
                } else {
                    imageLabel.setText("<html><center>Không có<br>hình ảnh</center></html>");
                }
                rowPanel.add(imageLabel, BorderLayout.WEST);
                StringBuilder info = new StringBuilder("<html>");
                info.append("Tên sản phẩm: SP#").append(ctsp.getMaSanPham())
                    .append("<br>Kích cỡ: ").append(ctsp.getKichCo())
                    .append("<br>Màu sắc: ").append(ctsp.getMauSac())
                    .append("<br>Số lượng: ").append(cthd.getSoLuong())
                    .append("<br>Đơn giá: ").append(String.format("%,.0f", cthd.getDonGia()))
                    .append("<br>Thành tiền: ").append(String.format("%,.0f", cthd.getDonGia() * cthd.getSoLuong()))
                    .append("</html>");
                JLabel infoLabel = new JLabel(info.toString());
                infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                rowPanel.add(infoLabel, BorderLayout.CENTER);
                rowPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200,200,200)));
                rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
                listPanel.add(rowPanel);
                rowCount++;
            }
        }
        if (!hasDetails) {
            JLabel noDetail = new JLabel("<html><body>Không có chi tiết hóa đơn nào.</body></html>");
            listPanel.add(noDetail);
            rowCount = 1;
        }
        int preferredHeight = Math.min(rowCount * rowHeight, maxHeight);
        scrollPane.setPreferredSize(new Dimension(400, preferredHeight));
        scrollPane.setMaximumSize(new Dimension(400, maxHeight));
        listPanel.revalidate();
        listPanel.repaint();
    }

    // Không còn cần setVisible ghi đè nếu không xử lý đặc biệt khi ẩn panel
}
