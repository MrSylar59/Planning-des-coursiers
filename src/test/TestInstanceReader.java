/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import io.InstanceReader;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import modele.Instance;

/**
 *
 * @author thoma
 */
public class TestInstanceReader {
    public static void main(String[] args) {
        
        final EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory("PlanningCoursiersPU");
        final EntityManager em = emf.createEntityManager();
        
        try {
            final EntityTransaction et = em.getTransaction();
            try {
                // creation d’une entite persistante
                InstanceReader rd = new InstanceReader("C:\\Users\\thoma"
                    + "\\Documents\\POO"
                    + "\\PlanningCoursiers\\tests\\instances\\instance_test.csv");
                Instance inst = rd.readInstance();
                
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