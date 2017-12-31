package application;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import encryption.CryptedImage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * (Composant EncryptorController du modèle MVC de l'application)
 * EncryptorController réagit aux actions de l'utilisateur:
 * - clics sur les différents menus
 * - manipulation de l'image chargée au préalable
 */
class EncryptorController implements ActionListener, MouseListener, MouseMotionListener {

    private EncryptorModel model;
    private EncryptorView view;

    private Point p1 = null;
    private Point p2 = null;

    EncryptorController(EncryptorModel model, EncryptorView view) {
        this.view = view;
        this.model = view.getEncryptorModel();
    }

    /* Interaction menu */

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Ouvrir":
                openFile();
                break;
            case "Fermer":
                exitFile();
                break;
            case "Crypter":
                encryptFile();
                break;
            case "Décrypter":
                decryptFile();
                break;
            case "Nettoyer":
                model.clearRectangles();
                model.repaint();
                break;
        }
    }

    /**
     * Permet de rechercher et d'ouvrir une image .png dans l'explorateur de fichiers
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(".png", "png");
        fileChooser.setFileFilter(extensionFilter);

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION)
            model.addImage(new File(fileChooser.getSelectedFile().getAbsolutePath()));

        view.resizeFrame();
        model.addMouseListener(this);
        model.addMouseMotionListener(this);
    }

    /**
     * Quitte l'application
     */
    private void exitFile() {
        view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Lance le processus de cryptage de la zone d'image sélectionnée
     * Si aucune zone n'a été définie par l'utilisateur, la fonction ne
     * retournera rien
     */
    private void encryptFile() {
    	if (model.getImage() == null){
            System.out.println("Ouvrez une image depuis le menu Edition/Ouvrir");
    	}
    	
        if (model.getImage() == null || model.getRectangles().isEmpty()) {
            System.out.println("Pas de zone à crypter");
            return;
        }
        char[] password = PopUp.PopupIdentification();
        encryption.Encryption.encryptImage(model.getRectangles(), model.getImage(), password); //L'image encryptée est celle qu'on a ouvert précédemment
        //ajouter les metadata
        //proposer d'enregistrer
    }

    /**
     * Lance le processus de décryptage de la zone sélectionnée
     * (...)
     */
    private void decryptFile() {
    	// JavaDoc à finir en fonction de ce qui est fait
    	//Fonction à refaire/finir/perfectionner

    	try {
    		JFileChooser fileChooser = new JFileChooser();
    		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(".png", "png");
    		fileChooser.setFileFilter(extensionFilter);
    		File chosen= new File(fileChooser.getSelectedFile().getAbsolutePath());
    		FileReader fr= new FileReader(chosen);
    		byte[] cryptedIMG= new byte[(int) chosen.length()];
    		int b;
    		int index=0;
    		while ((b=fr.read()) != -1){
    			cryptedIMG[index]= (byte) b;
    			index++;
    		}

    		char[] password=PopUp.PopupIdentification(); //on set le mdp avec la fenêtre popup

    		CryptedImage ci= new CryptedImage(chosen);
    		String list=ci.readMetadata(cryptedIMG);
    		ArrayList<Rectangle> rectCrypte= new ArrayList<Rectangle>();// récup metadata ici
    		rectCrypte.add(new Rectangle(0,0,0,0));
    		
    		encryption.Encryption.encryptImage(rectCrypte, model.getImage(), password);
    		//Enregistrer le fichier qqpart ici
    	} catch (IOException e) {
    		e.printStackTrace();
    	} 
    }

    /* Interaction image */

    /**
     * Met à jour les coordonnées du rectangle de sélection lorsque
     * l'utilisateur clique
     *
     * @param e événement de clic
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        model.update(p1, null);
    }

    /**
     * Récupère la coordonnée du premier point du rectangle de sélection
     * lorsque l'utilisateur clique
     *
     * @param e événement de clic
     */
    @Override
    public void mousePressed(MouseEvent e) {
        p1 = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    	model.addRectangle(model.getSelectionRectangle());
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Récupère la coordonnée du second point du rectangle de sélection
     * lorsque l'utilisateur maintient son curseur appuyé, puis met
     * à jour les coordonnées des deux points
     *
     * @param e événement de clic
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        p2 = e.getPoint();
        model.update(p1, p2);
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
