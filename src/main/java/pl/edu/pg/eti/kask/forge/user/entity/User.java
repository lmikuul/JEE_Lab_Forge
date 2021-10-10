package pl.edu.pg.eti.kask.forge.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {
    /**
     * User's login
     */
    private String login;
    /**
     * User's password
     */
    private String password;
    /**
     * User's birthdate
     */
    private LocalDate birthDate;
    /**
     * User's role in system defining privileges
     */
    private Role role;
}
