package com.stepanyuk.controllers;


import com.stepanyuk.beans.Pager;
import com.stepanyuk.comparators.ListComparator;
import com.stepanyuk.db.DataHelper;
import com.stepanyuk.entity.GenreEntity;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.*;
@ManagedBean
@SessionScoped
public class GenreController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();
    private Map<Long, GenreEntity> map;
    private List<GenreEntity> list;
    private Pager pager;
    private DataHelper dataHelper;
    @ManagedProperty(value = "#{bookListController}")
    private BookListController bookListController;

    @PostConstruct
    public void init() {
        pager = bookListController.getPager();
        dataHelper = bookListController.getDataHelper();

        map = new HashMap<Long, GenreEntity>();
        list = dataHelper.getAllGenres();
        Collections.sort(list, ListComparator.getInstance());

        list.add(0,getEmptyGenre());

        for (GenreEntity genre : list) {
            map.put(genre.getId(), genre);
            selectItems.add(new SelectItem(genre, genre.getName()));
        }

    }

    private GenreEntity getEmptyGenre() {
        GenreEntity genre = new GenreEntity();
        genre.setId(-1L);
        genre.setName("");
        return genre;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<GenreEntity> getGenreList() {
        return list;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((GenreEntity) value).getId().toString();
    }

    public BookListController getBookListController() {
        return bookListController;
    }

    public void setBookListController(BookListController bookListController) {
        this.bookListController = bookListController;
    }


}

