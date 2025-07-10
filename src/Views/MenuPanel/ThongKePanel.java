package Views.MenuPanel;

import javax.swing.*;
import java.awt.*;

public class ThongKePanel extends JPanel {
    public ThongKePanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Thống kê", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lbl, BorderLayout.CENTER);
    }
}