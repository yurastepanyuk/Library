package com.stepanyuk.controllers;

import com.stepanyuk.beans.Pager;
import com.stepanyuk.comparators.ListComparator;
import com.stepanyuk.db.DataHelper;
import com.stepanyuk.entity.PublisherEntity;

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

@ManagedBean(eager = true)
@SessionScoped
public class PublisherController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();
    private Map<Long, PublisherEntity> map;
    private List<PublisherEntity> list;
    private Pager pager;
    private DataHelper dataHelper;
    @ManagedProperty(value = "#{bookListController}")
    private BookListController bookListController;

    @PostConstruct
    public void init() {
        pager = bookListController.getPager();
        dataHelper = bookListController.getDataHelper();

        map = new HashMap<Long, PublisherEntity>();
        list = dataHelper.getAllPublishers();

        Collections.sort(list, ListComparator.getInstance());

        list.add(0, getEmptyPublisher());

        for (PublisherEntity publisher : list) {
            map.put(publisher.getId(), publisher);
            selectItems.add(new SelectItem(publisher, publisher.getName()));
        }

    }

    private PublisherEntity getEmptyPublisher() {
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setId(-1L);
        publisherEntity.setName("");

        return publisherEntity;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<PublisherEntity> getPublisherList() {
        return list;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((PublisherEntity) value).getId().toString();
    }

    public BookListController getBookListController() {
        return bookListController;
    }

    public void setBookListController(BookListController bookListController) {
        this.bookListController = bookListController;
    }
}

