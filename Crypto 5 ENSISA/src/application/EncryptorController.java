package application;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

class EncryptorController implements ActionListener {

    private EncryptorModel model;
    private EncryptorView view;

    EncryptorController(EncryptorModel model, EncryptorView view) {
        this.view = view;
        this.model = view.getEncryptorModel();
    }

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

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            model.addImage(new File(fileChooser.getSelectedFile().getAbsolutePath()));
            System.out.println(model.getImage());
        }
        view.resizeFrame();
    }

    private void exitFile() {
        view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
    }

    private void encryptFile() {
        //TODO
    }

}
