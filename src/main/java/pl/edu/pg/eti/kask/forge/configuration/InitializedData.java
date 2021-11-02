package pl.edu.pg.eti.kask.forge.configuration;

import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContextListener;
import javax.validation.constraints.Null;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Listener started automatically on servlet context initialized. Fetches instance of the datasource from the servlet
 * context and fills it with default content. Normally this class would fetch database datasource and init data only
 * in cases of empty database. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@ApplicationScoped
public class InitializedData {

    /**
     * Service for users operations.
     */
    private final UserService userService;
    private final EquipmentService equipmentService;
    private final ErrandService errandService;
    private RequestContextController requestContextController;

    @Inject
    public InitializedData(UserService userService, EquipmentService equipmentService, ErrandService errandService, RequestContextController requestContextController) {
        this.userService = userService;
        this.equipmentService = equipmentService;
        this.errandService = errandService;
        this.requestContextController = requestContextController;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }


    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     *
     */
    private synchronized void init() {
        requestContextController.activate();

        User admin = User.builder()
                .login("admin")
                .role(Role.SYSTEM_ADMIN)
                .password("admin")
                .birthDate(LocalDate.of(1990, 10, 21))
                .build();

        User alice = User.builder()
                .login("alice")
                .role(Role.ARCHER)
                .birthDate(LocalDate.of(2002, 3, 19))
                .password("password")
                .build();

        User bob = User.builder()
                .login("bob")
                .role(Role.COMMANDER)
                .birthDate(LocalDate.of(1999, 5, 12))
                .password("123456")
                .build();

        User cyryl = User.builder()
                .login("cyryl")
                .role(Role.ENGINEER)
                .birthDate(LocalDate.of(1990, 10, 30))
                .password("*****")
                .build();

        User diana = User.builder()
                .login("diana")
                .role(Role.FOOTMAN)
                .birthDate(LocalDate.of(2008, 12, 24))
                .password("ilovecats<3")
                .build();


        User elvin = User.builder()
                .login("elvin")
                .role(Role.KNIGHT)
                .birthDate(LocalDate.of(2001, 1, 16))
                .password("qwe123")
                .build();

        userService.create(admin);
        userService.create(alice);
        userService.create(bob);
        userService.create(cyryl);
        userService.create(diana);
        userService.create(elvin);

        Equipment sword = Equipment.builder()
                .id(1)
                .name("Sword")
                .type(EquipmentType.MELEE_WEAPON)
                .mainMaterial("Steel")
                .weight(20.5)
                .build();

        Equipment bow = Equipment.builder()
                .id(2)
                .name("Bow")
                .type(EquipmentType.RANGED_WEAPON)
                .mainMaterial("Wood")
                .weight(10.9)
                .build();

        Equipment shield = Equipment.builder()
                .id(3)
                .name("Shield")
                .type(EquipmentType.SHIELD)
                .mainMaterial("Steel")
                .weight(40)
                .build();

        Equipment helmet = Equipment.builder()
                .id(4)
                .name("Helmet")
                .type(EquipmentType.ARMOR)
                .mainMaterial("Leather")
                .weight(7.8)
                .build();

        equipmentService.create(sword);
        equipmentService.create(bow);
        equipmentService.create(shield);
        equipmentService.create(helmet);

        Errand errand1 = Errand.builder()
                .id(1)
                .user(diana)
                .equipment(sword)
                .price(100.50)
                .startDay(LocalDate.now())
                .details("It must be shiny")
                .build();

        Errand errand2 = Errand.builder()
                .id(2)
                .user(diana)
                .equipment(shield)
                .price(289.50)
                .startDay(LocalDate.now())
                .details("None")
                .build();

        Errand errand3 = Errand.builder()
                .id(3)
                .user(bob)
                .equipment(sword)
                .price(289.50)
                .startDay(LocalDate.now())
                .doneDay(LocalDate.parse("2022-01-01"))
                .details("With my name on it.")
                .build();

        errandService.create(errand1);
        errandService.create(errand2);
        errandService.create(errand3);

        requestContextController.deactivate();
    }


    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            assert is != null;
            return is.readAllBytes();
        }
        catch (NullPointerException e){
            return null;
        }
    }

}
