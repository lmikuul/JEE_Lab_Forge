package pl.edu.pg.eti.kask.forge.errand.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.repository.EquipmentRepository;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.repository.ErrandRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    private ErrandRepository errandRepository;
    private EquipmentRepository equipmentRepository;


    /**
     * @param errandRepository repository for user entity
     */
    @Inject
    public ErrandService(ErrandRepository errandRepository, EquipmentRepository equipmentRepository) {
        this.errandRepository = errandRepository;
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * @param id existing username
     * @return container (can be empty) with user
     */
    public Optional<Errand> find(Long id) {
        return errandRepository.find(id);
    }

    /**
     * @return list of users
     */
    public List<Errand> findAll() {
        return errandRepository.findAll();
    }

    /**
     * Saves new user.
     *
     * @param errand new user to be saved
     */
    public void create(Errand errand) {
        errandRepository.create(errand);
    }

    /**
     * Update user.
     *
     * @param errand new user to be saved
     */
    public void update(Errand errand) {
        errandRepository.update(errand);
    }

    /**
     * Delete user.
     *
     * @param id new user to be saved
     */
    public void delete(Long id) {
        errandRepository.delete(errandRepository.find(id).orElseThrow());
    }

    public Optional<List<Errand>> findAllForEquipment(Long equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.find(equipmentId);
        if(equipment.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(errandRepository.findAllByEquipment(equipmentId));
        }
    }

    public Long getNewId(){
        return errandRepository.getNewId();
    }

    public Optional<Errand> findForEquipment(Long equipmentId, Long errandId) {
        Optional<Equipment> equipment = equipmentRepository.find(equipmentId);
        if(equipment.isEmpty()){
            return Optional.empty();
        }
        else {
            return errandRepository.findByEquipment(errandId, equipment.get());
        }
    }

    public void deleteAllForEquipment(Long equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.find(equipmentId);
        if(equipment.isPresent()){
            errandRepository.deleteAllByEquipment(equipment.get());
        }
    }
}
