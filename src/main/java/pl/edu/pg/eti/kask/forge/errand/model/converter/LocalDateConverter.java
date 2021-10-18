package pl.edu.pg.eti.kask.forge.errand.model.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@FacesConverter(value = "LocalDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {
    @Override
    public LocalDate getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(!s.isEmpty()){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                return LocalDate.parse(s, dtf);
            }
            catch (DateTimeException e){
                FacesMessage msg = new FacesMessage("LocalDate parse error.", "Invalid date format");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ConverterException(msg);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, LocalDate o) {
        return o == null ? "" : o.toString();
    }
}
