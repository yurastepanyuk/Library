package com.stepanyuk.beans;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(eager = true)
@SessionScoped
public class User  implements Serializable {

    private String username;
    private String password;

    private String login;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {

        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try {

//            ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).logout();
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            //Используя метод сервлет login(username, password) мы проверим пользователя на его наличие в Realm
//            HttpServletRequest httpServletRequest = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
            httpServletRequest.getSession(false).getId();
//            httpServletRequest.login(username, password);


//            if (httpServletRequest.getUserPrincipal()==null || (httpServletRequest.getUserPrincipal()!=null && !httpServletRequest.getUserPrincipal().getName().equals(username))) {
//                httpServletRequest.logout();
//                httpServletRequest.login(username, password);
//            }
//
//            HttpServletResponse httpServletResponse = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            httpServletRequest.authenticate(httpServletResponse);
            return "books";
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Логин и пароль не подходят");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("loginform", message);
        }

        return "index";

    }

    public String logout() {
        String result = "/index.xhtml?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (ServletException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return result;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login();
    }

    public String goHome(){
        return "/index.xhtml?faces-redirect=true";
    }

    public String goBooks(){
        return "/pages/books.xhtml?faces-redirect=true";
    }
}
