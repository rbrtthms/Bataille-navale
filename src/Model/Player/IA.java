package Model.Player;

import Model.Map.Map;
import Model.Ship.Direction;
import Model.Ship.Ship;

public class IA extends Player {

    /**
     * Constructeur par default de la classe IA
     */
    public IA() {
        super("IA", false);
    }

    /**
     * Constructeur de la classe IA aves toutes les informations enregistrées de l'IA
     *
     * @param name        Nom du joueur
     * @param stateWinner Etat du joueur (gagnant ou perdant)
     * @param nbShoot     Nombre de tirs effectués
     * @param mapPlayer   Map du joueur
     * @param turnPlayer  Tour du joueur
     */
    public IA(String name, boolean stateWinner, int nbShoot, Map mapPlayer, boolean turnPlayer) {
        super(name, stateWinner, nbShoot, mapPlayer, turnPlayer);
    }

    /**
     * Permet à une IA de déplacer un de ses bateaux aléatoirement sur la map
     *
     * @param map map du joueur
     * @return true si le bateau a été déplacé, false sinon
     */
    public boolean moveShip(Map map) {
        Ship shipToMove = choseRandomShip();
        if (shipToMove != null) {
            Direction direction;
            do {
                direction = choseRandomDirection();
            } while (shipToMove.move(direction, map));
            System.out.println("IA : Le bateau " + shipToMove.getShipName() + " a été déplacé dans la direction " + direction);
            this.hasPlayed();
            return true;
        }
        return false;
    }

    /**
     * Méthode qui renvoie une direction aléatoire parmis les 4 directions possibles
     *
     * @return une direction aléatoire
     */
    public Direction choseRandomDirection() {
        return Direction.values()[(int) (Math.random() * Direction.values().length)];
    }

    /**
     * Méthode qui renvoie un tableau avec des informations aléatoires sur le tir du bateau
     *
     * @param ship le bateau qui tire
     * @return un tableau avec les coordonnées du tir aléatoire et de la case du bateau de départ
     */
    private int[] choseRandomCoord(Ship ship) {
        int[] coord = new int[2];
        coord[0] = (int) (Math.random() * ship.getShootPower()); // Ligne
        coord[1] = (int) (Math.random() * ship.getShootPower()); // Colonne
        return coord;
    }

    /**
     * Méthode qui renvoie un bateau aléatoire parmis les bateaux de l'IA
     *
     * @return un bateau aléatoire
     */
    public Ship choseRandomShip() {
        int random;
        do {
            random = (int) (Math.random() * this.getMapPlayer().getListShip().size());
        } while (this.getMapPlayer().getListShip().get(random).getShootsTaken() != 0);
        return this.getMapPlayer().getListShip().get(random);

    }

    /**
     * Méthode ToString qui retourne le contenu de la classe IA sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "\"IA\":{ " + super.toString() + " }";
    }
}
