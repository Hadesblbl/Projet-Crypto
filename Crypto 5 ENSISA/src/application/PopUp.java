package application;

import javax.swing.JOptionPane;

public class PopUp{

	/**
	 * Boite de message d'information
	 */
	public static void PopupInformation(){
		JOptionPane.showMessageDialog(null, "Fermeture","Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Boite du message d'erreur
	 */
	public static void PopupErreur() {
		JOptionPane.showMessageDialog(null, "Votre mot de passe est incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
	}
		
	/**
	 * Boite du message preventif
	 */
	public static void PopupPrevention() {
		JOptionPane.showMessageDialog(null, "Message Préventif","Attention", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Identification mot de passe
	 * return password
	 */
	static char[] PopupIdentification() {
		String reponse = JOptionPane.showInputDialog(null,"Veuillez entrer votre mot de passe","Identification",JOptionPane.QUESTION_MESSAGE);
	    if (reponse != null){
	    	JOptionPane.showMessageDialog(null, "Votre mot de passe est " + reponse, "Identité", JOptionPane.INFORMATION_MESSAGE);
	    	return reponse.toCharArray();
	    }
	    return null;
	}
}

