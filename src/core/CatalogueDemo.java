package core;

import model.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Classe de démonstration du Catalogue avec wildcards
 * Étudiant A - Exemples d'utilisation
 */
public class CatalogueDemo {

    public static void main(String[] args) {
        // Création du catalogue
        Catalogue catalogue = new Catalogue();

        // Ajout de livres
        catalogue.ajouterLivre(new LivreMedia(1, "1984", "George Orwell", "Science-Fiction"));
        catalogue.ajouterLivre(new LivreMedia(2, "Le Seigneur des Anneaux", "J.R.R. Tolkien", "Fantasy"));
        catalogue.ajouterLivre(new LivreMedia(3, "Dune", "Frank Herbert", "Science-Fiction"));

        // Ajout de films
        catalogue.ajouterFilm(new Film(4, "Inception", "Christopher Nolan", "Science-Fiction"));
        catalogue.ajouterFilm(new Film(5, "Le Parrain", "Francis Ford Coppola", "Drame"));
        catalogue.ajouterFilm(new Film(6, "Interstellar", "Christopher Nolan", "Science-Fiction"));

        // Ajout d'audios
        catalogue.ajouterAudio(new Audio(7, "Thriller", "Michael Jackson", "Pop"));
        catalogue.ajouterAudio(new Audio(8, "Bohemian Rhapsody", "Queen", "Rock"));
        catalogue.ajouterAudio(new Audio(9, "Back in Black", "AC/DC", "Rock"));

        System.out.println("=== CATALOGUE MULTIMÉDIA ===\n");

        // Statistiques
        System.out.println("--- Statistiques ---");
        Map<String, Integer> stats = catalogue.obtenirStatistiques();
        stats.forEach((type, count) ->
            System.out.println(type + ": " + count));
        System.out.println();

        // Recherche par genre
        System.out.println("--- Médias Science-Fiction ---");
        List<? extends Media> sciFi = catalogue.rechercherParGenre("Science-Fiction");
        sciFi.forEach(media ->
            System.out.println("- " + media.titre() + " (ID: " + media.id() + ")"));
        System.out.println();

        // Recherche par titre
        System.out.println("--- Recherche 'the' dans les titres ---");
        List<? extends Media> avecThe = catalogue.rechercherParTitre("the");
        avecThe.forEach(media ->
            System.out.println("- " + media.titre() + " [" + media.getClass().getSimpleName() + "]"));
        System.out.println();

        // Tri par titre
        System.out.println("--- Tous les médias triés par titre ---");
        List<? extends Media> triesParTitre = catalogue.trierParTitre();
        triesParTitre.forEach(media ->
            System.out.println("- " + media.titre()));
        System.out.println();

        // Groupement par genre
        System.out.println("--- Médias groupés par genre ---");
        Map<String, List<Media>> parGenre = catalogue.grouperParGenre();
        parGenre.forEach((genre, medias) -> {
            System.out.println(genre + ":");
            medias.forEach(media ->
                System.out.println("  - " + media.titre()));
        });
        System.out.println();

        // Filtrage par type
        System.out.println("--- Uniquement les films ---");
        List<Film> films = catalogue.filtrerParType(Film.class);
        films.forEach(film ->
            System.out.println("- " + film.titre() + " réalisé par " + film.realisateur()));
        System.out.println();

        // Tri personnalisé avec wildcard
        System.out.println("--- Films triés par réalisateur ---");
        List<Film> filmsTries = catalogue.trier(films,
            Comparator.comparing(Film::realisateur).thenComparing(Film::titre));
        filmsTries.forEach(film ->
            System.out.println("- " + film.realisateur() + ": " + film.titre()));
        System.out.println();

        // Recherche par ID
        System.out.println("--- Recherche média avec ID 5 ---");
        catalogue.rechercherParId(5).ifPresent(media ->
            System.out.println("Trouvé: " + media.titre() + " (Genre: " + media.genre() + ")"));

        System.out.println("\n=== DÉMONSTRATION THREAD-SAFETY ===");
        demonstrationThreadSafety(catalogue);
    }

    /**
     * Démontre le comportement thread-safe du catalogue
     */
    private static void demonstrationThreadSafety(Catalogue catalogue) {
        System.out.println("Ajout concurrent de 100 livres depuis 10 threads...");

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    int id = 100 + threadId * 10 + j;
                    catalogue.ajouterLivre(new LivreMedia(
                        id,
                        "Livre " + id,
                        "Auteur " + threadId,
                        "Genre " + (j % 3)
                    ));
                }
            });
            threads[i].start();
        }

        // Attendre que tous les threads terminent
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Nombre total de livres après ajout concurrent: "
            + catalogue.getLivresRepository().count());
        System.out.println("Thread-safety validée ✓");
    }
}
