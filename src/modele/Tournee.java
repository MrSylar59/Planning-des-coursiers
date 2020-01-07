package modele;

import java.util.Date;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * Classe représentant une tournée dans le modèle de données, elle se situe à la
 * base du modèle de données.
 * @author thomas
 */
@Entity
public class Tournee implements Serializable {
    /*  PARAMETRES  */
    private static final long serialVersionUID = 1L;
    
    /**
     * Id pour la table de la base de données
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Date de début d'une tournée
     */
    @Column(name = "DATE_DEBUT")
    private Date debut;
    
    /**
     * Date de fin d'une tournée
     */
    @Column(name = "DATE_FIN")
    private Date fin;
    
    /**
     * Instance à laquelle est liée la tournée. Utilisée pour créer les liens
     * dans le modèle de données
     */
    @ManyToOne
    @JoinColumn(name = "INST_ID")
    private Instance inst;
    
    /**
     * L'ensemble des shifts dans lesquelles apparaissent la tournée.
     */
    @ManyToMany(mappedBy = "tournees")
    private HashSet<Shift> shifts;
    
    /*  CONSTRUCTEURS  */
    public Tournee() {
        this.debut  = new Date();
        this.fin    = new Date();
    }
    
    /**
     * Constructeur de tournée, si la date de début ou la date de fin qui sont
     * fournies ne sont pas valides, alors le constructeur passera par le 
     * constructeur par défaut à la place
     * 
     * @param debut la date de début de la tournée
     * @param fin  la date de fin de la tournée
     */
    public Tournee(Date debut, Date fin) {
        this();
        
        if (debut != null && fin != null){
            this.debut  = debut;
            this.fin    = fin;
        }
        else
            System.out.println("[WARNING] Passed null argument to "
                    + "Tournee(Date, Date) defaulted to Tournee()");
    }
    
    /**
     * Constructeur par copie de tournée, si la tournée à copier n'est pas valide
     * le constructeur se rabat sur le constructeur par défaut
     * 
     * @param t la tournée à copier
     */
    public Tournee(Tournee t){
        this();
        
        if (t != null){
            this.debut  = t.debut;
            this.fin    = t.fin;
        }
        else
            System.out.println("[WARNING] Passed null argument to "
                    + "Tournee(Date, Date) defaulted to Tournee()");
    }
    
    /*  METHODES  */
    
    /**
     * Méthode qui permet d'ajouter un shift comme faisant partie des shifts utilisant
     * la tournée. Elle ne doit pas être appellée à part sans avoir appelé la fonction
     * Shift.AjouterTournee().
     * 
     * @param s le shift à ajouter
     * @return renvoie true si l'ajout s'est bien effectué et false sinon
     */
    public boolean AjouterShiftApparition(Shift s){
        return this.shifts.add(s);
    }
    
    /**
     * Fonction qui permet de savoir si une tournée est compatible avec un shift
     * déjà pré-existant ou non
     * 
     * @param s le shift avec lequel on souhaite tester la compatiblité
     * @return true si deux tournées sont compatibles et false sinon
     */
    public boolean compatible(Shift s){
        return !this.debut.before(s.DateFinShift()) 
                || !this.fin.after(s.DateDebutShift());
    }
    
    public Date getDebut() {
        return debut;
    }

    public Date getFin() {
        return fin;
    }
    
    public Instance getInstance(){
        return this.inst;
    }
    
    public int getDuree(){
        long diff = this.fin.getTime() - this.debut.getTime();
        return (int)TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }
    
    public void setInstance(Instance inst) {
        this.inst = inst;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.debut);
        hash = 97 * hash + Objects.hashCode(this.fin);
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
        final Tournee other = (Tournee) obj;
        if (!Objects.equals(this.debut, other.debut)) {
            return false;
        }
        if (!Objects.equals(this.fin, other.fin)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tournee{" + "debut=" + debut + ", fin=" + fin + '}';
    }
}
