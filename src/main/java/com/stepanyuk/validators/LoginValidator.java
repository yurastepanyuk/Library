package com.stepanyuk.validators;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.facelets.FaceletContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@FacesValidator("com.stepanyuk.validators.LoginValidator")
public class LoginValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String valueInput = value.toString().trim();

        try {
            if (getAccesLoginName().contains(valueInput)) {
                throw new IllegalArgumentException(resourceBundle.getString("login_name_error"));
            } else if (valueInput.length() < 4) {
                throw new IllegalArgumentException(resourceBundle.getString("login_length_error"));
            } else if (!Character.isLetter(valueInput.charAt(0))) {
                throw new IllegalArgumentException(resourceBundle.getString("login_first_char"));
            }
        }catch (IllegalArgumentException ex){
            FacesMessage facesMessage = new FacesMessage(ex.getMessage());
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        }
    }

    private List<String> getAccesLoginName(){

        List<String> result = new ArrayList<>();
        result.add("username");
        result.add("login");

        return result;
    }

}
