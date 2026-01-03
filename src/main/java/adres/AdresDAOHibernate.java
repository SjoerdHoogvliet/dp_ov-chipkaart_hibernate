package adres;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import reiziger.Reiziger;

public class AdresDAOHibernate implements AdresDAO {
    SessionFactory sessionFactory;

    public AdresDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean save(Adres adres) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(adres);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[AdresDAOHibernate.save] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(adres);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[AdresDAOHibernate.update] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(adres);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[AdresDAOHibernate.delete] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public Adres findById(Integer adres_id) {
        Session session = sessionFactory.openSession();
        try {
            Adres adres = session.find(Adres.class, adres_id);
            session.close();
            return adres;
        } catch (Exception e) {
            System.err.println("[AdresDAOHibernate.findById] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Session session = sessionFactory.openSession();
        try {
            Adres adres = session.createQuery("from Adres where reiziger = :reiziger", Adres.class)
                .setParameter("reiziger", reiziger)
                .getSingleResult();

            session.close();
            return adres;
        } catch (Exception e) {
            System.err.println("[AdresDAOHibernate.findByReiziger] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        Session session = sessionFactory.openSession();
        try {
            List<Adres> adressen = session.createQuery("from Adres", Adres.class).getResultList();
            session.close();
            return adressen;
        } catch (Exception e) {
            System.err.println("[AdresDAOHibernate.findAll] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }
}
