package pl.edu.pg.eti.kask.forge.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class UserService {

    /**
     * Repository for user entity.
     */
    private UserRepository repository;

    /**
     * @param repository repository for user entity
     */
    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * @param login existing username
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.find(login);
    }

    /**
     * @return list of users
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Saves new user.
     *
     * @param user new user to be saved
     */
    public void create(User user) {
        repository.create(user);
    }
}
