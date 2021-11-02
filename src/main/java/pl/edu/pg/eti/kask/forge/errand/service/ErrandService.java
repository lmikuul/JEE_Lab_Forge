package pl.edu.pg.eti.kask.forge.errand.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.repository.EquipmentRepository;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.repository.ErrandRepository;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    private UserRepository userRepository;


    /**
     * @param errandRepository repository for user entity
     */
    @Inject
    public ErrandService(ErrandRepository errandRepository, EquipmentRepository equipmentRepository, UserRepository userRepository) {
        this.errandRepository = errandRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
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
    @Transactional
    public void create(Errand errand) {
        errandRepository.create(errand);
        equipmentRepository.find(errand.getEquipment().getId()).ifPresent(
                equipment -> equipment.getErrands().add(errand)
        );
    }

    /**
     * Update user.
     *
     * @param errand new user to be saved
     */
    @Transactional
    public void update(Errand errand) {
        Errand original = errandRepository.find(errand.getId()).orElseThrow();
        errandRepository.detach(original);
        errandRepository.update(errand);
    }

    /**
     * Delete user.
     *
     * @param id new user to be saved
     */
    @Transactional
    public void delete(Long id) {
        Errand errand = errandRepository.find(id).orElseThrow();
        errand.getEquipment().getErrands().remove(errand);
        errandRepository.delete(errand);
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

    public Optional<List<Errand>> findAllForUser(String userLogin) {
        Optional<User> user = userRepository.find(userLogin);
        if(user.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(errandRepository.findAllByUser(userLogin));
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

    @Transactional
    public void deleteAllForEquipment(Long equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.find(equipmentId);
        equipment.ifPresent(value -> errandRepository.deleteAllByEquipment(value));
    }
}
