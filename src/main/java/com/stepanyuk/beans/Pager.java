package com.stepanyuk.beans;

import com.stepanyuk.entity.BookEntity;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class Pager<T> {

    private static Pager pager;

    private int rowIndex;

    private Long totalBooksCount;
    private BookEntity selectedBook;
    private List<BookEntity> list;
    private int from;
    private int to;

    private StreamedContent imageStream;

    public Pager() {
    }

//    public static Pager getInstance() {
//        if (pager == null) {
//            pager = new Pager();
//        }
//        return pager;
//    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<BookEntity> getList() {
        return list;
    }

    public void setList(List<BookEntity> list) {
        rowIndex = -1;
        this.list = list;
    }

    public void setTotalBooksCount(Long totalBooksCount) {
        this.totalBooksCount = totalBooksCount;
    }

    public Long getTotalBooksCount() {
        return totalBooksCount;
    }

    public BookEntity getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(BookEntity selectedBook) {
        this.selectedBook = selectedBook;
    }

    public int getRowIndex() {
        rowIndex+=1;
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public StreamedContent getImageStream(){
        InputStream stream = new ByteArrayInputStream(list.get(rowIndex+1).getImage());
        StreamedContent imageStream = new DefaultStreamedContent(stream, "image/jpeg");
        return imageStream;
    }

    private void setImageStream(StreamedContent input){
        this.imageStream = input;
    }

}
