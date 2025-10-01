package Views.FormDieuHuong;

import DAO.ChiTietGioHangDAO;
import DAO.KhuyenMaiDAO;
import DAO.SanPhamDAO;
import DAO.HoaDonDAO;
import DAO.ChiTietHoaDonDAO;
import DAO.GioHangDAO;
import DAO.ChiTietSanPhamDAO;

import Models.ChiTietGioHang;
import Models.HoaDon;
import Models.ChiTietHoaDon;
import Models.KhuyenMai;
import Models.NguoiDung;
import Models.SanPham;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Views.UIHelper.FormUIHelper.*;

public class FormTaoHoaDon extends JDialog {

    private final int maGioHang;
    private final NguoiDung nguoiDung;

    private JPanel pnlKhuyenMai = new JPanel(new GridLayout(0, 1));
    private List<JCheckBox> chkKhuyenMaiList = new ArrayList<>();

    private JComboBox<String> cmbHinhThuc = new JComboBox<>(new String[]{"Bán trực tiếp", "Giao hàng (+15K)"});
    private JTextField txtTongTien = new JTextField(15);
    private JTextField txtThanhToan = new JTextField(15);

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final ChiTietGioHangDAO chiTietGioHangDAO = new ChiTietGioHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    private final ChiTietSanPhamDAO chiTietSanPhamDAO = new ChiTietSanPhamDAO();

    private double tongTien = 0;

    public FormTaoHoaDon(JFrame owner, int maGioHang, NguoiDung nguoiDung) {
        super(owner, "Tạo Đơn Hàng", true);
        this.maGioHang = maGioHang;
        this.nguoiDung = nguoiDung;
        setSize(500, 480);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addFormRow(form, gbc, row++, "Mã Giỏ Hàng:", new JTextField(String.valueOf(maGioHang), 15) {
            {
                setEditable(false);
            }
        });

        JScrollPane spKhuyenMai = new JScrollPane(pnlKhuyenMai);
        spKhuyenMai.setPreferredSize(new Dimension(350, 100));
        addFormRow(form, gbc, row++, "Khuyến mãi:", spKhuyenMai);
        addFormRow(form, gbc, row++, "Hình thức:", cmbHinhThuc);
        addFormRow(form, gbc, row++, "Tổng tiền:", txtTongTien);
        addFormRow(form, gbc, row++, "Thanh toán:", txtThanhToan);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnTao = createStyledButton("Tạo đơn", new Color(70, 130, 180));
        JButton btnHuy = createStyledButton("Hủy", new Color(220, 20, 60));
        bottom.add(btnTao);
        bottom.add(btnHuy);
        add(bottom, BorderLayout.SOUTH);

        loadKhuyenMai();
        tinhTien();

        cmbHinhThuc.addActionListener(e -> tinhTien());
        btnHuy.addActionListener(e -> dispose());

        btnTao.addActionListener(e -> {
            tinhTien(); // Tính lại tiền trước khi tạo đơn

            // Lấy danh sách chi tiết giỏ hàng
            List<ChiTietGioHang> dsCT = chiTietGioHangDAO.layChiTietTheoGioHang(maGioHang);

            // Kiểm tra tồn kho
            for (ChiTietGioHang ct : dsCT) {
                int maChiTiet = ct.getMaChiTiet();
                int soLuongDat = ct.getSoLuong();
                int soLuongTon = sanPhamDAO.laySoLuongTon(maChiTiet);

                if (soLuongTon < soLuongDat) {
                    showErrorMessage("Không đủ hàng trong kho cho sản phẩm mã chi tiết " + maChiTiet
                            + ".\nTồn kho: " + soLuongTon + " | Đặt mua: " + soLuongDat);
                    return;
                }
            }

            // Tạo hóa đơn
            HoaDon hd = new HoaDon();
            hd.setMaNguoiDung(nguoiDung.getMaNguoiDung());
            hd.setNgayLap(LocalDateTime.now());
            if (cmbHinhThuc.getSelectedItem().toString().equals("Bán trực tiếp")) {
                hd.setTrangThai("ChuaThanhToan");
            } else {
                hd.setTrangThai("DangGiao");
            }

            hd.setTongTien(Double.parseDouble(txtThanhToan.getText()));

            List<KhuyenMai> dsKM = getKhuyenMaiDuocChon();
            if (!dsKM.isEmpty()) {
                // Có thể lưu nhiều mã KM, nhưng hiện tại chỉ lưu mã KM đầu tiên (có thể mở rộng logic lưu nhiều KM)
                hd.setMaKM(dsKM.get(0).getMaKM());
            }

            int maHD = hoaDonDAO.themVaLayMa(hd);

            // Tạo chi tiết hóa đơn
            for (ChiTietGioHang ct : dsCT) {
                int maChiTiet = ct.getMaChiTiet();
                double gia = 0;
                Models.ChiTietSanPham ctsp = chiTietSanPhamDAO.layChiTietSanPhamTheoMa(maChiTiet);
                if (ctsp != null) {
                    gia = ctsp.getGiaThanh();
                }
                int soLuong = ct.getSoLuong();
                double thanhTien = gia * soLuong;

                SanPham sp = sanPhamDAO.laySanPhamTheoMaChiTiet(maChiTiet);
                for (KhuyenMai km : dsKM) {
                    if (km.getMaSanPham() == sp.getMaSanPham()) {
                        thanhTien -= thanhTien * (km.getPhanTramGiam() / 100.0);
                        break;
                    }
                }

                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setMaHoaDon(maHD);
                cthd.setMaChiTiet(maChiTiet);
                cthd.setSoLuong(soLuong);
                cthd.setDonGia(thanhTien / soLuong);

                chiTietHoaDonDAO.themChiTiet(cthd);
            }

            GioHangDAO gioHangDAO = new GioHangDAO();
            gioHangDAO.xoaGioHang(maGioHang);

            showSuccessMessage("Tạo đơn hàng thành công!\nMã HĐ: " + maHD
                    + "\nTổng thanh toán: " + txtThanhToan.getText());
            dispose();
        });
    }

