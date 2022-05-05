package model.dao;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    	//path =  new File(System.getProperty("user.dir")).getParentFile().toString() +"\\"; //Testauksessa
    	//System.out.println("testaushakemisto:" + System.getProperty("user.dir"));
    	//path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus epäonnistui.");
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
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
	
	public boolean poistaAuto(String rekNo) { 
		// Oikeassa elämässä tiedot ensisijaisesti merkitään poistetuksi
		boolean paluuArvo = true;
		sql="DELETE FROM autot WHERE rekNo=?";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setString(1, rekNo);
			stmtPrep.executeUpdate();
			con.close();
		} catch (Exception e) { // Oli esimerkissä Exception. Miksi??
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
	
	public Auto etsiAuto(String rekno) {
		Auto auto = null;
		sql = "SELECT * FROM autot WHERE rekNo=?";
		try {
			con=yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, rekno);
				rs = stmtPrep.executeQuery();
				if (rs.isBeforeFirst()) {  // Jos kysely tuotti dataa, eli rekno on käytössä
					rs.next();
					auto = new Auto();
					auto.setRekno(rs.getString(1));
					auto.setMerkki(rs.getString(2));
					auto.setMalli(rs.getString(3));
					auto.setVuosi(rs.getInt(4));
				}
			}
			con.close();
		} catch (Exception e) {  // Oli esimerkissä Exception. Miksi??
			e.printStackTrace();
		}
		
		return auto;
	}
	
	public boolean muutaAuto(Auto auto, String rekNo) {
		boolean paluuArvo = true;
		sql = "UPDATE autot SET rekNo=?, merkki=?, malli=?, vuosi=? WHERE rekNo=?";
		try {
			con=yhdista();
			stmtPrep = con.prepareStatement(sql);
			stmtPrep.setString(1, auto.getRekno());
			stmtPrep.setString(2, auto.getMerkki());
			stmtPrep.setString(3, auto.getMalli());
			stmtPrep.setInt(4,  auto.getVuosi());
			stmtPrep.setString(5, rekNo);
			stmtPrep.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
	
	public boolean poistaKaikkiAutot(String pwd) {
		boolean paluuArvo = true;
		if(pwd != "nimda") {
			return false;
		}
		sql="DELETE FROM autot";
		try {
			con=yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
}
