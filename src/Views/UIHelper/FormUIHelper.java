package Views.UIHelper;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FormUIHelper {

    // Tạo text field có style
    public static JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return textField;
    }

    // Tạo button có style
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    // Thêm 1 dòng vào form
    public static void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createStyledLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Tạo label có style
    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    // Tạo combo box có style
    public static JComboBox<Integer> createStyledComboBox(Integer[] items) {
        JComboBox<Integer> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(200, 30));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return comboBox;
    }

    // Tạo panel có tiêu đề style
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
        ));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    public static JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(200, 30));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return tf;
    }

    public static void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Không vẽ viền
            }
        };
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void styleBlackWhiteButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Bo tròn góc
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Màu nền
                g2.setColor(button.getModel().isRollover() ? new Color(30, 30, 30) : Color.BLACK);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);

                super.paint(g, c);
                g2.dispose();
            }

            @Override
            public void paintButtonPressed(Graphics g, AbstractButton b) {
                g.setColor(new Color(50, 50, 50));
                g.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30);
            }
        });
    }

}
