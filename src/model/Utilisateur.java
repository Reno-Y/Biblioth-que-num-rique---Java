package model;

public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String email;




    public Utilisateur(int id, String nom, String prenom, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getEmail() {
        return email;
    }

    public String getPrenom() {
        return prenom;
    }
}
