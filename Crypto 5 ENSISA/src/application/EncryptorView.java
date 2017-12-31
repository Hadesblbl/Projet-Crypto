package application;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * (Composant Vue du modèle MVC de l'application)
 * EncryptorView est responsable de la mise en forme de la fenêtre principale
 * au lancement, c'est-à-dire de son dimensionnement, de ses menus et des
 * raccourcis menus qui leurs sont associés
 */
public class EncryptorView extends JFrame {

	private static final long serialVersionUID = 9055585013467848278L;

	private List<JMenuItem> menuItems = new ArrayList<>();
	private final JMenuBar MENU_BAR = new JMenuBar();
    private final JMenu FICHIER_MENU = new JMenu("Fichier");
    private final JMenuItem OUVRIR_MENU = new JMenuItem("Ouvrir");
    private final JMenuItem FERMER_MENU = new JMenuItem("Fermer");
    private final JMenu EDITION_MENU = new JMenu("Édition");
    private final JMenuItem CRYPTER_MENU = new JMenuItem("Crypter");
    private final JMenuItem DECRYPTER_MENU = new JMenuItem("Décrypter");
    private final JMenuItem CLEAR_MENU = new JMenuItem("Nettoyer");
    
    private final EncryptorModel encryptorModel = new EncryptorModel();

    EncryptorView() {
        super();
        setFrameProperties();
        createMenuBar();
    }

    EncryptorModel getEncryptorModel() {
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

    /**
     * Redimensionne la fenêtre active en fonction de l'image chargée
     *
     * @throws NullPointerException si aucune image est chargée
     */
    void resizeFrame() {
        try {
        	if (encryptorModel.getImage().getWidth() <= 900 && encryptorModel.getImage().getHeight() <= 900 ){
	            this.setSize(encryptorModel.getImage().getWidth(), encryptorModel.getImage().getHeight());
	            this.setLocationRelativeTo(null);
        	}
        	else {
        		 this.setSize(900, 600);
 	            this.setLocationRelativeTo(null);
        	}
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les différents menus disponibles et cliquables au lancement
     */
    private void createMenuBar() {
        setJMenuBar(MENU_BAR);

        createMenu(FICHIER_MENU);
        createSubMenu(OUVRIR_MENU, FICHIER_MENU);
        createSubMenu(FERMER_MENU, FICHIER_MENU);

        createMenu(EDITION_MENU);
        createSubMenu(CRYPTER_MENU, EDITION_MENU);
        createSubMenu(DECRYPTER_MENU, EDITION_MENU);
        createSubMenu(CLEAR_MENU, EDITION_MENU);

        setKeyboardShortcuts();
        getContentPane().add(encryptorModel);
    }


    /**
     * Crée un menu en haut de la fenêtre
     *
     * @param menu JMenu à créer
     */
    private void createMenu(JMenu menu) {
        MENU_BAR.add(menu);
    }

    /**
     * Crée un sous-menu à un menu parent (qui apparaît lorsqu'on clique sur ce dernier)
     *
     * @param menu JMenuItem à créer
     * @param parent JMenu parent où sera contenu menu
     */
    private void createSubMenu(JMenuItem menu, JMenu parent) {
        parent.add(menu);
        menuItems.add(menu);
    }

    /**
     * Spécifie les différents raccourcis claviers utilisables pour les menus
     */
    private void setKeyboardShortcuts() {
        OUVRIR_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        FERMER_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        CRYPTER_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        DECRYPTER_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
    }

    /**
     * Ajoute un ActionListener à chacun des sous-menus dans un Controller
     * Permet au Controller de réagir au clic de la souris et d'agir par conséquence
     *
     * @param controller le Controller responsable de l'écoute des sous-menus
     */
    void addController(EncryptorController controller) {
        for (JMenuItem item : menuItems)
            item.addActionListener(controller);
    }
}
