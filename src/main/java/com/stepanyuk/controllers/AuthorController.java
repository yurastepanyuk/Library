package com.stepanyuk.controllers;

import com.stepanyuk.beans.Pager;
import com.stepanyuk.comparators.ListComparator;
import com.stepanyuk.db.DataHelper;
import com.stepanyuk.entity.AuthorEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AuthorController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();
    private Map<Long, AuthorEntity> map;
    private List<AuthorEntity> list;
    private Pager pager;
    private DataHelper dataHelper;
    @ManagedProperty(value = "#{bookListController}")
    private BookListController bookListController;

    @PostConstruct
    public void init() {
        pager = bookListController.getPager();
        dataHelper = bookListController.getDataHelper();

        map = new HashMap<Long, AuthorEntity>();
        list = dataHelper.getAllAuthors();
        Collections.sort(list, ListComparator.getInstance());

        list.add(0, getEmptyAuthor());

        for (AuthorEntity author : list) {
            map.put(author.getId(), author);
            selectItems.add(new SelectItem(author, author.getFio()));
        }
    }

    private AuthorEntity getEmptyAuthor() {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(-1L);
        authorEntity.setFio("");
        return authorEntity;
    }

    public List<AuthorEntity> getAuthorList() {
        return list;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((AuthorEntity) value).getId().toString();
    }

    public BookListController getBookListController() {
        return bookListController;
    }

    public void setBookListController(BookListController bookListController) {
        this.bookListController = bookListController;
    }
}
