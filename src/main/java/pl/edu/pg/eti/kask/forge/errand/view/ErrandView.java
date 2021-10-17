package pl.edu.pg.eti.kask.forge.errand.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandModel;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandsModel;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
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
public class ErrandView implements Serializable {
    /**
     * Service for managing characters.
     */
    private final ErrandService service;

    /**
     * Character id.
     */
    @Setter
    @Getter
    private Long id;

    /**
     * Character exposed to the view.
     */
    @Getter
    private ErrandModel errand;

    @Inject
    public ErrandView(ErrandService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Errand> errand = service.find(id);
        if (errand.isPresent()) {
            this.errand = ErrandModel.entityToModelMapper().apply(errand.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
    }
}