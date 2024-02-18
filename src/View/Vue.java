/*
 * Copyright (c) 2022.
 */

package View;

import Model.Game;
import Model.Map.Case;

import javax.swing.*;
import java.awt.*;

public class Vue extends JFrame {
    private Game game;

    // Menu
    private JMenuItem menuNewGame;
    private JMenuItem menuLoadGame;
    private JMenuItem menuSaveGame;
    private JMenuItem menuHelp;

    // Map
    private JButton[][] mapHumain;
    private JButton[][] mapIA;

    // form

    // AUTRES
    private final int SIZE = 15;

    /**
     * Constructeur de la vue
     * @param game le jeu (modèle)
     */
    public Vue(Game game) {
        this.game = game;
        this.setSize(new Dimension(500, 500));
        this.setResizable(true); // TODO false
        this.setTitle("Bataille Navale");
        this.initAttribut();
        this.menuBar();
        this.affichageGame();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack();

    }

    /**
     * Initialise les attributs de la classe
     */
    public void initAttribut() {
        // Menu
        this.menuNewGame = new JMenuItem("Nouvelle partie");
        this.menuLoadGame = new JMenuItem("Charger une partie");
        this.menuSaveGame = new JMenuItem("Sauvegarder la partie et quitter");
        this.menuHelp = new JMenuItem("Aide");

        game.initHumain(null);
        game.initIA();
        // Maps
        int sizeButton = 10;
        this.mapHumain = new JButton[SIZE][SIZE];
        this.mapIA = new JButton[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.mapHumain[i][j] = new JCase(i, j);
                this.mapIA[i][j] = new JCase(i, j);
            }
        }
        for (Case[] c : game.getHumain().getMapPlayer().getListCase()) {
            for (Case c1 : c) {
                this.mapHumain[c1.getCoordinatesColumn()][c1.getCoordinatesRow()] = new JButton(c1.getContent());
                //this.mapHumain[c1.getCoordinatesColumn()][c1.getCoordinatesRow()].setDisplayedMnemonicIndex();
                this.mapHumain[c1.getCoordinatesColumn()][c1.getCoordinatesRow()].setPreferredSize(new Dimension(sizeButton, sizeButton));
            }
        }
        for (Case[] c : game.getIa().getMapPlayer().getListCase()) {
            for (Case c1 : c) {
                this.mapIA[c1.getCoordinatesColumn()][c1.getCoordinatesRow()] = new JButton(c1.getContent()); // todo mettre * apres debug
                this.mapIA[c1.getCoordinatesColumn()][c1.getCoordinatesRow()].setPreferredSize(new Dimension(sizeButton, sizeButton));
            }
        }
        // Form

    }

    /**
     * Création de la barre de menu
     */
    public void menuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(menuNewGame);
        menuBar.add(menuLoadGame);
        menuBar.add(menuSaveGame);
        menuBar.add(menuHelp);
        //menuBar.setVisible(false);
        this.setJMenuBar(menuBar);
    }

    /**
     * Affichage de la partie avec les deux maps
     */
    public void affichageGame() {
        JPanel game = new JPanel();
        game.setLayout(new BoxLayout(game, BoxLayout.Y_AXIS));
        JPanel maps = new JPanel();
        maps.setLayout(new BoxLayout(maps, BoxLayout.X_AXIS));
        maps.setAlignmentX(CENTER_ALIGNMENT);
        JPanel mapHumain = new JPanel(new GridLayout(SIZE, SIZE));
        mapHumain.setAlignmentX(Component.LEFT_ALIGNMENT);
        mapHumain.setAlignmentY(Component.TOP_ALIGNMENT);
        JPanel mapIA = new JPanel(new GridLayout(SIZE, SIZE));
        mapIA.setAlignmentX(Component.RIGHT_ALIGNMENT);
        mapIA.setAlignmentY(Component.TOP_ALIGNMENT);

        for (JButton[] c : this.mapHumain) {
            for (JButton c1 : c) {
                mapHumain.add(c1);
            }
        }
        for (JButton[] c : this.mapIA) {
            for (JButton c1 : c) {
                mapIA.add(c1);
            }
        }
        maps.add(mapIA);
        //maps.add(new JLabel("VS"));
        // ajouter une barre de séparation verticale
        maps.add(Box.createRigidArea(new Dimension(10, 0)));
        maps.add(mapHumain);

        // Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        game.add(maps);
        game.add(form);
        this.setContentPane(game);
    }

    /**
     * Méthode permettant de recréer une nouvelle partie
     * Méthode non terminée
     */
    public void newGame() {
        this.game = new Game();
        this.game.start();
        // TODO réaliser la méthode
    }

    public JMenuItem getMenuNewGame() {
        return menuNewGame;
    }

    public JMenuItem getMenuLoadGame() {
        return menuLoadGame;
    }

    public JMenuItem getMenuSaveGame() {
        return menuSaveGame;
    }

    public JMenuItem getMenuHelp() {
        return menuHelp;
    }

    public int getSIZE() {
        return SIZE;
    }
    public JButton[][] getMapHumain() {
        return mapHumain;
    }

    public JButton[][] getMapIA() {
        return mapIA;
    }
}
