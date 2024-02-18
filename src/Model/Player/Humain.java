package Model.Player;

import Model.Map.Map;
import Model.Ship.Direction;
import Model.Ship.Ship;

public class Humain extends Player {

    /**
     * Constructeur de la classe Humain
     * Donne le premier tour au joueur
     *
     * @param name Nom du joueur
     */
    public Humain(String name) {
        super(name, true);
    }

    /**
     * Constructeur de la classe Humain avec toutes les informations enregistrées de l'humain
     *
     * @param name        Nom du joueur
     * @param stateWinner État du joueur (gagnant ou perdant)
     * @param nbShoot     Nombre de tirs effectués
     * @param mapPlayer   Map du joueur
     * @param turnPlayer  Tour du joueur
     */
    public Humain(String name, boolean stateWinner, int nbShoot, Map mapPlayer, boolean turnPlayer) {
        super(name, stateWinner, nbShoot, mapPlayer, turnPlayer);
    }


    /**
     * Méthode qui permet de déplacer un bateau sur la map du joueur
     *
     * @param direction direction dans laquelle le bateau doit être déplacé
     * @param map       map du joueur
     * @param nameShip  nom du bateau à déplacer
     * @return true si le bateau a été déplacé, false sinon
     */
    public boolean moveShip(Direction direction, Map map, String nameShip) {
        Ship shipToMove = findShipWithName(nameShip);
        if (shipToMove != null) {
            if (shipToMove.move(direction, map)) {
                System.out.println("Le bateau " + shipToMove.getShipName() + " a été déplacé dans la direction " + direction);
                this.hasPlayed();
                return true;
            }
        }
        return false;
    }


    /**
     * Méthode qui retourne un bateau à partir de son nom sinon null
     *
     * @param nameShip nom du bateau à trouver
     * @return le bateau trouvé
     */
    private Ship findShipWithName(String nameShip) {
        for (Ship ship : this.getMapPlayer().getListShip()) {
            if (ship.getID().equals(nameShip)) { // Si un nom de bateau correspond alors on bouge le bateau
                return ship;
            }
        }
        System.err.println("Le bateau n'a pas été trouvé");
        return null;
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Humain sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "\"Humain\":{ " + super.toString() + " }";
    }
}
