
package com.mycompany.pizza_express_tycoon;

import UI.LoginFrame;

public class Pizza_Express_Tycoon {

    public static void main(String[] args) {

        
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });

    }
}