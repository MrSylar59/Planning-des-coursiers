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

public class Rectangle {
    private Point pHautGauche;
    private int height;
    private int width;
    private Color couleur;

    public Rectangle(Color couleur,Point pInit,int width, int height){
        this.couleur = couleur;
        this.pHautGauche = pInit;
        this.width = width;
        this.height = height;
    }

    public Point getpHautGauche() {
        return pHautGauche;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    

    public void seDessiner(Graphics g){
            g.drawRect(pHautGauche.getX(), pHautGauche.getY(), width, height);
    }
}
