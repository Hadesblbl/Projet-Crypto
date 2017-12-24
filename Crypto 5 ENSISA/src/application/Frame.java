package application;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

public class Frame extends JFrame implements ActionListener {

    private final JMenuBar MENU_BAR = new JMenuBar();
    private final JMenu FICHIER_MENU = new JMenu();
    private final JMenuItem OUVRIR_MENU = new JMenuItem();
    private final JMenuItem FERMER_MENU = new JMenuItem();
    private final Panel panel = new Panel();

    private FileNameExtensionFilter extensionFilter;

    public Frame() {
        super();
        setFrameProperties();
        createMenuBar();
        extensionFilter = new FileNameExtensionFilter(".jpg, .jpeg, .png", "jpg", "jpeg", "png");
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

        createMenu(FICHIER_MENU, "Fichier");
        createSubMenu(OUVRIR_MENU, "Ouvrir", FICHIER_MENU);
        createSubMenu(FERMER_MENU, "Fermer", FICHIER_MENU);

        getContentPane().add(panel);
    }

    private void createMenu(JMenu menu, String name) {
        MENU_BAR.add(menu);
        menu.setText(name);
    }

    private void createSubMenu(JMenuItem subMenu, String name, JMenu menu) {
        menu.add(subMenu);
        subMenu.addActionListener(this);
        subMenu.setText(name);
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
