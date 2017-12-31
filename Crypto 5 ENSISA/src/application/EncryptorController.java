package application;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import encryption.CryptedImage;

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
			clearFile();
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
		fileChooser.setDialogTitle("Ouvrir l'image PNG à crypter");

		if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION){
			model.addImage(new File(fileChooser.getSelectedFile().getAbsolutePath()));
			model.path=fileChooser.getSelectedFile().getAbsolutePath();
		}

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
		if (!model.isCryptable())	return;

		char[] password = PopUp.PopupIdentification();
		BufferedImage encryptedIMG = encryption.Encryption.encryptImage(model.getRectangles(), model.getImage(), password); //L'image encryptée est celle qu'on a ouvert précédemment
		byte[] b;
		
		try {
			b = CryptedImage.writeMetadata(model.getRectangles(), encryptedIMG);
			FileOutputStream fos = new FileOutputStream(model.path);
			fos.write(b);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//proposer d'enregistrer

		clearFile();
		model.repaint();
	}

	/**
	 * Recupère les bytes d'un file
	 */
	private byte[] fileToByte(File file){
		byte[] cryptedImage = null;
		try {
			FileInputStream fr = new FileInputStream(file);
			cryptedImage = new byte[(int) file.length()];
			int b;
			int index = 0;
			while ((b=fr.read()) != -1) {
				cryptedImage[index]= (byte) b;
				index++;
			}
			fr.close();
			return cryptedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cryptedImage;
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
			fileChooser.setDialogTitle("Choisir le fichier à décrypter");
			File chosen= null;
			if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION)
				chosen= new File(fileChooser.getSelectedFile().getAbsolutePath());
			if (chosen == null) throw new AssertionError();
			byte[] cryptedIMG= fileToByte(chosen);

			char[] password=PopUp.PopupIdentification(); //on set le mdp avec la fenêtre popup
			String list=CryptedImage.readMetadata(cryptedIMG);
			System.out.println(list);
			ArrayList<Rectangle> rectCrypte= new ArrayList<>();// récup metadata ici
			rectCrypte.add(new Rectangle(0,0,0,0));

			encryption.Encryption.decryptImage(rectCrypte, ImageIO.read(chosen), password);
			//Enregistrer le fichier qqpart ici
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Retire les différents rectangles créés par sélection
	 */
	private void clearFile() {
		model.clearRectangles();
		model.repaint();
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
