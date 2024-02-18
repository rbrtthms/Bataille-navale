/*
 * Copyright (c) 2022.
 */

import Controller.ControlButton;
import Controller.ControlForm;
import Controller.ControlMenu;
import Model.Game;
import View.Vue;

/**
 * Classe principale de l'application
 * Créer une instance de la classe Game et de la classe Vue et les lient entre eux (Controllers)
 */
public class Appli {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
        Vue vue = new Vue(game);
        ControlMenu controlMenu = new ControlMenu(game, vue);
        ControlButton controlButton = new ControlButton(game, vue);
        ControlForm controlForm = new ControlForm(game, vue);
    }
}
