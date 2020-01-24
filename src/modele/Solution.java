package modele;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    private List<Shift> shifts;
    
    /*  CONSTRUCTEURS  */
    /**
     * Constructeur par default
     */
    public Solution() {
        this.prix = 0;
        this.shifts = new LinkedList<>();
    }
    /**
     * Constructeur par data Instance et code algorithme
     * @param inst
     * @param algo 
     */
    public Solution(Instance inst, String algo){
        this();
        
        if (inst != null)
            this.inst = inst;
        
        if (algo != null && !algo.isBlank())
            this.algo = algo;
    }
    
    /**
     * Constructeur par data identifiant, prix totale, code de l'algorithme
     * @param id
     * @param prix
     * @param algo 
     */
    public Solution(long id,double prix, String algo){
        this();
        this.id = id;
        this.prix = prix;
        if (algo != null && !algo.isBlank())
            this.algo = algo;
    }
    
    /**
     * GETTEURS
     */
    
    public Instance getInstance(){
        return this.inst;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public double getPrix() {
        return prix;
    }
    
    public long getId(){
        return id;
    }
    
    /**
     * METHODES  
     */
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

    /**
     * Fonction permettant d'ajouter la solution en base
     * @param em 
     */
    public void ajouterEnBase(EntityManager em){
        final EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(this);
        et.commit();
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
        String str = "";
        
        for (Shift s : this.shifts){
            str += s;
            str += "\n";
        }
        
        return "Solution{" + "prix=" + prix + ", algo=" + algo + ", shifts=\n" + str + '}';
    }
}
