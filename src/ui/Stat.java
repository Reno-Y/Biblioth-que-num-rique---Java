package ui;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Stat {

    // ---- Structure des résultats (juste pour être propre) ----
    public static class Resultats {
        public final long nbDisponibles;
        public final long nbTotal;
        public final Map<String, Double> tauxEmpruntParGenre;     // genre -> % emprunté
        public final List<Map.Entry<String, Long>> topAuteurs;    // auteur -> nb emprunts
        public final long nbEmprunts;                              // total d'emprunts enregistrés

        public Resultats(long nbDisponibles,
                         long nbTotal,
                         Map<String, Double> tauxEmpruntParGenre,
                         List<Map.Entry<String, Long>> topAuteurs,
                         long nbEmprunts) {
            this.nbDisponibles = nbDisponibles;
            this.nbTotal = nbTotal;
            this.tauxEmpruntParGenre = tauxEmpruntParGenre;
            this.topAuteurs = topAuteurs;
            this.nbEmprunts = nbEmprunts;
        }


        public String toConsoleString() {
            StringBuilder sb = new StringBuilder();
            sb.append("--- STATISTIQUES ---\n");
            sb.append(String.format("Livres disponibles : %d/%d%n", nbDisponibles, nbTotal));
            sb.append(String.format("Emprunts enregistrés : %d%n", nbEmprunts));

            sb.append("\nTaux d’emprunt par genre :\n");
            if (tauxEmpruntParGenre.isEmpty()) {
                sb.append("  (aucun genre)\n");
            } else {
                tauxEmpruntParGenre.forEach((g, p) ->
                        sb.append(String.format(" - %s : %.0f%%%n", g, p)));
            }

            sb.append("\nTop auteurs les plus empruntés :\n");
            if (topAuteurs.isEmpty()) {
                sb.append("  (aucun emprunt saisi)\n");
            } else {
                int r = 1;
                for (var e : topAuteurs) {
                    sb.append(String.format(" %d) %s (%d)%n", r++, e.getKey(), e.getValue()));
                }
            }
            return sb.toString();
        }

        public String toFileString() {
            // même contenu pour le fichier
            return toConsoleString();
        }
    }

    // ---- Calcul principal (utilise tes structures actuelles) ----
    public static Resultats calculer(Bibliotheque bib) {
        List<Livre> livres = bib.Livres;
        List<Emprunt> emprunts = bib.Emprunts;

        long nbTotal = livres.size();
        long nbDispo = livres.stream().filter(Livre::isDisponible).count();

        // --- Taux d’emprunt par genre ---
        // On calcule à partir du statut des livres (disponible / non disponible).
        Map<String, Long> totalParGenre = livres.stream()
                .collect(Collectors.groupingBy(Livre::getGenre, Collectors.counting()));

        Map<String, Long> empruntesParGenre = livres.stream()
                .filter(l -> !l.isDisponible())
                .collect(Collectors.groupingBy(Livre::getGenre, Collectors.counting()));

        Map<String, Double> tauxParGenre = new TreeMap<>();
        for (var e : totalParGenre.entrySet()) {
            String genre = e.getKey();
            long total = e.getValue();
            long empruntes = empruntesParGenre.getOrDefault(genre, 0L);
            double pct = total == 0 ? 0.0 : (empruntes * 100.0 / total);
            tauxParGenre.put(genre, pct);
        }

        // --- Top auteurs les plus empruntés ---
        // Si la liste Emprunts est correctement remplie (Emprunt contient Livre),
        // on compte par auteur. Sinon on retombe sur les livres non disponibles.
        List<Map.Entry<String, Long>> topAuteurs;

        if (emprunts != null && !emprunts.isEmpty() && emprunts.get(0).getLivre() != null) {
            topAuteurs = emprunts.stream()
                    .map(Emprunt::getLivre)
                    .collect(Collectors.groupingBy(Livre::getAuteur, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(3)
                    .collect(Collectors.toList());
        } else {
            // Fallback: on estime à partir des livres non disponibles
            topAuteurs = livres.stream()
                    .filter(l -> !l.isDisponible())
                    .collect(Collectors.groupingBy(Livre::getAuteur, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(3)
                    .collect(Collectors.toList());
        }

        long nbEmprunts = emprunts == null ? 0 : emprunts.size();

        return new Resultats(nbDispo, nbTotal, tauxParGenre, topAuteurs, nbEmprunts);
    }

    // ---- Affichage console (utilitaire pratique) ----
    public static void afficher(Resultats r) {
        System.out.println(r.toConsoleString());
    }

    // ---- Export fichier texte ----
    public static void exporter(Resultats r, String outputPath) {
        try {
            Files.writeString(Path.of(outputPath), r.toFileString(), StandardCharsets.UTF_8);
            System.out.println("📄 Fichier '" + outputPath + "' généré.");
        } catch (Exception e) {
            System.err.println("Erreur écriture du fichier stats : " + e.getMessage());
        }
    }
}
