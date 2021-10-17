package pl.edu.pg.eti.kask.forge.user.view;

import pl.edu.pg.eti.kask.forge.user.model.UsersModel;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class UserList implements Serializable {

    /**
     * Service for managing characters.
     */
    private final UserService service;

    /**
     * Characters list exposed to the view.
     */
    private UsersModel users;

    @Inject
    public UserList(UserService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all characters
     */
    public UsersModel getUsers() {
        if (users == null) {
            users = UsersModel.entityToModelMapper().apply(service.findAll());
        }
        return users;
    }
}
