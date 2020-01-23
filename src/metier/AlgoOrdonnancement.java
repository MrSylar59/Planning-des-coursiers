package metier;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import modele.Instance;
import modele.Solution;
import modele.Tournee;
import modele.Shift;
import java.sql.SQLException;

/**
 * Classe permettant de trouver une solution valide pour une instance
 * @author thomas
 */
public class AlgoOrdonnancement {
    /*  PARAMETRES  */
    private Solution solution;
    private Instance instance;
    private RequeteDeliver2i requeteDeliver2i;
    
    
    
    /*  CONSTRUCTEURS  */
    public AlgoOrdonnancement(Instance inst) {
        initConnexion();
        this.instance = inst;
        this.solution = new Solution(instance, "Algo1");
    }
    
    /*  METHODES  */
    public void ordonnancer() {
        try{
            HashSet<Tournee> tournees = requeteDeliver2i.getTournees(instance.getId());
            Shift s = new Shift(solution);
            System.out.println("nb tournees dans hashset="+tournees.size());
            for (Tournee t : tournees){ //int j=0;j < tournees.size(); j++
                if (t.compatible(s)){
                    s.AjouterTournee(t);
                    if (s.getDuree() > this.instance.getDureeMax()){
                        s.SupprimerTournee(t);
                        solution.AjouterShift(s);
                        s = new Shift(solution);
                        s.AjouterTournee(t);
                    }
                    else {
                        s.AjouterTournee(t);
                    } 
                }
                else {
                    solution.AjouterShift(s);
                    s = new Shift(solution);
                    s.AjouterTournee(t);
                }
            }
            solution.AjouterShift(s);
        }catch(SQLException ex){
            
        }
    }
    
    private void initConnexion(){
        try {
            this.requeteDeliver2i = RequeteDeliver2i.getInstance();
        } catch (ClassNotFoundException ex) {
            
        } catch (SQLException ex) {
            
        }
    }
    /* GETTER */
    public Solution getSolution() {
        return solution;
    }

    @Override
    public String toString() {
        return "AlgoOrdonnancement{" + "solution=" + solution + "}";
    }
}
