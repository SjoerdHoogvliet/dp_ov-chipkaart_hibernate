package ovchipkaart;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ovchipkaartproduct.OVChipkaartProduct;
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

    // Update functie waar iets fout gaat met merge wat een update functie moet zijn (hij denkt nu nieuw te moeten inserten)
    @Override
    public boolean update(OVChipkaart ovchipkaart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Updating method for managed entities, as merge tried to insert on some occasions and we're using Hibernate 7.x
            OVChipkaart existingOVChipkaart = session.find(OVChipkaart.class, ovchipkaart.getKaartNummer());
            existingOVChipkaart.setSaldo(ovchipkaart.getSaldo());
            existingOVChipkaart.setGeldigTot(ovchipkaart.getGeldigTot());
            existingOVChipkaart.setKlasse(ovchipkaart.getKlasse());
            existingOVChipkaart.setReiziger(ovchipkaart.getReiziger());
            existingOVChipkaart.getProductRelaties().clear();
            
            List<OVChipkaartProduct> relations = session.createQuery("select relation from OVChipkaartProduct relation where relation.ovChipkaart = :ovChipkaart", OVChipkaartProduct.class)
                .setParameter("ovChipkaart", existingOVChipkaart)
                .getResultList();

            for (OVChipkaartProduct r : relations) {
                existingOVChipkaart.addProduct(r.getProduct());
            }
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

            Hibernate.initialize(ovchipkaart.getProductRelaties());

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
            List<OVChipkaart> ovchipkaarten = session.createQuery("from OVChipkaart where reiziger = :reiziger", OVChipkaart.class)
                .setParameter("reiziger", reiziger)
                .getResultList();

            for (OVChipkaart o : ovchipkaarten) {
                Hibernate.initialize(o.getProductRelaties());
            }

            session.close();
            return ovchipkaarten;
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
            List<OVChipkaart> ovchipkaarten = session.createQuery("from OVChipkaart", OVChipkaart.class).getResultList();

            for (OVChipkaart o : ovchipkaarten) {
                Hibernate.initialize(o.getProductRelaties());
            }

            session.close();
            return ovchipkaarten;
        } catch (Exception e) {
            System.err.println("[OVChipkaartDAOHibernate.findAll] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }
}
