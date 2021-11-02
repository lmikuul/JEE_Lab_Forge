package pl.edu.pg.eti.kask.forge.errand.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandsModel;
import pl.edu.pg.eti.kask.forge.errand.model.UserErrandsModel;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named
public class ErrandListByUser implements Serializable {

    /**
     * Service for managing characters.
     */
    private final ErrandService service;

    @Setter
    @Getter
    private String userLogin;
    /**
     * Characters list exposed to the view.
     */
    private UserErrandsModel errands;

    @Inject
    public ErrandListByUser(ErrandService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all characters
     */
    public UserErrandsModel getErrands() {
        if (errands == null) {
            errands = UserErrandsModel.entityToModelMapper().apply(service.findAllForUser(userLogin).get());
        }
        return errands;
    }

    public String deleteAction(UserErrandsModel.ModelErrand errand) {
        service.delete(errand.getId());
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "&faces-redirect=true";
    }
}
