package model;

/**
 * Record immuable représentant un livre dans le catalogue
 * Étudiant A - Implémentation Media
 */
public record LivreMedia(int id, String titre, String auteur, String genre) implements Media {
}

