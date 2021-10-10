package pl.edu.pg.eti.kask.forge.configuration;

import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Listener started automatically on servlet context initialized. Fetches instance of the datasource from the servlet
 * context and fills it with default content. Normally this class would fetch database datasource and init data only
 * in cases of empty database. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@ApplicationScoped
public class InitializedData implements ServletContextListener {

    /**
     * Service for users operations.
     */
    private final UserService userService;

    @Inject
    public InitializedData(UserService userService) {
        this.userService = userService;
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
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            return is.readAllBytes();
        }
    }

}
