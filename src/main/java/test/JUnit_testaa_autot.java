package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import model.Auto;
import model.dao.Dao;

@TestMethodOrder(OrderAnnotation.class)
class JUnit_testaa_autot {

	@Test
	@Order(1)
	public void testPoistaKaikkiAutot() {
		// Poistetaan kaikki autot
		Dao dao = new Dao();
		dao.poistaKaikkiAutot("nimda");
		ArrayList<Auto> autot = dao.listaaKaikki();
		assertEquals(0, autot.size());
	}
	
	@Test
	@Order(2)
	public void testLisaaAuto() {
		// Luodaan muutama uusi testiauto
		Dao dao = new Dao();
		Auto auto_1 = new Auto("AAA-111", "Honda", "Civic", 2015);
		Auto auto_2 = new Auto("BBB-222", "Fiat", "Scudo", 2016);
		Auto auto_3 = new Auto("CCC-333", "BMW", "330i", 2017);
		Auto auto_4 = new Auto("DDD-111", "Audi", "A4 Quattro 3.2", 2018);
		assertEquals(true, dao.lisaaAuto(auto_1));
		assertEquals(true, dao.lisaaAuto(auto_2));
		assertEquals(true, dao.lisaaAuto(auto_3));
		assertEquals(true, dao.lisaaAuto(auto_4));
		
	}

	@Test
	@Order(3)
	public void testMuutaAuto() {
		// Muutetaan yhtä autoa
		Dao dao = new Dao();
		Auto muutettava = dao.etsiAuto("AAA-111");
		muutettava.setRekno("A-1");
		muutettava.setMerkki("Ford");
		muutettava.setMalli("Focus");
		muutettava.setVuosi(2016);
		dao.muutaAuto(muutettava,  "AAA-111");
		
		assertEquals("A-1", dao.etsiAuto("A-1").getRekno());
		assertEquals("Ford", dao.etsiAuto("A-1").getMerkki());
		assertEquals("Focus", dao.etsiAuto("A-1").getMalli());
		assertEquals(2016, dao.etsiAuto("A-1").getVuosi());
	}
	
	@Test
	@Order(4)
	public void testPoistaAuto() {
		Dao dao = new Dao();
		dao.poistaAuto("A-1");
		assertEquals(null, dao.etsiAuto("A-1"));
	}
}
