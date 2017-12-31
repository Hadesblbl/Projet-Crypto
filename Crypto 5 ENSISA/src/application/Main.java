package application;

public class Main {

	/**
	 * Fonction principale du programme
	 * Met en place le modèle MVC et affiche la fenêtre principale
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		EncryptorModel model = new EncryptorModel();
		EncryptorView view = new EncryptorView();
		EncryptorController controller = new EncryptorController(model, view);

		view.addController(controller);
		view.setVisible(true);
	}

}
