package application;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class EncryptorController implements ActionListener, MouseListener, MouseMotionListener {

    private EncryptorModel model;
    private EncryptorView view;

    private Point p1 = null;
    private Point p2 = null;

    EncryptorController(EncryptorModel model, EncryptorView view) {
        this.view = view;
        this.model = view.getEncryptorModel();
    }

    /* Interaction menu */

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Ouvrir":
                openFile();
                break;
            case "Fermer":
                exitFile();
                break;
            case "Crypter":
                encryptFile();
                break;
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(".png", "png");
        fileChooser.setFileFilter(extensionFilter);

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION)
            model.addImage(new File(fileChooser.getSelectedFile().getAbsolutePath()));

        view.resizeFrame();
        model.addMouseListener(this);
        model.addMouseMotionListener(this);
    }

    private void exitFile() {
        view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
    }

    private void encryptFile() {
        if (model.getImage() == null || model.getSelectionRectangle() == null)
            return;

        model.encryptionTest();
    }

    /* Interaction image */

    @Override
    public void mouseClicked(MouseEvent e) {
        model.update(p1, null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        p1 = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        p2 = e.getPoint();
        model.update(p1, p2);
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
