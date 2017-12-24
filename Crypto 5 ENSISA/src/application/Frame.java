package application;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;

public class Frame extends JFrame implements ActionListener {

    private final JMenuBar MENU_BAR = new JMenuBar();
    private final JMenu FICHIER_MENU = new JMenu("Fichier");
    private final JMenuItem OUVRIR_MENU = new JMenuItem("Ouvrir");
    private final JMenuItem FERMER_MENU = new JMenuItem("Fermer");
    private final JMenu EDITION_MENU = new JMenu("Édition");
    private final JMenuItem CRYPTER_MENU = new JMenuItem("Crypter");
    private final Panel panel = new Panel();

    private FileNameExtensionFilter extensionFilter;

    public Frame() {
        super();

        setFrameProperties();
        createMenuBar();

        extensionFilter = new FileNameExtensionFilter(".png", "png");
    }

    private void setFrameProperties() {
        this.setTitle("Super projet de cryptographie");
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void resizeFrame() {
        this.setSize(panel.image.getWidth() + 16, panel.image.getHeight() + 62);
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
        getContentPane().add(panel);
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
        fileChooser.setFileFilter(extensionFilter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            panel.addImage(new File(fileChooser.getSelectedFile().getAbsolutePath()));
        resizeFrame();
    }

    private void exitFile() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
