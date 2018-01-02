package application;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

/**
 * (Composant Modèle du modèle MVC de l'application)
 * EncryptorModel stocke les informations nécessaires et se met à jour
 * en fonction de EncryptorController
 */
public class EncryptorModel extends JPanel {

	private static final long serialVersionUID = -7000404362747523378L;
	private File imageFile = null;
	private BufferedImage image = null;

    private Point p1 = null;
    private Point p2 = null;
    private Rectangle selectionRectangle;

    private JPanel canvas;
    private ArrayList<Rectangle> rectangles;
    private String path;

    private char[] password;

    EncryptorModel() {
        super();
        setRectangles(new ArrayList<>());
        this.canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(getImage(), 0, 0, null);
            }
        };
    }

    /**
     * Ajoute le rectangle à la liste
     */
    void addRectangle(Rectangle r){
        getRectangles().add(r);
    }

    /**
     * Remet la liste à 0
     */
    void clearRectangles(){
    	setRectangles(new ArrayList<>());
    	p1=null;
    	p2=null;
    	selectionRectangle=null;
    }

    /**
     * @return l'image enregistrée sous forme de fichier (File)
     */
    public File getImageFile() {
        return this.imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    ArrayList<Rectangle> getRectangles() {
        return this.rectangles;
    }

    private void setRectangles(ArrayList<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return l'image chargée (ou non) pour le cryptage ou décryptage
     */
    BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
     * @return la zone Rectangle sélectionnée par l'utilisateur
     */
    Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }
    
    /**
     * @param r remplace le selectionRectangle
     */
    void setSelectionRectangle(Rectangle r){
    	selectionRectangle=r;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword() {
        this.password = PopUp.PopupIdentification();
    }

    /**
     * Affiche l'image et le rectangle de sélection sur la fenêtre
     * paintComponent est appelée lorsqu'il est nécessaire de mettre à jour
     * les informations visibles par l'utilisateur, par exemple lorsqu'il
     * crée et/ou modifie le rectangle de sélection
     *
     * @param g composante graphique
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        drawImage(g2);
        drawCryptRectangle(g2);
        drawSelectionRectangle(g2);
    }

    /**
     * Stocke l'image dans le Modèle puis l'affiche sur la fenêtre
     *
     * @param imageFile image .png
     */
    void addImage(File imageFile) {
        try {
            setImage(ImageIO.read(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }
    
    /**
     * Dessine l'image chargée sur la fenêtre
     * Ne se passe rien si aucune image a été chargée
     *
     * @param g2 composante graphique
     */
    private void drawImage(Graphics2D g2) {
        if (getImage() == null)
            return;

        g2.drawImage(getImage(), 0, 0, getImage().getWidth(), getImage().getHeight(), this);

        canvas.setPreferredSize(new Dimension(getImage().getWidth(), getImage().getHeight()));
        JScrollPane sp = new JScrollPane(canvas);
        setLayout(new BorderLayout());
        //TODO: Code mis sous quarantaine parce que je sais pas comment gérer le repaint() (Olivier)
        //this.add(sp, BorderLayout.CENTER);
    }
    
    /**
     * Dessine les rectangle à crypter sur la fenêtre
     *
     * @param g2 composante graphique
     */
    private void drawCryptRectangle(Graphics2D g2) {
    	if (p1 == null || p2 == null)
    	    return;

        g2.setColor(Color.BLACK);
        for (Rectangle r : rectangles) {
            setRectangleTransparency(r, g2);
            setRectangleBorders(r, g2);
        }
    }
    
    /**
     * Dessine le rectangle de sélection sur la fenêtre
     *
     * @param g2 composante graphique
     */
    private void drawSelectionRectangle(Graphics2D g2) {
        if (p2 == null || p1 == null) {
            selectionRectangle = null;
            return;
        }

        g2.setColor(Color.BLACK);
        calculateSelectionRectangle();
        setRectangleTransparency(selectionRectangle, g2);
        setRectangleBorders(selectionRectangle, g2);
    }

    /**
     * Calcule les propriétés du rectangle de sélection (hauteur, largeur) en fonction des points p1 et p2
     */
    private void calculateSelectionRectangle() {
        selectionRectangle = new Rectangle(
                (p2.x > p1.x) ? p1.x : p2.x,
                (p2.y > p1.y) ? p1.y : p2.y,
                (p2.x > p1.x) ? p2.x - p1.x : p1.x - p2.x,
                (p2.y > p1.y) ? p2.y - p1.y : p1.y - p2.y
        );
    }


    /**
     * Calcule et affiche la transparence de fond du rectangle
     *
     * @param r rectangle cible
     * @param g2 composante graphique
     */
    private void setRectangleTransparency(Rectangle r, Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
        g2.fillRect(r.x, r.y, r.width, r.height);
    }

    /**
     * Calcule et affiche la bordure du rectangle
     *
     * @param r rectangle cible
     * @param g2 composante graphique
     */
    private void setRectangleBorders(Rectangle r, Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawRect(r.x, r.y, r.width, r.height);
    }

    /**
     * Met à jour les points de coordonnées pour le rectangle de sélection
     * et affiche ensuite la fenêtre
     *
     * @param p1 point haut-gauche du rectangle
     * @param p2 point bas-droite du rectangle
     */
    void update(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        repaint();
    }

    /**
     * @return true si on a des rectangles en mémoire dans Model
     */
	boolean isCryptable() {
        return !rectangles.isEmpty() && image != null;
    }
}
