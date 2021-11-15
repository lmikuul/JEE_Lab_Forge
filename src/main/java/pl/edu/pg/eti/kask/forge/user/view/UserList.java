package pl.edu.pg.eti.kask.forge.user.view;

import pl.edu.pg.eti.kask.forge.user.model.UsersModel;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.ejb.EJB;
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
    private UserService service;

    /**
     * Characters list exposed to the view.
     */
    private UsersModel users;

    public UserList() {
    }

    @EJB
    public void setService(UserService service){
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
