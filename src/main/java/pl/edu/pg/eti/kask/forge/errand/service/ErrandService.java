package pl.edu.pg.eti.kask.forge.errand.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.repository.EquipmentRepository;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.repository.ErrandRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class ErrandService {

    /**
     * Repository for user entity.
     */
    private ErrandRepository repository;


    /**
     * @param repository repository for user entity
     */
    @Inject
    public ErrandService(ErrandRepository repository) {
        this.repository = repository;
    }

    /**
     * @param id existing username
     * @return container (can be empty) with user
     */
    public Optional<Errand> find(Long id) {
        return repository.find(id);
    }

    /**
     * @return list of users
     */
    public List<Errand> findAll() {
        return repository.findAll();
    }

    /**
     * Saves new user.
     *
     * @param errand new user to be saved
     */
    public void create(Errand errand) {
        repository.create(errand);
    }

    /**
     * Update user.
     *
     * @param errand new user to be saved
     */
    public void update(Errand errand) {
        repository.update(errand);
    }

    /**
     * Delete user.
     *
     * @param id new user to be saved
     */
    public void delete(Long id) {
        repository.delete(repository.find(id).orElseThrow());
    }

    public List<Errand> findAllForEquipment(Long equipmentId) {
        return repository.findAllByEquipment(equipmentId);
    }
}
