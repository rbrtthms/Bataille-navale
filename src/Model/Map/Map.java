package Model.Map;

import Model.Ship.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Map {
    private final int SIZE_MAP = 15;
    private final String ALPHABET = "ABCDEFGHIJKLMNOP";
    private final Case[][] listCase;
    private final ArrayList<Ship> listShip;

    /**
     * Constructeur par default de la classe Map
     */
    public Map() {
        this.listCase = new Case[SIZE_MAP][SIZE_MAP];
        this.listShip = new ArrayList<>();
        for (int i = 0; i < SIZE_MAP; i++) {
            for (int j = 0; j < SIZE_MAP; j++) {
                this.listCase[i][j] = new Case(ALPHABET.charAt(j) + Integer.toString(i), i, j, 0);
                this.listCase[i][j].setVisible(false);
            }
        }
    }

    /**
     * Constructeur pour initialiser une avec une listeCase et une listeShip déjà existante
     *
     * @param listCase liste de cases
     * @param listShip liste de bateaux
     */
    public Map(Case[][] listCase, ArrayList<Ship> listShip) {
        this.listCase = listCase;
        this.listShip = listShip;
    }

    /**
     * Méthode qui permet de créer une map à partir d'un objet json
     *
     * @param objet objet contenant les informations necessaries à la création de la map
     * @return la map créée
     */
    public static Map makeMapFromJson(JSONObject objet) {
        objet = objet.getJSONObject("Map");
        int sizeMap = objet.getInt("SIZE_MAP");
        Case[][] listCase = new Case[sizeMap][sizeMap];
        for (int i = 0; i < sizeMap; i++) {
            for (int j = 0; j < sizeMap; j++) {
                JSONObject caseJson = objet.getJSONArray("listCase").getJSONArray(i).getJSONObject(j).getJSONObject("Case");
                JSONObject coordinates = caseJson.getJSONObject("coordinates").getJSONObject("Coordinates");
                listCase[i][j] = new Case(coordinates.getString("boxName"),
                        caseJson.getString("content"),
                        caseJson.getInt("stateReveal"),
                        coordinates.getInt("row"),
                        coordinates.getInt("col"),
                        caseJson.getBoolean("visible"));
            }
        }
        //listShip
        ArrayList<Ship> listShip = Ship.makeShipFromJson(objet.getJSONArray("listShip"), listCase);

        return new Map(listCase, listShip);
    }

    /**
     * Méthode qui permet de placer les bateaux dans la map aléatoirement
     */
    public void randomShipGeneration() {
        setListShip();
        Random random = new Random();
        int randomCoordColonne, randomCoordLigne, cpt = 0; // cpt permet de savoir cmb de bateau dans listship ont des coordonnées
        for (Ship ship : listShip) {
            boolean randomDirection = random.nextBoolean(); // false = horizontal - true = vertical
            ship.setOrientation(randomDirection);
            if (!randomDirection) { // horizontal
                do {
                    randomCoordLigne = random.nextInt(SIZE_MAP);
                    randomCoordColonne = random.nextInt(SIZE_MAP);
                    // les 2 paramètres vont par défaut correspondre à la tête du bateau qu'on va placer soit
                    // du Nord vers le Sud ou de l'ouest vers l'est
                } while (randomCoordLigne + ship.getSize() >= SIZE_MAP || collisions(randomCoordLigne, randomCoordColonne, ship.getSize(), randomDirection, cpt));
                for (int i = 0; i < ship.getSize(); i++) {
                    ship.addCells(listCase[randomCoordLigne + i][randomCoordColonne]); // +i car on va du nord au sud
                    listCase[randomCoordLigne + i][randomCoordColonne].setState(3);
                }

            } else { // verticale
                do {
                    randomCoordLigne = random.nextInt(SIZE_MAP);
                    randomCoordColonne = random.nextInt(SIZE_MAP);
                } while (randomCoordColonne + ship.getSize() > SIZE_MAP || collisions(randomCoordLigne, randomCoordColonne, ship.getSize(), randomDirection, cpt));
                for (int i = 0; i < ship.getSize(); i++) {
                    ship.addCells(listCase[randomCoordLigne][randomCoordColonne + i]); // +i, car on va de l'ouest vers l'est
                    listCase[randomCoordLigne][randomCoordColonne + i].setState(3);
                }
            }
            cpt++;
        }
    }

    /**
     * Méthode qui vérifie s'il y a une collision entre deux bateaux
     *
     * @param coordL    : coordonnées de la ligne du bateau sur lequel on vérifie s'il y a une collision
     * @param coordC    : coordonnées de la colonne du bateau sur lequel on vérifie s'il y a une collision
     * @param sizeShip  : taille du bateau
     * @param direction : direction du bateau
     * @return : true si il y a une collision, false sinon
     */
    private boolean collisions(int coordL, int coordC, int sizeShip, boolean direction, int boatWithCoordonates) {
        ArrayList<Case> temporaire = new ArrayList<>();
        if (listShip.isEmpty() || boatWithCoordonates == 0 || listShip.get(0).getSize() <= 0 || listShip.get(0).getCells().isEmpty())
            return false;
        if (!direction) { // !direction veut dire direction==false
            for (int i = 0; i < sizeShip; i++) {
                temporaire.add(listCase[coordL + i][coordC]);
            }
        } else {
            temporaire.addAll(Arrays.asList(listCase[coordL]).subList(0 + coordC + 0, sizeShip + coordC + 0));
        }
        for (int i = 0; i < boatWithCoordonates; i++) { // tous les bateaux avec des coordonnées réels
            for (int j = 0; j < listShip.get(i).getSize(); j++) { // taille des bateaux = nb cells
                for (int k = 0; k < temporaire.size(); k++) { // chaque cells dans temporaire
                    if (temporaire.get(k).getCoordinatesName().equals(listShip.get(i).getCells().get(j).getCoordinatesName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Ship> getListShip() {
        return listShip;
    }

    /**
     * Méthode qui permet d'afficher la liste des bateaux avec leurs informations
     */
    public void listShipOnMap() {
        for (int i = 0; i < listShip.size(); i++) {
            System.out.println(i + 1 + ") " + listShip.get(i).detailsBoat());
        }
    }

    public Case[][] getListCase() {
        return this.listCase;
    }

    /**
     * Méthode permettant d'initialiser les bateaux sans leur données de coordonnées dans la map
     */
    private void setListShip() {
        int NB_SHIP_CUIRASSE = 1;
        int NB_SHIP_CROISEURS = 2;
        int NB_SHIP_DESTROYERS = 3;
        int NB_SHIP_SUBMARINES = 4;
        for (int i = 0; i < NB_SHIP_CUIRASSE; i++) {
            this.listShip.add(new Cuirasse("CU"));
        }
        for (int i = 0; i < NB_SHIP_CROISEURS; i++) {
            this.listShip.add(new Croiseur("CR" + i));
        }
        for (int i = 0; i < NB_SHIP_DESTROYERS; i++) {
            this.listShip.add(new Destroyer("DE" + i));
        }
        for (int i = 0; i < NB_SHIP_SUBMARINES; i++) {
            this.listShip.add(new SubMarine("SM" + i));
        }
    }

    /**
     * Parcours du tableau de bateau et pour chaque case met en place le bon contenu à afficher
     * en fonction du numéro qu'on envoie et de la taille du bateau
     */
    public void distinctBoatOnMap() {
        for (Ship boat : listShip) {
            for (int a = 0; a < boat.getSize(); a++) {
                if (listCase[boat.oneCoordRow(a)][boat.oneCoordColumn(a)].getState() == 2) {
                    listCase[boat.oneCoordRow(a)][boat.oneCoordColumn(a)].setContentStart(2, boat.getSize());
                } else {
                    listCase[boat.oneCoordRow(a)][boat.oneCoordColumn(a)].setContentStart(3, boat.getSize()); // state >=3
                }

            }
        }
    }

    /**
     * Permet d'afficher la map du joueur en fonction de la position de ses bateaux et du type de map souhaité (humain ou ai)
     * Si vous faites this.humain.getMapPlayer().printMap(true) = toujours mettre true
     * Si vous faites this.ia.getMapPlayer().printMap(false) = toujours mettre false
     *
     * @param isHuman : true pour afficher les bateaux du joueur, false pour masquer les bateaux de l'ia
     */
    public void printMap(boolean isHuman) { // false = map ia qu'il faudra cacher - true = map joueur qu'il faut afficher
        for (int borderTopMap = 0; borderTopMap < SIZE_MAP; borderTopMap++) {
            System.out.print("+--");
        }
        String RESET_COLOR = "\u001B[0m";
        System.out.print("+");
        System.out.println();
        for (int y = 0; y < SIZE_MAP; y++) {
            System.out.print("|");
            for (int x = 0; x < SIZE_MAP; x++) {
                if (isHuman) {
                    System.out.print(" " + listCase[y][x].getContent() + " ");
                } else {
                    if (listCase[y][x].isVisible()) {
                        System.out.print(" " + listCase[y][x].getContent() + " ");
                    } else if (listCase[y][x].getState() == 1) {
                        System.out.print("\033[0;31m" + " " + "x" + " " + RESET_COLOR);
                    } else if (listCase[y][x].getState() == 4) {
                        System.out.print("\033[0;31m" + " " + listCase[y][x].getContent() + " " + RESET_COLOR);
                    } else {
                        System.out.print("\033[0;31m" + " " + "*" + " " + RESET_COLOR);
//                        System.out.print(" " + listCase[y][x].getContent() + " ");
                    }
                }

            }
            System.out.print("| " + (y));
            System.out.println();
        }
        for (int borderBottomMap = 0; borderBottomMap < SIZE_MAP; borderBottomMap++) {
            System.out.print("+--");
        }
        System.out.print("+");
        System.out.println();
        for (int num = 0; num < SIZE_MAP; num++) {
            System.out.print("  " + (ALPHABET.charAt(num)));
        }
        System.out.println();
    }

    public Case getCaseFromName(String name) {
        for (int l = 0; l < SIZE_MAP; l++) {
            for (int c = 0; c < SIZE_MAP; c++) {
                if (listCase[l][c].getCoordinatesName().equals(name)) return listCase[l][c];
            }
        }
        return null; // on n'arrive jamais ici si on fait bien les choses
    }

    public Case getCaseFromCoord(int c, int l) {
        if (l >= 0 && l < SIZE_MAP && c >= 0 && c < SIZE_MAP)
            return listCase[l][c];
        else {
            return null;
        }
    }

    /**
     * Dans une case, on a pour attribut :
     * - state : int
     * - content : String
     * state c'est comme une clé qui va déterminer content.
     * état 1 :
     * ~ o ~
     * ~ o ~
     * ~ o ~
     * Imaginons le tableau réduit(3x3) ci-dessus qui correspond à la map adverse. 6 cases possèdent state = 0 et content = "~"
     * les 3 autres possèdent state = 3 et content ="o"
     * attaquons cette map avec le cuirassé qui fait 9 de dégats de zone :
     * état 2 :
     * x x x
     * x x x
     * x x x
     * On obtient alors cet état temporaire pour l'affichage en console, on imagine donc qu'on a changé state et content
     * dans chaque case donc on a pour chaque case : state = 1 et content ="x"
     * Cependant l'état 2 est temporaire ... comment faire pour passer de l'état 2 à l'état 3 ci-dessous (avec les +
     * qui correspondent à des parties de bateau touché) ?
     * état 3 :
     * ~ + ~
     * ~ + ~
     * ~ + ~
     * Et bien la solution, c'est à chaque fois de ne changer que l'attribut state sans toucher le contenu.
     * Ainsi si on revient l'état 2 on aura cette fois pour 6 cases state=1 et content="~"
     * et pour 3 cases state=1 et content="o"
     * Maintenant il faut réussir à passer à l'état 3 tout en faisant la différence entre vague et bateau ...
     * C'est là que la méthode reloadState() intervient pour permettre de passer de l'état 2 à 3.
     * Si content="~" alors on remet state=1 sinon si le contenu est différent alors c'est forcément un bateau donc
     * state=2
     * Mais alors on change state, c'est bien ... mais content ? Et bien il faut voir la méthode distinctBoatOnMap()
     */
    public void reloadState() {
        for (int l = 0; l < SIZE_MAP; l++) {
            for (int c = 0; c < SIZE_MAP; c++) {
                if (listCase[l][c].getState() == 1) {
                    if (listCase[l][c].getContent().equals("~")) {
                        listCase[l][c].setState(0); // 0 pour la vague
                    } else {
                        listCase[l][c].setState(3); // 3 pour les bateaux
                    }
                }
                if (listCase[l][c].getState() == 4) {
                    if (listCase[l][c].getContent().equals("~")) {
                        listCase[l][c].setState(0);
                    } else if (listCase[l][c].getContent().equals("x")) {
                        listCase[l][c].setState(1);
                    } else if (listCase[l][c].getContent().equals("+")) {
                        listCase[l][c].setState(2);
                    } else {
                        listCase[l][c].setState(3);
                    }
                }
            }
        }
    }

    /**
     * Fonction qui retourne une case aléatoire. Elle est utilisée quand l'ia va attaquer de manière aléatoirement
     *
     * @return une case aléatoire
     */

    public Case getRandomCase() {
        Random rand = new Random();
        int l = rand.nextInt(SIZE_MAP); // entre 0 et 14
        int c = rand.nextInt(SIZE_MAP);
        return listCase[l][c];
    }

    /**
     * Méthode qui retourne le bateau à l'indice fourni en paramètre
     *
     * @param indice indice du bateau
     * @return le bateau
     */
    public Ship getBateau(int indice) {
        return this.listShip.get(indice);
    }

    /**
     * Fonction qui prend en paramètre une case et qui va changer son état et sa visibilité. Elle va aussi
     * vérifier quel bateau contient cette case et ainsi changer l'état du bateau pour le rendre fixe
     *
     * @param c case touchée
     */
    public void boatTouched(Case c) {
        c.setState(2);
        c.setVisible(true);
        c.setContentTouched();
        for (Ship s : listShip) {
            if (s.holdThisCase(c)) {
                s.upShootsTaken();
                s.setState(false);
            }
        }
    }

    /**
     * Fonction qui vérifie si à partir du paramètre nomCase qu'elle reçoit, il existe ou non une case portant le même
     * nom. On l'utilise pour quand l'utilisateur va saisir un nom de case pour attaquer l'ia pour vérifier qu'il
     * ne rentre pas un nom de case qui n'existe pas
     *
     * @param caseName : nom de la case
     * @return true si la case existe, false sinon
     */
    public boolean caseExistante(String caseName) {
        for (int l = 0; l < SIZE_MAP; l++) {
            for (int c = 0; c < SIZE_MAP; c++) {
                if (listCase[l][c].getCoordinatesName().equals(caseName)) return true;
            }
        }
        return false;
    }

    /**
     * Méthode ToString qui retourne le contenu de la classe Map sous forme Json
     *
     * @return String format Json
     */
    @Override
    public String toString() {
        StringBuilder listCase = new StringBuilder();
        listCase.append("[");
        for (int i = 0; i < SIZE_MAP; i++) {
            listCase.append("[");
            for (int j = 0; j < SIZE_MAP; j++) {
                listCase.append("{");
                listCase.append(this.listCase[i][j].toString());
                if (j != SIZE_MAP - 1) {
                    listCase.append("},");
                } else {
                    listCase.append("}");
                }
            }
            if (i != SIZE_MAP - 1) {
                listCase.append("],");
            } else {
                listCase.append("]");
            }
        }
        listCase.append("]");
        return "\"Map\":{" +
                "\"listCase\":" + listCase +
                ", \"SIZE_MAP\":" + SIZE_MAP +
                ", \"listShip\":" + listShip +
                ", \"ALPHABET\":\"" + ALPHABET + '\"' +
                '}';
    }

    /**
     * Méthode qui retourne true si un bateau est encore en vie
     *
     * @return booléen true en vie, false sinon
     */
    public boolean isThereAnyBoatOnTheMapExisting() {
        if (!isThereAnySubMarine()) return false;
        for (Ship s : listShip) {
            if (s.isAlive())
                return true;
        }
        return false;
    }

    /**
     * Méthode qui retourne true s'il y a des sous-marins en vie
     *
     * @return booléen
     */
    private boolean isThereAnySubMarine() {
        ArrayList<Ship> shipSubMarine = new ArrayList<>();
        for (Ship s : listShip) {
            if (s instanceof SubMarine) {
                if (s.isAlive())
                    shipSubMarine.add(s);
            }
        }
        return shipSubMarine.size() > 0;
    }
}
