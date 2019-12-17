package modele;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import metier.TourneeSort;

/**
 * Classe représentant un shift de travail attribué à un employé. Il doit respecter
 * certaines contraintes dans la durée (min max)
 * @author thomas
 */
@Entity
public class Shift implements Serializable {
     /*  PARAMETRES  */
    private static final long serialVersionUID = 1L;
    
    /**
     * Id pour la table de la base de données
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * La durée actuelle du shift
     */
    @Column(name = "DUREE")
    private int duree;
    
    /**
     * Le temps pendant lequel un shift est non fini mais tout de même payé
     */
    @Column(name = "TEMPS_MORT")
    private int tempsMort;
    
    /**
     * Le cout du shift en euros (choix 1min = 1€)
     */
    @Column(name = "PRIX")
    private double prix;
    
    /**
     * La solution à laquelle le shift est lié
     */
    @ManyToOne
    @JoinColumn(name = "SOL_ID")
    private Solution solution;
    
    /**
     * L'ensemble des tournées apparaissant dans ce shift
     */
    @ManyToMany
    @JoinTable(
            name = "TOURNEE-SHIFT",
            joinColumns = @JoinColumn(name = "SHIFT_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOURNEE_ID")
    )
    private List<Tournee> tournees;
    
    /*  CONSTRUCTEURS  */
    public Shift(Solution solution){
        this.prix = 0;
        this.duree = 0;
        this.tempsMort = 0;
        this.tournees = new LinkedList<>();
        if (solution != null)
            this.solution = solution;
    }

    /*  METHODES  */
    
    /**
     * Méthode qui permet d'ajouter une tournée à l'ensemble des tournées d'un
     * shift. Elle conserve la relation 1..*-1..* prévu dns le modèle relationnel
     * contrairement à la fonction Tournee.AjouterShiftApparition()
     * 
     * @param t la tournée à ajouter
     * @return renvoie true si l'association s'ets bien passé et alse sinon
     */
    public boolean AjouterTournee(Tournee t){
        if (!t.AjouterShiftApparition(this))
            return false;
        
        if(!this.tournees.contains(t))
            return false;
        
        this.tournees.add(t);
        this.tournees.sort(new TourneeSort());
        
        this.duree = CalculerDuree();
        this.tempsMort = CalculerTempsMort();
        this.prix = CalculerPrix();
        
        return true;
    }
    
    /**
     * Fonction qui calcule la durée d'activité d'un shift à partir de ses 
     * tournées. La liste doit avoir été préalablement triée.
     * 
     * Note: tester le comportement de la fonction car risque de perte de précision
     * avec le cast en int. Mais ne devrait pas poser de problème car unités en
     * minute.
     * 
     * @return la durée en minute qu'un shift est actif (ie. tournée en cours)
     */
    public int CalculerDuree(){
        long diff = this.tournees.get(0).getDebut().getTime() -
                this.tournees.get(this.tournees.size()).getDebut().getTime();
        
        return (int)TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Fonction qui calcule le temps mort d'un shift à partir de ses tournées.
     * La liste doit avoir été préalablement triée.
     * 
     * Note: tester le comportement de la fonction car risque de perte de précision
     * avec le cast en int. Mais ne devrait pas poser de problème car unités en
     * minute.
     * 
     * @return la durée en minute qu'un shift est innactif
     */
    public int CalculerTempsMort(){
        int sum = 0;
        int max = Math.max(this.solution.getInstance().getDureeMin(), this.duree);
        
        for(Tournee t : this.tournees){
            long diff = t.getFin().getTime() - t.getDebut().getTime();
            sum += (int)diff;
        }
        
        return max - sum;
    }
    
    public double CalculerPrix(){
        return (this.duree + this.tempsMort);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.duree;
        hash = 83 * hash + this.tempsMort;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.prix) ^ (Double.doubleToLongBits(this.prix) >>> 32));
        hash = 83 * hash + Objects.hashCode(this.solution);
        hash = 83 * hash + Objects.hashCode(this.tournees);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Shift other = (Shift) obj;
        if (this.duree != other.duree) {
            return false;
        }
        if (this.tempsMort != other.tempsMort) {
            return false;
        }
        if (Double.doubleToLongBits(this.prix) != Double.doubleToLongBits(other.prix)) {
            return false;
        }
        if (!Objects.equals(this.solution, other.solution)) {
            return false;
        }
        if (!Objects.equals(this.tournees, other.tournees)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "Shift{" + "duree=" + duree + ", tempsMort=" + tempsMort + ", prix=" + prix + '}';
    }
}
