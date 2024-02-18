/*
 * Copyright (c) 2022.
 */

package Controller;

import Model.Game;
import View.Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlButton implements ActionListener {
    private Game game;
    private Vue menu;

    public ControlButton(Game game, Vue menu) {
        this.game = game;
        this.menu = menu;
        this.init();
    }

    /**
     * Méthode qui initialise les listeners sur les boutons
     */
    private void init() {
        for (int i = 0; i < menu.getSIZE(); i++) {
            for (int j = 0; j < menu.getSIZE(); j++) {
                menu.getMapHumain()[i][j].addActionListener(this);
                menu.getMapIA()[i][j].addActionListener(this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action" + e.getSource());
        // TODO add element pour faire fonctionner le menue de départ
    }
}
