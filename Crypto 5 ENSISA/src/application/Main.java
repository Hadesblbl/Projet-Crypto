package application;

public class Main {

	public static void main(String[] args) {
		EncryptorModel model = new EncryptorModel();
		EncryptorView view = new EncryptorView();
		EncryptorController controller = new EncryptorController(model, view);
		view.addController(controller);

		view.setVisible(true);
	}

}
