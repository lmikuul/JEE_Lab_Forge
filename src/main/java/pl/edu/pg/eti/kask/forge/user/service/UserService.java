package pl.edu.pg.eti.kask.forge.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.servlet.MimeTypes;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.repository.UserRepository;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
@RolesAllowed(Role.USER)
public class UserService {

    /**
     * Repository for user entity.
     */
    private UserRepository repository;

    /**
     * Build in security context.
     */
    private SecurityContext securityContext;
    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    /**
     * @param repository repository for user entity
     */
    @Inject
    public UserService(UserRepository repository, SecurityContext securityContext, Pbkdf2PasswordHash pdkdf) {
        this.repository = repository;
        this.pbkdf = pdkdf;
        this.securityContext = securityContext;
    }

    /**
     * @param login existing username
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.find(login);
    }

    public Optional<User> find(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * @return list of users
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * @return logged user entity
     */
    public Optional<User> findCallerPrincipal() {
        if (securityContext.getCallerPrincipal() != null) {
            return find(securityContext.getCallerPrincipal().getName());
        } else {
            return Optional.empty();
        }
    }
    /**
     * Saves new user.
     *
     * @param user new user to be saved
     */
    @PermitAll
    public void create(User user) {
        if (!securityContext.isCallerInRole(Role.SYSTEM_ADMIN)) {
            user.setRoles(List.of(Role.USER));
        }
        user.setPassword(pbkdf.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    /**
     * Update user.
     *
     * @param user new user to be saved
     */
    public void update(User user) {
        repository.update(user);
    }

    public void delete(User user) {repository.delete(user);}

    /**
     * Uploads user's avatar to the server
     * @param avatar User's avatar
     * @param targetFilePath Path to the server avatar's directory
     * @throws IOException
     */
    public void uploadAvatar(InputStream avatar, String targetFilePath) throws IOException{

        Path targetFile = java.nio.file.Paths.get(targetFilePath);

        Files.copy(avatar, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Deletes user's file at pointed path
     * @param targetPath Path to file to delete
     * @throws IOException
     */
    public void deleteAvatar(String targetPath) throws IOException {
        Path path = java.nio.file.Paths.get(targetPath);

        if(Files.exists(path)){
            Files.delete(path);
        }
    }

    /**
     * Gets user's avatar from server and returns it as bytes array
     * @param avatarPath Path to the user's avatar
     * @return Avatar's data
     * @throws IOException
     */
    public byte[] findAvatar(String avatarPath) throws IOException{
        Path path = java.nio.file.Paths.get(avatarPath);

        if(Files.exists(path)) {
            return Files.readAllBytes(path);
        }
        return null;
    }
}
