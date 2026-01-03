import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import reiziger.Reiziger;
import reiziger.ReizigerDAO;
import reiziger.ReizigerDAOHibernate;


public class Main {
    public static void main(String[] args) {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    
            ReizigerDAO reizigerDAO = new ReizigerDAOHibernate(sessionFactory);
    
            testReizigerDAO(reizigerDAO);
        } catch (Exception e) {
            System.err.println("[Main] " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        // Slightly changed test code because I use LocalDate instead of java.sql.Date
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", LocalDate.parse(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update test door het sietske object aan te passen en opnieuw te persisteren.
        sietske.setTussenvoegsel("gewijzigd");
        System.out.println("[Test] Oud Sietske object: " + rdao.findById(77));
        rdao.update(sietske);
        System.out.println("[Test] Nieuw Sietske object: " + rdao.findById(77));

        // Haal alle reizigers op uit de database die geboren zijn op 1981-03-14
        List<Reiziger> reizigersFromGbdatum = rdao.findByGbdatum(LocalDate.parse(gbdatum));
        System.out.println("[Test] ReizigerDAO.findByGbdatum() geeft de volgende reizigers:");
        for (Reiziger r : reizigersFromGbdatum) {
            System.out.println(r);
        }
        System.out.println();

        // Verwijder sietske uit de database
        System.out.println("[Test] Alle reizigers voor delete: ");
        reizigers = rdao.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();
        rdao.delete(sietske);
        System.out.println("[Test] Alle reizigers na delete: ");
        reizigers = rdao.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
    }
}