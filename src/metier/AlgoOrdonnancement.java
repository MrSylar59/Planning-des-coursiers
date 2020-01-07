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
    private List<Shift> shifts;
    
    /*  CONSTRUCTEURS  */
    public AlgoOrdonnancement(Instance inst) {
        this.instance = inst;
        this.solution = new Solution(instance, "Algo1");
        this.shifts = new LinkedList<>();
    }
    
    /*  METHODES  */
    public void ordonnancer() {
        HashSet<Tournee> tournees = instance.getTournees();
        Shift s = new Shift(solution);
        
        for (Tournee t : tournees){
            if (s.getDuree() + t.getDuree() < instance.getDureeMax() && t.compatible(s)){
                s.AjouterTournee(t);
            }
            else {
                shifts.add(s);
                s = new Shift(solution);
                s.AjouterTournee(t);
            }
        }
    }

    public Solution getSolution() {
        return solution;
    }
}
