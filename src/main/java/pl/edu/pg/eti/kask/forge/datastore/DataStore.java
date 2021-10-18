package pl.edu.pg.eti.kask.forge.datastore;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.serialization.CloningUtility;
import pl.edu.pg.eti.kask.forge.servlet.MimeTypes;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Log
@ApplicationScoped
public class DataStore {

    /**
     * Set of all users.
     */
    private final Set<User> users = new HashSet<>();

    /**
     * Set of all equipments.
     */
    private final Set<Equipment> equipments = new HashSet<>();

    /**
     * Set of all equipments.
     */
    private final Set<Errand> errands = new HashSet<>();
    /**
     * Seeks for single user.
     *
     * @param login user's login
     * @return container (can be empty) with user
     */
    public synchronized Optional<User> findUser(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Seeks for all users.
     *
     * @return collection of all users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new user.
     *
     * @param user new user to be stored
     * @throws IllegalArgumentException if user with provided login already exists
     */
    public synchronized void createUser(User user) throws IllegalArgumentException {
        findUser(user.getLogin()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The user login \"%s\" is not unique", user.getLogin()));
                },
                () -> users.add(CloningUtility.clone(user)));
    }

    /**
     * Updates existing character.
     *
     * @param user character to be updated
     * @throws IllegalArgumentException if character with the same id does not exist
     */
    public synchronized void updateUser(User user) throws IllegalArgumentException {
        findUser(user.getLogin()).ifPresentOrElse(
                original -> {
                    users.remove(original);
                    users.add(CloningUtility.clone(user));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The character with login \"%s\" does not exist", user.getLogin()));
                });
    }
    public synchronized void deleteUser(String login){
        findUser(login).ifPresentOrElse(
                original -> {
                    List<Errand> errandsToDelete = this.findErrandsByUser(original);

                    for (Errand errand: errandsToDelete) {
                        this.deleteErrand(errand.getId());
                    }

                    users.remove(original);
                },
                () -> {
                    throw new IllegalArgumentException(
                            "The user is not found");
                });
    }

    public Optional<Equipment> findEquipment(Long id) {
        return equipments.stream()
                .filter(equipment -> equipment.getId() == id)
                .findFirst()
                .map(CloningUtility::clone);
    }
    public Optional<Equipment> findEquipment(String name) {
        return equipments.stream()
                .filter(equipment -> equipment.getName().equals(name))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public List<Equipment> findAllEquipment() {
        return equipments.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public void createEquipment(Equipment equipment) {
        findEquipment(equipment.getId()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The equipment id \"%s\" is not unique", equipment.getId()));
                },
                () -> equipments.add(CloningUtility.clone(equipment)));
    }

    public synchronized void deleteEquipment(Long id) {
        findEquipment(id).ifPresentOrElse(
                original -> {
                    List<Errand> errandsToDelete = this.findErrandsByEquipment(original);

                    for (Errand errand: errandsToDelete) {
                        this.deleteErrand(errand.getId());
                    }

                    equipments.remove(original);
                },
                () -> {
                    throw new IllegalArgumentException(
                            "The equipment not found");
                });
    }

    public synchronized void updateEquipment(Equipment equipment) {
        findEquipment(equipment.getId()).ifPresentOrElse(
                original -> {
                    equipments.remove(original);
                    equipments.add(CloningUtility.clone(equipment));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The equipment with id \"%s\" does not exist", equipment.getId()));
                });
    }

    public Optional<Errand> findErrand(Long id) {
        return errands.stream()
                .filter(errand -> errand.getId() == id)
                .findFirst()
                .map(CloningUtility::clone);
    }

    public List<Errand> findErrandsByEquipment(Equipment equipment) {
        return errands.stream()
                .filter(errand -> errand.getEquipment().equals(equipment))
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public List<Errand> findErrandsByUser(User user) {
        return errands.stream()
                .filter(errand -> errand.getUser().equals(user))
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public List<Errand> findAllErrands() {
        return errands.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createErrand(Errand errand) {
        findErrand(errand.getId()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The errand id \"%s\" is not unique", errand.getId()));
                },
                () -> errands.add(CloningUtility.clone(errand)));
    }

    public synchronized void deleteErrand(Long id) {
        findErrand(id).ifPresentOrElse(
                original -> errands.remove(original),
                () -> {
                    throw new IllegalArgumentException(
                            "The equipment not found");
                });
    }

    public synchronized void updateErrand(Errand errand) {
        findErrand(errand.getId()).ifPresentOrElse(
                original -> {
                    errands.remove(original);
                    errands.add(CloningUtility.clone(errand));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The errand with id \"%s\" does not exist", errand.getId()));
                });
    }

    public Long getNewErrandId() {
        Errand err =  errands.stream()
                .map(CloningUtility::clone)
                .max(Comparator.comparing(Errand::getId))
                .orElse(null);

        return err == null ? 1 : err.getId() + 1;
    }
}
