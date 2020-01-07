package modele;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * Classe qui représente une solution à une instance donnée.
 * @author thomas
 */
@Entity
public class Solution implements Serializable {
    /*  PARAMETRES  */
    private static final long serialVersionUID = 1L;
    
    /**
     * Id pour la table de la base de données
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Le cout de mise en place de la solution en fonction du nombre de shifts
     */
    @Column(name = "PRIX")
    private double prix;
    
    /**
     * Nom de l'algorithme utilisé pour générer la solution
     */
    @Column(name = "ALGO")
    private String algo;
        
    /**
     * L'instance pour laquelle la solution s'applique
     */
    @JoinColumn(name = "INST_ID")
    private Instance inst;
    
    /**
     * L'ensemble des shifts devant être réalisés dans la solution
     */
    @OneToMany(mappedBy="solution", cascade = CascadeType.PERSIST)
    private HashSet<Shift> shifts;
    
    /*  CONSTRUCTEURS  */
    public Solution() {
        this.prix = 0;
        this.shifts = new HashSet<>();
    }
    
    public Solution(Instance inst, String algo){
        this();
        
        if (inst != null)
            this.inst = inst;
        
        if (algo != null && !algo.isBlank())
            this.algo = algo;
    }
    
    /*  METHODES  */
    public Instance getInstance(){
        return this.inst;
    }
    
    /**
     * Fonction qui ajoute un shift à la liste des solution. Ajouter un shift
     * modifie le prix de la solution.
     * 
     * @param s le shift à ajouter à une solution
     * @return renvoie true si l'ajout a été fait et false sinon
     */
    public boolean AjouterShift(Shift s){
        this.prix += s.getPrix();
        return this.shifts.add(s);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.prix) ^ (Double.doubleToLongBits(this.prix) >>> 32));
        hash = 41 * hash + Objects.hashCode(this.algo);
        hash = 41 * hash + Objects.hashCode(this.inst);
        hash = 41 * hash + Objects.hashCode(this.shifts);
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
        final Solution other = (Solution) obj;
        if (Double.doubleToLongBits(this.prix) != Double.doubleToLongBits(other.prix)) {
            return false;
        }
        if (!Objects.equals(this.algo, other.algo)) {
            return false;
        }
        if (!Objects.equals(this.inst, other.inst)) {
            return false;
        }
        if (!Objects.equals(this.shifts, other.shifts)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Solution{" + "prix=" + prix + ", algo=" + algo + '}';
    }
}
