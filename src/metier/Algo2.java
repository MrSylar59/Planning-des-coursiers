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
 * Classe réalisant l'algorithme 2
 * @author cyril
 */
public class Algo2 {
     /*  PARAMETRES  */
    /**
     * Solution de l'algorithme
     */
    private Solution solution;
    /**
     * Instance concernée
     */
    private Instance instance;
    /**
     * Instance de connexion à la base de données
     */
    private RequeteDeliver2i requeteDeliver2i;
    
    /*  CONSTRUCTEURS  */
    public Algo2(Instance inst) {
        initConnexion();
        this.instance = inst;
        this.solution = new Solution(instance, "Algo2");
    }
    
    /**
     * METHODES
     */
    
    /**
     * Fonction qui effectue le rangement des tournées dans des shift en respectant
     * les conditions de l'instance selon l'algorithme 2
     */
    public void ordonnancer() {
        try{
            List<Tournee> tournees = requeteDeliver2i.getTourneeOrder(instance.getId());
            List<Shift> lshift = new ArrayList<>();
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

                                System.out.println("add");
                                lshift.get(min).AjouterTournee(tournees.get(j));

                            }else{
                                System.out.println("3 non");
                                solution.AjouterShift(lshift.get(min));
                                lshift.remove(min);
                                j--;
                                min_changed = 1;
                                /*shift = new Shift(solution);
                                shift.AjouterTournee(tournees.get(j));
                                lshift.add(shift);*/
                            }
                        }else{
                            
                            solution.AjouterShift(lshift.get(min));
                            lshift.remove(min);
                            j--;
                            min_changed = 1;
                            /*shift = new Shift(solution);
                            shift.AjouterTournee(tournees.get(j));
                            lshift.add(shift);*/
                        }
                    }else{
                        System.out.println("1 non");
                        shift = new Shift(solution);
                        shift.AjouterTournee(tournees.get(j));
                        lshift.add(shift);
                    }
                }
                if(min_changed == 0){
                    if(lshift.get(min).getDuree() >= instance.getDureeMin()){
                        System.out.println("add to sol "+min);

                        solution.AjouterShift(lshift.get(min));
                        lshift.remove(min);
                    }
                }
                min = 0;
                for(int i=0;i<lshift.size();i++){
                    if(lshift.get(min).getTournees().get(lshift.get(min).getTournees().size()-1).getFin().getTime()
                    > lshift.get(i).getTournees().get(lshift.get(i).getTournees().size()-1).getFin().getTime() ){
                        System.out.println("min devient="+i);
                        min = i;
                    }
                }
            }
            for(int i=0;i<lshift.size();i++){
                solution.AjouterShift(lshift.get(i));
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
