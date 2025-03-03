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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author=author;
    }
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