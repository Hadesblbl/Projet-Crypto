package application;

import javax.swing.*;

public class Frame extends JFrame {

    private final JMenuBar MENU_BAR = new JMenuBar();

    public Frame() {
        super();
        this.setTitle("Super projet de cryptographie");
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMenu();
    }

    private void createMenu() {
        setJMenuBar(MENU_BAR);
    }

}
