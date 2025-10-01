package Views.UIHelper;

import javax.swing.border.TitledBorder;
import java.awt.*;
import javax.swing.*;

public class RoundedTitledBorder extends TitledBorder {

    public RoundedTitledBorder(String title, Color color) {
        super(BorderFactory.createEmptyBorder(), title, LEFT, TOP, new Font("Segoe UI", Font.BOLD, 13), color);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getTitleColor());
        g2.setFont(getTitleFont());

        int arc = 20;
        int textHeight = g2.getFontMetrics().getHeight();
        int titleWidth = g2.getFontMetrics().stringWidth(getTitle()) + 10;

        // Bo tròn phần khung
        g2.setColor(new Color(200, 220, 255));
        g2.drawRoundRect(x + 1, y + textHeight / 2, width - 3, height - textHeight / 2 - 1, arc, arc);

        // Vẽ lại tiêu đề
        g2.setColor(getTitleColor());
        g2.drawString(getTitle(), x + 10, y + textHeight);
    }
}
