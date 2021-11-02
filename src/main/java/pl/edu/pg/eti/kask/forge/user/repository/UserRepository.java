package pl.edu.pg.eti.kask.forge.user.repository;

import pl.edu.pg.eti.kask.forge.datastore.DataStore;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.repository.Repository;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity. Repositories should be used in business layer (e.g.: in services).
 */
@RequestScoped
public class UserRepository implements Repository<User, String> {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> find(String login) {
        return Optional.ofNullable(em.find(User.class, login));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select e from User e", User.class).getResultList();
    }

    @Override
    public void create(User entity) {
        em.persist(entity);
    }

    @Override
    public void update(User entity) {
        em.merge(entity);
    }

    @Override
    public void delete(User entity) {
        em.remove(em.find(User.class, entity.getLogin()));
    }

    @Override
    public void detach(User entity){
        em.detach(entity);
    }
}