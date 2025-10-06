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
            br.readLine(); // Ignorer l'en-tête

            while ((ligne = br.readLine()) != null) {
                try {
                    String[] valeurs = ligne.split(separateur);
                    if (valeurs.length == 5) {
                        int id = Integer.parseInt(valeurs[0].trim());
                        String titre = valeurs[1].trim();
                        String genre = valeurs[2].trim();
                        String auteur = valeurs[3].trim();
                        boolean disponible = Boolean.parseBoolean(valeurs[4].trim());
                        livres.add(new Livre(id, titre, genre, auteur, disponible));
                    } else {
                        System.err.println("Ligne ignorée (format invalide) : " + ligne);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Erreur de parsing pour la ligne : " + ligne);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
            e.printStackTrace();
        }
        return livres;
    }


    public static void afficherLivres(List<Livre> livres) {
        if (livres.isEmpty()) {
            System.out.println("Aucun livre trouvé.");
            return;
        }

        System.out.println("\n=== Liste des livres ===");
        System.out.println("-".repeat(100));
        System.out.printf("%-5s | %-30s | %-20s | %-20s | %-10s%n",
                "ID", "Titre", "Genre", "Auteur", "Disponible");
        System.out.println("-".repeat(100));

        for (Livre livre : livres) {
            System.out.printf("%-5d | %-30s | %-20s | %-20s | %-10s%n",
                    livre.getId(),
                    livre.getTitre(),
                    livre.getGenre(),
                    livre.getAuteur(),
                    livre.isDisponible() ? "Oui" : "Non");
        }
        System.out.println("-".repeat(100));
        System.out.println("Total : " + livres.size() + " livre(s)\n");
    }

    public void afficherDetails() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       DÉTAILS DU LIVRE               ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("ID          : " + id);
        System.out.println("Titre       : " + titre);
        System.out.println("Genre       : " + genre);
        System.out.println("Auteur      : " + auteur);
        System.out.println("Disponible  : " + (disponible ? "✓ Oui" : "✗ Non"));
        System.out.println("─".repeat(40));
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
