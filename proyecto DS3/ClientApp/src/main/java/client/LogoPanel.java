package client;

import javax.swing.*;
import java.awt.*;

public class LogoPanel extends JPanel {

    private Image image;

    public LogoPanel() {
        try {
            // Carga desde carpeta resources
            image = new ImageIcon(getClass().getResource("/download.png")).getImage();
        } catch (Exception e) {
            System.out.println("ERROR cargando imagen: " + e.getMessage());
        }

        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {

            // Dibuja imagen redonda
            int size = Math.min(getWidth(), getHeight());
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            // Clip circular
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setClip(new java.awt.geom.Ellipse2D.Float(x, y, size, size));

            g2.drawImage(image, x, y, size, size, null);

            g2.dispose();
        }
    }
}
