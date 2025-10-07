package ui;

import model.Livre;
import model.Media;
import repo.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RechercheService {

    // ---------- Recherches simples ----------

    public <T extends Media> List<T> byTitre(List<Livre> repo, String mot) {
        String q = normalize(mot);
        return repo.findAll().stream()
                .filter(m -> normalize(m.titre()).contains(q))
                .collect(Collectors.toList());
    }

    public <T extends Media> List<T> byGenre(Repository<? extends T> repo, String genre) {
        String q = normalize(genre);
        return repo.findAll().stream()
                .filter(m -> normalize(m.genre()).contains(q))
                .collect(Collectors.toList());
    }

    // ---------- Recherches flexibles (avec Predicate / keyExtractor) ----------

    public <T extends Media> List<T> by(Repository<? extends T> repo, Predicate<? super T> predicate) {
        return repo.findAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public <T extends Media> List<T> byKeyContains(Repository<? extends T> repo,
                                                   Function<? super T, String> keyExtractor,
                                                   String query) {
        String q = normalize(query);
        return repo.findAll().stream()
                .filter(m -> normalize(keyExtractor.apply(m)).contains(q))
                .collect(Collectors.toList());
    }

    // ---------- Tri / Top-K ----------

    public <T extends Media> List<T> topK(Repository<? extends T> repo,
                                          Comparator<? super T> comparator,
                                          int k) {
        return repo.findAll().stream()
                .sorted(comparator)     // penser à .reversed() lors de l'appel si besoin
                .limit(Math.max(0, k))
                .collect(Collectors.toList());
    }

    public <T extends Media> List<T> searchAndSort(Repository<? extends T> repo,
                                                   Predicate<? super T> predicate,
                                                   Comparator<? super T> comparator,
                                                   int limit) {
        return repo.findAll().stream()
                .filter(predicate)
                .sorted(comparator)
                .limit(Math.max(0, limit))
                .collect(Collectors.toList());
    }

    // ---------- Utils ----------

    private static String normalize(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT).trim();
    }

    public List<Livre> topK(List<Livre> livres, Comparator<Livre> comparing, int k) {
        return livres;
    }

    public List<Livre> byAuteur(List<Livre> livres, String rowling) {
        return livres;
    }
}
