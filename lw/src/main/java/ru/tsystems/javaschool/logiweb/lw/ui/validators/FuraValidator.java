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
 * This class checks is a fura number matches to the correct format.
 *
 * @author Irina Nikulina
 */
@FacesValidator("furaValidator")
public class FuraValidator implements Validator {

    private static final String NAME_PATTERN = "^[A-z]{2}\\d{5}$";

    private Pattern pattern;
    private Matcher matcher;

    /**
     * Constructs a new object of the FuraValidator and sets a pattern to enter correct text data.
     */
    public FuraValidator(){
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
                    new FacesMessage(" Fura number has to be in 2 letters and 5 digits format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }

    }
}
