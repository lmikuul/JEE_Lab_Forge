package pl.edu.pg.eti.kask.forge.user.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.model.UserModel;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@RequestScoped
@Named
public class UserView implements Serializable {
    /**
     * Service for managing characters.
     */
    private final UserService service;

    /**
     * Character id.
     */
    @Setter
    @Getter
    private String login;

    /**
     * Character exposed to the view.
     */
    @Getter
    private UserModel user;

    @Inject
    public UserView(UserService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<User> user = service.find(login);
        if (user.isPresent()) {
            this.user = UserModel.entityToModelMapper().apply(user.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
    }
}
