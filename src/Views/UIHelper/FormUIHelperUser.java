package Views.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FormUIHelperUser {

    public static void styleMenuButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
    }

    public static void styleIconButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Tạo panel nền gradient
    public static JPanel createGradientPanel(Color start, Color end) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, start, 0, h, end);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }

    // ======= Hiệu ứng mở rộng/thu gọn có animation =======
    public static void togglePanelWithAnimation(JPanel panel, boolean expand) {
        Timer timer = new Timer(5, null);
        int maxHeight = getFullHeight(panel);

        if (expand) {
            panel.setVisible(true);
            panel.setPreferredSize(new Dimension(panel.getWidth(), 0));
            timer.addActionListener(new AbstractAction() {
                int height = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    height += 10;
                    if (height >= maxHeight) {
                        height = maxHeight;
                        timer.stop();
                    }
                    panel.setPreferredSize(new Dimension(panel.getWidth(), height));
                    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
                    panel.revalidate();
                    panel.invalidate();
                    panel.getParent().revalidate();
                    panel.getParent().doLayout();
                    panel.getParent().repaint();
                }
            });
        } else {
            timer.addActionListener(new AbstractAction() {
                int height = panel.getHeight();

                @Override
                public void actionPerformed(ActionEvent e) {
                    height -= 10;
                    if (height <= 0) {
                        height = 0;
                        panel.setVisible(false);
                        timer.stop();
                    }
                    panel.setPreferredSize(new Dimension(panel.getWidth(), height));
                    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
                    panel.revalidate();
                    panel.invalidate();
                    panel.getParent().revalidate();
                    panel.getParent().doLayout();
                    panel.getParent().repaint();
                }
            });
        }

        timer.start();
    }

// Tính tổng chiều cao thực của panel
    private static int getFullHeight(JPanel panel) {
        int height = 0;
        for (Component c : panel.getComponents()) {
            height += c.getPreferredSize().height;
        }
        return height;
    }

}