    private void loadKhuyenMai() {
        pnlKhuyenMai.removeAll();
        chkKhuyenMaiList.clear();

        JCheckBox chkNone = new JCheckBox("Không áp dụng khuyến mãi");
        chkNone.setSelected(true);
        chkNone.addActionListener(e -> tinhTien());
        pnlKhuyenMai.add(chkNone);

        List<ChiTietGioHang> dsCT = chiTietGioHangDAO.layChiTietTheoGioHang(maGioHang);
        List<Integer> maSanPhamTrongGio = new ArrayList<>();
        for (ChiTietGioHang ct : dsCT) {
            SanPham sp = sanPhamDAO.laySanPhamTheoMaChiTiet(ct.getMaChiTiet());
            if (sp != null && !maSanPhamTrongGio.contains(sp.getMaSanPham())) {
                maSanPhamTrongGio.add(sp.getMaSanPham());
            }
        }

        List<KhuyenMai> ds = khuyenMaiDAO.layTatCa();
        for (KhuyenMai km : ds) {
            if (!maSanPhamTrongGio.contains(km.getMaSanPham())) {
                continue;
            }

            SanPham sp = sanPhamDAO.laySanPhamTheoMa(km.getMaSanPham());
            String tenSP = (sp != null) ? sp.getTenSanPham() : "Không rõ";
            String label = String.format("KM#%d - %s (%d%%) → %s (Mã SP: %d)",
                    km.getMaKM(), km.getTenKM(), (int) km.getPhanTramGiam(), tenSP, km.getMaSanPham());

            JCheckBox chk = new JCheckBox(label);
            chk.putClientProperty("data", km);
            chk.addActionListener(e -> tinhTien());
            chkKhuyenMaiList.add(chk);
            pnlKhuyenMai.add(chk);
        }

        pnlKhuyenMai.revalidate();
        pnlKhuyenMai.repaint();
    }

    private void tinhTien() {
        List<ChiTietGioHang> dsCT = chiTietGioHangDAO.layChiTietTheoGioHang(maGioHang);
        tongTien = 0;

        List<KhuyenMai> dsKM = getKhuyenMaiDuocChon();

        for (ChiTietGioHang ct : dsCT) {
            int maChiTiet = ct.getMaChiTiet();
            double gia = 0;
            Models.ChiTietSanPham ctsp = chiTietSanPhamDAO.layChiTietSanPhamTheoMa(maChiTiet);
            if (ctsp != null) {
                gia = ctsp.getGiaThanh();
            }
            int soLuong = ct.getSoLuong();
            double thanhTien = gia * soLuong;

            SanPham sp = sanPhamDAO.laySanPhamTheoMaChiTiet(maChiTiet);

            // Áp dụng tất cả mã khuyến mãi phù hợp với sản phẩm
            for (KhuyenMai km : dsKM) {
                if (km.getMaSanPham() == sp.getMaSanPham()) {
                    thanhTien -= thanhTien * (km.getPhanTramGiam() / 100.0);
                    break; // Chỉ áp dụng một KM cho mỗi sản phẩm (có thể bỏ break nếu muốn cộng dồn KM)
                }
            }

            tongTien += thanhTien;
        }

        double ship = cmbHinhThuc.getSelectedIndex() == 1 ? 15000 : 0;
        double thanhToan = tongTien + ship;

        txtTongTien.setText(String.format("%.0f", tongTien));
        txtThanhToan.setText(String.format("%.0f", thanhToan));
    }

    private List<KhuyenMai> getKhuyenMaiDuocChon() {
        List<KhuyenMai> selectedKM = new ArrayList<>();
        for (JCheckBox chk : chkKhuyenMaiList) {
            if (chk.isSelected()) {
                selectedKM.add((KhuyenMai) chk.getClientProperty("data"));
            }
        }
        return selectedKM;
    }
}
