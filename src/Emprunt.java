public class Emprunt {
    private int idEmprunt;
    private String dateEmprunt;
    private String dateRetour;
    private Livre livre;
    private Utilisateur utilisateur;

    public Emprunt(int idEmprunt, String dateEmprunt, String dateRetour, Livre livre, Utilisateur utilisateur) {
        this.idEmprunt = idEmprunt;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.livre = livre;
        this.utilisateur = utilisateur;
    }

    public int getIdEmprunt() {
        return idEmprunt;
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
