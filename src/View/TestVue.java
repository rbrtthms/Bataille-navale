/*
 * Copyright (c) 2022.
 */

package View;

import Controller.ControlButton;
import Controller.ControlForm;
import Controller.ControlMenu;
import Model.Game;

public class TestVue {
    public static void main(String[] args) {
        MenuNaval menuNaval = new MenuNaval();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game game = new Game();
                Vue vue = new Vue(game);
                ControlMenu controlMenu = new ControlMenu(game, vue);
                ControlButton controlButton = new ControlButton(game, vue);
                ControlForm controlForm = new ControlForm(game, vue);
            }
        });
    }
}
