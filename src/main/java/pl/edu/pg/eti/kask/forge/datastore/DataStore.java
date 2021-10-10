package pl.edu.pg.eti.kask.forge.datastore;

import lombok.extern.java.Log;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@ApplicationScoped
public class DataStore {

    /**
     * Set of all users.
     */
    private final Set<User> users = new HashSet<>();

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
}
