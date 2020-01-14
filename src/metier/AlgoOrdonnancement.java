package metier;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import modele.Instance;
import modele.Solution;
import modele.Tournee;
import modele.Shift;

/**
 * Classe permettant de trouver une solution valide pour une instance
 * @author thomas
 */
public class AlgoOrdonnancement {
    /*  PARAMETRES  */
    private Solution solution;
    private Instance instance;
    
    /*  CONSTRUCTEURS  */
    public AlgoOrdonnancement(Instance inst) {
        this.instance = inst;
        this.solution = new Solution(instance, "Algo1");
    }
    
    /*  METHODES  */
    public void ordonnancer() {
        HashSet<Tournee> tournees = instance.getTournees();
        Shift s = new Shift(solution);
        
        for (Tournee t : tournees){
            if (t.compatible(s)){
                s.AjouterTournee(t);
                if (s.getDuree() > this.instance.getDureeMax()){
                    s.SupprimerTournee(t);
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
    }

    public Solution getSolution() {
        return solution;
    }

    @Override
    public String toString() {
        return "AlgoOrdonnancement{" + "solution=" + solution + "}";
    }
}
