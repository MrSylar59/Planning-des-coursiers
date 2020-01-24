/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.JOptionPane;
import modele.Instance;
import modele.Shift;
import modele.Solution;
import modele.Tournee;

/**
 * Classe réalisant l'algorithme 3
 * @author cyril
 */
public class Algo3 {
     /*  PARAMETRES  */
    private Solution solution;
    private Instance instance;
    private RequeteDeliver2i requeteDeliver2i;
    
    /*  CONSTRUCTEURS  */
    public Algo3(Instance inst) {
        initConnexion();
        this.instance = inst;
        this.solution = new Solution(instance, "Algo3");
    }
    
    /**
     * METHODES
     */
    /**
     * Fonction qui effectue le rangement des tournées dans des shift en respectant
     * les conditions de l'instance selon l'algorithme 3
     */
    public void ordonnancer() {
        try{
            List<Tournee> tournees = requeteDeliver2i.getTourneeOrder(instance.getId());
            List<Shift> lshift = new ArrayList<>();
            List<Shift> endshift = new ArrayList<>();
            List<Shift> verifshift = new ArrayList<>();
            Shift shift = new Shift(solution);
            shift.AjouterTournee(tournees.get(0));
            lshift.add(shift);
            int min = 0;
            int min_changed = 0;
            for (int j=1;j < tournees.size(); j++){ 
                min_changed = 0;
                if(lshift.isEmpty()){
                    shift = new Shift(solution);
                    shift.AjouterTournee(tournees.get(j));
                    lshift.add(shift);
                }else{
                    if(lshift.get(min).getTournees().get(lshift.get(min).getTournees()
                    .size()-1).getFin().getTime() <= tournees.get(j).getDebut().getTime() ){

                        if(Math.toIntExact(tournees.get(j).getDebut().getTime()/60000) - Math.toIntExact(lshift.get(min).getTournees()
                        .get(lshift.get(min).getTournees().size()-1).getFin().getTime()/60000) <= 
                        instance.getDureeMin() - lshift.get(min).getDuree() ){

                            if( lshift.get(min).getDuree() + Math.toIntExact((tournees.get(j).getFin().getTime()
                            - tournees.get(j).getDebut().getTime() )/60000) + 
                            Math.toIntExact(( tournees.get(j).getDebut().getTime() -
                            lshift.get(min).getTournees().get(lshift.get(min).getTournees().size()-1).getFin().getTime())/60000) 
                            <= instance.getDureeMax() ){

                                lshift.get(min).AjouterTournee(tournees.get(j));

                            }else{
                                verifshift.add(lshift.get(min));
                                lshift.remove(min);
                                j--;
                                min_changed = 1;
                            }
                        }else{
                            verifshift.add(lshift.get(min));
                            lshift.remove(min);
                            j--;
                            min_changed = 1;
                        }
                    }else{
                        shift = new Shift(solution);
                        shift.AjouterTournee(tournees.get(j));
                        lshift.add(shift);
                    }
                }
                if(min_changed == 0){
                    if(lshift.get(min).getDuree() >= instance.getDureeMin()){
                        endshift.add(lshift.get(min));
                        lshift.remove(min);
                    }
                }
                min = 0;
                for(int i=0;i<lshift.size();i++){
                    if(lshift.get(min).getTournees().get(lshift.get(min).getTournees().size()-1).getFin().getTime()
                    > lshift.get(i).getTournees().get(lshift.get(i).getTournees().size()-1).getFin().getTime() ){
                        min = i;
                    }
                }
            }
            for(int i = 0;i<verifshift.size();i++){
                lshift.add(verifshift.get(i));
            }
            for(int i=0;i<lshift.size();i++){
                int indice=-1;
                int temps_mort=Integer.MAX_VALUE;
                for(int k=0;k<endshift.size();k++){
                    
                    long fin_end_shift = endshift.get(k).getTournees().get(endshift.get(k).getTournees().size()-1).getFin().getTime();
                    long debut_shift_non_fini = lshift.get(i).getTournees().get(lshift.get(i).getTournees().size()-1).getDebut().getTime();
                    if(fin_end_shift < debut_shift_non_fini){
                        
                        int diff = Math.toIntExact((debut_shift_non_fini-fin_end_shift)/60000);
                        int duree_shift_end = endshift.get(k).getDuree();
                        int duree_shift_non_fini = lshift.get(i).getDuree();
                        if(duree_shift_end+diff+duree_shift_non_fini < instance.getDureeMax()){
                            if(diff <= instance.getDureeMin()-duree_shift_non_fini){
                                if(diff < temps_mort){
                                    temps_mort = diff;
                                    indice = k;
                                }
                            }
                        }
                    }
                }
                if(indice != -1){
                    for(int l=0;l<lshift.get(i).getTournees().size();l++){
                        endshift.get(indice).AjouterTournee(lshift.get(i).getTournees().get(l));
                    }
                }else{
                    endshift.add(lshift.get(i));
                }
            }
            for(int j=0;j<endshift.size();j++){
                solution.AjouterShift(endshift.get(j));
            }
        }catch(SQLException ex){
            
        }
    }
    
    /**
     * Fonction qui initialise la connexion à la base de données
     */
    private void initConnexion(){
        try {
            this.requeteDeliver2i = RequeteDeliver2i.getInstance();
        } catch (ClassNotFoundException ex) {
            
        } catch (SQLException ex) {
            
        }
    }

    /**
     * GETTEURS
     */
    public Solution getSolution() {
        return solution;
    }
    
    
}
