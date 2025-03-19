package Modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Libro {

    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String author;
    private String genre;

    public Libro() {

    }

    public Libro(long id, String title, String author, String genre) {
        this.id=id;
        this.title=title;
        this.author=author;
        this.genre=genre;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id=id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author=author;
    }
    @JsonProperty("genre")
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre=genre;
    }

    @Override
    public String toString() {
        return "Id: " + id + ", Title: " + title + ", Author: " + author + ", Genre: " + genre;
    }
}