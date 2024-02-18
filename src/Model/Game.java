package Model;

import Model.Map.Case;
import Model.Player.Humain;
import Model.Player.IA;
import Model.Player.Player;
import Model.Ship.Direction;
import Model.Ship.Ship;
import Model.Ship.SubMarine;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Humain humain;
    private IA ia;
    private String name;

    /**
     * Constructeur de la classe Game
     */
    public Game() {
        this.name = "";
    }

    /**
     * Méthode permettant d'afficher le menu console du jeu et d'appeler les méthodes correspondantes avec la boucle principale du jeu
     */
    public void start() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("-------- Bonjour jeune aventurier ! Bienvenue dans le jeu de la Bataille Navale --------");
            System.out.println("Ajuste la fenêtre pour que la première ligne s'affiche sur une ligne entière");
            System.out.println("Menu (entre le numéro correspondant à l'action souhaité) : " +
                    "\n\t1. Jouer une nouvelle partie" +
                    "\n\t2. Jouer en mode triche" +
                    "\n\t3. Charger la dernière partie" +
                    "\n\t4. Aide (Règle du jeu)" +
                    "\n\t5. Quitter le jeu");
            int choice = -1;
            choice = toIntegerMoreThanOne(sc.nextLine());
            if (choice < 1 || choice > 5) {
                System.err.println("Erreur : Veuillez entrer un nombre entre 1 et 5");
            } else {
                switch (choice) {
                    case 1:
                        this.newGame(sc, false);
                        break;
                    case 2:
                        this.newGame(sc, true);
                        break;
                    case 3:
                        if (this.loadGame()) {
                            System.out.println("Chargement de la partie ...");
                            this.principal(sc, false);
                        } else {
                            System.err.println("Erreur : Aucune partie sauvegardée, merci de créer une partie pour jouer");
                        }
                        break;
                    case 4:
                        this.help(sc);
                        break;
                    case 5:
                        System.out.println("A bientôt " + (name.isEmpty() ? "jeune aventurier" : name) + " !");
                        System.exit(0);
                        break;
                }
            }
        } while (true);
    }

    /**
     * Méthode qui démarre une nouvelle partie
     *
     * @param sc Scanner pour récupérer les entrées de l'utilisateur
     */
    private void newGame(Scanner sc,boolean cheat) {
        this.initIA();
        this.initHumain(sc);
        this.principal(sc, cheat);
    }

    /**
     * Méthode qui initialise le joueur humain avec son nom et qui charge sa map
     *
     * @param sc Scanner pour récupérer les entrées de l'utilisateur
     */
    public void initHumain(Scanner sc) {
        System.out.println("Entrez votre nom : ");
        if (sc != null) {
            this.name = sc.nextLine();
        } else {
            this.name = "test";
        }
        this.humain = new Humain(name.replace(" ", ""));
    }

    /**
     * Méthode qui initialise l'IA
     */
    public void initIA() {
        this.ia = new IA();
    }


    /**
     * Méthode qui permet de charger une partie existante
     */
    private boolean loadGame() {
        ArrayList<Player> players = SaveGame.loadGameFromJsonFile();
        if (players.size() == 2) {
            this.humain = (Humain) players.get(0);
            this.ia = (IA) players.get(1);
            return true;
        }
        return false;
    }

    /**
     * Méthode qui permet de faire tourner le jeu
     *
     * @param sc Scanner pour récupérer les entrées de l'utilisateur
     * @param statCheated boolean qui permet de savoir si le joueur a activé le mode triche
     */
    public void principal(Scanner sc, boolean statCheated) {
        System.out.println("Voici la map de ton adversaire l'ia : ");
        this.ia.getMapPlayer().printMap(statCheated);

        System.out.println(humain.getName() + " voici ta map : ");
        this.humain.getMapPlayer().printMap(true);

        do {
            // Boucle de jeux : tour du joueur humain
            if (humain.isTurnPlayer()) {
                // 1. CHOIX BATEAU
                int nb = -1;
                this.humain.getMapPlayer().distinctBoatOnMap();
                this.humain.getMapPlayer().listShipOnMap();
                while ((nb <= 0 || nb >= 11) || !this.humain.getMapPlayer().getBateau(nb - 1).getState()) { // il y a 10 bateaux - conditions pour verif si utilisables
                    System.out.println("Capitaine voici la liste de nos bateaux, veuillez saisir le numéro d'un bateau non touché avec lequel" +
                            " vous souhaitez jouer durant ce tour.");
                    nb = toIntegerMoreThanOne(sc.nextLine());
                    if ((nb <= 0 || nb >= 11) || !this.humain.getMapPlayer().getBateau(nb - 1).getState()) {
                        System.err.println("Aie ce bateau a été touché on ne peut plus l'utiliser");
                    }
                }
                Ship selected = this.humain.getMapPlayer().getBateau(nb - 1); // -1 car le tableau commence à 0
                System.out.println("Très bien tu as choisis le " + selected.getShipName() + "-" + selected.getID() + " ! Que souhaites-tu" +
                        " faire ? ");
                // 2. CHOIX ACTION - s'il ne peut pas se déplacer, car bloqué on force attaque
                int choix = -1;
                while (choix != 1 && choix != 2) {

                    if (selected.getOrientation()) { // horizontale
                        // si une des 2 directions est possible
                        if (selected.checkPossibilityMove(Direction.WEST, this.humain.getMapPlayer().getListCase()) ||
                                selected.checkPossibilityMove(Direction.EAST, this.humain.getMapPlayer().getListCase())) {
                            System.out.println("1. Attaquer\n2. Déplacer ");
                            choix = toIntegerMoreThanOne(sc.nextLine());
                        } else {
                            System.out.println("Capitaine ce bateau est bloqué ! On va devoir attaquer ");
                            choix = 1;
                        }
                    } else {
                        if (selected.checkPossibilityMove(Direction.NORTH, this.humain.getMapPlayer().getListCase()) ||
                                selected.checkPossibilityMove(Direction.SOUTH, this.humain.getMapPlayer().getListCase())) {
                            System.out.println("1. Attaquer\n2. Déplacer ");
                            choix = toIntegerMoreThanOne(sc.nextLine());
                        } else {
                            System.out.println("Capitaine ce bateau est bloqué ! On va devoir attaquer ");
                            choix = 1;
                        }
                    }

                }
                // 3. ATTAQUE(choix==1) OU DÉPLACEMENT(else)
                if (choix == 1) {
                    String nomCase = "";
                    while (!ia.getMapPlayer().caseExistante(nomCase)) {
                        System.out.println("Saisie une case (valide) que tu veux attaquer avec ton bateau : ");
                        nomCase = sc.nextLine().toUpperCase();
                        if (this.ia.getMapPlayer().getCaseFromName(nomCase) != null) {
                            if (selected.alreadyShoot(ia.getMapPlayer().getCaseFromName(nomCase).getCoordinatesRow(),
                                    ia.getMapPlayer().getCaseFromName(nomCase).getCoordinatesColumn(), ia.getMapPlayer())) {
                                System.out.println("Capitaine on a déjà tiré ici on peut mieux faire !");
                                nomCase = null; // ici on met ce qu'on veut, c'est juste pour pas sortir de la boucle
                            }
                        }
                    }
                    Case caseCible = this.ia.getMapPlayer().getCaseFromName(nomCase);
                    selected.attack(caseCible.getCoordinatesRow(), caseCible.getCoordinatesColumn(), ia.getMapPlayer());
                    this.ia.getMapPlayer().printMap(statCheated);
                    this.humain.incrementNbShoot();
                    this.ia.getMapPlayer().reloadState();
                    this.ia.getMapPlayer().printMap(statCheated);
                    System.out.println("\nVoici l'état de la map adverse mtn !!! ");
                } else {
                    // Déplacement
                    Direction direction = null;
                    do {
                        int directionChoix = -1;
                        while (directionChoix != 1 && directionChoix != 2 && directionChoix != 3 && directionChoix != 4) {
                            System.out.println("Saisie une direction (N, S, E, O) dans laquelle tu veux déplacer ton bateau : ");
                            if (selected instanceof SubMarine) {
                                System.out.println(" 1. Nord" +
                                        "\n 2. Sud" +
                                        "\n 3. Est" +
                                        "\n 4. Ouest");
                            } else if (selected.getOrientation()) {
                                System.out.println(" 3. Est" +
                                        "\n 4. Ouest");
                            } else {
                                System.out.println(" 1. Nord" +
                                        "\n 2. Sud");
                            }
                            directionChoix = toIntegerMoreThanOne(sc.nextLine());
                            System.err.println(directionChoix);
                        }
                        switch (directionChoix) {
                            case 1:
                                direction = Direction.NORTH;
                                break;
                            case 2:
                                direction = Direction.SOUTH;
                                break;
                            case 3:
                                direction = Direction.EAST;
                                break;
                            case 4:
                                direction = Direction.WEST;
                                break;
                        }
                    } while (this.humain.moveShip(direction, this.humain.getMapPlayer(), selected.getID()));
                    this.humain.getMapPlayer().reloadState();
                    this.ia.hasPlayed();
                    this.humain.getMapPlayer().printMap(true);
                }
                System.out.println("\n-------------------------------------------------------------------");
                // 4. CHANGEMENT ÉTAT TOUR DES JOUEURS
                this.humain.setTurnPlayer(false);
                this.ia.setTurnPlayer(true);
            } else { // tour de l'IA
                System.out.println("Cest moi l'ia ------------");
                // 1. CHOIX DU BATEAU
                Ship selected = this.ia.choseRandomShip();
                // 2. CHOIX ACTION - 80% chance attaque - 20% chance déplacement - verification si déplacement possible
                double choixActionIA = Math.random();
                if (selected.getOrientation()) {
                    if (!selected.checkPossibilityMove(Direction.WEST, this.ia.getMapPlayer().getListCase()) && // aucun moyen de se déplacer
                            !selected.checkPossibilityMove(Direction.EAST, this.ia.getMapPlayer().getListCase())) {
                        // alors on set choixActionIa > 0.8
                        choixActionIA = 1; // valeur prise aléatoirement
                    }
                } else {
                    if (!selected.checkPossibilityMove(Direction.NORTH, this.ia.getMapPlayer().getListCase()) && // aucun moyen de se déplacer
                            !selected.checkPossibilityMove(Direction.SOUTH, this.ia.getMapPlayer().getListCase())) {
                        // alors on set choixActionIa > 0.8
                        choixActionIA = 1; // valeur prise aléatoirement
                    }
                }
                if (choixActionIA < 0.8) {
                    Case cible;
                    do {
                        cible = this.humain.getMapPlayer().getRandomCase();
                    } while (selected.alreadyShoot(humain.getMapPlayer().getCaseFromName(cible.getCoordinatesName()).getCoordinatesRow(),
                            humain.getMapPlayer().getCaseFromName(cible.getCoordinatesName()).getCoordinatesColumn(), humain.getMapPlayer()));

                    System.out.println("\nIA : Bien à mon tour, je vais attaquer avec " + selected.getShipName() + "-" + selected.getID()
                            + " en " + cible.getCoordinatesName());
                    this.humain.incrementNbShoot();
                    selected.attack(cible.getCoordinatesRow(), cible.getCoordinatesColumn(), humain.getMapPlayer());
                    this.humain.getMapPlayer().printMap(true);
                    this.humain.getMapPlayer().reloadState();
                    System.out.println("\nIA : Voici l'état de ta map mtn !!! ");
                } else {
                    // Déplacement
                    this.ia.moveShip(this.ia.getMapPlayer());
                    this.humain.getMapPlayer().printMap(true);
                    this.humain.hasPlayed();
                }
                System.out.println("\n-------------------------------------------------------------------");
                this.humain.setTurnPlayer(true);
                this.ia.setTurnPlayer(false);
            }
            if (!this.ia.getMapPlayer().isThereAnyBoatOnTheMapExisting())
                humain.won();
            if (!this.humain.getMapPlayer().isThereAnyBoatOnTheMapExisting())
                ia.won();
        } while (!(humain.isWinner() || ia.isWinner()));
        if (humain.isWinner()) {
            System.out.println("Bravo tu as gagné !!! ");
        } else {
            System.out.println("Dommage tu as perdu !!! \nRetente une partie pour te rattraper !!! ");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * Méthode qui permet de retourner un entier supérieur à zéro à partir d'une chaine de caractère
     *
     * @param str String à convertir en int
     * @return int
     */
    private int toIntegerMoreThanOne(String str) {
        if (str.equals("q") || str.equals("Q") || str.equals("quit") || str.equals("Quit")) {
            System.out.println("Vous avez quitté la partie, votre partie a été sauvegardée");
            this.saveGame();
        }
        try {
            int var = Integer.parseInt(str);
            if (var < 1) {
                System.err.println("Erreur : Veuillez entrer un nombre supérieur à 0");
                return -1;
            }
            return var;
        } catch (NumberFormatException e) {
            System.err.println("Erreur : Veuillez entrer un nombre");
            return -1;
        }
    }

    /**
     * Méthode qui permet de sauvegarder la partie en cours
     */
    private void saveGame() {
        if (SaveGame.saveGametInJsonFile(humain.toString(), ia.toString())) {
            System.out.println("La sauvegarde a bien été effectué");
        } else {
            System.err.println("Erreur : La sauvegarde n'a pas pu être effectué");
        }
        System.exit(0);
    }

    /**
     * Méthode permettant de rentrer dans l'aide du jeu Bataille Navale
     *
     * @param sc Scanner crée dans la méthode start()
     */
    private void help(Scanner sc) {
        System.out.println("            -------- Bienvenue, jeune aventurier, dans le jeu bataille navale --------" +
                "\n" +
                "\n Tu trouveras ici toutes les informations pour maitrisez le jeu et vaincre notre IA." +
                "\n" +
                "\n Ce jeu consiste à faire couler tous les bateaux de l'adversaire. Ici les navires sont placés aléatoirement sur votre grille." +
                "\n" +
                "\n A chaque tour les joueurs doivent se déplacer ou tirer sur l'une des cases de l'adversaire pour lui toucher ou " +
                "\n couler un navire, pour tirer il faudra indiquer l'emplacement de la case composées d'une lettre puis d'un nombre." +
                "\n" +
                "\n Si vous toucher ou couler un bateau, il est retiré de la grille de l'adversaire" +
                "\n" +
                "\n Si l'un de votre bateau est touché, il n'est plus possible pour lui de tirer ni de se déplacer." +
                "\n" +
                "\n Le but final étant d'éliminer tous les bateaux de l'adversaire." +
                "\n" +
                "\n Vous avez 4 types de navires à disposition : le cuirassé, le destroyer, le sous-marin et le croiseur." +
                "\n" +
                "\n Vous aurez sur votre grille 4 sous-marins représentés avec ce caractère '#' qui peuvent être coulés seulement par un" +
                "\n sous-marin de l'adversaire, 3 destroyers représentés avec ce caractère '^', 2 croiseurs représentés avec ce caractère" +
                "\n 'u' et 1 cuirassé représentés avec ce caractère 'o' qui peuvent être touchés et coulés par tout le monde." +
                "\n Si une partie du bateau est touché, le caractère '+' s'affiche sur la map pour remplacer le caractère représentant l'un de vos navires" +
                "\n                                     -----Bonne partie!!-----");

        System.err.println("Pour retourner au menu principal, appuyez sur la touche 'q' puis la touche entrer");
        while (true) {
            String rep = sc.nextLine();
            if (rep.equals("q")) {
                break;
            }
        }
    }

    public Humain getHumain() {
        return humain;
    }

    public IA getIa() {
        return ia;
    }
}
