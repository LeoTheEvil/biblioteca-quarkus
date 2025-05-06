import Modelo.Libro;
import Modelo.Prestamo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/biblioteca")
@Transactional
public class RecursoLibro {
    @Inject
    private RepositorioLibro repo;
    @Inject
    private RepositorioPrestamos repoP;

    HashMap<Long, List<String>> librosPrestados = new HashMap<Long, List<String>>();
    private Validador validador = new Validador();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Libro> guardarLibro(Libro libro) {
        try {
            validador.validar(libro);
        } catch (ParametroIncorrecto error) {
            throw new WebApplicationException(Response.status(400).entity(error.getMessage()).build());
        }
        repo.persist(libro);
        List<String> listaPrestatarios = new ArrayList<String>();
        librosPrestados.put(libro.getId(), listaPrestatarios);
        return RestResponse.ResponseBuilder.ok(libro, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Libro> obtenerTodosLibros(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("size") @DefaultValue("1") int size) {
        return repo.findAll().page(offset, size).list();
    }

    @GET
    @Path("/{idLibro}")
    @Produces(MediaType.APPLICATION_JSON)
    public Libro obtenerLibro(@PathParam("idLibro") Long idLibro) {
        var libroBuscado = repo.findById(idLibro);
        if (libroBuscado != null) {
            return libroBuscado;
        }
        throw new WebApplicationException(Response.status(404).entity("El libro " + idLibro + " no esta en esta biblioteca.").build());
    }

    @PUT
    @Path("/{idLibro}")
    public Libro libroAModificar(@PathParam("idLibro") long idLibro, Libro libro) {
        var libroBuscado = repo.findById(idLibro);
        if (libroBuscado != null) {
            libroBuscado.setId(libro.getId());
            libroBuscado.setTitle(libro.getTitle());
            libroBuscado.setAuthor(libro.getAuthor());
            libroBuscado.setGenre(libro.getGenre());
            repo.persist(libroBuscado);
            return libroBuscado;
        }
        throw new WebApplicationException(Response.status(404).entity("El libro " + idLibro + " no esta en esta biblioteca.").build());
    }

    @DELETE
    @Path("/{idLibro}")
    @Produces("plain/text")
    public RestResponse<String> eliminarLibro(@PathParam("idLibro") long idLibro) {
        if (repo.deleteById(idLibro)) {
            repoP.deleteById(idLibro);
            librosPrestados.remove(idLibro);
            return RestResponse.ResponseBuilder.ok("El libro " + idLibro + " ha sido eliminado.").build();
        } else {
            throw new WebApplicationException(Response.status(404).entity("El libro " + idLibro + " no esta en esta biblioteca.").build());
        }
    }

    @POST
    @Path("/prestamos")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Prestamo> pedirLibro(Prestamo prestamo) {
        try {
            validador.validarP(prestamo);
        } catch (ParametroIncorrecto error) {
            throw new WebApplicationException(Response.status(400).entity(error.getMessage()).build());
        }
        var libroBuscado = repo.findById(prestamo.getId());
        if (libroBuscado != null) {
            List<String> listaPrestatarios = librosPrestados.get(prestamo.getId());
            String mensaje = null;
            if (listaPrestatarios != null && !listaPrestatarios.isEmpty()) {
                mensaje = "El libro " + prestamo.getId() + " ya esta prestado a " + listaPrestatarios.get(0);
            }
            if (libroPrestado(prestamo.getId())) {
                listaPrestatarios.add(prestamo.getPrestatario());
                throw new WebApplicationException(Response.status(409).entity(mensaje).build());
            } else {
                repoP.persist(prestamo);
                return RestResponse.ResponseBuilder.ok(prestamo, MediaType.APPLICATION_JSON).build();
            }
        } else {
            throw new WebApplicationException(Response.status(404).entity("El libro " + prestamo.getId() + " no esta en esta biblioteca.").build());
        }
    }

    @GET
    @Path("/prestamos/{idLibro}")
    public boolean libroPrestado(@PathParam("idLibro") long idLibro) {
        var libroBuscado = repoP.findById(idLibro);
        if (libroBuscado != null) {return true;} else {return false;}
    }

    @DELETE
    @Path("/prestamos/{idPrestamo}")
    public void devolverLibro(@PathParam("idPrestamo") long idLibro) {
        List<String> listaPrestatarios = librosPrestados.get(idLibro);
        boolean encontrado = false;
        for (long i : librosPrestados.keySet()) {
            if (i == idLibro) {
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            if (listaPrestatarios.isEmpty()) {
                librosPrestados.remove(idLibro);
            } else {
                listaPrestatarios.remove(0);
            }
        } else {
            repoP.deleteById(idLibro);
        }
    }
}