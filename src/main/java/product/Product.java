package product;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import ovchipkaart.OVChipkaart;
import ovchipkaartproduct.OVChipkaartProduct;

@Entity
public class Product {
    @Id
    @Column(name = "product_nummer")
    private Integer productNummer;
    private String naam;
    private String beschrijving;
    private float prijs;
    // Since the relations should only exist in relation to a product cascade = all. Also we dont want relations that are not linked to a product, therefore, orphanRemoval = true
    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OVChipkaartProduct> ovChipkaartRelaties = new ArrayList<>();

    // JPA required empty constructor
    public Product() {}

    public Product(Integer productNummer, String naam, String beschrijving, float prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    // TODO: Add something that gives some information about the OV Chipkaarten
    public String toString() {
        String returnString = "Product #%d: %s (%s) kost %.2f".formatted(
            this.productNummer,
            this.naam,
            this.beschrijving,
            this.prijs
        );

        if(Hibernate.isInitialized(this.ovChipkaartRelaties) && this.ovChipkaartRelaties.size() > 0) {
            returnString += "\n\tGeplaatst op OV Chipkaarten: ";
            for(OVChipkaartProduct p : this.ovChipkaartRelaties) {
                returnString += p.getOvChipkaart().getKaartNummer() + " ";
            }
        }

        return returnString;
    }

    // As we can have multiple OV Chipkaarten, add these functions to add and remove OV Chipkaarten without getting and setting the whole list
    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        for(OVChipkaartProduct p : this.ovChipkaartRelaties) {
            if(p.getOvChipkaart().getKaartNummer() == ovChipkaart.getKaartNummer()) {
                return;
            }
        }
        this.ovChipkaartRelaties.add(new OVChipkaartProduct(ovChipkaart, this));
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaartRelaties.removeIf(okp -> {
            boolean match = okp.getOvChipkaart().equals(ovChipkaart);
            if (match) {
                okp.getOvChipkaart().getProductRelaties().remove(okp);
                this.ovChipkaartRelaties.remove(okp);
            }
            return match;
        });
    }

    //*** Get/Set ***//
    public Integer getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(Integer productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public float getPrijs() {
        return prijs;
    }

    public void setPrijs(float prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaartProduct> getOVChipkaartRelaties() {
        return ovChipkaartRelaties;
    }
}
