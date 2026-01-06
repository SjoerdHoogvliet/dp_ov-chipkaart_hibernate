package ovchipkaart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ovchipkaartproduct.OVChipkaartProduct;
import product.Product;
import reiziger.Reiziger;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name = "kaart_nummer")
    private Integer kaartNummer;
    @Column(name = "geldig_tot")
    private LocalDate geldigTot;
    private Integer klasse;
    private float saldo;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reiziger_id", nullable = false)
    private Reiziger reiziger;
    @OneToMany(mappedBy = "ovChipkaart", orphanRemoval = true)
    private List<OVChipkaartProduct> productRelaties = new ArrayList<>();

    // JPA required empty constructor
    public OVChipkaart() {}

    public OVChipkaart(Integer kaartNummer, LocalDate geldigTot, Integer klasse, float saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    // TODO: Add something that gives some information about the products
    public String toString() {
        String returnString = "OV Chipkaart #%d: met %.2f euro is geldig tot %s in klasse %d".formatted(
            this.kaartNummer,
            this.saldo,
            this.geldigTot,
            this.klasse
        );

        if(Hibernate.isInitialized(this.productRelaties) && this.productRelaties.size() > 0) {
            returnString += "\n\tHeeft de volgende producten: ";
            for(OVChipkaartProduct p : this.productRelaties) {
                returnString += p.getProduct().getProductNummer() + " ";
            }
        }

        return returnString;
    }

    public void addProduct(Product product) {
        for(OVChipkaartProduct p : this.productRelaties) {
            if(p.getProduct().getProductNummer() == product.getProductNummer()) {
                return;
            }
        }
        this.productRelaties.add(new OVChipkaartProduct(this, product));
    }

    public void removeProduct(Product product) {
        productRelaties.removeIf(okp -> {
            boolean match = okp.getProduct().equals(product);
            if (match) {
                okp.getProduct().getOVChipkaartRelaties().remove(okp);
                this.productRelaties.remove(okp);
            }
            return match;
        });
    }

    //*** Get/Set ***//
    public Integer getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(Integer kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public LocalDate getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(LocalDate geldigTot) {
        this.geldigTot = geldigTot;
    }

    public Integer getKlasse() {
        return klasse;
    }

    public void setKlasse(Integer klasse) {
        this.klasse = klasse;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<OVChipkaartProduct> getProductRelaties() {
        return productRelaties;
    }

    public void setProductRelaties(List<OVChipkaartProduct> productRelaties) {
        this.productRelaties = productRelaties;
    }
}
