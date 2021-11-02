package pl.edu.pg.eti.kask.forge.errand.repository;

import pl.edu.pg.eti.kask.forge.datastore.DataStore;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.repository.Repository;
import pl.edu.pg.eti.kask.forge.serialization.CloningUtility;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
public class ErrandRepository implements Repository<Errand, Long> {

    private EntityManager em;

    @PersistenceContext
    private void setEm(EntityManager em){
        this.em = em;
    }

    @Override
    public Optional<Errand> find(Long id) {
        return Optional.ofNullable(em.find(Errand.class, id));
    }

    @Override
    public List<Errand> findAll() {
        return em.createQuery("select e from Errand e", Errand.class).getResultList();
    }

    @Override
    public void create(Errand entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Errand entity) {
        em.remove(em.find(Errand.class, entity.getId()));
    }

    @Override
    public void update(Errand entity) {
        em.merge(entity);
    }

    @Override
    public void detach(Errand entity){
        em.detach(entity);
    }

    public List<Errand> findAllByEquipment(Long equipmentId) {
        return em.createQuery("select e from Errand e where e.equipment.id = :equipmentId", Errand.class)
                .setParameter("equipmentId", equipmentId)
                .getResultList();
    }

    public Long getNewId() {
        long id = Long.parseLong((em.createQuery("select MAX(e.id) from Errand e").getSingleResult()).toString());
        return id + 1;
    }

    public Optional<Errand> findByEquipment(Long errandId, Equipment equipment) {
        return Optional.of(em.createQuery("select e from Errand e where e.id = :errandId and e.equipment.id = :eqId", Errand.class)
                .setParameter("errandId", errandId)
                .setParameter("eqId", equipment.getId())
                .getSingleResult());
    }

    public void deleteAllByEquipment(Equipment equipment) {
        List<Errand> errands = findAllByEquipment(equipment.getId());
        for (Errand errand:errands){
            delete(errand);
        }
    }
}
