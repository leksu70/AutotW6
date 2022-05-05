package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.Auto;
import model.dao.Dao;

@WebServlet("/autot/*")  // Endpoint ja lis‰ksi kansiot mukaan t‰hdell‰
public class Autot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Autot() {
        super();
        System.out.println("Autot.Autot()");
    }

    // GET /autot/{hakusana}
	// GET /autot/haeyksi/rekno
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doGet()");
		
		// Haetaan kutsun polkutiedot, esim. /autot/audi
		String pathInfo = request.getPathInfo();
		System.out.println("polku: " + pathInfo);
				
		// Daon m‰‰ritykset
		Dao dao = new Dao();
		ArrayList<Auto> autot;
		//System.out.println(autot);
		
		// Muutetaan JSONiksi
		String strJSON = "";
		if (pathInfo == null) { // Haetaan kaikki autot
			autot = dao.listaaKaikki();
			strJSON = new JSONObject().put("autot", autot).toString();
		} else if (pathInfo.indexOf("haeyksi") != -1) { // Polussa on sana "haeyksi", eli haetaan yhden auton tiedot
			String rekno = pathInfo.replace("/haeyksi/", ""); // Poistetaan polusta "/haeyksi/", j‰ljelle j‰‰ rekno
			Auto auto = dao.etsiAuto(rekno);
			if (auto == null) {  // Etsitt‰v‰‰ reknoa ei lˆydy
				strJSON = "{}";
			} else {
				JSONObject JSON = new JSONObject();
				JSON.put("merkki", auto.getMerkki());
				JSON.put("malli", auto.getMalli());
				JSON.put("vuosi", auto.getVuosi());
				JSON.put("rekno", auto.getRekno());
				strJSON = JSON.toString();
			}
		} else { // Haetaan hakusanan mukaiset autot
			String hakusana = pathInfo.replace("/", "");
			autot = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("autot", autot).toString();
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}

	// Lis‰‰
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPost()");
		// Muutetaan kutsun mukana tuleva json-string json-objektiksi:
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		Auto auto = new Auto();
		auto.setRekno(jsonObj.getString("rekNo"));
		auto.setMerkki(jsonObj.getString("merkki"));
		auto.setMalli(jsonObj.getString("malli"));
		auto.setVuosi(jsonObj.getInt("vuosi"));
		System.out.println(auto);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		Dao dao = new Dao();
		if (dao.lisaaAuto(auto)) { // Metodi palauttaa true/false
			out.println("{\"response\":1}"); // Lis‰‰minen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}"); // Lis‰‰minen ep‰onnistui {"response":0}
		}
		
	}

	// Muuta
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPut()");

		// Muutetaan kutsun mukana tuleva json-string json-objektiksi:
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		String vanharekno = jsonObj.getString("vanharekno");
		System.out.println("vanharekno:" + vanharekno);

		Auto auto = new Auto();
		auto.setRekno(jsonObj.getString("rekNo"));
		auto.setMerkki(jsonObj.getString("merkki"));
		auto.setMalli(jsonObj.getString("malli"));
		auto.setVuosi(jsonObj.getInt("vuosi"));
		System.out.println(auto);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		Dao dao = new Dao();
		if (dao.muutaAuto(auto, vanharekno)) { // Metodi palauttaa true/false
			out.println("{\"response\":1}"); // Muuttaminen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}"); // Muuttaminen ep‰onnistui {"response":0}
		}

	}

	// Poista
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doDelete()");
		String pathInfo = request.getPathInfo();  // Haetaan kutsun polkutiedot, esim. /ABC-123
		System.out.println("polku: " + pathInfo);
		String poistettavaRekno = pathInfo.replace("/", "");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();
		if (dao.poistaAuto(poistettavaRekno)) { // Metodi palauttaa true/false
			out.println("{\"response\":1}"); // Poistaminen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}"); // Poistaminen ep‰onnistui {"response":0}
		}
	}

}
