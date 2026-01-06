package product;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ovchipkaart.OVChipkaart;
import ovchipkaartproduct.OVChipkaartProduct;

public class ProductDAOHibernate implements ProductDAO {
    SessionFactory sessionFactory;

    public ProductDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean save(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(product);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[ProductDAOHibernate.save] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Updating method for managed entities, as merge tried to insert on some occasions and we're using Hibernate 7.x
            Product existingProduct = session.find(Product.class, product.getProductNummer());
            existingProduct.setNaam(product.getNaam());
            existingProduct.setBeschrijving(product.getBeschrijving());
            existingProduct.setPrijs(product.getPrijs());
            existingProduct.getOVChipkaartRelaties().clear();
            
            List<OVChipkaartProduct> relations = session.createQuery("select relation from OVChipkaartProduct relation where relation.product = :product", OVChipkaartProduct.class)
                .setParameter("product", existingProduct)
                .getResultList();

            for (OVChipkaartProduct r : relations) {
                existingProduct.addOVChipkaart(r.getOvChipkaart());
            }
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[ProductDAOHibernate.update] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(product);
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            System.err.println("[ProductDAOHibernate.delete] " + e.getMessage());
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
    }

    @Override
    public Product findByProductNummer(Integer productNummer) {
        Session session = sessionFactory.openSession();
        try {
            Product product = session.find(Product.class, productNummer);

            Hibernate.initialize(product.getOVChipkaartRelaties());

            session.close();
            return product;
        } catch (Exception e) {
            System.err.println("[ProductDAOHibernate.findByProductNummer] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        Session session = sessionFactory.openSession();
        try {
            List<Product> producten = session.createQuery("from Product p JOIN p.ovChipkaartRelaties ocr WHERE ocr.ovChipkaart.kaartNummer = :ovChipkaartNummer", Product.class)
                .setParameter("ovChipkaartNummer", ovChipkaart.getKaartNummer())
                .getResultList();

            for (Product p : producten) {
                Hibernate.initialize(p.getOVChipkaartRelaties());
            }

            session.close();
            return producten;
        } catch (Exception e) {
            System.err.println("[ProductDAOHibernate.findByOVChipkaart] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    @Override
    public List<Product> findAll() {
        Session session = sessionFactory.openSession();
        try {
            List<Product> products = session.createQuery("from Product", Product.class).getResultList();
            
            for (Product p : products) {
                Hibernate.initialize(p.getOVChipkaartRelaties());
            }
            
            session.close();
            return products;
        } catch (Exception e) {
            System.err.println("[ProductDAOHibernate.findAll] " + e.getMessage());
            e.printStackTrace();
            session.close();
            return null;
        }
    }
}
