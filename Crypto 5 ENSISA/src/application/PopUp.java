package application;

import javax.swing.JOptionPane;

public class PopUp {
	/**
	 * Identification mot de passe
	 * return password
	 */
	static char[] PopupIdentification() {
		String reponse = "";
		while (reponse.equals(""))
			reponse = JOptionPane.showInputDialog(null,"Veuillez entrer votre mot de passe","Identification",JOptionPane.QUESTION_MESSAGE);
		JOptionPane.showMessageDialog(null, "Votre mot de passe est " + reponse, "Identité", JOptionPane.INFORMATION_MESSAGE);
		return reponse.toCharArray();
	}
}

