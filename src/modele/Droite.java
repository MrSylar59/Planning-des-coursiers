/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 * Classe permettant de dessiner des droites dans un JPanel
 * @author cyril
 */
import java.awt.Color;
import java.awt.Graphics;

public class Droite{
    /**
     * PARAMETRES
     */
    /**
     * Couleur de la droite
     */
    private Color couleur;
    /**
     * Point de debut
     */
    private Point debut;
    /**
     * Point de fin
     */
    private Point fin;

    /**
     * CONSTRUCTEUR
     */
    public Droite(Color couleur,Point debut,Point fin){
        this.couleur = couleur;
        this.debut = debut;
        this.fin = fin;		
    }

    /**
     * METHODE
     */
    /**
     * Fonction permettant de dessiner la droite
     * @param g 
     */
    public void seDessiner(Graphics g){
        g.drawLine(debut.getX(), debut.getY(), fin.getX(), fin.getY());
    }
    
    /**
     * GETTEURS
     */
    public Point getDebut() {
        return debut;
    }

    public Point getFin() {
        return fin;
    }

    
    
}
