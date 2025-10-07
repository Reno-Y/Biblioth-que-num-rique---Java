package ui;

import java.util.*;
import core.Bibliotheque;
import core.LivreIndisponibleException;
import model.Livre;
import model.Utilisateur;


public class App {


    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        Bibliotheque bib = new Bibliotheque();
        RechercheService search = new RechercheService();

        try {
            List<Livre> livres = Livre.lireLivresDepuisCSV("livres/livres.csv"); // <-- mets ton vrai chemin
            bib.Livres.addAll(livres);
        } catch (Exception e) {
            System.err.println("Impossible de charger les livres : " + e.getMessage());
        }
        if (bib.Utilisateurs.isEmpty()) {
            bib.Utilisateurs.add(new Utilisateur(1, "Sghaier", "Souhaib", "souhaib@example.com"));
            bib.Utilisateurs.add(new Utilisateur(2, "Gabriel", "V.", "gabriel@example.com"));
            bib.Utilisateurs.add(new Utilisateur(3, "Nathan", "C.", "nathan@example.com"));
        }
        int choix;
        do {
            afficherMenu();
            choix = lireEntier("Votre choix : ");
            switch (choix) {
                case 1 -> listerLivresDisponibles(bib);
                case 2 -> rechercherLivre(bib);
                case 3 -> emprunterLivre(bib);
                case 4 -> rendreLivre(bib);
                case 5 -> listerTousLesLivres(bib);
                case 0 -> System.out.println("Au revoir 👋");
                default -> System.out.println("Choix invalide.");
            }
            System.out.println();
        } while (choix != 0);

        List<Livre> trouves = search.byTitre(bib.Livres, "harry");
        trouves.forEach(l -> System.out.println(l.getTitre()));

        List<Livre> auteurs = search.byAuteur(bib.Livres, "rowling");
        auteurs.forEach(l -> System.out.println(l.getTitre()));

        List<Livre> top3 = search.topK(bib.Livres, Comparator.comparing(Livre::getTitre), 3);
        top3.forEach(l -> System.out.println(l.getTitre()));

        SC.close();
    }

    // ----------------- Menu & Actions -----------------

    private static void afficherMenu() {
        System.out.println("""
                --- MENU BIBLIOTHÈQUE ---
                1. Lister les livres disponibles
                2. Rechercher un livre (titre/auteur/genre)
                3. Emprunter un livre
                4. Rendre un livre
                5. Lister tous les livres (tableau)
                0. Quitter
                """);
    }

    private static void listerLivresDisponibles(Bibliotheque bib) {
        bib.afficheLivresDisponibles();
    }

    private static void listerTousLesLivres(Bibliotheque bib) {
        Livre.afficherLivres(bib.Livres);
    }

    private static void rechercherLivre(Bibliotheque bib) {
        System.out.println("""
                Rechercher par :
                1) Titre
                2) Auteur
                3) Genre
                """);
        int type = lireEntier("Type de recherche : ");
        String q = lireTexte("Mot-clé : ").toLowerCase();

        List<Livre> res = new ArrayList<>();
        for (Livre l : bib.Livres) {
            if (type == 1 && l.getTitre().toLowerCase().contains(q)) res.add(l);
            else if (type == 2 && l.getAuteur().toLowerCase().contains(q)) res.add(l);
            else if (type == 3 && l.getGenre().toLowerCase().contains(q)) res.add(l);
        }

        if (res.isEmpty()) {
            System.out.println("Aucun résultat.");
        } else {
            Livre.afficherLivres(res);
        }
    }


    private static void emprunterLivre(Bibliotheque bib) {
        if (bib.Utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur n'existe. Ajoutez-en au moins un.");
            return;
        }
        int id = lireEntier("ID (numérique) du livre à emprunter : ");
        Utilisateur u = choisirUtilisateur(bib);

        try {
            bib.emprunterLivre(id, u);
            System.out.println("✅  Le livre est emprunté !");
        } catch (LivreIndisponibleException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("❌ Aucun livre à l'index " + id + " (vérifie l'index de la liste).");
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
        }
    }



    private static void rendreLivre(Bibliotheque bib) {
        if (bib.Utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur n'existe. Ajoutez-en au moins un.");
            return;
        }
        int id = lireEntier("ID (numérique) du livre à rendre : ");
        Utilisateur u = choisirUtilisateur(bib);

        try {
            bib.rendreLivre(id, u);
            System.out.println("✅ Retour effectué.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("❌ Aucun livre à l'index " + id + " (vérifie l'index de la liste).");
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
        }
    }

    // ----------------- Utilitaires I/O -----------------

    private static int lireEntier(String label) {
        System.out.print(label);
        while (!SC.hasNextInt()) {
            SC.nextLine();
            System.out.print("Veuillez entrer un entier : ");
        }
        int v = SC.nextInt();
        SC.nextLine();
        return v;
    }

    private static String lireTexte(String label) {
        System.out.print(label);
        return SC.nextLine().trim();
    }

    private static Utilisateur choisirUtilisateur(Bibliotheque bib) {
        for (int i = 0; i < bib.Utilisateurs.size(); i++) {
            Utilisateur u = bib.Utilisateurs.get(i);
            System.out.printf("%d) %s %s%n", i + 1, u.getPrenom(), u.getNom());
        }
        int idx = lireEntier("Choisir un utilisateur : ") - 1;
        if (idx < 0 || idx >= bib.Utilisateurs.size()) {
            System.out.println("Choix invalide → on prend le premier.");
            return bib.Utilisateurs.get(0);
        }
        return bib.Utilisateurs.get(idx);

    }

}