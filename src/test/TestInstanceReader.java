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
import modele.Solution;
import metier.AlgoOrdonnancement;

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
<<<<<<< HEAD
                InstanceReader rd = new InstanceReader("C:\\Users\\Cyril\\Documents\\"+
                "NetBeansProjects\\Planning-des-coursiers\\tests\\instances\\instance_test.csv");
=======
                InstanceReader rd = new InstanceReader("C:\\Users\\thoma\\Documents\\"+
                "POO\\PlanningCoursiers\\tests\\instances\\instance_test.csv");
                
                Instance inst;
>>>>>>> dev
                inst = rd.readInstance();
                
                AlgoOrdonnancement ord = new AlgoOrdonnancement(inst);
                ord.ordonnancer();
                System.out.println();
                System.out.println("MES TESTS");
                System.out.println(ord);
                System.out.println("FIN MES TESTS");
                System.out.println();
                
                et.begin();
                
                em.persist(inst);
                em.persist(ord.getSolution());
                
                et.commit();
                
                rd = new InstanceReader("C:\\Users\\thoma\\Documents\\"+
                "POO\\PlanningCoursiers\\tests\\instances\\instance_1.csv");
                inst = rd.readInstance();
                ord = new AlgoOrdonnancement(inst);
                
                ord.ordonnancer();
                
                System.out.println();
                System.out.println("MES TESTS");
                System.out.println(ord);
                System.out.println("FIN MES TESTS");
                System.out.println();
                
                et.begin();
                
                em.persist(inst);
                em.persist(ord.getSolution());
                
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