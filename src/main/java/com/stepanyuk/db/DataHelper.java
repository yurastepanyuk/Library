package com.stepanyuk.db;

import com.stepanyuk.beans.Pager;
import com.stepanyuk.entity.*;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;

import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DataHelper {

    private SessionFactory sessionFactory = null;
    private static DataHelper dataHelper;
    private DetachedCriteria bookListCriteria;
    private DetachedCriteria booksCountCriteria;
    private ProjectionList bookProjection;

    private Pager pager;

    public DataHelper(Pager pager) {

        this.pager = pager;

        prepareCriterias();

        sessionFactory = HibernateUtil.getSessionFactory();

        bookProjection = Projections.projectionList();
        bookProjection.add(Projections.property("id"), "id");
        bookProjection.add(Projections.property("name"), "name");
        bookProjection.add(Projections.property("image"), "image");
        bookProjection.add(Projections.property("genre"), "genre");
        bookProjection.add(Projections.property("pageCount"), "pageCount");
        bookProjection.add(Projections.property("isbn"), "isbn");
        bookProjection.add(Projections.property("publisher"), "publisher");
        bookProjection.add(Projections.property("author"), "author");
        bookProjection.add(Projections.property("publishYear"), "publishYear");
        bookProjection.add(Projections.property("descr"), "descr");
        bookProjection.add(Projections.property("rating"), "rating");
        bookProjection.add(Projections.property("voteCount"), "voteCount");

        getAllBooks();
    }

//    public static DataHelper getInstance() {
//        if (dataHelper == null) {
//            dataHelper = new DataHelper();
//        }
//        return dataHelper;
//    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<GenreEntity> getAllGenres() {

        List<GenreEntity> genreList = getSession().createCriteria(GenreEntity.class).list();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("nls.messagess", FacesContext.getCurrentInstance().getViewRoot().getLocale());

        GenreEntity genreAll = new GenreEntity();
        genreAll.setId(Long.MAX_VALUE);
        genreAll.setName(resourceBundle.getString("book_all"));

        genreList.add(0, genreAll);

        return genreList;
    }

    public List<PublisherEntity> getAllPublishers() {
        return getSession().createCriteria(PublisherEntity.class).list();
    }

    public List<AuthorEntity> getAllAuthors() {
        return getSession().createCriteria(AuthorEntity.class).list();
    }

    public AuthorEntity getAuthor(long id) {
        return (AuthorEntity) getSession().get(AuthorEntity.class, id);
    }

    public void getAllBooks() {
        prepareCriterias();
        populateList();
    }

    public void getBooksByGenre(Long genreId) {

        Criterion criterion = Restrictions.eq("genre.id", genreId);

        prepareCriterias(criterion);
        populateList();
    }

    public void getBooksByLetter(Character letter) {

        Criterion criterion = Restrictions.ilike("b.name", letter.toString(), MatchMode.START);

        prepareCriterias(criterion);
        populateList();
    }

    public void getBooksByAuthor(String authorName) {

        Criterion criterion = Restrictions.ilike("author.fio", authorName, MatchMode.ANYWHERE);

        prepareCriterias(criterion);
        populateList();
    }

    public void getBooksByName(String bookName) {

        Criterion criterion = Restrictions.ilike("b.name", bookName, MatchMode.ANYWHERE);

        prepareCriterias(criterion);
        populateList();
    }

    public byte[] getContent(Long id) {
        Criteria criteria = getSession().createCriteria(BookEntity.class);
        criteria.setProjection(Property.forName("content"));
        criteria.add(Restrictions.eq("id", id));
        return (byte[]) criteria.uniqueResult();

//        ProjectionList idProjection = Projections.projectionList();
//        idProjection.add(Projections.property("content"), "content");
//        criteria.setProjection(idProjection);

//        byte[] content = (byte[])criteria.uniqueResult();
        //return content;
//        List<BookEntity> ss = criteria.list();
        //return null;
    }

    private void runBookListCriteria() {
        Criteria criteria = bookListCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.asc("b.name")).setProjection(bookProjection).setResultTransformer(Transformers.aliasToBean(BookEntity.class));

        criteria.setFirstResult(pager.getFrom()).setMaxResults(pager.getTo());

        List<BookEntity> list = criteria.list();
        pager.setList(list);
    }

    private void runCountCriteria() {
        Criteria criteria = booksCountCriteria.getExecutableCriteria(getSession());
        Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        pager.setTotalBooksCount(total);
    }

    public void update() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        for (Object object : pager.getList()) {
            BookEntity book = (BookEntity) object;
            if (book.isEdit()) {
                session.update(book);
            }

        }
        transaction.commit();
        session.flush();
        session.close();

    }

    private void prepareCriterias(Criterion criterion) {
        bookListCriteria = DetachedCriteria.forClass(BookEntity.class, "b");
        createAliases(bookListCriteria);
        bookListCriteria.add(criterion);

        booksCountCriteria = DetachedCriteria.forClass(BookEntity.class, "b");
        createAliases(booksCountCriteria);
        booksCountCriteria.add(criterion);
    }

    private void prepareCriterias() {
        bookListCriteria = DetachedCriteria.forClass(BookEntity.class, "b");
        createAliases(bookListCriteria);

        booksCountCriteria = DetachedCriteria.forClass(BookEntity.class, "b");
        createAliases(booksCountCriteria);
    }

    private void createAliases(DetachedCriteria criteria) {
        criteria.createAlias("b.author", "author");
        criteria.createAlias("b.genre", "genre");
        criteria.createAlias("b.publisher", "publisher");
    }

    private void createAliases(Criteria criteria) {
        criteria.createAlias("b.author", "author");
        criteria.createAlias("b.genre", "genre");
        criteria.createAlias("b.publisher", "publisher");
    }

    public void populateList() {
        runCountCriteria();
        runBookListCriteria();
    }

    public void deleteBook(BookEntity book) {
        Query query = getSession().createQuery("delete from BookEntity where id = :id");
        query.setParameter("id", book.getId());
        int result = query.executeUpdate();
    }

    public void updateBook(BookEntity book) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update BookEntity ");
        queryBuilder.append("set name = :name, ");
        queryBuilder.append("pageCount = :pageCount, ");
        queryBuilder.append("isbn = :isbn, ");
        queryBuilder.append("genre = :genre, ");
        queryBuilder.append("author = :author, ");
        queryBuilder.append("publishYear = :publishYear, ");
        queryBuilder.append("publisher = :publisher, ");

        if (book.isImageEdited()) {
            queryBuilder.append("image = :image, ");
        }

        if (book.isContentEdited()) {
            queryBuilder.append("content = :content, ");
        }

        queryBuilder.append("descr = :descr ");

        queryBuilder.append("where id = :id");


        Query query = getSession().createQuery(queryBuilder.toString());


        query.setParameter("name", book.getName());
        query.setParameter("pageCount", book.getPageCount());
        query.setParameter("isbn", book.getIsbn());
        query.setParameter("genre", book.getGenre());
        query.setParameter("author", book.getAuthor());
        query.setParameter("publishYear", book.getPublishYear());
        query.setParameter("publisher", book.getPublisher());
        query.setParameter("descr", book.getDescr());
        query.setParameter("id", book.getId());

        if (book.isImageEdited()) {
            query.setParameter("image", book.getImage());
        }

        if (book.isContentEdited()) {
            query.setParameter("content", book.getContent());
        }


        int result = query.executeUpdate();

    }

    public void addBook(BookEntity book) {
        getSession().save(book);
    }

    public void rateBook(BookEntity book, String username) {
        VoteEntity vote = new VoteEntity();
        vote.setBook(book);
        vote.setUsername(username);
        vote.setValue((book.getRating() == null)?0:book.getRating());
        getSession().save(vote);

        updateBookRate(book);

    }

    private int calculateAverage(List <Integer> values) {
        Integer sum = 0;
        if(!values.isEmpty()) {
            for (Integer mark : values) {
                sum += mark;
            }
            return sum / values.size();
        }
        return sum;
    }

    private void updateBookRate(BookEntity book) {

        //Query query = getSession().createQuery("select new map(round(avg(value)) as rating, count(value) as voteCount)  from VoteEnity v where v.book.id=:id");
        Query query = getSession().createQuery("select v.value  from VoteEntity v where v.book.id=:id");
        query.setParameter("id", book.getId());

        List list = query.list();

        long voteCount = list.size();
        Integer rating = calculateAverage(list);


        //long voteCount = Long.valueOf(map.get("voteCount").toString());
        //int rating = Long.valueOf(map.get("rating").toString()).intValue();

        Query query2 = getSession().createQuery("update BookEntity set rating = :rating, "
                + " voteCount = :voteCount"
                + " where id = :id");

        query2.setParameter("rating", rating);
        query2.setParameter("voteCount", voteCount);
        query2.setParameter("id", book.getId());

        int result = query2.executeUpdate();

    }

    public void getBooksByRate() {
        prepareOrderedCriterias("rating");
        populateList();
    }

    private void prepareOrderedCriterias(String field) {
        bookListCriteria = DetachedCriteria.forClass(BookEntity.class, "b");
        bookListCriteria.addOrder(Order.desc("b."+field));
        createAliases(bookListCriteria);

        booksCountCriteria = DetachedCriteria.forClass(BookEntity.class, "b");
        createAliases(booksCountCriteria);
    }

    public boolean isIsbnExist(String isbn, Long id) {
        Criteria criteria = getSession().createCriteria(BookEntity.class,"b");
        createAliases(criteria);
        criteria.add(Restrictions.ilike("b.isbn", isbn, MatchMode.EXACT));

        if (id!=null){
            criteria.add(Restrictions.not(Restrictions.eq("b.id", id)));
        }

        Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

        return total>=1;
    }
}

