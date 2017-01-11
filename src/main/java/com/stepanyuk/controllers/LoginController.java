package com.stepanyuk.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    public LoginController() {
    }

    public static String login(){
        return "books";
    }

    public String logoff(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "exit";
    }
}
