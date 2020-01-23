/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 * Classe permettant de céer des Points
 * @author cyril
 */
public class Point {
    /**
     * PARAMETRES
     */
    /**
     * Coordonée en x
     */
    private int x;
    /**
     * Coordonée en y
     */
    private int y;

    /**
     * CONSTRUCTEUR
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(Point p){
        this.x = p.x;
        this.y = p.y;
    }
    
    /**
     * GETTEURS
     */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "POINT[ x = "+ this.x +", y = "+this.y+"]";
    }

    public static void main(String[] args){
        Point point = new Point(1,2);
        Point point2 = new Point(0,8);
        Point point3 = new Point(point);
        System.out.println("point 1 "+point);
        System.out.println("point 2 "+point2);
        System.out.println("point 3 "+point3);
    }
}
