package pl.edu.pg.eti.kask.forge.user.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.model.UserEditModel;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class UserEdit implements Serializable {

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
    private UserEditModel user;

    /**
     * Multipart part for uploaded portrait file.
     */
    @Getter
    @Setter
    private Part avatar;

    @Inject
    public UserEdit(UserService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<User> user = service.find(login);
        if (user.isPresent()) {
            this.user = UserEditModel.entityToModelMapper().apply(user.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
    }

    /**
     * Action initiated by clicking save button.
     *
     * @return navigation case to the same page
     */
    public String saveAction() {
        service.update(UserEditModel.modelToEntityUpdater().apply(service.find(login).orElseThrow(), user));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }

    /**
     * Action initiated by clicking upload button.
     *
     * @return navigation case to the same page
     */
    public String uploadAction() throws IOException {
        String path = "E:/JEE/avatars/"+login+".jpg";

        service.uploadAvatar(avatar.getInputStream(), path);
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
