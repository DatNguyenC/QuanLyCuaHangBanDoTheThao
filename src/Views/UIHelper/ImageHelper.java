/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Views.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageHelper {

    // Resize ảnh chất lượng cao
    public static ImageIcon scaleHighQuality(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();

        // Set rendering hints để làm ảnh mịn
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();

        return new ImageIcon(resized);
    }
}
