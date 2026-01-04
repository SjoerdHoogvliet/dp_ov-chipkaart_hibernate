package ovchipkaart;

import java.util.List;

import reiziger.Reiziger;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovchipkaart);
    public boolean update(OVChipkaart ovchipkaart);
    public boolean delete(OVChipkaart ovchipkaart);
    public OVChipkaart findByKaartNummer(Integer kaartNummer);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findAll();
}
