/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 * Classe permettant de créer des Rectangle graphiquement
 * @author cyril
 */
import java.awt.Color;
import java.awt.Graphics;

public class Rectangle {
    /**
     * PARAMETRES
     */
    /**
     * Point haut gauche du rectangle
     */
    private Point pHautGauche;
    /**
     * La hauteur du rectangle
     */
    private int height;
    /**
     * La longueur du rectangle
     */
    private int width;
    /**
     * La couleur du rectangle
     */
    private Color couleur;

    /**
     * CONSTRUCTEURS
     */
    
    public Rectangle(Color couleur,Point pInit,int width, int height){
        this.couleur = couleur;
        this.pHautGauche = pInit;
        this.width = width;
        this.height = height;
    }

    /**
     * GETTEURS
     */
    
    public Point getpHautGauche() {
        return pHautGauche;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    /**
     * METHODE
     */
    /**
     * Fonction qui dessine un rectangle dans la zone graphique g en fonction
     * des paramètres de l'object rectangle
     * @param g 
     */
    public void seDessiner(Graphics g){
        g.setColor(couleur);
        g.fillRect(pHautGauche.getX(), pHautGauche.getY(), width, height);
    }
}
