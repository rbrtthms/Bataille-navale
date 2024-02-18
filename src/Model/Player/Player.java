package Model.Player;

import Model.Map.Map;
import org.json.JSONObject;

public abstract class Player {

    private int nbShoot;
    private String name;
    private boolean stateWinner;
    private Map mapPlayer;
    private boolean turnPlayer;

    /**
     * Constructeur par défaut de la classe Player
     */
    public Player() {
        this.name = "Player";
        this.stateWinner = false;
        this.nbShoot = 0;
    }

    /**
     * Constructeur de la classe Player
     *
     * @param name       Nom du joueur
     * @param turnPlayer Tour du joueur (Le joueur qui commence est un Humain donc la map est visible sont parameter est true aussi)
     *                   Bof du point de vue maintenabilité
     */
    public Player(String name, boolean turnPlayer) {
        this();
        this.name = name;
        this.turnPlayer = turnPlayer;
        this.mapPlayer = new Map();
        // Initialisation de la map avec les bateaux
        this.mapPlayer.randomShipGeneration();
        this.mapPlayer.distinctBoatOnMap();
    }

    /**
     * Constructeur de la classe Player avec toutes les informations enregistrées
     *
     * @param name        nom du joueur enregistré
     * @param stateWinner état du joueur (gagnant ou non)
     * @param nbShoot     nombre de tirs effectué par le joueur
     * @param mapPlayer   map du joueur
     * @param turnPlayer  tour du joueur
     */
    public Player(String name, boolean stateWinner, int nbShoot, Map mapPlayer, boolean turnPlayer) {
        this.name = name;
        this.stateWinner = stateWinner;
        this.nbShoot = nbShoot;
        this.mapPlayer = mapPlayer;
        this.turnPlayer = turnPlayer;
    }

    /**
     * Méthode qui permet de créer un joueur à partir d'une ligne de fichier json
     *
     * @param objet    ligne de fichier json
     * @param isHumain true si le joueur est humain, false AI
     * @return le joueur créé
     */
    public static Player makePlayerFromJson(String objet, boolean isHumain) {
        Player player;
        JSONObject json = new JSONObject(objet);
        String player1 = "Player";
        String name = "name";
        String stateWinner = "stateWinner";
        String nbShoot = "nbShoot";
        String mapPlayer = "mapPlayer";
        String turnPlayer = "turnPlayer";
        if (isHumain) {
            JSONObject humain = json.getJSONObject("Humain");
            JSONObject playerJson = humain.getJSONObject(player1);
            player = new Humain(playerJson.getString(name),
                    playerJson.getBoolean(stateWinner),
                    playerJson.getInt(nbShoot),
                    Map.makeMapFromJson(playerJson.getJSONObject(mapPlayer)),
                    playerJson.getBoolean(turnPlayer));
        } else { // IA
            JSONObject ia = json.getJSONObject("IA");
            JSONObject playerJson = ia.getJSONObject(player1);
            player = new IA(playerJson.getString(name),
                    playerJson.getBoolean(stateWinner),
                    playerJson.getInt(nbShoot),
                    Map.makeMapFromJson(playerJson.getJSONObject(mapPlayer)),
                    playerJson.getBoolean(turnPlayer));
        }
        return player;
    }


    public Map getMapPlayer() {
        return this.mapPlayer;
    }

    /**
     * Permet de récupérer le nom du joueur
     *
     * @return le nom du joueur
     */
    public String getName() {
        return this.name;
    }

    /**
     * Permet d'enregistrer que le joueur a gagné
     */
    public void won() {
        this.stateWinner = true;
    }

    /**
     * Méthode qui permet de savoir si le joueur a gagné
     *
     * @return true si le joueur a gagné, false sinon
     */
    public boolean isWinner() {
        return this.stateWinner;
    }

    public boolean isTurnPlayer() {
        return this.turnPlayer;
    }

    public void setTurnPlayer(boolean turn) {
        this.turnPlayer = turn;
    }

    public void incrementNbShoot(){
        this.nbShoot++;
    }
    public void hasPlayed() {
        this.turnPlayer = !this.turnPlayer;
    }
    /**
     * Méthode ToString qui retourne le contenu de la classe Player sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "\"Player\":{" +
                "\"name\":\"" + name + '\"' +
                ", \"stateWinner\":" + stateWinner +
                ", \"nbShoot\":" + nbShoot +
                ", \"mapPlayer\":{" + mapPlayer +
                "}, \"turnPlayer\":" + turnPlayer +
                '}';
    }
}
