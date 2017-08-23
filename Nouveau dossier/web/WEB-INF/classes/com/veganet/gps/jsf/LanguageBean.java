package com.veganet.gps.jsf;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author user
 */
@ManagedBean(name = "language")
@SessionScoped
public class LanguageBean implements Serializable {

    private Locale locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();

    public Locale getLocale() {

        return locale;
    }

    public LanguageBean() {

    }

    public String getLanguage() {
         return locale.getLanguage();
    }

    public void changeLanguage(String language) {

        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
    }
}
