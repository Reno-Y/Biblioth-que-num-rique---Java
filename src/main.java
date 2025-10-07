import model.Livre;

import java.util.List;




public class main {
    public static void main(String[] args) {
        System.out.println("=== TEST DE LA BIBLIOTHÈQUE ===\n");

        // Charger les livres depuis le CSV
        List<Livre> livres = Livre.lireLivresDepuisCSV("livres/livres.csv");
        System.out.println(livres.size() + " livre(s) chargé(s) avec succès.\n");

        // Afficher tous les livres
        Livre.afficherLivres(livres);

        // Test 1 : Recherche par titre
        System.out.println("\n=== RECHERCHE PAR TITRE (mot-clé: 'Prince') ===");
        List<Livre> resultatsParTitre = Livre.rechercherParTitre(livres, "Prince");
        Livre.afficherLivres(resultatsParTitre);

        // Test 2 : Recherche par auteur
        System.out.println("\n=== RECHERCHE PAR AUTEUR (mot-clé: 'Orwell') ===");
        List<Livre> resultatsParAuteur = Livre.rechercherParAuteur(livres, "Orwell");
        Livre.afficherLivres(resultatsParAuteur);

        // Test 3 : Recherche par genre
        System.out.println("\n=== RECHERCHE PAR GENRE (genre: 'Science-Fiction') ===");
        List<Livre> resultatsParGenre = Livre.rechercherParGenre(livres, "Science-Fiction");
        Livre.afficherLivres(resultatsParGenre);

        // Test 4 : Recherche sans résultat
        System.out.println("\n=== RECHERCHE SANS RÉSULTAT (auteur: 'Tolkien') ===");
        List<Livre> resultatsVide = Livre.rechercherParAuteur(livres, "Tolkien");
        Livre.afficherLivres(resultatsVide);

        // Afficher les détails d'un livre spécifique
        if (!livres.isEmpty()) {
            System.out.println("\n=== DÉTAILS D'UN LIVRE ===");
            livres.get(0).afficherDetails();
        }
    }
}
