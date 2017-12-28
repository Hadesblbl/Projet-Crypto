package application;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class EncryptorModel extends JPanel {

	private static final long serialVersionUID = -7000404362747523378L;

	private BufferedImage image = null;
    private Point p1 = null;
    private Point p2 = null;
    private Rectangle selectionRectangle;

    EncryptorModel() {
        super();
    }

    /* --- Image --- */

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        drawImage(g2);
        drawSelectionRectangle(g2);
  }

    private void drawImage(Graphics2D g2) {
        if (image != null)
            g2.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
    }

    private void drawSelectionRectangle(Graphics2D g2) {
        if (p2 == null || p1 == null) {
            selectionRectangle = null;
            return;
        }

        g2.setColor(Color.BLACK);

        // calcul de la selection
        selectionRectangle = new Rectangle((p2.x > p1.x) ? p1.x : p2.x,
                (p2.y > p1.y) ? p1.y : p2.y,
                (p2.x > p1.x) ? p2.x - p1.x : p1.x
                        - p2.x, (p2.y > p1.y) ? p2.y
                - p1.y : p1.y - p2.y);

        // dessine le fond de la selection avec un effet de transparence
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
        g2.fillRect(selectionRectangle.x, selectionRectangle.y, selectionRectangle.width, selectionRectangle.height);

        // suppression de la transparence pour dessiner la bordure
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawRect(selectionRectangle.x, selectionRectangle.y, selectionRectangle.width, selectionRectangle.height);
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

    /* --- Encryption --- */

    public void encryptionTest() {
        // A supprimer après, juste du test
        String imageToString;

        imageToString = getRGBToString();
        modifyThisShit();

        System.out.println(": " + imageToString);
        repaint();
    }

    /**
     * Permet d'obtenir les valeurs RGB du rectangle sélectionné dans l'image sous forme de String
     *
     * @return valeurs RGB sous forme de String
     */
    private String getRGBToString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (selectionRectangle.contains(new Point(i,j))) {
                    result.append(String.valueOf(image.getRGB(i, j) + "|"));
                }
            }
        }

        return result.toString();
    }

    private void modifyThisShit() {
        Random rand = new Random();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (selectionRectangle.contains(new Point(i,j))) {
                    int alpha = 255;
                    int red = rand.nextInt(255);
                    int green = rand.nextInt(255);
                    int blue = rand.nextInt(255);
                    int p = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    image.setRGB(i, j, p);
                }
            }
        }

    }

}
