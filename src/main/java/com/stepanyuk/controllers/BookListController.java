package com.stepanyuk.controllers;

import com.stepanyuk.beans.Pager;
import com.stepanyuk.db.DataHelper;

import com.stepanyuk.entity.AuthorEntity;
import com.stepanyuk.entity.BookEntity;
import com.stepanyuk.entity.GenreEntity;
import com.stepanyuk.entity.PublisherEntity;
import com.stepanyuk.enums.SearchType;
import com.stepanyuk.models.BookListDataModel;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RateEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(eager = true, name = "bookListController")
@SessionScoped
public class BookListController implements Serializable {

    private ResourceBundle bundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    private DataGrid dataTable;
//    private DataTable dataTable;
    private BookEntity selectedBook;
    private DataHelper dataHelper;
    private LazyDataModel<BookEntity> bookListModel;
    private Long selectedAuthorId;// текущий автор книги из списка при редактировании книги
    // критерии поиска
    private char selectedLetter; // выбранная буква алфавита, по умолчанию не выбрана ни одна буква
    private SearchType selectedSearchType = SearchType.TITLE;// хранит выбранный тип поиска, по-умолчанию - по названию
    private long selectedGenreId; // выбранный жанр
    private String currentSearchString; // хранит поисковую строку
    private Pager pager;
    //-------
    private boolean editMode;// отображение режима редактирования
    private boolean addMode;// отображение режима добавление

    public BookListController() {
        pager = new Pager();
        dataHelper = new DataHelper(pager);
        bookListModel = new BookListDataModel(dataHelper, pager);
    }

    private void submitValues(Character selectedLetter, long selectedGenreId) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
        dataTable.setFirst(0);
    }

    public DataHelper getDataHelper() {
        return dataHelper;
    }

    //<editor-fold defaultstate="collapsed" desc="запросы в базу">
    private void fillBooksAll() {
        dataHelper.getAllBooks();
    }

    public void fillBooksByGenre() {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        selectedGenreId = Long.valueOf(params.get("genre_id"));

        submitValues(' ', selectedGenreId);
        if (selectedGenreId == Long.MAX_VALUE) {
            dataHelper.getAllBooks();
        } else {
            dataHelper.getBooksByGenre(selectedGenreId);
        }
    }

    public void fillBooksByLetter() {

//        cancelEditMode();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter_search").charAt(0);

        submitValues(selectedLetter, -1);


        dataHelper.getBooksByLetter(selectedLetter);


    }

    public void fillBooksBySearch() {

        cancelEditMode();

        submitValues(' ', -1);

        if (currentSearchString.trim().length() == 0) {
            fillBooksAll();

        }

        if (selectedSearchType == SearchType.AUTHOR) {
            dataHelper.getBooksByAuthor(currentSearchString);
        } else if (selectedSearchType == SearchType.TITLE) {
            dataHelper.getBooksByName(currentSearchString);
        }


    }

//    public void updateBooks() {
//
//        dataHelper.update();
//
//        cancelEditMode();
//
//        dataHelper.populateList();
//
//    }
    //</editor-fold>

    public void updateBook() {

        dataHelper.updateBook(selectedBook);
        cancelEditMode();
        dataHelper.populateList();

//        RequestContext.getCurrentInstance().execute("dlgEditBook.hide");

        ResourceBundle bundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("updated")));

        dataTable.setFirst(calcSelectedPage());

    }

//    public void buttonAction(ActionEvent actionEvent) {
//        dataHelper.updateBook(selectedBook);
//    }

