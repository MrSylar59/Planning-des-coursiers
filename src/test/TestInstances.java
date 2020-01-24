package test;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import modele.Tournee;
import modele.Instance;

/**
 * Fichier permettant de tester le bon fonctionnement des Instances en
 * concordance avec les tournées.
 * @author thomas
 */
public class TestInstances {
    public static void main(String[] args) {
        
        final EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory("PlanningCoursiersPU");
        final EntityManager em = emf.createEntityManager();
        
        try {
            final EntityTransaction et = em.getTransaction();
            try {
                // creation d’une entite persistante
                Tournee t1 = new Tournee(new Date(9), new Date(10));
                Tournee t2 = new Tournee(new Date(10), new Date(11));
                Tournee t3 = new Tournee(new Date(20), new Date(25));
                
                Instance inst = new Instance("test1", 150, 300, new Date());
                
                inst.AjouterTournee(t1);
                inst.AjouterTournee(t2);
                inst.AjouterTournee(t3);
                
                et.begin();
                
                em.persist(inst);
                
                et.commit();

            } catch (Exception ex) {
                if (et.isActive())
                    et.rollback();
                System.err.println(ex);
            }
        } finally {
            if(em != null && em.isOpen()){
                em.close();
            }
            if(emf != null && emf.isOpen()){
                emf.close();
            }
        }
    }
}
