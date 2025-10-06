import java.time.LocalDate;
import java.util.*;

class Bibliotheque{
    List<Livre> Livres = new ArrayList<>();
    List<Utilisateur> Utilisateurs = new ArrayList<>() ;
    List<Emprunt> Emprunts = new LinkedList<>();

    void emprunterLivre(int idLivre, Utilisateur u) throws LivreIndisponibleException{
        if(Livres.get(idLivre).disponible) throw new LivreIndisponibleException("Ce livre n'est pas disponible");
        Emprunts.add(new Emprunt(idLivre,u.id, LocalDate.now()));
        Livres.get(idLivre).disponible = false;
    }
    void rendreLivre(int idLivre,Utilisateur u){
        for(Emprunt e : Emprunts){
            if(e.idLivre == idLivre && e.uid=u.id){
                Emprunts.remove(e);
                Livres.get(idLivre).disponible = true;
            }
        }
    }
    void afficheLivresDisponibles(){
        String lst = "Livres disponibles : ";
        for(Livre l: Livres){
            if(l.disponible){
                lst = lst + l.name + " ";
            }
        }
        System.out.println(lst);
    }


}