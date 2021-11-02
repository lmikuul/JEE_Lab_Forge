package pl.edu.pg.eti.kask.forge.equipment.repository;

import pl.edu.pg.eti.kask.forge.datastore.DataStore;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.repository.Repository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class EquipmentRepository implements Repository<Equipment, Long> {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Equipment> find(Long id) {
        return Optional.ofNullable(em.find(Equipment.class, id));
    }
    public Optional<Equipment> find(String name) {
        return Optional.ofNullable(em.find(Equipment.class, name));
    }

    @Override
    public List<Equipment> findAll() {
        return em.createQuery("select e from Equipment e", Equipment.class).getResultList();
    }

    @Override
    public void create(Equipment entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Equipment entity) {
        em.remove(em.find(Equipment.class, entity.getId()));
    }
    @Override
    public void update(Equipment entity) {
        em.merge(entity);
    }
    @Override
    public void detach(Equipment entity){
        em.detach(entity);
    }

    public void deleteAll() {
        List<Equipment> equipments = findAll();

        for (Equipment eq : equipments){
            delete(eq);
        }
    }
}
