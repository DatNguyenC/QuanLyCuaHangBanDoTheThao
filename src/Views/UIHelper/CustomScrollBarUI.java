package Views.UIHelper;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollBarUI extends BasicScrollBarUI {

    private Color thumbColor;
    private Color trackColor;

    public CustomScrollBarUI() {
        this.thumbColor = new Color(50, 50, 50); // màu phần kéo
        this.trackColor = new Color(220, 220, 220); // màu nền thanh cuộn
    }

    public CustomScrollBarUI(Color thumbColor, Color trackColor) {
        this.thumbColor = thumbColor;
        this.trackColor = trackColor;
    }

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = this.thumbColor;
        this.trackColor = this.trackColor;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 12, 12);
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(trackColor);
        g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 12, 12);
        g2.dispose();
    }
}
