package com.stepanyuk.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean(eager=true, name = "localeChanger")
@ApplicationScoped
public class LocaleChanger implements Serializable {

   private Locale curLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    private String strLocale;

    public LocaleChanger() {
        strLocale = curLocale.toString();
    }

    public Locale getCurLocale() {
        return curLocale;
    }

    public void changeLocale(String localCode){
        this.curLocale =new Locale.Builder().setLanguage(localCode.substring(0,2)).setRegion(localCode.substring(3)).build();
        strLocale = curLocale.toString();
    }

    public String getStrLocale() {
        return strLocale;
    }
}


