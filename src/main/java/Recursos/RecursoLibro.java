package Recursos;

import Modelo.Libro;
import Servicio.ServicioLibro;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/biblioteca")
public class RecursoLibro {

    private ServicioLibro servicio;

    @Inject
    public RecursoLibro(ServicioLibro servicio) {
        this.servicio = servicio;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Libro guardarLibro(Libro libro) {
        servicio.nuevoLibro(libro);
        return libro;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Libro> obtenerTodosLibros() {
        return servicio.listarLibros();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/obtenerLibro")
    public Libro obtenerLibro(long idLibro) {
        return servicio.getLibro(idLibro);
    }
}