import java.time.LocalDate;
import java.util.*;

class Bibliotheque{
    List<Livre> Livres = new ArrayList<>();
    List<Utilisateur> Utilisateurs = new ArrayList<>() ;
    List<Emprunt> Emprunts = new LinkedList<>();

    void emprunterLivre(int idLivre, Utilisateur u) throws LivreIndisponibleException{
        if(Livres.get(idLivre).isDisponible()) throw new LivreIndisponibleException("Ce livre n'est pas disponible");
        Emprunts.add(new Emprunt(LocalDate.now(),LocalDate.now().plusDays(15),Livres.get(idLivre),u));
        Livres.get(idLivre).setDisponible(false);
    }
    void rendreLivre(int idLivre,Utilisateur u){
        for(Emprunt e : Emprunts){
            if(e.getLivre().getId() == idLivre && e.getUtilisateur().getId()==u.getId()){
                Emprunts.remove(e);
                Livres.get(idLivre).setDisponible(true);
            }
        }
    }
    void afficheLivresDisponibles(){
        String lst = "Livres disponibles : ";
        for(Livre l: Livres){
            if(l.isDisponible()){
                lst = lst + l.getTitre() + " ";
            }
        }
        System.out.println(lst);
    }



}