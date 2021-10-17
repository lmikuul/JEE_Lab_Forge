package pl.edu.pg.eti.kask.forge.errand.repository;

import pl.edu.pg.eti.kask.forge.datastore.DataStore;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.repository.Repository;
import pl.edu.pg.eti.kask.forge.serialization.CloningUtility;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ErrandRepository implements Repository<Errand, Long> {
    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private final DataStore store;
    /**
     * @param store data store
     */
    @Inject
    public ErrandRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Errand> find(Long id) {
        return store.findErrand(id);
    }

    @Override
    public List<Errand> findAll() {
        return store.findAllErrands();
    }

    @Override
    public void create(Errand entity) {
        store.createErrand(entity);
    }

    @Override
    public void delete(Errand entity) {
        store.deleteErrand(entity.getId());
    }

    @Override
    public void update(Errand entity) {
        store.updateErrand(entity);
    }

    public List<Errand> findAllByEquipment(Long equipmentId) {
        return store.findAllErrands().stream()
                .filter(errand -> errand.getEquipment().getId() == equipmentId)
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

}
