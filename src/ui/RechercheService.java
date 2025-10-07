package ui;

import core.*;

import javax.print.attribute.standard.Media;
import java.util.*;
import java.util.stream.Collectors;

public class RechercheService {

    public <T extends Media> List<T> byTitre(Repository<? extends T> repo, String mot) {
        String q = mot.toLowerCase(); // pour ignorer les majuscules/minuscules

        return repo.findAll().stream() // on récupère tous les médias
                .filter(m -> m.titre().toLowerCase().contains(q)) // on garde ceux dont le titre contient le mot
                .collect(Collectors.toList()); // on les met dans une nouvelle liste
    }

    /**
     * Retourne les K premiers médias selon un critère de tri (ex: popularité)
     * @param repo dépôt des médias (Repository fourni par A)
     * @param comparator critère de tri (ex: par nombre d’emprunts)
     * @param k nombre maximum d’éléments à renvoyer
     * @param <T> type de média (Livre, Film, Audio…)
     * @return liste des K premiers médias selon le critère
     */
    public <T extends Media> List<T> topK(Repository<? extends T> repo,
                                          Comparator<? super T> comparator,
                                          int k) {
        return repo.findAll().stream() // récupère tous les médias
                .sorted(comparator) // trie selon le comparateur
                .limit(k) // garde les k premiers
                .collect(Collectors.toList()); // retourne une nouvelle liste
    }

    /**
     * (Optionnel) Recherche par auteur / réalisateur / artiste
     * Utile pour étendre la recherche sans recopier du code.
     */
    public <T extends Media> List<T> byAuteur(Repository<? extends T> repo, String nom) {
        String q = nom.toLowerCase();
        return repo.findAll().stream()
                .filter(m -> m.personnePrincipale().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}
