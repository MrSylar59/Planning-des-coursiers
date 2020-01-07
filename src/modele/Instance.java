package modele;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Classe représentant une instance de livraison pour laquelle nous souhaitons
 * trouver un planning permettant d'économiser un maximum
 * @author thomas
 */
@Entity
public class Instance implements Serializable {
    /*  PARAMETRES  */
    private static final long serialVersionUID = 1L;
    
    /**
     * Id pour la table de la base de données
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nom de l'instance
     */
    @Column(name = "NOM")
    private String nom;
    
    /**
     * Durée minimale d'un shift valide pour cette instance
     */
    @Column(name = "DUR_MIN")
    private int dureeMin;
    
    /**
     * Durée maximale d'un shift valide pour cette instance
     */
    @Column(name = "DUR_MAX")
    private int dureeMax;
    
    /**
     * Date à laquelle aura lieux cette instance
     */
    @Column(name = "DATE")
    private Date date;
    
    /**
     * L'ensemble des tournées devant avoir lieux pour cette instance
     */
    @OneToMany(mappedBy="inst", cascade = CascadeType.PERSIST)
    private HashSet<Tournee> tournees;
    
    /**
     * La liste de toutes les solutions qui ont été trouvées pour une instance
     */
    @OneToMany(mappedBy="inst", cascade = CascadeType.PERSIST)
    private List<Solution> solutions;
    
    /*  CONSTRUCTEURS  */
    public Instance() {
        this.nom        = "empty";
        this.dureeMin   = 0;
        this.dureeMax   = 0;
        this.date       = new Date();
        this.tournees   = new HashSet<>();
        this.solutions = new LinkedList<>();
    }
    
    public Instance(Long id,String nom, int dureeMin, int dureeMax, Date date) {
        this();
        boolean warn = false;
        
        this.id = id;
        
        if (nom != null && !nom.isEmpty())
            this.nom = nom;
        else
            warn = true;
        
        if (dureeMin > 0 && dureeMax >= dureeMin){
            this.dureeMin = dureeMin;
            this.dureeMax = dureeMax;
        }
        else
            warn = true;
        
        if (date != null)
            this.date = date;
        else
            warn = true;
        
        if (warn)
            System.out.println("[WARNING] Passed invalid argument to "
                    + "Instance(String, int, int, Date) defaulted to Instance()");
    }
    
    public Instance(String nom, int dureeMin, int dureeMax, Date date) {
        this();
        boolean warn = false;
        
        if (nom != null && !nom.isEmpty())
            this.nom = nom;
        else
            warn = true;
        
        if (dureeMin > 0 && dureeMax >= dureeMin){
            this.dureeMin = dureeMin;
            this.dureeMax = dureeMax;
        }
        else
            warn = true;
        
        if (date != null)
            this.date = date;
        else
            warn = true;
        
        if (warn)
            System.out.println("[WARNING] Passed invalid argument to "
                    + "Instance(String, int, int, Date) defaulted to Instance()");
    }
    
    /*  METHODES  */
    /**
     * Permet d'ajouter une tournée à l'ensemble des tournées de l'instance.
     * Si une tournée est déjà présente dans cet ensemble elle n'est pas ajoutée
     * 
     * @param t la tournée à ajouter dans l'instance
     * @return Si l'ajout s'est bien effecuté elle renvoie true et false sinon
     */
    public boolean AjouterTournee(Tournee t){
        if (t.getInstance() != null)
            if (!t.getInstance().tournees.remove(t))
                return false;
        
        t.setInstance(this);
        return this.tournees.add(t);
    }
    
    /**
     * Permet d'ajouter une solution à la liste des solutions de l'instance.
     * Si la solution est déjà présente dans la liste alors, la nouvelle la remplace
     * 
     * @param s la solution à ajouter à la liste des solutions
     * @return Si l'ajout a bien été effectué, alors elle revoie true et false sinon
     */
    public boolean AjouterSolution(Solution s){
        if (s.getInstance() != this)
            return false;
        
        if (this.solutions.contains(s))
            this.solutions.remove(s);
        
        return this.solutions.add(s);
    }
    
    public long getId(){
        return id;
    } 

    public String getNom() {
        return nom;
    }    

    public int getDureeMin() {
        return dureeMin;
    }

    public int getDureeMax() {
        return dureeMax;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nom);
        hash = 53 * hash + this.dureeMin;
        hash = 53 * hash + this.dureeMax;
        hash = 53 * hash + Objects.hashCode(this.date);
        hash = 53 * hash + Objects.hashCode(this.tournees);
        hash = 53 * hash + Objects.hashCode(this.solutions);
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
        final Instance other = (Instance) obj;
        if (this.dureeMin != other.dureeMin) {
            return false;
        }
        if (this.dureeMax != other.dureeMax) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.tournees, other.tournees)) {
            return false;
        }
        if (!Objects.equals(this.solutions, other.solutions)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nom;
    }
}
