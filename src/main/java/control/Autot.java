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

@WebServlet("/autot/*")  // Endpoint ja lis�ksi kansiot mukaan t�hdell�
public class Autot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Autot() {
        super();
        System.out.println("Autot.Autot()");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Autot.doGet()");
		
		// Haetaan kutsun polkutiedot, esim. /autot/audi
		String pathInfo = request.getPathInfo();
		System.out.println("polku: " + pathInfo);
		String hakusana = "";
		if (pathInfo != null) {
			hakusana = pathInfo.replace("/", "");
		}
		
		// Daon m��ritykset
		Dao dao = new Dao();
		ArrayList<Auto> autot = dao.listaaKaikki(hakusana);
		System.out.println(autot);
		
		// Muutetaan JSONiksi
		String strJSON = new JSONObject().put("autot", autot).toString();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}

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
			out.println("{\"response\":1}"); // Lis��minen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}"); // Lis��minen ep�onnistui {"response":0}
		}
		
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPut()");
	}

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
			out.println("{\"response\":0}"); // Poistaminen ep�onnistui {"response":0}
		}
	}

}
