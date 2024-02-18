package Model.Ship;

import Model.Map.Case;
import Model.Map.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Ship {
    protected final int shootPower; // mis en protected pour que les classes filles puissent y accéder
    protected final ArrayList<Case> cells;
    private final String ID;
    private final String shipName;
    private final int size;
    private int shootsTaken; // nb tir encaissé
    private boolean state; // pour savoir si il bouge ou non false = non - true = oui
    private boolean orientation; // True = horizontale False = verticale


    /**
     * Constructeur de la classe Ship
     *
     * @param ID        identifiant du bateau
     * @param shipName  nom du bateau
     * @param size      taille du bateau
     * @param shotPower puissance de tir du bateau
     */
    public Ship(String ID, String shipName, int size, int shotPower) {
        this.ID = ID;
        this.shipName = shipName;
        this.size = size;
        this.shootPower = shotPower;
        this.cells = new ArrayList<>();
        this.state = true;
        this.shootsTaken = 0;
    }

    /**
     * Constructeur de la classe Ship avec toutes les variables enregistré lors d'une partie
     *
     * @param ID          identifiant du bateau
     * @param shipName    nom du bateau
     * @param size        taille du bateau
     * @param shotPower   puissance de tir du bateau
     * @param cells       liste des cases du bateau
     * @param shipState   état du bateau
     * @param shootsTaken nombre de tirs encaissé
     * @param orientation orientation du bateau
     */
    public Ship(String ID, String shipName, int size, int shotPower, ArrayList<Case> cells, boolean shipState, int shootsTaken, boolean orientation) {
        this.ID = ID;
        this.shipName = shipName;
        this.size = size;
        this.shootPower = shotPower;
        this.cells = cells;
        this.state = shipState;
        this.shootsTaken = shootsTaken;
        this.orientation = orientation;
    }

    /**
     * Méthode qui permet de créer une liste de bateau à partir d'un objet json
     *
     * @param objet    objet contenant les informations necessaries à la création de la liste de bateau
     * @param listCase liste de case de la map permettant de créer les bateaux
     * @return la liste de bateau créée
     */
    public static ArrayList<Ship> makeShipFromJson(JSONArray objet, Case[][] listCase) {
        ArrayList<Ship> listShip = new ArrayList<>();
        for (int i = 0; i < objet.length(); i++) {
            JSONObject ship = objet.getJSONObject(i);
            Object o = objet.getJSONObject(i).names().get(0);
            ship = ship.getJSONObject(o.toString());
            String id = "ID";
            String name = "name";
            String size = "size";
            String shotPower = "shotPower";
            String cells = "cells";
            String shipState = "state";
            String shootsTaken = "shootsTaken";
            String orientation = "orientation";
            if ("Cuirasse".equals(o)) {
                listShip.add(Cuirasse.makeCuirasseFromJson(listCase, ship, id, name, size, shotPower, cells, shipState, shootsTaken, orientation));
            } else if ("Croiseur".equals(o)) {
                listShip.add(Croiseur.makeCroiseurFromJson(listCase, ship, id, name, size, shotPower, cells, shipState, shootsTaken, orientation));
            } else if ("Destroyer".equals(o)) {
                listShip.add(Destroyer.makeDestroyerFromJson(listCase, ship, id, name, size, shotPower, cells, shipState, shootsTaken, orientation));
            } else if ("SubMarine".equals(o)) {
                listShip.add(SubMarine.makeSubMarineFromJson(listCase, ship, id, name, size, shotPower, cells, shipState, shootsTaken, orientation));
            } else {
                System.err.println("Erreur lors de la création du bateau : \n" + ship);
            }
        }
        return listShip;
    }

    /**
     * Méthode qui permet de déplacer un bateau d'une case en fonction de la direction donnée
     *
     * @param dir direction dans laquelle le bateau doit se déplacer
     * @param map map sur laquelle le bateau se déplace
     * @return true si le bateau a pu se déplacer, false sinon
     */
    public boolean move(Direction dir, Map map) {
        String phraseObstacle = "Oups Chef il y a un obstacle dans cette direction !";
        String phraseVertical = "Oups le bateau ne peux pas se déplacer verticalement !";
        String phraseHorizontal = "Oups le bateau ne peux pas se déplacer horizontalement !";
        String phraseMoveShip = "Erreur dépassement de la zone de jeux !";

        if (this.state) {
            switch (dir) {
                case NORTH:
                    if (this.orientation) {
                        System.err.println(phraseVertical);
                        return true;
                    }
                    Case cN = map.getCaseFromCoord(cells.get(0).getCoordinatesColumn(), cells.get(0).getCoordinatesRow() - 1);
                    if (cN == null) {
                        System.err.println(phraseMoveShip);
                        return true;
                    }
                    if (cN.getState() != 0) {
                        System.err.println(phraseObstacle);
                        return true;
                    }
                    cN.setContentAndState(3, cells.get(cells.size() - 1).getContent());
                    cells.get(cells.size() - 1).removeContentAndState();
                    cells.remove(cells.size() - 1);
                    cells.add(cN);
                    break;
                case SOUTH:
                    if (this.orientation) {
                        System.err.println(phraseVertical);
                        return true;
                    }
                    Case cS = map.getCaseFromCoord(cells.get(cells.size() - 1).getCoordinatesColumn(), cells.get(cells.size() - 1).getCoordinatesRow() + 1);
                    if (cS == null) {
                        System.err.println(phraseMoveShip);
                        return true;
                    }
                    if (cS.getState() != 0) {
                        System.err.println(phraseObstacle);
                        return true;
                    }
                    cS.setContentAndState(3, cells.get(0).getContent());
                    cells.get(0).removeContentAndState();
                    cells.remove(0);
                    cells.add(cS);
                    break;
                case EAST:
                    if (!this.orientation) {
                        System.err.println(phraseHorizontal);
                        return true;
                    }
                    Case cE = map.getCaseFromCoord(cells.get(cells.size() - 1).getCoordinatesColumn() + 1, cells.get(cells.size() - 1).getCoordinatesRow());
                    if (cE == null) {
                        System.err.println(phraseMoveShip);
                        return true;
                    }
                    if (cE.getState() != 0) {
                        System.err.println(phraseObstacle);
                        return true;
                    }
                    cE.setContentAndState(3, cells.get(0).getContent());
                    cells.get(0).removeContentAndState();
                    cells.remove(0);
                    cells.add(cE);
                    break;
                case WEST:
                    if (!this.orientation) {
                        System.err.println(phraseHorizontal);
                        return true;
                    }
                    Case cW = map.getCaseFromCoord(cells.get(0).getCoordinatesColumn() - 1, cells.get(0).getCoordinatesRow());
                    if (cW == null) {
                        System.err.println(phraseMoveShip);
                        return true;
                    }
                    if (cW.getState() != 0) {
                        System.err.println(phraseObstacle);
                        return true;
                    }
                    cW.setContentAndState(3, cells.get(cells.size() - 1).getContent());
                    cells.get(cells.size() - 1).removeContentAndState();
                    cells.remove(cells.size() - 1);
                    cells.add(cW);
                    break;
            }
            Collections.sort(cells);
            return false;
        }
        return true;
    }

    public String getShipName() {
        return this.shipName;
    }

    public int getSize() {
        return this.size;
    }

    /**
     * Méthode qui permet d'ajouter une case à la liste de case du bateau
     *
     * @param c case à ajouter
     */
    public void addCells(Case c) {
        this.cells.add(c);
    }

    public abstract boolean alreadyShoot(int rowBegin, int colBegin, Map enemy);

    /**
     * Permet de récupérer la coordonnée du n° de ligne
     *
     * @param i : permet de choisir la cellule dont on veut récupérer le n° de ligne
     * @return le n° de ligne de la cellule
     */
    public int oneCoordRow(int i) {
        return cells.get(i).getCoordinatesRow();
    }

    public abstract void attack(int rowBegin, int colBegin, Map enemy);

    /**
     * Permet de récupérer la coordonnée du n° de colonne
     *
     * @param i : permet de choisir la cellule dont on veut récupérer le n° de colonne
     * @return le n° de colonne de la cellule
     */
    public int oneCoordColumn(int i) {
        return cells.get(i).getCoordinatesColumn();
    }

    public ArrayList<Case> getCells() {
        return this.cells;
    }

    /**
     * Cette fonction permet de vérifier l'état de la case à côté du bateau. Si c'est une vague, on retourne true sinon false
     *
     * @param dir   : direction dans laquelle on veut vérifier l'état de la case
     * @param cases : liste des cases du plateau
     * @return true si la case est une vague, false sinon
     */
    public boolean checkPossibilityMove(Direction dir, Case[][] cases) {
        switch (dir) {
            case NORTH:
                if (cells.get(0).getCoordinatesRow() - 1 >= 0
                        && cases[cells.get(0).getCoordinatesRow() - 1][cells.get(0).getCoordinatesColumn()].getState() == 0) {
                    return true;
                }
                break;

            case SOUTH:
                if (cells.get(cells.size() - 1).getCoordinatesRow() + 1 <= 14 &&
                        cases[cells.get(cells.size() - 1).getCoordinatesRow() + 1][cells.get(cells.size() - 1).getCoordinatesColumn()].getState() == 0) {
                    return true;
                }
                break;
            case EAST:
                if (cells.get(cells.size() - 1).getCoordinatesColumn() + 1 <= 14 &&
                        cases[cells.get(cells.size() - 1).getCoordinatesRow()][cells.get(cells.size() - 1).getCoordinatesColumn() + 1].getState() == 0) {
                    return true;
                }
                break;
            case WEST:
                if (cells.get(0).getCoordinatesColumn() - 1 >= 0 &&
                        cases[cells.get(0).getCoordinatesRow()][cells.get(0).getCoordinatesColumn() - 1].getState() == 0) {
                    return true;
                }
                break;

        }
        return false;
    }

    /**
     * Méthode qui permet de savoir si un bateau contient bien une certaine case
     *
     * @param c : la case qu'on veut vérifier
     * @return true si la case est dans le bateau, false sinon
     */
    public boolean holdThisCase(Case c) {
        for (int j = 0; j < cells.size(); j++) {
            if (cells.get(j).getCoordinatesName().equals(c.getCoordinatesName())) return true;
        }
        return false;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isAlive() {
        return this.size - shootsTaken > 0;
    }

    public int getShootPower() {
        return this.shootPower;
    }

    public void upShootsTaken() {
        if (this.shootsTaken != this.size) {
            this.shootsTaken = this.shootsTaken + 1;
        }
    }

    public int getShootsTaken() {
        return this.shootsTaken;
    }

    public boolean getOrientation() {
        return this.orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }

    public String getID() {
        return this.ID;
    }

    /**
     * Méthode qui permet de fabriquer un String contenant les coordonnées de toutes les cases du bateau
     *
     * @return le String contenant les coordonnées de toutes les cases du bateau
     */
    protected StringBuilder getCellsToJson() {
        StringBuilder cells = new StringBuilder();
        cells.append("[");
        for (Case c : this.cells) {
            if (this.cells.indexOf(c) == this.cells.size() - 1) {
                cells.append("\"").append(c.getCoordinatesName()).append("\"");
            } else {
                cells.append("\"").append(c.getCoordinatesName()).append("\",");
            }
        }
        cells.append("]");
        return cells;
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Ship sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "{" + //{"Ship":
                "\"ID\":\"" + ID + '\"' +
                ", \"name\":\"" + shipName + '\"' +
                ", \"cells\":" + getCellsToJson() +
                ", \"size\":" + size +
                ", \"shotPower\":" + shootPower +
                ", \"state\":" + state +
                ", \"shootsTaken\":" + shootsTaken +
                ", \"orientation\":" + orientation +
                "}";
    }

    /**
     * Méthode qui permet d'afficher les informations utiles pour le joueur
     *
     * @return String contenant les informations utiles pour le joueur
     */
    public String detailsBoat() {
        String coord = "";
        for (Case c : cells) {
            coord = coord + " " + c.getCoordinatesName();
        }
        String state = "";
        if (!this.state) {
            state = "touché";
        } else {
            state = "pas touché";
        }
        return shipName + "-" + ID + ":{" +
                " cases:" + coord +
                ", taille:" + size +
                ", puissance de tir:" + shootPower +
                ", état du bateau:" + state +
                '}';
    }
}

