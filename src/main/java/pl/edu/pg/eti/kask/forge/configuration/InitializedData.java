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

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.ServletContextListener;
import javax.validation.constraints.Null;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * Listener started automatically on servlet context initialized. Fetches instance of the datasource from the servlet
 * context and fills it with default content. Normally this class would fetch database datasource and init data only
 * in cases of empty database. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
public class InitializedData {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    @Inject
    public InitializedData(Pbkdf2PasswordHash pbkdf) {
        this.pbkdf = pbkdf;
    }

    public InitializedData() {
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     *
     */
    @PostConstruct
    private synchronized void init() {
        User admin = User.builder()
                .login("admin")
                .roles(List.of(Role.SYSTEM_ADMIN, Role.USER, Role.ARCHER, Role.ENGINEER, Role.FOOTMAN, Role.COMMANDER, Role.KNIGHT))
                .password(pbkdf.generate("admin".toCharArray()))
                .birthDate(LocalDate.of(1990, 10, 21))
                .build();

        User alice = User.builder()
                .login("alice")
                .roles(List.of(Role.USER, Role.ARCHER))
                .birthDate(LocalDate.of(2002, 3, 19))
                .password(pbkdf.generate("password".toCharArray()))
                .build();

        User bob = User.builder()
                .login("bob")
                .roles(List.of(Role.USER, Role.COMMANDER))
                .birthDate(LocalDate.of(1999, 5, 12))
                .password(pbkdf.generate("123456".toCharArray()))
                .build();

        User cyryl = User.builder()
                .login("cyryl")
                .roles(List.of(Role.USER, Role.ENGINEER))
                .birthDate(LocalDate.of(1990, 10, 30))
                .password(pbkdf.generate("*****".toCharArray()))
                .build();

        User diana = User.builder()
                .login("diana")
                .roles(List.of(Role.USER, Role.FOOTMAN))
                .birthDate(LocalDate.of(2008, 12, 24))
                .password(pbkdf.generate("ilovecats<3".toCharArray()))
                .build();


        User elvin = User.builder()
                .login("elvin")
                .roles(List.of(Role.USER, Role.KNIGHT))
                .birthDate(LocalDate.of(2001, 1, 16))
                .password(pbkdf.generate("qwe123".toCharArray()))
                .build();

        em.persist(admin);
        em.persist(alice);
        em.persist(bob);
        em.persist(cyryl);
        em.persist(diana);
        em.persist(elvin);

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

        em.persist(sword);
        em.persist(bow);
        em.persist(shield);
        em.persist(helmet);

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

        em.persist(errand1);
        em.persist(errand2);
        em.persist(errand3);
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
