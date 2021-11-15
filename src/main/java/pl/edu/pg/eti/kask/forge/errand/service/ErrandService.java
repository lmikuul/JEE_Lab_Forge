package pl.edu.pg.eti.kask.forge.errand.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.repository.EquipmentRepository;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.repository.ErrandRepository;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
@RolesAllowed(Role.USER)
public class ErrandService {

    /**
     * Repository for user entity.
     */
    private ErrandRepository errandRepository;
    private EquipmentRepository equipmentRepository;
    private UserRepository userRepository;
    /**
     * Build in security context.
     */
    private SecurityContext securityContext;


    /**
     * @param errandRepository repository for user entity
     */
    @Inject
    public ErrandService(ErrandRepository errandRepository, EquipmentRepository equipmentRepository,
                         UserRepository userRepository, SecurityContext securityContext) {
        this.errandRepository = errandRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    /**
     * @param id existing username
     * @return container (can be empty) with user
     */
    //@RolesAllowed(Role.SYSTEM_ADMIN)
    public Optional<Errand> find(Long id) {
        if(securityContext.isCallerInRole(Role.SYSTEM_ADMIN))
            return errandRepository.find(id);
        else
            return errandRepository.findByIdAndUser(id, userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }

    public Optional<Errand> findForCallerPrincipal(Long id){
        return errandRepository.findByIdAndUser(id, userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }

    /**
     * @return list of users
     */
    //@RolesAllowed(Role.SYSTEM_ADMIN)
    public List<Errand> findAll() {
        if(securityContext.isCallerInRole(Role.SYSTEM_ADMIN))
            return errandRepository.findAll();
        else
            return errandRepository.findAllByUser(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }

    public List<Errand> findAllForCallerPrincipal(Long id){
        return errandRepository.findAllByUser(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }
    /**
     * Saves new user.
     *
     * @param errand new user to be saved
     */
    public void create(Errand errand) {
        errandRepository.create(errand);
        equipmentRepository.find(errand.getEquipment().getId()).ifPresent(
                equipment -> equipment.getErrands().add(errand)
        );
        userRepository.find(errand.getUser().getLogin()).ifPresent(
                user -> user.getErrands().add(errand)
        );
    }

    public void createForCallerPrincipal(Errand errand) {
        User user = userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow();
        errand.setUser(user);
        user.getErrands().add(errand);
        errandRepository.create(errand);
    }

    /**
     * Update user.
     *
     * @param errand new user to be saved
     */
    public void update(Errand errand) {
        Errand original = errandRepository.find(errand.getId()).orElseThrow();

        if ((securityContext.isCallerInRole(Role.USER) && original.getUser().getLogin().equals(securityContext.getCallerPrincipal().getName()))
                || securityContext.isCallerInRole(Role.SYSTEM_ADMIN)) {
            errandRepository.detach(original);
            if (!original.getUser().getLogin().equals(errand.getUser().getLogin())) {
                original.getUser().getErrands().removeIf(errandToRemove -> errandToRemove.getId() == errand.getId());
                userRepository.find(errand.getUser().getLogin()).ifPresent(user -> user.getErrands().add(errand));
            }
            errandRepository.update(errand);
        } else {
            throw new EJBAccessException("Authorization failed for user " + securityContext.getCallerPrincipal().getName());
        }
    }

    public void delete(Long id) {
        Errand errand = errandRepository.find(id).orElseThrow();
        if ((securityContext.isCallerInRole(Role.USER) && errand.getUser().getLogin().equals(securityContext.getCallerPrincipal().getName()))
                || securityContext.isCallerInRole(Role.SYSTEM_ADMIN)) {
            errand.getEquipment().getErrands().remove(errand);
            errand.getUser().getErrands().remove(errand);
            errandRepository.delete(errand);
        }
        else {
            throw new EJBAccessException("Authorization failed for user " + securityContext.getCallerPrincipal().getName());
        }
    }

    @RolesAllowed(Role.SYSTEM_ADMIN)
    public void deleteAll() {
        List<Errand> errands = errandRepository.findAll();

        for (Errand errand: errands) {
            errand.getEquipment().getErrands().remove(errand);
            errand.getUser().getErrands().remove(errand);
            errandRepository.delete(errand);
        }
    }

    public Optional<List<Errand>> findAllForEquipment(Long equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.find(equipmentId);
        if(equipment.isEmpty()){
            return Optional.empty();
        }
        else {
            if(securityContext.isCallerInRole(Role.SYSTEM_ADMIN))
                return Optional.of(errandRepository.findAllByEquipment(equipmentId));
            else
                return Optional.of(errandRepository.findAllByEquipmentAndUser(equipmentId,
                        userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow()));
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
            if(securityContext.isCallerInRole(Role.SYSTEM_ADMIN))
                return errandRepository.findByEquipment(errandId, equipment.get());
            else
                return errandRepository.findByEquipment(errandId, equipment.get(),
                        userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
        }
    }

    @RolesAllowed(Role.SYSTEM_ADMIN)
    public void deleteAllForEquipment(Long equipmentId) {
        Optional<Equipment> equipment = equipmentRepository.find(equipmentId);
        equipment.ifPresent(value -> errandRepository.deleteAllByEquipment(value));
    }
}
