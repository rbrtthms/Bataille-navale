package Model.Ship;

import Model.Map.Case;
import Model.Map.Map;
import org.json.JSONObject;

import java.util.ArrayList;

public class Destroyer extends Ship {
    private static final int LENGTH_DESTROYER = 3;
    private static final int SHOOT_POWER = 1;
    private static final String SHIP_NAME = "Destroyer";
    private int nbFlareBomb; // fusée éclairante

    /**
     * Constructeur de la classe Destroyer
     *
     * @param id identifiant du bateau
     */
    public Destroyer(String id) {
        super(id, SHIP_NAME, LENGTH_DESTROYER, SHOOT_POWER);
        this.nbFlareBomb = 1;
    }

    /**
     * Constructeur de la classe Destroyer avec toutes ses informations en paramètre
     *
     * @param id          identifiant du bateau
     * @param name        nom du bateau
     * @param size        taille du bateau
     * @param shotPower   puissance de tir du bateau
     * @param cells       liste des cases occupées par le bateau
     * @param shipState   état du bateau
     * @param nbShoot     nombre de tir
     * @param orientation orientation du bateau
     * @param nbFlareBomb nombre de fusées éclairantes
     */
    public Destroyer(String id, String name, int size, int shotPower, ArrayList<Case> cells, boolean shipState, int nbShoot, boolean orientation, int nbFlareBomb) {
        super(id, name, size, shotPower, cells, shipState, nbShoot, orientation);
        this.nbFlareBomb = nbFlareBomb;
    }

    /**
     * Méthode qui permet de créer un Destroyeur à partir d'un JSONObject avec le nom chacun des champs contenant les informations
     *
     * @param listCase    liste des cases de la map
     * @param ship        JSONObject contenant les informations du destroyeur
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
    protected static Destroyer makeDestroyerFromJson(Case[][] listCase, JSONObject ship, String id, String name, String size, String shotPower, String cells, String shipState, String shootsTaken, String orientation) {
        return new Destroyer(id,
                ship.getString(name),
                ship.getInt(size),
                ship.getInt(shotPower),
                Case.getCellsForShipFromJson(ship.getJSONArray(cells), listCase),
                ship.getBoolean(shipState),
                ship.getInt(shootsTaken),
                ship.getBoolean(orientation),
                ship.getInt("nbFlareBomb"));
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
        if (nbFlareBomb > 0)
            return false; // par défaut, on admet qu'il peut tirer avec flare bomb même sur case déjà reveal
        if (enemy.getListCase()[rowBegin][colBegin].getState() != 2) {
            return false;
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
        // énoncé explique que le 1er tir du destroyer dévoile automatiquement les cases
        int cpt=0;
        if (this.nbFlareBomb > 0) {
            for (int l = 0; l < this.getSize() - 1; l++) {
                for (int c = 0; c < this.getSize() - 1; c++) {
                    if (rowBegin + l <= 14 && rowBegin + l >= 0 && colBegin + c <= 14 && colBegin + c >= 0) {
                        enemy.getListCase()[rowBegin + l][colBegin + c].setState(4);
                        cpt--;
                    }
                }
            }
            this.nbFlareBomb = 0;
        } else {
            // on a besoin de traiter que 2 cas => bateaux ou vague le reste pas besoin
            if (enemy.getListCase()[rowBegin][colBegin].getState() == 0) {
                enemy.getListCase()[rowBegin][colBegin].setState(1);
                // pas visible
            } else {
                if (enemy.getListCase()[rowBegin][colBegin].getContent().equals("#")) {
                    enemy.getListCase()[rowBegin][colBegin].setState(1);
                } else {
                    cpt++;
                    enemy.boatTouched(enemy.getListCase()[rowBegin][colBegin]);
                }
            }
        }
        if(cpt<0) System.out.println("Capitaine, Regarder !");
        else if(cpt>0) System.out.println("Cible touché chef");
        else System.out.println("plouf");
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Destroyeur sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "{\"Destroyer\":" + "{" +
                "\"ID\":\"" + getID() + '\"' +
                ", \"name\":\"" + getShipName() + '\"' +
                ", \"cells\":" + getCellsToJson() +
                ", \"size\":" + getSize() +
                ", \"shotPower\":" + shootPower +
                ", \"state\":" + getState() +
                ", \"shootsTaken\":" + getShootsTaken() +
                ", \"orientation\":" + getOrientation() +
                ", \"nbFlareBomb\":" + nbFlareBomb +
                "}}";
    }

}