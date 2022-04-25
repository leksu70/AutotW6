package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Auto;

public class Dao {
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Autot.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");    	
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); //Eclipsessa
    	//path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus ep‰onnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public ArrayList<Auto> listaaKaikki(){
		ArrayList<Auto> autot = new ArrayList<Auto>();
		System.out.println("Dao.listaaKaikki()");
		sql = "SELECT * FROM autot";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					//con.close();					
					while(rs.next()){
						Auto auto = new Auto();
						auto.setRekno(rs.getString(1));
						auto.setMerkki(rs.getString(2));
						auto.setMalli(rs.getString(3));	
						auto.setVuosi(rs.getInt(4));	
						autot.add(auto);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return autot;
	}
	
	public ArrayList<Auto> listaaKaikki(String hakusana){
		ArrayList<Auto> autot = new ArrayList<Auto>();
		System.out.println("Dao.listaaKaikki(\"" + hakusana + "\")");
		sql = "SELECT * FROM autot WHERE rekno LIKE ? OR merkki LIKE ? OR malli LIKE ?";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");
				stmtPrep.setString(3, "%" + hakusana + "%");
				
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					//con.close();					
					while(rs.next()){
						Auto auto = new Auto();
						auto.setRekno(rs.getString(1));
						auto.setMerkki(rs.getString(2));
						auto.setMalli(rs.getString(3));	
						auto.setVuosi(rs.getInt(4));	
						autot.add(auto);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return autot;
	}
	
	public boolean lisaaAuto(Auto auto) {
		boolean paluuArvo = true;
		System.out.println("Dao.lisaaAuto()");
		sql="INSERT INTO autot VALUES(?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setString(1, auto.getRekno());
			stmtPrep.setString(2, auto.getMerkki());
			stmtPrep.setString(3, auto.getMalli());
			stmtPrep.setInt(4, auto.getVuosi());
			stmtPrep.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
	
	public boolean poistaAuto(String rekNo) { 
		// Oikeassa el‰m‰ss‰ tiedot ensisijaisesti merkit‰‰n poistetuksi
		boolean paluuArvo = true;
		sql="DELETE FROM autot WHERE rekNo=?";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setString(1, rekNo);
			stmtPrep.executeUpdate();
			con.close();
		} catch (SQLException e) { // Oli esimerkiss‰ Exception.
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
}
