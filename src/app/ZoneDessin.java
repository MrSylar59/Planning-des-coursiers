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
 * JPanel acceuillant le graphe contenant l'affichage des solutions
 * 
 * @author cyril
 */
public class ZoneDessin extends javax.swing.JPanel {
    /**
     * PARAMETRES
     */
    
    /**
     * objet Instance correspondant à l'instance séléctionnée pour affichage
     */
    private Instance instance;
    /**
     * Entité de management de la base de données
     */
    private EntityManager em;
    /**
     * Graphe contenant les informations nécessaire à l'affichage
     */
    private Graphe graphe;
    /**
     * Liste des rectangle à afficher dans la zone
     */
    private List<Rectangle> lRectangle;
    /**
     * Instance de connexion à la base de données
     */
    private RequeteDeliver2i requeteDeliver2i;
    
    /*  CONSTRUCTEURS  */
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
    
    /* METHODES */
    
    /**
     * Fonction qui affiche dans le JPanel la liste des tournées de l'instance 
     * sans les trier avec une tournée par ligne
     */
    private void afficherDefault(){
        Point haut_gauche = new Point(30,100);
        Point bas_droite = new Point(1000,550);
        try{
            int date_max;
            int date_min;
            int nb;
            List<Tournee> lTournee = requeteDeliver2i.getTourneebyInstance(instance.getId());
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
    
    /**
     * Modification de la fonction paintComponent afin de dessiner le graphe ainsi
     * que les rectangles contenu dans la liste lRectangle 
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphe.seDessiner(g);
        afficherHeure();
        for(Rectangle rectangle : lRectangle){
            rectangle.seDessiner(g);
        }
    }
    /**
     * Fonction qui ajoute sur l'affichage les graduations correspondant à l'heure
     */
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
    
    /**
     * Fonction qui permet d'afficher la solution correspondant à l'algorithme séléctionné 
     * et à l'instance séléctionnée de manière trié par shift ( une ligne correspond à un shift)
     * en affichant les temps morts en rouge et les tournées en vert
     */
    public void afficherAlgo(EnumAlgo algo){
        switch(algo){// switch case algo
            case AlgoOrdonnancement: 
                try{
                    Solution sol = requeteDeliver2i.getSolutionbyInstance(instance.getId(),"Algo1"); // verification présence de la solution
                    if(sol == null){   // si absente
                        AlgoOrdonnancement algoOrdo = new AlgoOrdonnancement(instance); 
                        algoOrdo.ordonnancer(); // création de la solution
                        sol = algoOrdo.getSolution();
                        sol.ajouterEnBase(em); // ajout dans la base
                    }
                    List<Shift> lshift = requeteDeliver2i.getShiftbySolution(sol.getId()); //recupération de la liste de shift
                    jLabel1.setText("prix = "+sol.getPrix()); // ajoux du prix
                    Point haut_gauche = new Point(30,100);
                    Point bas_droite = new Point(1000,550);
                    int min_debut = requeteDeliver2i.getMinDebutInstance(instance); // récupération de l'heure minimal
                    int max_fin = requeteDeliver2i.getMaxFinInstance(instance); // récupération de l'heure maximal
                    this.graphe = new Graphe(haut_gauche, bas_droite, min_debut, max_fin, lshift.size()); //création du graphe adapté au données
                    this.lRectangle = createRectangleByShift(lshift); // remplissage de la liste de rectangle
                }catch(SQLException ex){
                    
                }
                repaint(); // appel de la fonction paintComponent
            break;
            case Algo2:
                try{ // idem mais algorithme différent
                    Solution sol = requeteDeliver2i.getSolutionbyInstance(instance.getId(),"Algo2");
                    if(sol == null){
                        Algo2 algo2 = new Algo2(instance);
                        algo2.ordonnancer();
                        sol = algo2.getSolution();
                        sol.ajouterEnBase(em);
                    }
                    List<Shift> lshift = requeteDeliver2i.getShiftbySolution(sol.getId());
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
                try{ // idem mais algorithme différent
                    Solution sol = requeteDeliver2i.getSolutionbyInstance(instance.getId(),"Algo3");
                    if(sol == null){
                        Algo3 algo3 = new Algo3(instance);
                        algo3.ordonnancer();
                        sol = algo3.getSolution();
                        sol.ajouterEnBase(em);
                    }
                    List<Shift> lshift = requeteDeliver2i.getShiftbySolution(sol.getId());
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
                jLabel1.setText(""); // pas de prix donc text vide
                afficherDefault(); // affichage par défaut
                repaint(); // appel de la fonction paintComponent
            break;
        }
    }
    
    /**
     * Fonction renvoyant une liste de rectangle ( des rectangle vert pour les 
     * tournées et des rectangles rouges pour des temps morts) à partir d'une
     * liste de shift.
     * @param lshift
     * @return 
     */
    private List<Rectangle> createRectangleByShift(List<Shift> lshift){
        List<Rectangle> lRect = new ArrayList<>(); // initialisation d'une liste de rectangle
        for(int i=0;i < lshift.size();i++){ //parcours de la liste
            try {
                List<Tournee> lTournee = requeteDeliver2i.getTourneeByShift(lshift.get(i).getId()); // récupération de la liste de Tournee triée par date de debut croissante
                int min = Math.toIntExact(lTournee.get(0).getDebut().getTime()/60000); // création variable min initialisé à la valeur de début de la première tournée
                int max = 0;
                for (int j = 0; j < lTournee.size(); j++) { // parcours des tournées
                    long diff_milli = (lTournee.get(j).getFin().getTime()-lTournee.get(j).getDebut().getTime()); //difference entre le debut et la fin de la tournée en milliseconde
                    int diff_min = Math.toIntExact(diff_milli/60000); //passage en minutes
                    long deb = (Math.toIntExact(lTournee.get(j).getDebut().getTime()/60000)-graphe.getDebut()); //difference entre le debut de la tournée et le debut de la première tournée de l'instance
                    int deb_min = Math.toIntExact(deb); //passage en int
                    Point origine = new Point(graphe.getOrigine().getX()+deb_min*graphe.getWidth(),
                            graphe.getOrigine().getY()-(i+1)*graphe.getHeight()); // création du point d'origine du rectangle
                    //x = debut du graphe + diff de but de tournéee et de la première tournée de l'instance ( en minutes ) * le ratio pixel/minute du graphe
                    //y = le numéro du shift + 1 * le ratio pixel/shift du graphe
                    Rectangle rect = new Rectangle(Color.GREEN,origine,diff_min*graphe.getWidth(),graphe.getHeight());
                    //création du rectangle à partir de son point d'origine
                    //sa longueur = différence entre debut et fin de la tournée * le ratio pixel/minutes
                    //sa heuteur = ratio pixel/shift
                    lRect.add(rect); // ajout du rectangle dans la liste
                    /*
                    if(min > Math.toIntExact(lTournee.get(j).getDebut().getTime()/60000)) // recherche du debut de la première tournée
                        min = Math.toIntExact(lTournee.get(j).getDebut().getTime()/60000);
                    
                    if(max < Math.toIntExact(lTournee.get(j).getFin().getTime()/60000)) // recherche de la fin de la dernière tournée
                        max = Math.toIntExact(lTournee.get(j).getFin().getTime()/60000);*/
                    if(j+1 < lTournee.size()){ // création du temps mort entre la tournée actuelle et la suivante si existe
                        origine = new Point(graphe.getOrigine().getX()+
                            (Math.toIntExact(lTournee.get(j).getFin().getTime()/60000-graphe.getDebut()))*graphe.getWidth(),
                            graphe.getOrigine().getY()-(i+1)*graphe.getHeight());
                        rect = new Rectangle(Color.RED,origine,
                            (Math.toIntExact(lTournee.get(j+1).getDebut().getTime()-
                            Math.toIntExact(lTournee.get(j).getFin().getTime()))/60000)*graphe.getWidth(),graphe.getHeight());
                        lRect.add(rect);
                    }else{
                        max = Math.toIntExact(lTournee.get(j).getFin().getTime()/60000); // max prend la valeur de fin de la dernière tournée
                    }
                }
                if(max-min < instance.getDureeMin()){ // si le shift est plus court que la durée minimal
                    Point origine = new Point(graphe.getOrigine().getX()+(max-graphe.getDebut())*graphe.getWidth(),
                            graphe.getOrigine().getY()-(i+1)*graphe.getHeight());
                    Rectangle rect = new Rectangle(Color.RED,origine,(instance.getDureeMin()-(max-min))*graphe.getWidth(),graphe.getHeight());
                    lRect.add(rect); // ajout d'un rectangle en fin de shift représentant le temps mort jusqu'à la durée minmal
                }
            } catch (SQLException ex) {
                
            }
        }
        return lRect;
    }    
    
    /**
     * Fonction qui initialise la connexion à la base de données
     */
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
