package Model.Ship;

import Model.Map.Case;
import Model.Map.Map;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cuirasse extends Ship {

    private static final int LENGTH_CUIRASSE = 7;
    private static final int SHOOT_POWER = 9;
    private static final String SHIP_NAME = "Cuirasse";

    /**
     * Constructeur de la classe Cuirasse
     *
     * @param id identifiant du bateau
     */
    public Cuirasse(String id) {
        super(id, SHIP_NAME, LENGTH_CUIRASSE, SHOOT_POWER);
    }

    /**
     * Constructeur de la classe Cuirasse avec toutes ses informations en paramètre
     *
     * @param id          identifiant du bateau
     * @param name        nom du bateau
     * @param size        taille du bateau
     * @param shotPower   puissance de tir du bateau
     * @param cells       liste des cases occupées par le bateau
     * @param shipState   état du bateau
     * @param nbShoot     nombre de tir
     * @param orientation orientation du bateau
     */
    public Cuirasse(String id, String name, int size, int shotPower, ArrayList<Case> cells, boolean shipState, int nbShoot, boolean orientation) {
        super(id, name, size, shotPower, cells, shipState, nbShoot, orientation);
    }

    /**
     * Méthode qui permet de créer un cuirasse à partir d'un JSONObject avec le nom chacun des champs contenant les informations
     *
     * @param listCase    liste des cases de la map
     * @param ship        JSONObject contenant les informations du cuirasse
     * @param id          nom du champ id
     * @param name        nom du champ name
     * @param size        nom du champ size
     * @param shotPower   nom du champ shotPower
     * @param cells       nom du champ cells
     * @param shipState   nom du champ shipState
     * @param shootsTaken nom du champ shootsTaken
     * @param orientation nom du champ orientation
     * @return un cuirasse
     */
    protected static Cuirasse makeCuirasseFromJson(Case[][] listCase, JSONObject ship, String id, String name, String size, String shotPower, String cells, String shipState, String shootsTaken, String orientation) {
        return new Cuirasse(ship.getString(id),
                ship.getString(name),
                ship.getInt(size),
                ship.getInt(shotPower),
                Case.getCellsForShipFromJson(ship.getJSONArray(cells), listCase),
                ship.getBoolean(shipState),
                ship.getInt(shootsTaken),
                ship.getBoolean(orientation));
    }

    /**
     * Vérifie si on a déjà tiré sur la case, si c'est le cas return true donc on ne pourra pas re-tirer sur la case
     * si return false alors on a pas déjà tiré sur la case et donc on va pouvoir tirer dessus
     * À utiliser avant de sélectionner la case de tir
     *
     * @param rowBegin coordonnée ligne à vérifier
     * @param colBegin coordonnée cologne à vérifier
     * @param enemy    map du joueur adverse
     * @return un booléen en fonction de la case
     */
    public boolean alreadyShoot(int rowBegin, int colBegin, Map enemy) {
        for (int l = -1; l < (this.shootPower / 2) - 2; l++) {
            for (int c = -1; c < (this.shootPower / 2) - 2; c++) {
                if (rowBegin + l >= 0 && rowBegin + l < 15 && colBegin + c >= 0 && colBegin + c < 15) {
                    if (enemy.getListCase()[rowBegin + l][colBegin + c].getState() != 2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Méthode permettant de modifier l'état de chaque case étant touché par l'attaque d'un bateau
     * état 4 = dévoilement de la case temporaire
     * état 1 = tir sur une vague (état temporaire)
     * état 2 = tir réussi sur un bateau
     *
     * @param rowBegin coordonnée ligne de la case
     * @param colBegin coordonnée colonne de la case
     * @param enemy    map de l'ennemie
     */
    public void attack(int rowBegin, int colBegin, Map enemy) {
        // on a besoin de traiter que 2 cas => bateaux ou vague le reste pas besoin
        // * * *
        // * x * => rowBegin et colBegin correspond au X, donc on va faire -1, -1
        // * * *
        int cpt = 0;
        for (int l = -1; l < (this.shootPower / 2) - 2; l++) {
            for (int c = -1; c < (this.shootPower / 2) - 2; c++) {
                if (rowBegin + l >= 0 && rowBegin + l < 15 && colBegin + c >= 0 && colBegin + c < 15) {
                    if (enemy.getListCase()[rowBegin + l][colBegin + c].getState() == 0) {
                        enemy.getListCase()[rowBegin + l][colBegin + c].setState(1);
                    } else {
                        if (enemy.getListCase()[rowBegin + l][colBegin + c].getContent().equals("#")) {
                            enemy.getListCase()[rowBegin + l][colBegin + c].setState(1);
                        } else {
                            cpt++;
                            enemy.boatTouched(enemy.getListCase()[rowBegin + l][colBegin + c]);
                        }
                    }
                }
            }
        }
        if(cpt!=0) System.out.println("Attaque réussi capitaine");
        else System.out.println("RAAAAAARRRRRR ! à côté capitaine");

    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Cuirasse sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "{\"Cuirasse\":" + super.toString() + "}";
    }
}

