package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PopUp{
	
	
	
	
	@SuppressWarnings("static-access")
	public static void PopupInformation(){
		//Boite de message d'information
		JOptionPane jop1;
		jop1= new JOptionPane ();
		jop1.showMessageDialog(null, "Fermeture","Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void PopupErreur() {
		//Boite du message d'erreur
		JOptionPane jop2;
		jop2= new JOptionPane();
		jop2.showMessageDialog(null, "Votre mot de passe est incorrect", "Erreur", jop2.ERROR_MESSAGE);
	}
		
	public static void PopupPrevention() {
		//Boite du message preventif
		JOptionPane jop3;
		jop3 = new JOptionPane();
		jop3.showMessageDialog(null, "Message PrÃ©ventif","Attention", jop3.WARNING_MESSAGE);
	}
	
	public static char[] PopupIdentification() {
		//Identification mot de passe
	    JOptionPane jop4;
	    JOptionPane jop5;
	    jop4 = new JOptionPane();
	    jop5 = new JOptionPane ();
	    @SuppressWarnings("static-access")
		String reponse = jop4.showInputDialog(null,"Veuillez entrer votre mot de passe","Identification",JOptionPane.QUESTION_MESSAGE);
	    jop5.showMessageDialog(null, "Votre mot de passe est " + reponse, "IdentitÃ©", JOptionPane.INFORMATION_MESSAGE);
	    return reponse.toCharArray();
	}
}

		/*if(reponse == JOptionPane.OK_OPTION){
	    	System.exit(1);
	    	}*/
	
	/*public static void PopupIdentification() {
		
		JFrame frame;
		
		
		public void setVisible(boolean state) {
			frame.setVisible(state);
		}
		
		JFrame frame1 = new JFrame();
		frame1.setTitle("Indentification Cryptage");
		frame1.setSize(300, 50);
		frame1.setBounds(100, 100, 450, 300);
		frame1.getContentPane().setLayout(null);

		JLabel mdpLabel = new JLabel("Veuillez entrer votre mot de passe:");
		mdpLabel.setBounds(31, 83, 138, 36);
		frame1.getContentPane().add(mdpLabel);

		JTextField mdpTextField = new JTextField();
		mdpTextField.setBounds(193, 83, 208, 36);
		frame1.getContentPane().add(mdpTextField);
		mdpTextField.setColumns(10);

		JButton CrypterButton = new JButton("Crypter");
		CrypterButton.setBounds(251, 142, 105, 36);
		frame1.getContentPane().add(CrypterButton);

		JButton AnnulerButton = new JButton("Annuler");
		AnnulerButton.setBounds(251, 189, 105, 36);
		frame1.getContentPane().add(AnnulerButton);

		AnnulerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame1.setVisible(false);
				frame1.dispose();
			}
		});

		CrypterButton.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {

				String mdp;
				if (mdpTextField.getText() != null)
					mdp = mdpTextField.getText();
			}
		});
		
	}
}

				/*Object model;
				if (model != null) {
					BufferedImage image = model.getImage();

					// Recuperer les rectangles de la vue
					rectangles = view.getPreparedRectangles();

					// le String correspondant aux donnees a crypter
					String rgbString = getRGBToString(image);
					SecretKey aesKey = null;
					try {
						aesKey = encryptionPassword(password);
					} catch (NoSuchAlgorithmException | InvalidKeySpecException e3) {
						e3.printStackTrace();
					}

					try {
						String initVector = "RandomInitVector";
						String encryptedString = newEncrypt(initVector, rgbString, aesKey);

						String[] json = null;
						json = imageModelJSON.writeImageModelJSONFile(path, fileName, encryptedString);
						String extension = fileName.split("\\.")[1];
						model.saveIMG(path, extension);

						String jsonFolder = json[0];
						String jsonName = json[1];
						String imageFolderPath = path.split(fileName)[0];
						hideZipInImage(imageFolderPath, fileName, jsonFolder, jsonName);

					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				frame.setVisible(false);
				frame.dispose();
				model.loadImage(path);
				view.setRectangles(new ArrayList<Rectangle>());
				view.repaint();
			}

		});*/

