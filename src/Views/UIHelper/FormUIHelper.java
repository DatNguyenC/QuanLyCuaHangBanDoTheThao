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

}
