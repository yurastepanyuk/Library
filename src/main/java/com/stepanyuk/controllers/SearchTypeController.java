package com.stepanyuk.controllers;

import com.stepanyuk.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean
@RequestScoped
public class SearchTypeController {

    Map<String, SearchType> searchList = new HashMap<String, SearchType>();

    public SearchTypeController() {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.put(resourceBundle.getString("book_name"), SearchType.TITLE);
        searchList.put(resourceBundle.getString("author_name"), SearchType.AUTHOR);

    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }
}
