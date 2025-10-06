public class Emprunt {

    private String dateEmprunt;
    private String dateRetour;
    private Livre livre;
    private Utilisateur utilisateur;

    public Emprunt( String dateEmprunt, String dateRetour, Livre livre, Utilisateur utilisateur) {
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.livre = livre;
        this.utilisateur = utilisateur;
    }

    public String getDateEmprunt() {
        return dateEmprunt;
    }
    public String getDateRetour() {
        return dateRetour;
    }
    public Livre getLivre() {
        return livre;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }


}
