import Modelo.Libro;
import Modelo.Prestamo;

public class Validador {
    public void validar(Libro libro) {
        if (libro.getId() < 1) {
            throw new ParametroIncorrecto("Identificador no valido");
        }
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

    public void validarP(Prestamo prestamo) {
        if (prestamo.getPrestatario().isBlank()) {
            throw new ParametroIncorrecto("El prestatario debe tener nombre.");
        }
    }
}