package Model.Ship;

import Model.Map.Case;
import Model.Map.Map;
import org.json.JSONObject;

import java.util.ArrayList;

public class Croiseur extends Ship {

    private static final int LENGTH_CROISEUR = 5;
    private static final int SHOOT_POWER = 4;
    private static final String SHIP_NAME = "Croiseur";

    /**
     * Constructeur de la classe Croiseur
     *
     * @param id identifiant du croiseur
     */
    public Croiseur(String id) {
        super(id, SHIP_NAME, LENGTH_CROISEUR, SHOOT_POWER);
    }

    /**
     * Constructeur de la classe Croiseur avec toutes ces caractéristiques
     *
     * @param id          identifiant du croiseur
     * @param name        nom du croiseur
     * @param size        taille du croiseur
     * @param shotPower   puissance de tir du croiseur
     * @param cells       liste des cases du croiseur
     * @param shipState   état du croiseur
     * @param nbShoot     nombre de tirs du croiseur qui a touché le bateau
     * @param orientation orientation du croiseur
     */
    public Croiseur(String id, String name, int size, int shotPower, ArrayList<Case> cells, boolean shipState, int nbShoot, boolean orientation) {
        super(id, name, size, shotPower, cells, shipState, nbShoot, orientation);
    }

    /**
     * Méthode qui permet de créer un Croiseur à partir d'un JSONObject avec le nom chacun des champs contenant les informations
     *
     * @param listCase    liste des cases de la map
     * @param ship        JSONObject contenant les informations du croiseur
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
    protected static Croiseur makeCroiseurFromJson(Case[][] listCase, JSONObject ship, String id, String name, String size, String shotPower, String cells, String shipState, String shootsTaken, String orientation) {
        return new Croiseur(id,
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
     * si return false alors on n'a pas déjà tiré sur la case et donc on va pouvoir tirer dessus
     * À utiliser avant de sélectionner la case de tir
     *
     * @param rowBegin coordonnée ligne à vérifier
     * @param colBegin coordonnée cologne à vérifier
     * @param enemy    map du joueur adverse
     * @return un booléen en fonction de la case
     */
    public boolean alreadyShoot(int rowBegin, int colBegin, Map enemy) {
        for (int l = 0; l < this.shootPower / 2; l++) {
            for (int c = 0; c < this.shootPower / 2; c++) {
                if (rowBegin + l > 0 && rowBegin + l < 15 && colBegin + c > 0 && colBegin + c < 15) {
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
        int cpt=0;
        // on a besoin de traiter que 2 cas => bateaux ou vague le reste pas besoin
        for (int l = 0; l < this.shootPower / 2; l++) {
            for (int c = 0; c < this.shootPower / 2; c++) {
                if (rowBegin + l > 0 && rowBegin + l < 15 && colBegin + c > 0 && colBegin + c < 15) {
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
        if(cpt!=0) System.out.println("BOUM ! ça fait mouche");
        else System.out.println("Purée de pomme de terre on a pas de chance capitaine");

    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Croiseur sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "{\"Croiseur\":" + super.toString() + "}";
    }
}
