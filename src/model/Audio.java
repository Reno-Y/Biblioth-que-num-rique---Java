package model;

/**
 * Record immuable représentant un média audio dans le catalogue
 * Étudiant A - Implémentation Media
 */
public record Audio(int id, String titre, String artiste, String genre) implements Media {
}

