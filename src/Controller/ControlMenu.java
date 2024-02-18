/*
 * Copyright (c) 2022.
 */

package Controller;

import Model.Game;
import View.Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlMenu implements ActionListener {
    private final Game game;
    private final Vue menu;

    /**
     * Constructeur de la classe ControlMenu
     * @param game le jeu
     * @param menu le menu
     */
    public ControlMenu(Game game, Vue menu) {
        this.menu = menu;
        this.game = game;
        this.init();
    }

    /**
     * Méthode qui initialise les listeners sur les boutons du menu
     */
    private void init() {
        this.menu.getMenuNewGame().addActionListener(this);
        this.menu.getMenuLoadGame().addActionListener(this);
        this.menu.getMenuSaveGame().addActionListener(this);
        this.menu.getMenuHelp().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO add element pour faire fonctionner le menue de départ (fait par aidan)
        if (e.getSource() == menu.getMenuNewGame()) {
            System.out.println("new game");
        } else if (e.getSource() == menu.getMenuLoadGame()) {
            System.out.println("load game");
        } else if (e.getSource() == menu.getMenuSaveGame()) {
            System.out.println("save game");
        } else if (e.getSource() == menu.getMenuHelp()) {
            System.out.println("help");
        }
    }
}
