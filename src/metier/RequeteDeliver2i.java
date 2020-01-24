/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import modele.Instance;
import modele.Tournee;
import modele.Shift;
import modele.Solution;


/**
 * Classe permettant de communiquer avec la base de données
 * @author cyril
 */
public class RequeteDeliver2i {
    /**
     * PARAMETRES
     */
    /**
     * Object contenant les informations de connexion à la base de données
     */
    private Connection connection;
    /**
     * Instance de connexion à la base de données
     */
    private static RequeteDeliver2i instance;
       
    /**
     * CONSTRUCTEUR
     */
    
    private RequeteDeliver2i() throws ClassNotFoundException, SQLException{
        connect();
    }
    /**
     * METHODES
     */
    /**
     * Fonction qui renvoie l'instance actuel ou en recréer une puis l'envoie
     * @return 
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static RequeteDeliver2i getInstance() throws ClassNotFoundException, SQLException{
        if(instance == null){
            instance = new RequeteDeliver2i();
        }
        return instance;
    }
    
    /**
     * Fonction de connexion à la base
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private void connect()throws ClassNotFoundException, SQLException{
        
        String driverClass = "org.apache.derby.jdbc.ClientDriver";
        String urlDatabase = "jdbc:derby://localhost:1527/Deliver2iBDD";
        String user = "sql123";
        String pwd = "sql123";
        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(urlDatabase, user, pwd);        
        this.connection = conn;
    }
    
    /**
     * Fonction qui renvoie la liste des Instances présente dans la base
     * @return List d'Instance
     * @throws SQLException 
     */
    public List<Instance> getInstanceList() throws SQLException{
        List<Instance> lInstance = new ArrayList<>();
        String requete = "SELECT * FROM Instance";
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(requete);
        while(result.next()){
            Instance inst = new Instance(result.getLong("ID"),result.getString("NOM"),
                    result.getInt("DUR_MIN"),result.getInt("DUR_MAX") ,result.getDate("DATE"));
            requete = "SELECT * FROM Tournee WHERE INST_ID = ?";
            PreparedStatement psmt = connection.prepareStatement(requete);       
            psmt.setLong(1, inst.getId());
            ResultSet rs = psmt.executeQuery();
            while(rs.next()){
                Tournee t = new Tournee(rs.getInt("ID"),rs.getTime("DATE_DEBUT"),rs.getTime("DATE_FIN"),inst);
                inst.AjouterTournee(t);
            }
            lInstance.add(inst);
        }
        return lInstance;
    }
    
    /**
     * Fonction qui renvoie la liste des tournées présentes dans un shift par ordre de date de début
     * @param id
     * @return 
     * @throws SQLException 
     */
    public List<Tournee> getTourneeByShift(long id) throws SQLException{
        String requete = "SELECT t.date_debut as deb, t.date_fin as fin FROM Tournee as t, TOURNEE_SHIFT as ts WHERE t.id = ts.tournee_id AND ts.shift_id = ? ORDER BY t.date_debut ASC";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, id);
        ResultSet result = stmt.executeQuery();
        List<Tournee> ltournee = new ArrayList<>();
        while(result.next()){
            ltournee.add(new Tournee(result.getTime("DEB"),result.getTime("FIN")));
        }
        return ltournee;
    }
    
    /**
     * Fonction qui renvoie la liste des tournées d'une instance
     * @param id
     * @return
     * @throws SQLException 
     */
    public List<Tournee> getTourneebyInstance(Long id) throws SQLException{
        List<Tournee> lTournee = new ArrayList<>();
        String requete = "SELECT * FROM Tournee WHERE INST_ID = ?";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, id);
        ResultSet result = stmt.executeQuery();
        while(result.next()){
            lTournee.add(new Tournee(result.getTime("DATE_DEBUT"), result.getTime("DATE_FIN")));
        }
        return lTournee;
    }
    
    /**
     * Fonction qui renvoie un hashset contenant les tournées d'un instance 
     * @param id
     * @return
     * @throws SQLException 
     */
    public HashSet<Tournee> getTournees(Long id) throws SQLException{
        HashSet<Tournee> lTournee = new HashSet<>();
        String requete = "SELECT * FROM Tournee WHERE INST_ID = ?";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, id);
        ResultSet result = stmt.executeQuery();
        while(result.next()){
            lTournee.add(new Tournee(result.getTime("DATE_DEBUT"), result.getTime("DATE_FIN")));
        }
        return lTournee;
    }
    
    /**
     * Fonction qui renvoie la liste des tournées d'un instance triée par date de début
     * @param id
     * @return
     * @throws SQLException 
     */
    public List<Tournee> getTourneeOrder(Long id) throws SQLException{
        List<Tournee> lTournee = new ArrayList<>();
        String requete = "SELECT * FROM Tournee WHERE INST_ID = ? ORDER BY date_debut";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, id);
        ResultSet result = stmt.executeQuery();
        while(result.next()){
            lTournee.add(new Tournee(result.getTime("DATE_DEBUT"), result.getTime("DATE_FIN")));
        }
        return lTournee;
    }
    
    /**
     * Fonction qui renvoie la solution à partir de l'identifiant de l'instance
     * et du code de l'algorithme
     * @param id
     * @param algo
     * @return
     * @throws SQLException 
     */
    public Solution getSolutionbyInstance(long id,String algo) throws SQLException{
        String requete = "SELECT * FROM Solution WHERE INST_ID = ? AND ALGO = ?";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, id);     
        stmt.setString(2, algo);
        ResultSet result = stmt.executeQuery();
        if( result.next()){
            Solution sol = new Solution(result.getLong("ID"),result.getDouble("PRIX"),result.getString("ALGO"));
            return sol;
        }else{
            return null;
        }
    }
    
    /**
     * Fonction qui renvoie la liste des shift d'un solution à partir de
     * l'identifiant de la solution
     * @param id_sol
     * @return
     * @throws SQLException 
     */
    public List<Shift> getShiftbySolution(long id_sol) throws SQLException{
        List<Shift> lShift = new ArrayList<>();
        String requete = "SELECT * FROM Shift WHERE SOL_ID = ?";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, id_sol);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            lShift.add(new Shift(rs.getLong("ID"),rs.getInt("DUREE"),rs.getInt("TEMPS_MORT"),rs.getDouble("PRIX")));
        }
        return lShift;
    }
    
    /**
     * Fonction qui renvoie la tournée ayant la plus petite date de début dans 
     * l'instance à partir de l'identifiant de l'instance
     * @param inst
     * @return
     * @throws SQLException 
     */
    public int getMinDebutInstance(Instance inst) throws SQLException{
        String requete = "SELECT MIN(DATE_DEBUT) as DEB FROM Tournee WHERE INST_ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, inst.getId());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            int debut = Math.toIntExact(rs.getTime("DEB").getTime()/60000);
            return debut;
        }else{
            return 0;
        }
    }
    
    /**
     * Fonction qui renvoie la tournée ayant la plus grande date de fin dans 
     * l'instance à partir de l'identifiant de l'instance
     * @param inst
     * @return
     * @throws SQLException 
     */
    public int getMaxFinInstance(Instance inst) throws SQLException{
        String requete = "SELECT MAX(DATE_FIN) as FIN FROM Tournee WHERE INST_ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setLong(1, inst.getId());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            int fin = Math.toIntExact(rs.getTime("FIN").getTime()/60000);
            return fin;
        }else{
            return 0;
        }
    }
    
    
    /**
     * GETTEURS
     */
    public Connection getConnection() {
        return connection;
    }
}
