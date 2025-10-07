package core;

import model.Media;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Interface générique pour le Repository
 * Étudiant A - Repository avec wildcards
 * @param <T> Type de média géré par le repository
 */
public interface Repository<T extends Media> {

    /**
     * Ajoute un média au repository
     */
    void add(T media);

    /**
     * Supprime un média par son ID
     */
    boolean remove(int id);

    /**
     * Recherche un média par son ID
     */
    Optional<T> findById(int id);

    /**
     * Retourne tous les médias
     */
    List<T> findAll();

    /**
     * Recherche des médias selon un prédicat (utilise ? extends T)
     */
    List<T> findBy(Predicate<? super T> predicate);

    /**
     * Compte le nombre de médias
     */
    int count();

    /**
     * Vérifie si le repository contient un média avec l'ID donné
     */
    boolean exists(int id);

    /**
     * Vide le repository
     */
    void clear();
}

