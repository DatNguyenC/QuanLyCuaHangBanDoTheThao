/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Views.UIHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author PC
 */
// ButtonRenderer.java
public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer(String text) {
        setText(text);
        setOpaque(true);
        setForeground(Color.WHITE);
        setBackground(new Color(30, 144, 255)); // màu xanh dương đậm
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215)));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        return this;
    }
}

// ButtonEditor.java

