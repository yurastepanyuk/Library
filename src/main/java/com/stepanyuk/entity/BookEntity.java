package com.stepanyuk.entity;

import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.persistence.*;
import java.io.*;
import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book", schema = "library")
public class BookEntity implements Serializable {
    private Long id;
    private String name;
    private byte[] content;
    private int pageCount;
    private String isbn;
    private Date publishYear;
    private byte[] image;
    private String descr;

    private PublisherEntity publisher;
    private AuthorEntity author;
    private GenreEntity genre;

    @Transient
    private boolean edit;

    @Transient
    private boolean imageEdited;

    @Transient
    private boolean contentEdited;

    private Integer rating;

    private Long voteCount;

    private Set<VoteEntity> votes = new HashSet(0);

    @Transient
    private byte[] uploadedImage;

    @Transient
    private byte[] uploadedContent;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Basic
    @Column(name = "page_count")
    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Basic
    @Column(name = "isbn", length = 100)
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "publish_year")
    public Date getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Date publishYear) {
        this.publishYear = publishYear;
//        new java.sql.Date(publishYear.getTime());
    }
    @Transient
    private java.util.Date publishYearFormatted;
    @Transient
    public java.util.Date getPublishYearFormatted(){
        return new java.util.Date(getPublishYear() == null?0L:getPublishYear().getTime());
    }
    @Transient
    public void setPublishYearFormatted(java.util.Date newDate){
        setPublishYear(new java.sql.Date( newDate.getTime()));
    }

    @Basic
    @Column(name = "image", nullable = true)
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Basic
    @Column(name = "descr", length = 500)
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookEntity that = (BookEntity) o;

        if (id != that.id) return false;
        if (pageCount != that.pageCount) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!Arrays.equals(content, that.content)) return false;
        if (isbn != null ? !isbn.equals(that.isbn) : that.isbn != null) return false;
        if (publishYear != null ? !publishYear.equals(that.publishYear) : that.publishYear != null) return false;
        if (!Arrays.equals(image, that.image)) return false;
        if (descr != null ? !descr.equals(that.descr) : that.descr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + pageCount;
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        result = 31 * result + (publishYear != null ? publishYear.hashCode() : 0);
        result = 31 * result + (descr != null ? descr.hashCode() : 0);
        return result;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id" , referencedColumnName = "id")
    public PublisherEntity getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    @Transient
    public boolean isEdit() {
        return edit;
    }

    @Transient
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Transient
    public void setImageEdited(boolean imageEdited) {
        this.imageEdited = imageEdited;
    }

    @Transient
    public boolean isImageEdited() {
        return imageEdited;
    }

    @Transient
    public void setContentEdited(boolean contentEdited) {
        this.contentEdited = contentEdited;
    }

    @Transient
    public boolean isContentEdited() {
        return contentEdited;
    }

    public Integer getRating() {
        if (rating == null) {
            return 0;
        }
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getVoteCount() {
        if (voteCount == null) {
            return Long.valueOf(0);
        }
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.LAZY)
    public Set<VoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(Set<VoteEntity> votes) {
        this.votes = votes;
    }

    public void handleRate(RateEvent rateEvent) {
        Integer rate = (Integer) rateEvent.getRating();
        setRating(rate);
    }

    @Transient
    public BookEntity getNewBook(){
        BookEntity book = new BookEntity();
        book.setAuthor(getAuthor());
        book.setContent(getContent());
        book.setDescr(getDescr());
        book.setGenre(getGenre());
        book.setImage(getImage());
        book.setIsbn(getIsbn());
        book.setName(getName());
        book.setPageCount(getPageCount());
        book.setPublishYear(getPublishYear());
        book.setPublisher(getPublisher());
        book.setRating(getRating());
        book.setVoteCount(getVoteCount());
        book.setVotes(getVotes());
        return book;
    }

    @Transient
    public byte[] getUploadedContent() {
        return uploadedContent;
    }

    @Transient
    public byte[] getUploadedImage() {
        return uploadedImage;
    }

    @Transient
    public void setUploadedImage(byte[] uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    @Transient
    public void setUploadedContent(byte[] uploadedContent) {
        this.uploadedContent = uploadedContent;
    }
}
