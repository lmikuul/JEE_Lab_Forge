package pl.edu.pg.eti.kask.forge.user.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UserEditModel {
    /**
     * User's login
     */
    private String login;
    /**
     * User's birthdate
     */
    private LocalDate birthDate;
    /**
     * User's role in system defining privileges
     */
    private Role role;
    /**
     * User's role in system defining privileges
     */
    private String avatar;
    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<User, UserEditModel> entityToModelMapper() {
        return user -> UserEditModel.builder()
                .login(user.getLogin())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .avatar("/api/avatars/" + user.getLogin())
                .build();
    }

    /**
     * @return updater for convenient updating entity object using model object
     */
    public static BiFunction<User, UserEditModel, User> modelToEntityUpdater() {
        return (user, request) -> {
            user.setBirthDate(request.getBirthDate());
            user.setRole(request.getRole());

            return user;
        };
    }
}
