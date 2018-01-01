package application;


import encryption.CryptedImage;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
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
			model.setImageFile(new File(fileChooser.getSelectedFile().getAbsolutePath()));
			model.addImage(model.getImageFile());
			model.setPath(fileChooser.getSelectedFile().getAbsolutePath());
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
		//System.out.println(encryption.Encryption.test2(model.getRectangles(),model.getImage(),"oui".toCharArray()));
		//model.setImage(encryption.Encryption.test(model.getRectangles(),model.getImage(),"oui".toCharArray()));
		//System.out.println(encryption.Encryption.testGetPixels(model.getRectangles(), model.getImage()));
		if (!model.isCryptable())	return;

		model.setPassword();

		BufferedImage encryptedIMG = encryption.Encryption.encryptImage(model.getRectangles(), model.getImage(), model.getPassword()); //L'image encryptée est celle qu'on a ouvert précédemment
		byte[] b;
		
		try {
			b = CryptedImage.writeMetadata(model.getRectangles(), encryptedIMG);
			FileOutputStream fos = new FileOutputStream(model.getPath());
			fos.write(b);//on remplace l'image courante par l'image cryptée
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		clearFile();
		model.repaint();
	}
	
	/**
	 * Lance le processus de décryptage de la zone sélectionnée
	 * (...)
	 */
	private void decryptFile() {
		try {
			if (model.getImageFile() == null)
				return;
			byte[] cryptedIMG = fileToByte(model.getImageFile());
			model.setPassword();
			
			//Début de récupération de la liste des rectangles
			ArrayList<Rectangle> rectCrypte= new ArrayList<>();
			String list=CryptedImage.readMetadata(cryptedIMG);
			if (list!=null){
				//System.out.println(list);
				String[] rect=list.split("\n");
				for(String s:rect){
					rectCrypte.add(stringToRect(s));
				}
			}
			//Fin de récupération de la liste des rectangles
			
			//Début d'enregistrement de l'image
			BufferedImage img=encryption.Encryption.decryptImage(rectCrypte, ImageIO.read(model.getImageFile()), model.getPassword());
			try {
				ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
			    ImageWriteParam writeParam = writer.getDefaultWriteParam();
			    ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    ImageOutputStream stream = ImageIO.createImageOutputStream(baos);
			    writer.setOutput(stream); // on dit qu'on écrit dans baos
			    IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
			    writer.write(metadata, new IIOImage(img, null, metadata), writeParam); //pour récupérer l'image sous forme d'un byteArray
			    
				FileOutputStream fos = new FileOutputStream(model.getPath());
				fos.write(baos.toByteArray());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			//Fin d'enregistrement de l'image
			
			model.setImage(img);
			view.resizeFrame();
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @param s contenant les 4 valeurs nécessaires pour faire le rectangle
	 * @return le rectangle correspondant
	 */
	private Rectangle stringToRect(String s){
		String[] value=s.split(" ");
		if (value.length != 4){
			return new Rectangle(0,0,0,0);
		}
		return new Rectangle(Integer.parseInt(value[0]),Integer.parseInt(value[1]),Integer.parseInt(value[2]),Integer.parseInt(value[3]));
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
		if (model.getSelectionRectangle()!=null)
			model.addRectangle(model.getSelectionRectangle());
		model.setSelectionRectangle(null);
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
