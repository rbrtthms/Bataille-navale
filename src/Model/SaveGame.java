package Model;


import Model.Player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SaveGame {
    private static final String FILE_NAME = "Save-Bataille-Navale.json";
    private final String pathFile; // Chemin du dossier ou enregistré le fichier dans l'ordinateur du joueur

    /**
     * Constructeur de la classe SaveGame
     */
    public SaveGame() {
        this.pathFile = new File(".").getAbsolutePath();
    }

    /**
     * Méthode qui permet de sauvegarder une partie dans un fichier
     * @param humain joueur humain
     * @param ia joueur ia
     * @return true si la sauvegarde s'est bien passée, false sinon
     */
    public static boolean saveGametInJsonFile(String humain, String ia) {
        String date = new Date().toString();
        try {
            FileWriter file = new FileWriter(FILE_NAME);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write(("{\"Date\":{\"date\":\"" + date + "\"},\n" +
                    humain + ",\n" +
                    ia + "}"));
            writer.close();
            System.out.println("La partie a bien été sauvegardée");
            return true;
        } catch (IOException e) {
            System.err.println("L'enregistrement à échoué" + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        } finally {
            System.out.println("A bientôt !");
        }
    }

    /**
     * Méthode qui permet de charger une partie à partir d'un fichier
     *
     * @return toutes les informations de la partie sauvegardée sous forme d'une liste d'objet
     */
    public static ArrayList<Player> loadGameFromJsonFile() {
        BufferedReader reader;
        String date;
        String humain;
        String ia;
        try {
            FileReader file = new FileReader(FILE_NAME);
            reader = new BufferedReader(file);
            date = reader.readLine();
            humain = reader.readLine();
            ia = reader.readLine();
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier n'a pas été trouvé");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier, veuillez réessayer");
            throw new RuntimeException(e);
        }
        System.out.println(date);
        ArrayList<Player> listPlayers = new ArrayList<>();
        // supprimer la virgule à la fin de la ligne pour humain -> humain.substring(0, humain.length() - 1)
        listPlayers.add(Player.makePlayerFromJson(("{" + humain.substring(0, humain.length() - 1) + "}"), true));
        listPlayers.add(Player.makePlayerFromJson(("{" + ia + "}"), false));

        return listPlayers;
    }
}