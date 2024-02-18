package Model.Map;


public class Coordinates {
    private final String boxName; // nom de la case ... Exemple : A3 - B9 - D1 etc ...
    private final int row; // ligne
    private final int col; // colonne

    /**
     * Constructeur de la classe Coordinates
     *
     * @param caseName nom de la case
     * @param l        ligne de la case
     * @param c        colonne de la case
     */
    public Coordinates(String caseName, int l, int c) {
        this.boxName = caseName;
        this.row = l;
        this.col = c;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public String getBoxName() {
        return this.boxName;
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Coordinates sous forme Json
     *
     * @return String au format Json
     */
    @Override
    public String toString() {
        return "\"Coordinates\":{" + "\"boxName\":\"" + boxName + '\"' + ", \"row\":" + row + ", \"col\":" + col + '}';
    }
}
