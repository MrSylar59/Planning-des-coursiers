/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.Color;
import java.awt.Graphics;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import metier.Algo2;
import metier.EnumAlgo;
import metier.RequeteDeliver2i;
import modele.Graphe;
import modele.Instance;
import modele.Point;
import modele.Rectangle;
import modele.Tournee;
import modele.Shift;
import modele.Solution;
import metier.AlgoOrdonnancement;
import java.text.DecimalFormat;
import metier.Algo3;

/**
 *
 * @author cyril
 */
public class ZoneDessin extends javax.swing.JPanel {
    private Instance instance;
    private EntityManager em;
    private Graphe graphe;
    private List<Rectangle> lRectangle;
    private RequeteDeliver2i requeteDeliver2i;
    
    /**
     * Creates new form ZoneDessin
     */
    public ZoneDessin(Instance instance, EntityManager em){
        initComponents();
        initConnexion();
        this.em = em;
        this.instance = instance;
        this.setBackground(Color.white);
        this.setBounds(25, 100, 1300, 600);
        afficherDefault();
        repaint();
    }
    
    private void afficherDefault(){
        Point haut_gauche = new Point(30,100);
        Point bas_droite = new Point(1000,550);
        try{
            int date_max;
            int date_min;
            int nb;
            List<Tournee> lTournee = requeteDeliver2i.getTourneebyInstance(instance.getId());
            /*int date_min_comp = Math.toIntExact(lTournee.get(0).getDebut().getTime()/60000);
            int date_max_comp = Math.toIntExact(lTournee.get(0).getFin().getTime()/60000);
            for (int i = 0; i < lTournee.size(); i++) {
                if(date_min_comp > Math.toIntExact(lTournee.get(i).getDebut().getTime()/60000)){
                    date_min_comp = Math.toIntExact(lTournee.get(i).getDebut().getTime()/60000);
                }
                if(date_max_comp < Math.toIntExact(lTournee.get(i).getFin().getTime()/60000)){
                    date_max_comp = Math.toIntExact(lTournee.get(i).getFin().getTime()/60000);
                }
            }*/
            date_min = requeteDeliver2i.getMinDebutInstance(instance);
            date_max = requeteDeliver2i.getMaxFinInstance(instance);
            nb=lTournee.size();
            System.out.println(date_max+"=max min= "+date_min);
            this.graphe = new Graphe(haut_gauche, bas_droite, date_min, date_max, nb);
            this.lRectangle = new LinkedList<>();
            for (int i = 0; i < lTournee.size(); i++) {
                long diff_milli = (lTournee.get(i).getFin().getTime()-lTournee.get(i).getDebut().getTime());
                int diff_min = Math.toIntExact(diff_milli/60000);
                long deb = (Math.toIntExact(lTournee.get(i).getDebut().getTime()/60000)-graphe.getDebut());
                int deb_min = Math.toIntExact(deb);
                Point origine = new Point(graphe.getOrigine().getX()+deb_min*graphe.getWidth(),
                        graphe.getOrigine().getY()-(i+1)*graphe.getHeight());
                Rectangle rect = new Rectangle(Color.blue,origine,diff_min*graphe.getWidth(),graphe.getHeight());
                this.lRectangle.add(rect);
            }
        }catch (SQLException ex) {
            
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        graphe.seDessiner(g);
        afficherHeure();
        for(Rectangle rectangle : lRectangle){
            //System.out.println(rectangle.getpHautGauche().getX()+" "+rectangle.getpHautGauche().getY()+" "+rectangle.getWidth()+" "+rectangle.getHeight());
            rectangle.seDessiner(g);
        }
    }
    
    private void afficherHeure(){
        int heure_debut = Math.toIntExact(graphe.getDebut()/60)+1;
        int minute_debut = graphe.getDebut()%60;
        DecimalFormat formater = new DecimalFormat("00");
        JLabel label = new JLabel(heure_debut+":"+formater.format(minute_debut));
        label.setBounds(graphe.getOrigine().getX()-10, graphe.getOrigine().getY()+10, 100, 20);
        this.add(label);
        int i=0;
        while( i < 23-heure_debut){
            i++;
            label = new JLabel((heure_debut+i)+":"+formater.format(minute_debut));
            label.setBounds(graphe.getOrigine().getX()-10+(graphe.getWidth()*60*i), graphe.getOrigine().getY()+10, 100, 20);
            this.add(label);
        }
    }
    
    public void afficherAlgo(EnumAlgo algo){
        switch(algo){
            case AlgoOrdonnancement:
                try{
                    List<Shift> lshift = requeteDeliver2i.getShift(instance.getId(),"Algo1");
                    if(lshift == null){
                        AlgoOrdonnancement algoOrdo = new AlgoOrdonnancement(instance);
                        algoOrdo.ordonnancer();
                        algoOrdo.ajouterEnBase(em);
                        lshift = requeteDeliver2i.getShift(instance.getId(),"Algo1");
                    }
                    Solution sol = requeteDeliver2i.getSolutionbyInstance(instance.getId(),"Algo1");
                    jLabel1.setText("prix = "+sol.getPrix());
                    Point haut_gauche = new Point(30,100);
                    Point bas_droite = new Point(1000,550);
                    int min_debut = requeteDeliver2i.getMinDebutInstance(instance);
                    int max_fin = requeteDeliver2i.getMaxFinInstance(instance);
                    this.graphe = new Graphe(haut_gauche, bas_droite, min_debut, max_fin, lshift.size());
                    this.lRectangle = createRectangleByShift(lshift);
                    
                }catch(SQLException ex){
                    
                }
                repaint();
            break;
            case Algo2:
                try{
                    List<Shift> lshift = requeteDeliver2i.getShift(instance.getId(),"Algo2");
                    if(lshift == null){
                        Algo2 algo2 = new Algo2(instance);
                        algo2.ordonnancer();
                        algo2.ajouterEnBase(em);
                        lshift = requeteDeliver2i.getShift(instance.getId(),"Algo2");
                    }
                    Solution sol = requeteDeliver2i.getSolutionbyInstance(instance.getId(),"Algo2");
                    jLabel1.setText("prix = "+sol.getPrix());
                    Point haut_gauche = new Point(30,100);
                    Point bas_droite = new Point(1000,550);
                    int min_debut = requeteDeliver2i.getMinDebutInstance(instance);
                    int max_fin = requeteDeliver2i.getMaxFinInstance(instance);
                    this.graphe = new Graphe(haut_gauche, bas_droite, min_debut, max_fin, lshift.size());
                    this.lRectangle = createRectangleByShift(lshift);
                    
                }catch(SQLException ex){
                    
                }
                repaint();
            break;
            case Algo3:
                try{
                    List<Shift> lshift = requeteDeliver2i.getShift(instance.getId(),"Algo3");
                    if(lshift == null){
                        Algo3 algo3 = new Algo3(instance);
                        algo3.ordonnancer();
                        algo3.ajouterEnBase(em);
                        lshift = requeteDeliver2i.getShift(instance.getId(),"Algo3");
                    }
                    Solution sol = requeteDeliver2i.getSolutionbyInstance(instance.getId(),"Algo3");
                    jLabel1.setText("prix = "+sol.getPrix());
                    Point haut_gauche = new Point(30,100);
                    Point bas_droite = new Point(1000,550);
                    int min_debut = requeteDeliver2i.getMinDebutInstance(instance);
                    int max_fin = requeteDeliver2i.getMaxFinInstance(instance);
                    this.graphe = new Graphe(haut_gauche, bas_droite, min_debut, max_fin, lshift.size());
                    this.lRectangle = createRectangleByShift(lshift);
                    
                }catch(SQLException ex){
                    
                }
                repaint();
            break;
            case Default:
                jLabel1.setText("");
                afficherDefault();
                repaint();
            break;
        }
    }
    
    private List<Rectangle> createRectangleByShift(List<Shift> lshift){
        List<Rectangle> lRect = new ArrayList<>();
        for(int i=0;i < lshift.size();i++){
            try {
                List<Tournee> lTournee = requeteDeliver2i.getTourneeByShift(lshift.get(i).getId());
                int min = Math.toIntExact(lTournee.get(0).getDebut().getTime()/60000);
                int max = Math.toIntExact(lTournee.get(0).getFin().getTime()/60000);
                for (int j = 0; j < lTournee.size(); j++) {
                    long diff_milli = (lTournee.get(j).getFin().getTime()-lTournee.get(j).getDebut().getTime());
                    int diff_min = Math.toIntExact(diff_milli/60000);
                    long deb = (Math.toIntExact(lTournee.get(j).getDebut().getTime()/60000)-graphe.getDebut());
                    int deb_min = Math.toIntExact(deb);
                    Point origine = new Point(graphe.getOrigine().getX()+deb_min*graphe.getWidth(),
                            graphe.getOrigine().getY()-(i+1)*graphe.getHeight());
                    Rectangle rect = new Rectangle(Color.GREEN,origine,diff_min*graphe.getWidth(),graphe.getHeight());
                    lRect.add(rect);
                    if(min > Math.toIntExact(lTournee.get(j).getDebut().getTime()/60000))
                        min = Math.toIntExact(lTournee.get(j).getDebut().getTime()/60000);
                    if(max < Math.toIntExact(lTournee.get(j).getFin().getTime()/60000))
                        max = Math.toIntExact(lTournee.get(j).getFin().getTime()/60000);
                    if(j+1 < lTournee.size()){
                        origine = new Point(graphe.getOrigine().getX()+
                            (Math.toIntExact(lTournee.get(j).getFin().getTime()/60000-graphe.getDebut()))*graphe.getWidth(),
                            graphe.getOrigine().getY()-(i+1)*graphe.getHeight());
                        rect = new Rectangle(Color.RED,origine,
                            (Math.toIntExact(lTournee.get(j+1).getDebut().getTime()-
                            Math.toIntExact(lTournee.get(j).getFin().getTime()))/60000)*graphe.getWidth(),graphe.getHeight());
                        lRect.add(rect);
                    
                    }
                }
                if(max-min < instance.getDureeMin()){
                    Point origine = new Point(graphe.getOrigine().getX()+(max-graphe.getDebut())*graphe.getWidth(),
                            graphe.getOrigine().getY()-(i+1)*graphe.getHeight());
                    Rectangle rect = new Rectangle(Color.RED,origine,(instance.getDureeMin()-(max-min))*graphe.getWidth(),graphe.getHeight());
                    lRect.add(rect);
                }
            } catch (SQLException ex) {
                
            }
        }
        return lRect;
    }    
    
    private void initConnexion(){
        try {
            this.requeteDeliver2i = RequeteDeliver2i.getInstance();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex,"ClassNotFoundException",JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex,"SQLException",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(390, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(290, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
