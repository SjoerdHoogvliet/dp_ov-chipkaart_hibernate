package ovchipkaartproduct;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * For the creation of this embeddable ChatGPT has been used
 * Using the following prompt: "How do I create an ID for this join class that will follow the existing PK of the ov_chipkaart_product table?"
 */

@Embeddable
public class OVChipkaartProductId implements Serializable {

    @Column(name = "kaart_nummer")
    private Integer kaartNummer;

    @Column(name = "product_nummer")
    private Integer productNummer;

    public OVChipkaartProductId() {}

    public OVChipkaartProductId(Integer kaartNummer, Integer productNummer) {
        this.kaartNummer = kaartNummer;
        this.productNummer = productNummer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OVChipkaartProductId)) return false;
        OVChipkaartProductId that = (OVChipkaartProductId) o;
        return Objects.equals(kaartNummer, that.kaartNummer) &&
               Objects.equals(productNummer, that.productNummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kaartNummer, productNummer);
    }
}
