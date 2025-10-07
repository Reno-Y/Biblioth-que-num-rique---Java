package model;

import java.time.LocalDate;
public class Emprunt {


    private LocalDate dateEmprunt;
    private LocalDate dateRetour;
    private Livre livre;
    private Utilisateur utilisateur;

    public Emprunt(LocalDate dateEmprunt, LocalDate dateRetour, Livre livre, Utilisateur utilisateur) {
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.livre = livre;
        this.utilisateur = utilisateur;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }
    public LocalDate getDateRetour() {
        return dateRetour;
    }
    public Livre getLivre() {
        return livre;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }


}
