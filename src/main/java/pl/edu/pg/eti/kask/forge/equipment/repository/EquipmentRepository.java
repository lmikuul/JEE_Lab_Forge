package pl.edu.pg.eti.kask.forge.equipment.repository;

import pl.edu.pg.eti.kask.forge.datastore.DataStore;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.repository.Repository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class EquipmentRepository implements Repository<Equipment, Long> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private final DataStore store;
    /**
     * @param store data store
     */
    @Inject
    public EquipmentRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Equipment> find(Long id) {
        return store.findEquipment(id);
    }
    public Optional<Equipment> find(String name) {
        return store.findEquipment(name);
    }

    @Override
    public List<Equipment> findAll() {
        return store.findAllEquipment();
    }

    @Override
    public void create(Equipment entity) {
        store.createEquipment(entity);
    }

    @Override
    public void delete(Equipment entity) {
        store.deleteEquipment(entity.getId());

    }
    @Override
    public void update(Equipment entity) {
        store.updateEquipment(entity);
    }

    public void deleteAll() {
        List<Equipment> equipments = findAll();

        for (Equipment eq : equipments){
            store.deleteEquipment(eq.getId());
        }
    }
}
