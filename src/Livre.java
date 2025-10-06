import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Livre {
    private int id;
    private String titre;
    private String genre;
    private String auteur;
    private boolean disponible;

    public Livre(int id, String titre,String genre, String auteur, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.genre = genre;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }
    public String getTitre() {
        return titre;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuteur() {
        return auteur;
    }
    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }


    public static List<Livre> lireLivresDepuisCSV(String cheminFichier) {
        List<Livre> livres = new ArrayList<>();
        String ligne;
        String separateur = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            // Ignorer la ligne d'en-tête
            br.readLine();
            while ((ligne = br.readLine()) != null) {
                String[] valeurs = ligne.split(separateur);
                if (valeurs.length == 5) {
                    int id = Integer.parseInt(valeurs[0]);
                    String titre = valeurs[1];
                    String genre = valeurs[2];
                    String auteur = valeurs[3];
                    boolean disponible = Boolean.parseBoolean(valeurs[4]);
                    livres.add(new Livre(id, titre, genre, auteur, disponible));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return livres;
    }


    @Override
    public String toString() {
        return "Livre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", genre='" + genre + '\'' +
                ", disponible=" + disponible +
                '}';
    }



}
