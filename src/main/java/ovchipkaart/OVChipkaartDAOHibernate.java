package ovchipkaart;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import reiziger.Reiziger;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    SessionFactory sessionFactory;

    public OVChipkaartDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean save(OVChipkaart ovchipkaart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(ovchipkaart);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.save] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovchipkaart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(ovchipkaart);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.update] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovchipkaart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(ovchipkaart);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.delete] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public OVChipkaart findByKaartNummer(Integer kaartNummer) {
        Session session = sessionFactory.openSession();
        try {
            OVChipkaart ovchipkaart = session.find(OVChipkaart.class, kaartNummer);
            session.close();
            return ovchipkaart;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.findByKaartNummer] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        Session session = sessionFactory.openSession();
        try {
            List<OVChipkaart> ovchipkaarts = session.createQuery("from OVChipkaart where reiziger = :reiziger", OVChipkaart.class)
                .setParameter("reiziger", reiziger)
                .getResultList();

            session.close();
            return ovchipkaarts;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.findByReiziger] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {
        Session session = sessionFactory.openSession();
        try {
            List<OVChipkaart> ovchipkaarts = session.createQuery("from OVChipkaart", OVChipkaart.class).getResultList();
            session.close();
            return ovchipkaarts;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.findAll] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }
}
