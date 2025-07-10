/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Views.UIHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author PC
 */
public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private final ButtonClickHandler clickHandler;
    private int currentRow;

    public interface ButtonClickHandler {

        void onClick(int row);
    }

    public ButtonEditor(JCheckBox checkBox, ButtonClickHandler handler) {
        super(checkBox);
        this.clickHandler = handler;

        button = new JButton();
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 144, 255));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215)));

        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.currentRow = row;
        this.label = (value == null) ? "Xử lý" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            clickHandler.onClick(currentRow);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
