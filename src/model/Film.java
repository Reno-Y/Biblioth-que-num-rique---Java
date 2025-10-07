package model;

/**
 * Record immuable représentant un film dans le catalogue
 * Étudiant A - Implémentation Media
 */
public record Film(int id, String titre, String realisateur, String genre) implements Media {
}

