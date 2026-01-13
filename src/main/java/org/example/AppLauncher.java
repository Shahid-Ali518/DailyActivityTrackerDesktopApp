package org.example;

import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args) {
        try {
            Main.main(args);
        } catch (Exception e) {
            // This will pop up a Windows alert box with the error
            JOptionPane.showMessageDialog(null, "App Crash: " + e.getMessage() +
                    "\nCheck if resources (CSS/Images) exist in the JAR.");
            e.printStackTrace();
        }
    }
}
