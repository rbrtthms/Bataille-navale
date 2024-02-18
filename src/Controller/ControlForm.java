/*
 * Copyright (c) 2022.
 */

package Controller;

import Model.Game;
import View.Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlForm implements ActionListener {
    private Game game;
    private Vue menu;


    public ControlForm(Game game, Vue menu) {
        this.game = game;
        this.menu = menu;
        this.init();
    }

    private void init() {
        // tODO mettre des listener sur tout les elems form
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // tODO : faire le controller de la fenêtre de saisie
    }
}
