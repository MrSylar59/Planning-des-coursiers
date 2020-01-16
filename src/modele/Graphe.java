/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Color;
import java.awt.Graphics;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author cyril
 */
public class Graphe {
    private Point origine;
    private Droite abscisse;
    private Droite ordonnee;
    private int debut;
    private int height;
    private int width;
    
    public Graphe(Point haut_gauche, Point bas_droite,int heure_min,int heure_max,int nb_shift){
        origine = new Point(haut_gauche.getX(),bas_droite.getY());
        abscisse = new Droite(Color.black, haut_gauche,origine);
        ordonnee = new Droite(Color.black, bas_droite,origine);
        this.debut = heure_min;
        this.height = (bas_droite.getY()-haut_gauche.getY())/nb_shift;
        long difference = (heure_max-heure_min);
        this.width= (bas_droite.getX()-haut_gauche.getX())/(Math.toIntExact(difference));
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Point getOrigine() {
        return origine;
    }
    
    public int getDebut(){
        return debut;
    }

    public Droite getAbscisse() {
        return abscisse;
    }

    public Droite getOrdonnee() {
        return ordonnee;
    }
    
    
    
    public void seDessiner(Graphics g){
        abscisse.seDessiner(g);
        ordonnee.seDessiner(g);
    }
    
}
