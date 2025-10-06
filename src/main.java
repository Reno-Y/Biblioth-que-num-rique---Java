
import java.util.List;


public class main {
    public static void main(String[] args) {
        // Charger les livres
        List<Livre> livres = Livre.lireLivresDepuisCSV("livres/livres.csv");

        // Afficher tous les livres
        Livre.afficherLivres(livres);

        // Afficher les détails d'un livre spécifique
        if (!livres.isEmpty()) {
            livres.get(1).afficherDetails();
        }
    }

}