//    public void handleChangeFieldBook(){
//        System.out.println("New value: ");
//
//    }

    public void handleChangeAuthorBook(ValueChangeEvent event) {
        selectedBook.setAuthor((AuthorEntity) event.getNewValue());
    }

    public void handleChangeGenreBook(ValueChangeEvent event) {
        selectedBook.setGenre((GenreEntity) event.getNewValue());
    }

    public void handleChangePublisherBook(ValueChangeEvent event) {
        selectedBook.setPublisher((PublisherEntity) event.getNewValue());
    }

    public void handleChangeDataBook(SelectEvent event) {
        Object ddd = event.getObject();
//        selectedBook.setPublishYear((Date) event.getObject());
        //selectedBook.setPublishYear((new SimpleDateFormat("yyyy-MM-dd")).format(event.getObject()));
    }


//    public void handleChangeFieldBook(AjaxBehaviorEvent event){
//        System.out.println("New value: " + event);
//    }


    public void deleteBook() {
        dataHelper.deleteBook(selectedBook);
        dataHelper.populateList();

//        RequestContext.getCurrentInstance().execute("dlgDeleteBook.hide()");
        ResourceBundle bundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("deleted")));

        dataTable.setFirst(calcSelectedPage());

    }

    private int calcSelectedPage() {
        int page = dataTable.getPage();// текущий номер страницы (индексация с нуля)

        int leftBound = pager.getTo() * (page - 1);
        int rightBound = pager.getTo() * page;

        boolean goPrevPage = pager.getTotalBooksCount() > leftBound & pager.getTotalBooksCount() <= rightBound;


        if (goPrevPage) {
            page--;
        }

        return (page > 0) ? page * pager.getTo() : 0;
    }

    public void switchEditMode() {

        editMode = true;
//        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').show()");
    }

    public void anyaction() {
        int uuu=2;
    }

    public void switchEditModeAction(ActionEvent actionEvent) {
        Map<String, Object> params = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        selectedBook = (BookEntity) params.get("bookBean");
        switchEditMode();
    }

    public void cancelEditMode() {
//        editModeView = false;
//        List<BookEntity> listsBook = pager.getList();
//        for (BookEntity cur_book : listsBook
//             ) {
//            cur_book.setEdit(false);
//        }

        editMode = false;
//        RequestContext.getCurrentInstance().execute("dlgEditBook.hide");

    }

    public Character[] getRussianLetters() {
        Character[] letters = new Character[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',};
        return letters;
    }

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        selectedSearchType = (SearchType) e.getNewValue();
    }


    //<editor-fold defaultstate="collapsed" desc="гетеры сетеры">
    public boolean isEditMode() {
        return editMode;
    }

    public String getSearchString() {
        return currentSearchString;
    }

    public void setSearchString(String searchString) {
        this.currentSearchString = searchString;
    }

    public SearchType getSearchType() {
        return selectedSearchType;
    }

    public void setSearchType(SearchType searchType) {
        this.selectedSearchType = searchType;
    }

    public long getSelectedGenreId() {
        return selectedGenreId;
    }

    public void setSelectedGenreId(long selectedGenreId) {
        this.selectedGenreId = selectedGenreId;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char selectedLetter) {
        this.selectedLetter = selectedLetter;
    }

    public Long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(Long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

    public Pager getPager() {
        return pager;
    }

    public LazyDataModel<BookEntity> getBookListModel() {
        return bookListModel;
    }

    public List<Character> getLetterListChar() {

        char[] charList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        List<Character> result = new ArrayList<>();

        for (char curChar : charList) {
            result.add(curChar);
        }

        return result;
    }

    public BookEntity getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(BookEntity selectedBook) {
        this.selectedBook = selectedBook;
    }
//
//    public DataTable getDataTable() {
//        return dataTable;
//    }
//
//    public void setDataTable(DataTable dataTable) {
//        this.dataTable = dataTable;
//    }

    public void fillBooksByRate(){
        dataHelper.getBooksByRate();
    }

    public DataGrid getDataGrid() {
        return dataTable;
    }

    public void setDataGrid(DataGrid dataTable) {
        this.dataTable = dataTable;
    }

    public void setRate(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int newRate = Integer.parseInt(params.get("newRate"));
        int bookIndex = Integer.parseInt(params.get("bookIndex"));

        BookEntity book = (BookEntity)pager.getList().get(bookIndex);
        book.setRating(newRate);

    }

    public void setRate_(RateEvent rateEvent) {
        Long rateVote     = Long.valueOf(rateEvent.getRating().toString());
        String selectedObjID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedObj");

        BookEntity rateBook = null;

        List<BookEntity> listBookPager = pager.getList();

        for (BookEntity curBook: listBookPager
             ) {
            if (curBook.getId() == Long.valueOf(selectedObjID)) {
                rateBook = curBook;

                break;
            }
        }

        if (rateBook != null) {
            String username = "user" + String.valueOf(new Random().nextInt(100));

            rateBook.setRating((Integer.parseInt(rateVote.toString())));

            dataHelper.rateBook(rateBook, username);
        }


    }

    public void rate() {
//        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        int bookIndex = Integer.parseInt(params.get("bookIndex"));
//        int newRate = Integer.parseInt(params.get("newRate"));
//
        int newRate = 0;
        if (newRate == 0) {
            return;
        }

//        FacesContext facesContext = FacesContext.getCurrentInstance();
        //String username = facesContext.getExternalContext().getUserPrincipal().getName();

//        String username = "user" + String.valueOf(new Random().nextInt(100));
//
//        BookEntity book = (BookEntity)pager.getList().get(bookIndex);
//
//        book.setRating(newRate);
//
//        dataHelper.rateBook(book, username);

    }

    public void cancelModes() {
        if (addMode) {
            addMode = false;
        }

        if (editMode) {
            editMode = false;
        }

        if (selectedBook != null) {
            selectedBook.setUploadedContent(null);
            selectedBook.setUploadedImage(null);
        }

//        RequestContext.getCurrentInstance().execute("dlgEditBook.hide()");
    }

    public ActionListener saveListener() {
        return new ActionListener() {
            @Override
            public void processAction(ActionEvent event) {
                System.err.println("ActionListener saveListener() book");

                if (!validateFields()) {
                    try {
                        throw new Exception("ActionListener saveListener() book");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    return;
                }


                if (editMode) {
                    dataHelper.updateBook(selectedBook);
                } else if (addMode) {
                    dataHelper.addBook(selectedBook.getNewBook());
                }

                cancelModes();
                dataHelper.populateList();

                ResourceBundle bundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("updated")));

                dataTable.setFirst(calcSelectedPage());
            }
        };
    }

    private boolean validateFields() {

        if (isNullOrEmpty(selectedBook.getAuthor())
                || isNullOrEmpty(selectedBook.getDescr())
                || isNullOrEmpty(selectedBook.getGenre())
                || isNullOrEmpty(selectedBook.getIsbn())
                || isNullOrEmpty(selectedBook.getName())
                || isNullOrEmpty(selectedBook.getPageCount())
                || isNullOrEmpty(selectedBook.getPublishYear())
                || isNullOrEmpty(selectedBook.getPublisher())) {
            failValidation(bundle.getString("error_fill_all_fields"));
            return false;

        }


        if (dataHelper.isIsbnExist(selectedBook.getIsbn(), selectedBook.getId())){
            failValidation(bundle.getString("error_isbn_exist"));
            return false;
        }

        if (addMode) {

            if (selectedBook.getUploadedContent() == null) {
                failValidation(bundle.getString("error_load_pdf"));
                return false;
            }

            if (selectedBook.getUploadedImage() == null) {
                failValidation(bundle.getString("error_load_image"));
                return false;
            }

        }


        return true;
    }

    private void failValidation(String message) {
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, bundle.getString("error")));
    }

    private boolean isNullOrEmpty(Object obj) {
        if (obj == null || obj.toString().equals("")) {
            return true;
        }

        return false;
    }

    public void switchAddMode() {
        addMode = true;
        selectedBook = new BookEntity().getNewBook();
//        RequestContext.getCurrentInstance().execute("dlgEditBook.show()");

    }


}
