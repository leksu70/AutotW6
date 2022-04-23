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

@WebServlet("/autot")  // Endpoint
public class Autot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Autot() {
        super();
        System.out.println("Autot.Autot()");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Autot.doGet()");
		
		// Daon m‰‰ritykset
		Dao dao = new Dao();
		ArrayList<Auto> autot = dao.listaaKaikki();
		System.out.println(autot);
		
		// Muutetaan JSONiksi
		String strJSON = new JSONObject().put("autot", autot).toString();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPost()");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doPut()");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Autot.doDelete()");
	}

}
