package Views.FormDieuHuong;

import DAO.GioHangDAO;
import DAO.NguoiDungDAO;
import Models.GioHang;
import Models.NguoiDung;
import Views.UIHelper.FormUIHelper;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class FormTaoGioHang extends JDialog {
    private JComboBox<NguoiDung> cboNguoiDung;
    private JButton btnThem, btnHuy;
    private final GioHangDAO gioHangDAO = new GioHangDAO();
    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final Runnable callbackReload;

    public FormTaoGioHang(JFrame parent, Runnable callbackReload) {
        super(parent, "Tạo Giỏ Hàng", true);
        this.callbackReload = callbackReload;
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel nhập liệu
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panel.add(new JLabel("Chọn người dùng:"));
        cboNguoiDung = new JComboBox<>();
        List<NguoiDung> users = nguoiDungDAO.getAllKhachHang();
        for (NguoiDung user : users) {
            cboNguoiDung.addItem(user);
        }
        panel.add(cboNguoiDung);

        add(panel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnThem = FormUIHelper.createStyledButton("Thêm", new Color(34, 139, 34));
        btnHuy = FormUIHelper.createStyledButton("Hủy", new Color(169, 169, 169));
        buttonPanel.add(btnThem);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);

        // Renderer: hiển thị rõ mã người dùng
        cboNguoiDung.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NguoiDung nd) {
                    setText("ID: " + nd.getMaNguoiDung() + " - " + nd.getHoTen());
                }
                return this;
            }
        });

        // Sự kiện nút
        btnThem.addActionListener(e -> {
            NguoiDung selectedUser = (NguoiDung) cboNguoiDung.getSelectedItem();
            if (selectedUser == null) {
                FormUIHelper.showErrorMessage("Vui lòng chọn người dùng.");
                return;
            }

            GioHang gh = new GioHang();
            gh.setMaNguoiDung(selectedUser.getMaNguoiDung());
            gh.setNgayTao(LocalDateTime.now());

            if (gioHangDAO.themGioHang(gh)) {
                FormUIHelper.showSuccessMessage("Tạo giỏ hàng thành công!");
                if (callbackReload != null) callbackReload.run();
                dispose();
            } else {
                FormUIHelper.showErrorMessage("Thêm thất bại!");
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }
}
