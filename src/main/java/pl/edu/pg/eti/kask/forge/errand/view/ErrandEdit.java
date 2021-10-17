package pl.edu.pg.eti.kask.forge.errand.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandEditModel;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
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
import java.util.List;
import java.util.Optional;

@ViewScoped
@Named
public class ErrandEdit implements Serializable {

    /**
     * Service for managing characters.
     */
    private final ErrandService service;
    /**
     * Service for managing characters.
     */
    private final EquipmentService equipmentService;

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
    private ErrandEditModel errand;


    @Inject
    public ErrandEdit(ErrandService service, EquipmentService equipmentService) {
        this.service = service;
        this.equipmentService = equipmentService;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Errand> errand = service.find(id);
        if (errand.isPresent()) {
            this.errand = ErrandEditModel.entityToModelMapper().apply(errand.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Errand not found");
        }
    }

    /**
     * Action initiated by clicking save button.
     *
     * @return navigation case to the same page
     */
    public String saveAction() {
        service.update(ErrandEditModel.modelToEntityUpdater().apply(service.find(id).orElseThrow(), errand));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
