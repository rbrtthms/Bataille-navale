package Model.Map;

import org.json.JSONArray;

import java.util.ArrayList;

public class Case implements Comparable<Case> {
    // Pour différencier affichage au niveau de chaque case de la map
    // ~ = dévoilé, mais pas de bateau (fusée éclairante) - vague
    // x = Tir ennemi
    // + = bateau touché
    // choix multiple = bateau
    // * = caché
    private final Coordinates coordinates;
    private boolean visible; // Uniquement utile pour ordi : false = non => on cache pour ordi - true = oui on affiche content
    private int state; // clé permettant de savoir quel content afficher
    // 0 => ~
    // 1 => x => temporaire le temps d'un tour lors d'une attaque
    // 2 => +
    // 3 => choix multiple
    // 4 => temporaire le temps d'un tour lors d'un dévoilement de case du Destroyer
    private String content;

    /**
     * Constructeur de la classe Case
     *
     * @param name  nom de la case
     * @param l     ligne de la case
     * @param c     colonne de la case
     * @param state état de la case
     */
    public Case(String name, int l, int c, int state) {
        this.state = state;
        this.content = "~";
        this.coordinates = new Coordinates(name, l, c);
    }

    /**
     * Constructeur de la classe Case avec toutes les informations
     *
     * @param name    nom de la case
     * @param content contenu de la case
     * @param state   état de la case
     * @param l       ligne de la case
     * @param c       colonne de la case
     * @param visible visibilité de la case
     */
    public Case(String name, String content, int state, int l, int c, boolean visible) {
        this(name, l, c, state);
        this.content = content;
        this.visible = visible;
    }

    /**
     * Permet de récupérer les cases d'un bateau à partir d'un JSONArray
     *
     * @param ship     JSONArray contenant les cases du bateau
     * @param listCase des cases dans la map
     * @return les cases du bateau
     */
    public static ArrayList<Case> getCellsForShipFromJson(JSONArray ship, Case[][] listCase) {
        ArrayList<Case> cells = new ArrayList<>();
        for (int i = 0; i < ship.length(); i++) {
            cells.add(findCaseWithName(ship.getString(i), listCase));
        }
        return cells;
    }

    /**
     * Méthode permettant de trouver une case dans la liste des cases de la map
     *
     * @param name     nom de la case à trouver
     * @param listCase liste des cases de la map
     * @return la case trouvée
     */
    private static Case findCaseWithName(String name, Case[][] listCase) {
        for (int i = 0; i < listCase.length; i++) {
            for (int j = 0; j < listCase[i].length; j++) {
                if (listCase[i][j].coordinates.getBoxName().equals(name)) {
                    return listCase[i][j];
                }
            }
        }
        return null;
    }


    /**
     * Permet de modifier le contenu d'une case
     *
     * @param state : numéro de la clé qui va pointer sur content
     * @param size  : taille du bateau pour savoir quoi afficher en console
     */
    public void setContentStart(int state, int size) {
        if (state == 0) {
            content = "~";
        }
        if (state == 1) {
            content = "x";
        }
        if (state == 2) {
            content = "+";
        }
        if (state == 3) {
            if (size == 1) {
                content = "#";
            } else if (size == 3) {
                content = "^";
            } else if (size == 5) {
                content = "u";
            } else {
                content = "o";
            }

        }
    }

    /**
     * Permet de modifier le contenu d'une case et son état
     *
     * @param state   : nouvel état de la case
     * @param content : nouveau contenu de la case
     */
    public void setContentAndState(int state, String content) {
        this.state = state;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCoordinatesRow() {
        return this.coordinates.getRow();
    }

    public int getCoordinatesColumn() {
        return this.coordinates.getCol();
    }

    public String getCoordinatesName() {
        return this.coordinates.getBoxName();
    }

    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Méthode qui permet de changer la visibilité d'une case
     *
     * @param visible stat de visibilité de la case
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Permet de modifier le contenu d'une case touché par un tir ennemi
     */
    public void setContentTouched() {
        this.content = "+";
    }

    /**
     * Méthode permettant d'affecter une case en tant que mer (vide)
     */
    public void removeContentAndState() {
        this.content = "~";
        this.state = 0;
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Case sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "\"Case\":{" +
                "\"visible\":" + visible +
                ", \"stateReveal\":" + state +
                ", \"content\":\"" + content + '\"' +
                ", \"coordinates\":{" + coordinates +
                "}}";
    }

    /**
     * Méthode permettant de comparer deux cases
     *
     * @param c case à comparer
     * @return indice de comparaison entre les deux cases
     */
    @Override
    public int compareTo(Case c) {
        if (this.getCoordinatesRow() == c.getCoordinatesRow()) {
            return this.getCoordinatesColumn() - c.getCoordinatesColumn();
        } else {
            return this.getCoordinatesRow() - c.getCoordinatesRow();
        }
    }
}
