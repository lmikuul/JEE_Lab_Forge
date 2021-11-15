package pl.edu.pg.eti.kask.forge.errand.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandCreateModel;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandEditModel;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
import pl.edu.pg.eti.kask.forge.user.model.UserModel;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class ErrandCreate implements Serializable {

    /**
     * Service for managing characters.
     */
    private ErrandService errandService;
    private EquipmentService equipmentService;
    private UserService userService;

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
    private ErrandCreateModel errand;
    /**
     * Character exposed to the view.
     */
    @Getter
    private List<EquipmentModel> equipments;
    @Getter
    private List<UserModel> users;

    public ErrandCreate(){};

    @EJB
    public void setErrandService(ErrandService errandService){
        this.errandService = errandService;
    }
    @EJB
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    @EJB
    public void setEquipmentService(EquipmentService equipmentService){
        this.equipmentService = equipmentService;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    @PostConstruct
    public void init() throws IOException {
        errand = ErrandCreateModel.builder().build();

        users = userService.findAll().stream()
                .map(UserModel.entityToModelMapper())
                .collect(Collectors.toList());
        equipments = equipmentService.findAll().stream()
                .map(EquipmentModel.entityToModelMapper())
                .collect(Collectors.toList());
    }

    /**
     * Action initiated by clicking save button.
     *
     * @return navigation case to the same page
     */
    public String saveAction() {
        errand.setId(errandService.getNewId());

        errandService.create(ErrandCreateModel.modelToEntityMapper(
                equipment -> equipmentService.find(equipment).orElseThrow(),
                user -> userService.find(user).orElseThrow())
                .apply(errand));

        return "/equipment/equipments.xhtml?faces-redirect=true";
    }


    /**
     * Action initiated by clicking save button.
     *
     * @return navigation case to the same page
     */
    public String cancelAction() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return "/equipment/equipments.xhtml?faces-redirect=true";
    }
}
