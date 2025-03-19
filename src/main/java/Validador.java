import Modelo.Libro;

public class Validador {
    public void validar(Libro libro) {
        if (libro.getTitle().isBlank()) {
            throw new ParametroIncorrecto("El titulo no puede ser vacio.");
        }
        if (libro.getAuthor().isBlank()) {
            throw new ParametroIncorrecto("El autor no puede ser vacio.");
        }
        if (libro.getGenre().isBlank()) {
            throw new ParametroIncorrecto("El genero no puede ser vacio.");
        }
    }
}