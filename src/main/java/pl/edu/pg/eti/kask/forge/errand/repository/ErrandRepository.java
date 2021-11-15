package pl.edu.pg.eti.kask.forge.errand.repository;

import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.repository.Repository;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Dependent
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


    public Optional<Errand> findByIdAndUser(Long id, User user) {
        try {
            return Optional.of(em.createQuery("select e from Errand e where e.id = :id and e.user = :user", Errand.class)
                    .setParameter("id", id)
                    .setParameter("user", user)
                    .getSingleResult());
        }
        catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public List<Errand> findAllByEquipment(Long equipmentId) {
        return em.createQuery("select e from Errand e where e.equipment.id = :equipmentId", Errand.class)
                .setParameter("equipmentId", equipmentId)
                .getResultList();
    }

    public List<Errand> findAllByEquipmentAndUser(Long equipmentId, User user) {
        return em.createQuery("select e from Errand e where e.equipment.id = :equipmentId and e.user = :user", Errand.class)
                .setParameter("equipmentId", equipmentId)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Errand> findAllByUser(String userLogin) {
        return em.createQuery("select e from Errand e where e.user.login = :userLogin", Errand.class)
                .setParameter("userLogin", userLogin)
                .getResultList();
    }

    public List<Errand> findAllByUser(User user) {
        return em.createQuery("select e from Errand e where e.user = :user", Errand.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Long getNewId() {
        long id = Long.parseLong((em.createQuery("select MAX(e.id) from Errand e").getSingleResult()).toString());
        return id + 1;
    }

    public Optional<Errand> findByEquipment(Long errandId, Equipment equipment) {
        try {
            return Optional.of(em.createQuery("select e from Errand e where e.id = :errandId and e.equipment.id = :eqId", Errand.class)
                    .setParameter("errandId", errandId)
                    .setParameter("eqId", equipment.getId())
                    .getSingleResult());
        }
        catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Errand> findByEquipment(Long errandId, Equipment equipment, User user) {
        try {
            return Optional.of(em.createQuery("select e from Errand e where e.id = :errandId and e.equipment.id = :eqId and e.user = :user", Errand.class)
                    .setParameter("errandId", errandId)
                    .setParameter("eqId", equipment.getId())
                    .setParameter("user", user)
                    .getSingleResult());
        }
        catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public void deleteAllByEquipment(Equipment equipment) {
        List<Errand> errands = findAllByEquipment(equipment.getId());
        for (Errand errand:errands){
            delete(errand);
        }
    }

}
