package reiziger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import adres.Adres;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import ovchipkaart.OVChipkaart;

@Entity
public class Reiziger {
    @Id
    private Integer reiziger_id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    @OneToOne(mappedBy = "reiziger", cascade = CascadeType.ALL)
    private Adres adres;
    // We only want OV Chipkaarten when there is a reiziger associated, therefore we can do Cascade = ALL
    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();

    // JPA required empty constructor
    public Reiziger() {}

    public Reiziger(Integer reiziger_id, String voorletters, String tussenvoegsel, String achternaam, LocalDate geboortedatum) {
        this.reiziger_id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public String getNaam() {
        String naam = "";
        
        for (String letter : voorletters.split("")) {
            naam += letter + ".";
        }
        
        naam += " ";
        if (tussenvoegsel != null) {
            naam += tussenvoegsel + " ";
        }
        naam += achternaam;
        return naam;
    }

    public String toString() {
        String returnString = "Reiziger #%d: %s (%s)".formatted(
            reiziger_id,
            this.getNaam(),
            geboortedatum
        );

        if (adres != null) {
            returnString += "; " + adres.toString();
        }

        return returnString;
    }

    public void addOVChipkaart(OVChipkaart ovchipkaart) {
        if(!this.ovChipkaarten.contains(ovchipkaart)) {
            this.ovChipkaarten.add(ovchipkaart);
        }
    }

    public void removeOVChipkaart(OVChipkaart ovchipkaart) {
        this.ovChipkaarten.remove(ovchipkaart);
    }

    //*** Get/Set ***//
    public Integer getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(Integer reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }
}
