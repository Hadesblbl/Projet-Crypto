package application;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class EncryptorView extends JFrame {

	private static final long serialVersionUID = 9055585013467848278L;

	private List<JMenuItem> menuItems = new ArrayList<>();
	private final JMenuBar MENU_BAR = new JMenuBar();
    private final JMenu FICHIER_MENU = new JMenu("Fichier");
    private final JMenuItem OUVRIR_MENU = new JMenuItem("Ouvrir");
    private final JMenuItem FERMER_MENU = new JMenuItem("Fermer");
    private final JMenu EDITION_MENU = new JMenu("Édition");
    private final JMenuItem CRYPTER_MENU = new JMenuItem("Crypter");

    private final EncryptorModel encryptorModel = new EncryptorModel();

    EncryptorView() {
        super();
        setFrameProperties();
        createMenuBar();
    }

    public EncryptorModel getEncryptorModel() {
        return encryptorModel;
    }

    /**
     * Définit les propriétés de la fenêtre générée au lancement du programme
     */
    private void setFrameProperties() {
        this.setTitle("Super projet de cryptographie");
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void resizeFrame() {
        try {
            this.setSize(encryptorModel.getImage().getWidth() + 16, encryptorModel.getImage().getHeight() + 62);
            this.setLocationRelativeTo(null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void createMenuBar() {
        setJMenuBar(MENU_BAR);

        createMenu(FICHIER_MENU);
        createSubMenu(OUVRIR_MENU, FICHIER_MENU);
        createSubMenu(FERMER_MENU, FICHIER_MENU);

        createMenu(EDITION_MENU);
        createSubMenu(CRYPTER_MENU, EDITION_MENU);

        setKeyboardShortcuts();
        getContentPane().add(encryptorModel);
    }

    private void createMenu(JMenu menu) {
        MENU_BAR.add(menu);
    }

    private void createSubMenu(JMenuItem menu, JMenu parent) {
        parent.add(menu);
        menuItems.add(menu);
    }

    private void setKeyboardShortcuts() {
        OUVRIR_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        FERMER_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
    }

    public void addController(EncryptorController controller) {
        for (JMenuItem item : menuItems)
            item.addActionListener(controller);
    }

}
