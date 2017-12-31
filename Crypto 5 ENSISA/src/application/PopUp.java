package application;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class PopUp{
	
	@SuppressWarnings("static-access")
	public static void createPopup(){
		//Boite de message d'information
		JOptionPane jop1;
		jop1= new JOptionPane ();
		jop1.showMessageDialog(null, "Message informatif","Information", JOptionPane.INFORMATION_MESSAGE);
		
		//Boite du message d'erreur
		JOptionPane jop2;
		jop2= new JOptionPane();
		jop2.showMessageDialog(null, "Votre mot de passe est incorrect", "Erreur", jop2.ERROR_MESSAGE);
		
		
		//Boite du message preventif
		JOptionPane jop3;
		jop3 = new JOptionPane();
		jop3.showMessageDialog(null, "Message Préventif","Attention", jop3.WARNING_MESSAGE);
		
		//Identification mot de passe
	    JOptionPane jop4;
	    JOptionPane jop5;
	    jop4 = new JOptionPane();
	    jop5 = new JOptionPane ();
	    @SuppressWarnings("static-access")
		int reponse = jop4.showConfirmDialog(null,"Veuillez entrer votre mot de passe","Identification",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
	    jop5.showMessageDialog(null, "Votre mot de passe est " + reponse, "Identité", JOptionPane.INFORMATION_MESSAGE);
	    
	  
		if(reponse == JOptionPane.OK_OPTION)
	    	System.exit(1);
	    
	}
}
