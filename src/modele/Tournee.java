package modele;

import java.util.Date;

/**
 * Classe représentant une tournée dans le modèle de données, elle se situe à la
 * base du modèle de données.
 * @author thomas
 */
public class Tournee {
    /*  PARAMETRES  */
    /**
     * Date de début d'une tournée
     */
    private Date debut;
    
    /**
     * Date de fin d'une tournée
     */
    private Date fin;
    
    /*  CONSTRUCTEURS  */
    public Tournee() {
        this.debut = new Date();
        this.fin = new Date();
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
            this.debut = debut;
            this.fin = fin;
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
            this.debut = t.debut;
            this.fin = t.fin;
        }
        else
            System.out.println("[WARNING] Passed null argument to "
                    + "Tournee(Date, Date) defaulted to Tournee()");
    }
    
    /*  METHODES  */
    public Date getDebut() {
        return debut;
    }

    public Date getFin() {
        return fin;
    }

    @Override
    public String toString() {
        return "Tournee{" + "debut=" + debut + ", fin=" + fin + '}';
    }
}
