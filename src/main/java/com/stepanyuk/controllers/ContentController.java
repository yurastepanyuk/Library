package com.stepanyuk.controllers;


import org.primefaces.event.FileUploadEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class ContentController implements Serializable {

//    private byte[] uploadedContent;
//    private boolean showContent;

    @ManagedProperty(value = "#{bookListController}")
    private BookListController bookListController;

    public BookListController getBookListController() {
        return bookListController;
    }

    public void setBookListController(BookListController bookListController) {
        this.bookListController = bookListController;
    }

    public void handleFileUpload(FileUploadEvent event) {
        bookListController.getSelectedBook().setUploadedContent(event.getFile().getContents());
//        uploadedContent = event.getFile().getContents();
//        if (uploadedContent!=null && uploadedContent.length>0){
//            showContent = true;
//        }
    }

    public ActionListener saveListener() {
        return new ActionListener() {
            @Override
            public void processAction(ActionEvent event) {
//                if (uploadedContent != null) {
//                    bookListController.getSelectedBook().setContent(uploadedContent);
//                    bookListController.getSelectedBook().setContentEdited(true);
//                }
//                clear();
            }
        };
    }

    public ActionListener clearListener() {
        return new ActionListener() {
            @Override
            public void processAction(ActionEvent event) {
//                clear();
            }
        };
    }

//    public void clear() {
//        uploadedContent = null;
//        showContent = false;
//    }


    public boolean isShowContent() {
        if (bookListController.getSelectedBook().getUploadedContent() != null) {
            return true;
        }

        return false;
//        return showContent;
    }

    public byte[] getUploadedContent() {
        return bookListController.getSelectedBook().getUploadedContent();
//        return uploadedContent;
    }

}
