package pl.edu.pg.eti.kask.forge.equipment.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.repository.EquipmentRepository;
import pl.edu.pg.eti.kask.forge.errand.repository.ErrandRepository;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@NoArgsConstructor
@RolesAllowed(Role.USER)
public class EquipmentService {

    /**
     * Repository for user entity.
     */
    private EquipmentRepository equipmentRepository;

    /**
     * @param equipmentRepository repository for user entity
     */
    @Inject
    public EquipmentService(EquipmentRepository equipmentRepository, UserRepository userRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * @param id existing username
     * @return container (can be empty) with user
     */
    public Optional<Equipment> find(Long id) {
        return equipmentRepository.find(id);
    }


    public Optional<Equipment> find(String name) {
        return equipmentRepository.find(name);
    }

    /**
     * @return list of users
     */
    public List<Equipment> findAll() {
        return equipmentRepository.findAll();
    }

    /**
     * Saves new user.
     *
     * @param equipment new user to be saved
     */
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public void create(Equipment equipment) {
        equipmentRepository.create(equipment);
    }

    /**
     * Update user.
     *
     * @param equipment new user to be saved
     */
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public void update(Equipment equipment) {
        equipmentRepository.update(equipment);
    }

    /**
     * Delete user.
     *
     * @param id new user to be saved
     */
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public void delete(Long id) {
        equipmentRepository.delete(equipmentRepository.find(id).orElseThrow());
    }

    @RolesAllowed(Role.SYSTEM_ADMIN)
    public void deleteAll() {
        equipmentRepository.deleteAll();
    }
}
