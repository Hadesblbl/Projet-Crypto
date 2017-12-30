package application;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EncryptorModel extends JPanel {

	private static final long serialVersionUID = -7000404362747523378L;
	private BufferedImage image = null;
    private Point p1 = null;
    private Point p2 = null;
    private JPanel canvas;

    EncryptorModel() {
        super();
        this.canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
    }

    public BufferedImage getImage() {
        return image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        drawImage(g2);
        drawSelectionRectangle(g2);
        
  }

    private void drawImage(Graphics2D g2) {
        if (image != null){
            g2.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
	        canvas.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	        JScrollPane sp = new JScrollPane(canvas);
	        setLayout(new BorderLayout());
	        add(sp, BorderLayout.CENTER);
        }
    }

    private void drawSelectionRectangle(Graphics2D g2) {
        if (p2 == null || p1 == null)
            return;

        g2.setColor(Color.BLACK);

        // calcul de la selection
        final Rectangle rect = new Rectangle((p2.x > p1.x) ? p1.x : p2.x,
                (p2.y > p1.y) ? p1.y : p2.y,
                (p2.x > p1.x) ? p2.x - p1.x : p1.x
                        - p2.x, (p2.y > p1.y) ? p2.y
                - p1.y : p1.y - p2.y);

        // dessine le fond de la selection avec un effet de transparence
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
        g2.fillRect(rect.x, rect.y, rect.width, rect.height);

        // suppression de la transparence pour dessiner la bordure
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    protected void addImage(File imageFile) {
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        repaint();
    }

    public void update(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        repaint();
    }
}
