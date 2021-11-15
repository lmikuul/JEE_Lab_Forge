package pl.edu.pg.eti.kask.forge.user.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.user.entity.Role;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET users response. Contains logins of users in the system.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUsersResponse {
    /**
     * Represents single character in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class User {

    private String login;

    private List<String> roles;

    private LocalDate birthDate;

}

    @Singular
    private List<User> users;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.forge.user.entity.User>, GetUsersResponse> entityToDtoMapper() {
        return users -> {
            GetUsersResponse.GetUsersResponseBuilder response = GetUsersResponse.builder();
            users.stream()
                    .map(user -> User.builder()
                            .login(user.getLogin())
                            .roles(user.getRoles())
                            .birthDate(user.getBirthDate())
                            .build())
                    .forEach(response::user);
            return response.build();
        };
    }

}
