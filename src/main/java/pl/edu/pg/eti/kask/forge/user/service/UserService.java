package pl.edu.pg.eti.kask.forge.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.servlet.MimeTypes;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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

    /**
     * Update user.
     *
     * @param user new user to be saved
     */
    public void update(User user) {
        repository.update(user);
    }

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
