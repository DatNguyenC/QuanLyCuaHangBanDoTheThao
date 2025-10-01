package Views.MenuPanel;

import DAO.ChiTietHoaDonDAO;
import DAO.ChiTietSanPhamDAO;
import Views.UIHelper.FormUIHelper;
import DAO.HoaDonDAO;
import DAO.SanPhamDAO;
import Models.ChiTietHoaDon;
import Models.ChiTietSanPham;
import Models.HoaDon;
import Models.SanPham;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

public class ThongKePanel extends JPanel {

    private CardLayout chartLayout;
    private JPanel chartContainer;

    public ThongKePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === LEFT: Panel chức năng ===
        JPanel leftPanel = FormUIHelper.createTitledPanel("Chức năng thống kê");
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(280, 0));
        leftPanel.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));

        JButton btnDoanhThu = FormUIHelper.createStyledButton("Thống kê doanh thu hàng tháng", new Color(52, 152, 219));
        JButton btnSanPhamBanChay = FormUIHelper.createStyledButton("Thống kê sản phẩm bán chạy", new Color(46, 204, 113));
        JButton btnBaoCao = FormUIHelper.createStyledButton("Báo cáo tổng hợp", new Color(231, 76, 60));

        Dimension btnSize = new Dimension(240, 45);
        for (JButton btn : new JButton[]{btnDoanhThu, btnSanPhamBanChay, btnBaoCao}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(btnDoanhThu);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(btnSanPhamBanChay);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(btnBaoCao);
        leftPanel.add(Box.createVerticalGlue());

        // === RIGHT: Biểu đồ ===
        chartLayout = new CardLayout();
        chartContainer = FormUIHelper.createTitledPanel("Biểu đồ thống kê");
        chartContainer.setLayout(chartLayout);

        chartContainer.add(createCenteredLabel("📊 Chưa có biểu đồ"), "EMPTY");

        add(leftPanel, BorderLayout.WEST);
        add(chartContainer, BorderLayout.CENTER);

        // === Sự kiện ===
        btnDoanhThu.addActionListener((ActionEvent e) -> {
            chartContainer.removeAll();
            chartContainer.revalidate();
            chartContainer.repaint();
            chartContainer.add(createDoanhThuChartPanel(), "DOANHTHU");
            chartLayout.show(chartContainer, "DOANHTHU");
        });

        btnSanPhamBanChay.addActionListener((ActionEvent e) -> {
            chartContainer.removeAll();
            chartContainer.revalidate();
            chartContainer.repaint();
            chartContainer.add(createSanPhamBanChayChartPanel(), "SANPHAM");
            chartLayout.show(chartContainer, "SANPHAM");
        });

        btnBaoCao.addActionListener((ActionEvent e) -> {
            taoBaoCaoTongHopWord(); // Gọi hàm xuất Word
        });

    }

    private JPanel createDoanhThuChartPanel() {
        Map<String, Double> doanhThuTheoThang = new TreeMap<>();

        for (HoaDon hd : new HoaDonDAO().layTatCaHoaDon()) {
            if (!"DaThanhToan".equalsIgnoreCase(hd.getTrangThai())) {
                continue;
            }

            LocalDateTime ngay = hd.getNgayLap();
            String key = String.format("%02d/%d", ngay.getMonthValue(), ngay.getYear()); // VD: 07/2025
            doanhThuTheoThang.put(key, doanhThuTheoThang.getOrDefault(key, 0.0) + hd.getTongTien());
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : doanhThuTheoThang.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
        }

        // === Tạo biểu đồ ===
        JFreeChart chart = ChartFactory.createBarChart(
                "Biểu đồ doanh thu theo tháng", // Tiêu đề
                "Tháng", // Trục X
                "Tổng doanh thu (VNĐ)", // Trục Y
                dataset
        );

        // === Trang trí biểu đồ ===
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        chart.setBackgroundPaint(Color.WHITE);
        chart.addSubtitle(new TextTitle("(Chỉ bao gồm các hóa đơn đã thanh toán)", new Font("Segoe UI", Font.ITALIC, 12)));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Trục X
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        xAxis.setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Trục Y
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        yAxis.setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);
        yAxis.setNumberFormatOverride(currencyFormat);

        // Cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 128, 185)); // Màu xanh dương
        renderer.setSeriesToolTipGenerator(0, new StandardCategoryToolTipGenerator());

        return new ChartPanel(chart);
    }

    private JPanel createSanPhamBanChayChartPanel() {
        Map<Integer, Integer> maSPToSoLuong = new HashMap<>();
        ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
        ChiTietSanPhamDAO ctspDAO = new ChiTietSanPhamDAO();
        SanPhamDAO sanPhamDAO = new SanPhamDAO();

        for (ChiTietHoaDon ct : cthdDAO.layTatCa()) {
            int maCT = ct.getMaChiTiet();
            ChiTietSanPham ctsp = ctspDAO.timTheoMa(maCT);
            if (ctsp == null) {
                continue;
            }

            int maSP = ctsp.getMaSanPham();
            maSPToSoLuong.put(maSP, maSPToSoLuong.getOrDefault(maSP, 0) + ct.getSoLuong());
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Integer> entry : maSPToSoLuong.entrySet()) {
            int maSP = entry.getKey();
            int tongSoLuong = entry.getValue();

            String tenSP = sanPhamDAO.laySanPhamTheoMa(maSP).getTenSanPham();
            dataset.addValue(tongSoLuong, "Số lượng bán", tenSP);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top sản phẩm bán chạy",
                "Sản phẩm",
                "Số lượng bán",
                dataset
        );

        // Làm đẹp
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(46, 204, 113));
        renderer.setItemMargin(0.05f);

        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));

        return new ChartPanel(chart);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + text + "</div></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void taoBaoCaoTongHopWord() {
        int result = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc chắn muốn xuất báo cáo tổng hợp ra file Word?",
                "Xác nhận xuất báo cáo",
                JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu báo cáo Word");
        fileChooser.setSelectedFile(new File("BaoCaoTongHop.docx"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // Người dùng hủy chọn file
        }

        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.getName().toLowerCase().endsWith(".docx")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".docx");
        }

        try {
            XWPFDocument doc = new XWPFDocument();

            // Tiêu đề chính
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("BÁO CÁO TỔNG HỢP DOANH THU");
            titleRun.setBold(true);
            titleRun.setFontSize(18);

            // === Doanh thu theo tháng ===
            XWPFParagraph header1 = doc.createParagraph();
            XWPFRun run1 = header1.createRun();
            run1.setText("1. Thống kê doanh thu theo tháng");
            run1.setBold(true);
            run1.setFontSize(14);

            Map<String, Double> doanhThuTheoThang = new TreeMap<>();
            for (HoaDon hd : new HoaDonDAO().layTatCaHoaDon()) {
                if (!"DaThanhToan".equals(hd.getTrangThai())) {
                    continue;
                }
                LocalDateTime ngay = hd.getNgayLap();
                String key = ngay.getMonthValue() + "/" + ngay.getYear();
                doanhThuTheoThang.put(key, doanhThuTheoThang.getOrDefault(key, 0.0) + hd.getTongTien());
            }

            XWPFTable table1 = doc.createTable();
            XWPFTableRow headerRow1 = table1.getRow(0);
            headerRow1.getCell(0).setText("Tháng/Năm");
            headerRow1.addNewTableCell().setText("Tổng doanh thu (VNĐ)");

            NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            for (Map.Entry<String, Double> entry : doanhThuTheoThang.entrySet()) {
                XWPFTableRow row = table1.createRow();
                row.getCell(0).setText(entry.getKey());
                row.getCell(1).setText(currency.format(entry.getValue()));
            }

            // === Sản phẩm bán chạy ===
            XWPFParagraph header2 = doc.createParagraph();
            XWPFRun run2 = header2.createRun();
            run2.setText("2. Sản phẩm bán chạy nhất");
            run2.setBold(true);
            run2.setFontSize(14);

            Map<Integer, Integer> sanPhamSoLuong = new HashMap<>();
            ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
            SanPhamDAO sanPhamDAO = new SanPhamDAO();
            ChiTietSanPhamDAO ctspDAO = new ChiTietSanPhamDAO();

            for (ChiTietHoaDon cthd : cthdDAO.layTatCa()) {
                int maSP = ctspDAO.layMaSPTheoChiTiet(cthd.getMaChiTiet());
                sanPhamSoLuong.put(maSP, sanPhamSoLuong.getOrDefault(maSP, 0) + cthd.getSoLuong());
            }

            List<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(sanPhamSoLuong.entrySet());
            sorted.sort((a, b) -> b.getValue() - a.getValue());

            XWPFTable table2 = doc.createTable();
            XWPFTableRow headerRow2 = table2.getRow(0);
            headerRow2.getCell(0).setText("Tên sản phẩm");
            headerRow2.addNewTableCell().setText("Số lượng bán");

            for (Map.Entry<Integer, Integer> entry : sorted) {
                SanPham sp = sanPhamDAO.laySanPhamTheoMa(entry.getKey());
                XWPFTableRow row = table2.createRow();
                row.getCell(0).setText(sp != null ? sp.getTenSanPham() : "Không rõ");
                row.getCell(1).setText(String.valueOf(entry.getValue()));
            }

            try (FileOutputStream out = new FileOutputStream(selectedFile)) {
                doc.write(out);
            }

            JOptionPane.showMessageDialog(null, "Đã xuất báo cáo thành công:\n" + selectedFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo báo cáo: " + ex.getMessage());
        }
    }

}
