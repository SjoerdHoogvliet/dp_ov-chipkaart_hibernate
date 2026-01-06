package ovchipkaartproduct;

import java.time.LocalDate;

import jakarta.persistence.*;
import ovchipkaart.OVChipkaart;
import product.Product;
    
/**
 * This class has been added as Hibernate is unable to create a correct JoinTable between OVChipkaart and Product
 * This is because of the status and lastUpdate fields, that is more than just the two FKs
 * The Id has been made an embeddable to not persist it in the db and have it correctly look at both the FKs together as a PK
 */


@Entity
@Table(name = "ov_chipkaart_product")
public class OVChipkaartProduct {

    @EmbeddedId
    private OVChipkaartProductId id;

    @ManyToOne
    @MapsId("kaartNummer")
    @JoinColumn(name = "kaart_nummer")
    private OVChipkaart ovChipkaart;

    @ManyToOne
    @MapsId("productNummer")
    @JoinColumn(name = "product_nummer")
    private Product product;

    // Since we get to ignore these further they are never set or updated
    private String status;

    @Column(name = "last_update")
    private LocalDate lastUpdate;

    public OVChipkaartProduct() {}

    // Added constructor where we immediately set ignored fields to null
    public OVChipkaartProduct(OVChipkaart ovChipkaart, Product product) {
        this.ovChipkaart = ovChipkaart;
        this.product = product;
        this.status = null;
        this.lastUpdate = null;
        this.id = new OVChipkaartProductId(ovChipkaart.getKaartNummer(), product.getProductNummer());
    }

    public OVChipkaartProduct(OVChipkaart ovChipkaart, Product product, String status, LocalDate lastUpdate) {
        this.ovChipkaart = ovChipkaart;
        this.product = product;
        this.status = status;
        this.lastUpdate = lastUpdate;
        this.id = new OVChipkaartProductId(ovChipkaart.getKaartNummer(), product.getProductNummer());
    }

    // Get for the OV Chipkaart and Product for comparison functions
    public OVChipkaart getOvChipkaart() {
        return ovChipkaart;
    }

    public Product getProduct() {
        return product;
    }
}
