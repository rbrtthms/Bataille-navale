package Model.Ship;

import Model.Map.Case;
import Model.Map.Map;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class SubMarine extends Ship {

    private static final int LENGTH_SUBMARINE = 1;
    private static final int SHOOT_POWER = 1;
    private static final String SHIP_NAME = "SubMarine";

    /**
     * Constructeur de la classe SousMarin
     *
     * @param id identifiant du sous-marin
     */
    public SubMarine(String id) {
        super(id, SHIP_NAME, LENGTH_SUBMARINE, SHOOT_POWER);
    }

    /**
     * Constructeur de la classe SousMarin avec toutes ces caractéristiques
     *
     * @param id          identifiant du sous-marin
     * @param name        nom du sous-marin
     * @param size        taille du sous-marin
     * @param shotPower   puissance de tir du sous-marin
     * @param cells       liste des cases du sous-marin
     * @param shipState   état du sous-marin
     * @param nbShoot     nombre de tirs du sous-marin
     * @param orientation orientation du sous-marin
     */
    public SubMarine(String id, String name, int size, int shotPower, ArrayList<Case> cells, boolean shipState, int nbShoot, boolean orientation) {
        super(id, name, size, shotPower, cells, shipState, nbShoot, orientation);
    }

    /**
     * Méthode qui permet de créer un Sous-marin à partir d'un JSONObject avec le nom chacun des champs contenant les informations
     *
     * @param listCase    liste des cases de la map
     * @param ship        JSONObject contenant les informations du sous-marin
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
    protected static SubMarine makeSubMarineFromJson(Case[][] listCase, JSONObject ship, String id, String name, String size, String shotPower, String cells, String shipState, String shootsTaken, String orientation) {
        return new SubMarine(id,
                ship.getString(name),
                ship.getInt(size),
                ship.getInt(shotPower),
                Case.getCellsForShipFromJson(ship.getJSONArray(cells), listCase),
                ship.getBoolean(shipState),
                ship.getInt(shootsTaken),
                ship.getBoolean(orientation));
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
        int cpt=0;
        if (enemy.getListCase()[rowBegin][colBegin].getState() == 0) {
            enemy.getListCase()[rowBegin][colBegin].setState(1);
        } else {
            cpt++;
            enemy.boatTouched(enemy.getListCase()[rowBegin][colBegin]);
        }
        if(cpt!=0) System.out.println("Dans le mille");
        else System.out.println("J'enrage capitaine ! on a loupé");
    }

    /**
     * Méthode qui permet de déplacer un bateau d'une case en fonction de la direction donnée
     *
     * @param dir direction dans laquelle le bateau doit se déplacer
     * @param map map sur laquelle le bateau se déplace
     * @return true si le bateau peut se déplacer dans la direction dir, false sinon
     */
    @Override
    public boolean move(Direction dir, Map map) {
        String phraseObstacle = "Oups Chef il y a un obstacle dans cette direction !";
        String phraseMoveShip = "Erreur dépassement de la zone de jeux !";

        if (this.getState()) {
            switch (dir) {
                case NORTH:
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
        return enemy.getListCase()[rowBegin][colBegin].getState() == 2;
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Sous-marin sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "{\"SubMarine\":" + super.toString() + "}";
    }

    public boolean checkPossibilityMove(Direction dir, Case[][] cases) {
        // dir inutile
        if (cells.get(0).getCoordinatesRow() - 1 >= 0
                && cases[cells.get(0).getCoordinatesRow() - 1][cells.get(0).getCoordinatesColumn()].getState() == 0) {
            return true;
        }
        if (cells.get(cells.size() - 1).getCoordinatesRow() + 1 <= 14 &&
                cases[cells.get(cells.size() - 1).getCoordinatesRow() + 1][cells.get(cells.size() - 1).getCoordinatesColumn()].getState() == 0) {
            return true;
        }

        if (cells.get(cells.size() - 1).getCoordinatesColumn() + 1 <= 14 &&
                cases[cells.get(cells.size() - 1).getCoordinatesRow()][cells.get(cells.size() - 1).getCoordinatesColumn() + 1].getState() == 0) {
            return true;
        }
        if (cells.get(0).getCoordinatesColumn() - 1 >= 0 &&
                cases[cells.get(0).getCoordinatesRow()][cells.get(0).getCoordinatesColumn() - 1].getState() == 0) {
            return true;
        }
        return false;
    }
}