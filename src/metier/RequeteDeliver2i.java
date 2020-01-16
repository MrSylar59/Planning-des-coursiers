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
import java.util.List;
import modele.Instance;
import modele.Tournee;
import modele.Shift;
import modele.Solution;


/**
 *
 * @author cyril
 */
public class RequeteDeliver2i {
    private Connection connection;
    private static RequeteDeliver2i instance;
            
    private RequeteDeliver2i() throws ClassNotFoundException, SQLException{
        connect();
    }
    
    public static RequeteDeliver2i getInstance() throws ClassNotFoundException, SQLException{
        if(instance == null){
            instance = new RequeteDeliver2i();
        }
        return instance;
    }
    
    
    private void connect()throws ClassNotFoundException, SQLException{
        
        String driverClass = "org.apache.derby.jdbc.ClientDriver";
        String urlDatabase = "jdbc:derby://localhost:1527/Deliver2iBDD";
        String user = "sql123";
        String pwd = "sql123";
        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(urlDatabase, user, pwd);        
        this.connection = conn;
    }

    public Connection getConnection() {
        return connection;
    }
    
    public List<Instance> getInstanceList() throws SQLException{
        List<Instance> lInstance = new ArrayList<>();
        String requete = "SELECT * FROM Instance";
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(requete);
        while(result.next()){
            lInstance.add(new Instance(result.getLong("ID"),result.getString("NOM"), result.getInt("DUR_MIN"),result.getInt("DUR_MAX") ,result.getDate("DATE") ));
        }
        return lInstance;
    }
    
    public Tournee getTourneebyId(int id) throws SQLException{
        String requete = "SELECT * FROM Tournee WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setString(1, ""+id+"");
        ResultSet result = stmt.executeQuery();
        Tournee tournee;
        tournee = new Tournee(result.getDate("debut"),result.getDate("fin"));
        return tournee;
    }
    
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
    
    public Solution getSolution(int id, String algo) throws SQLException{
        String requete = "SELECT * FROM Solution WHERE INST_ID = ? AND ALGO = \"?\"";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setInt(1, id);     
        stmt.setString(1, algo);
        ResultSet result = stmt.executeQuery();
        Solution solution = new Solution();//TODO créer sol et return
        return solution;
    }
    
    public Shift getShiftbyId(int id) throws SQLException{ //à end
        /*String requete = "SELECT * FROM Shift WHERE id = %";
        PreparedStatement stmt = connection.prepareStatement(requete);       
        stmt.setString(1, ""+id+"");
        ResultSet result = stmt.executeQuery(requete);
        Shift shift = new Shift();  
        */return null; /*shift;*/
    }
    
    public Solution getSolutionbyId(int id) throws SQLException{
        return null;
    }
}
