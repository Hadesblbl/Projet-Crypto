package application;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;

public class EncryptorView extends JFrame implements ActionListener {

    /*
    * TODO: Implémentation d'ActionListener dans EncryptorController ?
    * */

	private static final long serialVersionUID = 9055585013467848278L;
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

    /* --- Window frame ---*/

    private void setFrameProperties() {
        this.setTitle("Super projet de cryptographie");
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void resizeFrame() {
        this.setSize(encryptorModel.getImage().getWidth() + 16, encryptorModel.getImage().getHeight() + 62);
        this.setLocationRelativeTo(null);
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

    private void createSubMenu(JMenuItem subMenu, JMenu menu) {
        menu.add(subMenu);
        subMenu.addActionListener(this);
    }

    private void setKeyboardShortcuts() {
        OUVRIR_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        FERMER_MENU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
    }

    /* --- Action performed ---*/

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(OUVRIR_MENU)) {
            openFile();
        } else if (e.getSource().equals(FERMER_MENU)) {
            exitFile();
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(".png", "png");
        fileChooser.setFileFilter(extensionFilter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            encryptorModel.addImage(new File(fileChooser.getSelectedFile().getAbsolutePath()));
        resizeFrame();
    }

    private void exitFile() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
