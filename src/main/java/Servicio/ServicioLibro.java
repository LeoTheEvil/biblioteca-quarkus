package Servicio;

import Modelo.Libro;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ServicioLibro {

    private List<Libro> biblioteca = new ArrayList<>();

    public void nuevoLibro(Libro libro) {
        biblioteca.add(libro);
    }

    public List<Libro> listarLibros() {
        return Collections.unmodifiableList(biblioteca);
    }

    public Libro getLibro(long idLibro) {
        Libro libroEncontrado = new Libro();
        for(Libro libro : biblioteca) {
            if (libro.getId() == idLibro) {
                libroEncontrado = libro;
            }
        }
        return libroEncontrado;
    }
}