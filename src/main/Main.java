package main;

import com.formdev.flatlaf.FlatDarkLaf;
import core.views.MegaferiaFrame;
import javax.swing.UIManager;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setProperty("flatlaf.useNativeLibrary", "false");

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MegaferiaFrame().setVisible(true);
            }
        });
    }
}
