package com.stepanyuk.models;


import com.stepanyuk.beans.Pager;
import com.stepanyuk.db.DataHelper;
import com.stepanyuk.entity.BookEntity;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

public class BookListDataModel extends LazyDataModel<BookEntity> {

    private List<BookEntity> bookList;
    private DataHelper       dataHelper ;//= DataHelper.getInstance()
    private Pager            pager;//= Pager.getInstance()

    public BookListDataModel() {

    }

    public BookListDataModel(DataHelper dataHelper, Pager pager) {
        this.dataHelper = dataHelper;
        this.pager = pager;
    }

    @Override
    public BookEntity getRowData(String rowKey) {

//        for(BookEntity book : bookList) {
        List<BookEntity> listsBook = pager.getList();
        for(BookEntity book : listsBook) {
            if(book.getId() == Long.valueOf(rowKey))
                return book;
        }
        return null;
    }

    @Override
    public Object getRowKey(BookEntity book) {
        return book.getId();
    }


    @Override
    public List<BookEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

//        if (first == pager.getFrom() && pager.getList() != null) {
//            return pager.getList();
//        }

        pager.setFrom(first);
        pager.setTo(pageSize);

        dataHelper.populateList();

        this.setRowCount(pager.getTotalBooksCount().intValue());

        return pager.getList();
    }

}
