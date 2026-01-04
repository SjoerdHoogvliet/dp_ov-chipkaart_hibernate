package reiziger;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import adres.AdresDAO;

public class ReizigerDAOHibernate implements ReizigerDAO {
    SessionFactory sessionFactory;
    AdresDAO adresDAO;

    public ReizigerDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void setAdresDAO(AdresDAO adresDAO) {
        this.adresDAO = adresDAO;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(reiziger);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[ReizigerDAOHibernate.save] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(reiziger);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[ReizigerDAOHibernate.update] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(reiziger);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[ReizigerDAOHibernate.delete] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public Reiziger findById(Integer reiziger_id) {
        Session session = sessionFactory.openSession();
        try {
            Reiziger reiziger = session.find(Reiziger.class, reiziger_id);
            session.close();

            reiziger.setAdres(adresDAO.findByReiziger(reiziger));;

            return reiziger;
        } catch (Exception e) {
            System.err.println("[ReizigerDAOHibernate.findById] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(LocalDate geboortedatum) {
        Session session = sessionFactory.openSession();
        try {
            List<Reiziger> reizigers = session.createQuery("from Reiziger where geboortedatum = :geboortedatum", Reiziger.class)
                .setParameter("geboortedatum", geboortedatum)
                .getResultList();

            session.close();

            for (Reiziger r : reizigers) {
                r.setAdres(adresDAO.findByReiziger(r));
            }

            return reizigers;
        } catch (Exception e) {
            System.err.println("[ReizigerDAOHibernate.findByGbdatum] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        Session session = sessionFactory.openSession();
        try {
            List<Reiziger> reizigers = session.createQuery("from Reiziger", Reiziger.class).getResultList();
            session.close();
            
            for (Reiziger r : reizigers) {
                r.setAdres(adresDAO.findByReiziger(r));
            }

            return reizigers;
        } catch (Exception e) {
            System.err.println("[ReizigerDAOHibernate.findAll] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }
}
