package ru.tsystems.javaschool.logiweb.lw.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class checks is a driver name matches to the correct format.
 *
 * @author Irina Nikulina
 */
@FacesValidator("nameValidator")
public class NameValidator implements Validator {

    private static final String NAME_PATTERN = "^[A-z]{1}[a-z]*$";

    private Pattern pattern;
    private Matcher matcher;

    /**
     * Constructs a new object of the Validator and sets a pattern to enter correct text data.
     */
    public NameValidator(){
        pattern = Pattern.compile(NAME_PATTERN);
    }

    /**
     * Validates an entered information.
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
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
