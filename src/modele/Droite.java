/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author cyril
 */
import java.awt.Color;
import java.awt.Graphics;

public class Droite{
    private Color couleur;
    private Point debut;
    private Point fin;

    public Droite(Color couleur,Point debut,Point fin){
        this.couleur = couleur;
        this.debut = debut;
        this.fin = fin;		
    }

    public Point getDebut() {
        return debut;
    }

    public Point getFin() {
        return fin;
    }

    
    
    public void seDessiner(Graphics g){
        g.drawLine(debut.getX(), debut.getY(), fin.getX(), fin.getY());
    }
}
