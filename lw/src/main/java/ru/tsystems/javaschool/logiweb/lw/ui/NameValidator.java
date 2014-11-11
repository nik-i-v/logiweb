package ru.tsystems.javaschool.logiweb.lw.ui;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("nameValidator")
public class NameValidator implements Validator {

    private static final String NAME_PATTERN = "^[_A-Za-z0-9-]+(\\." +
            "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";

    private Pattern pattern;
    private Matcher matcher;

    public NameValidator(){
        pattern = Pattern.compile(NAME_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        matcher = pattern.matcher(value.toString());
        if(!matcher.matches()){

            FacesMessage msg =
                    new FacesMessage(" Invalid format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }

    }
}
