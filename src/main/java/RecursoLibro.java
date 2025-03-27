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
import java.util.NoSuchElementException;

import static org.locationtech.jts.util.Debug.print;

@Path("/biblioteca")
@Transactional
public class RecursoLibro {

    @Inject
    private RepositorioLibro repo;

    @Inject
    private RepositorioPrestamos repoP;

    HashMap<Long, List<String>> librosPrestados = new HashMap<Long, List<String>>();
    List<String> listaPrestatarios = new ArrayList<String>();

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
        throw new NoSuchElementException("El libro" + idLibro + "no esta en esta biblioteca.");
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
        throw new NoSuchElementException("El libro" + idLibro + "no esta en esta biblioteca.");
    }

    @DELETE
    @Path("/{idLibro}")
    public void eliminarLibro(@PathParam("idLibro") long idLibro) {
        repo.deleteById(idLibro);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Prestamo> pedirLibro(Long idLibro, String nuevoPrestatario) {
        Prestamo prestamo = new Prestamo(idLibro, nuevoPrestatario);
        try {
            validador.validarP(prestamo);
        } catch (ParametroIncorrecto error) {
            throw new WebApplicationException(Response.status(400).entity(error.getMessage()).build());
        }
        if (libroPrestado(prestamo)) {
            listaPrestatarios.add(nuevoPrestatario);
        } else {
            repoP.persist(prestamo);
        }
        return RestResponse.ResponseBuilder.ok(prestamo, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{prestamo}")
    public boolean libroPrestado(@PathParam("prestamo") Prestamo prestamo) {
        var libroBuscado = repoP.findById(prestamo.getId());
        if (libroBuscado != null) {
            print("El libro " + prestamo.getId() + " ya esta prestado a " + prestamo.getPrestatario());
            return true;
        }
        print("El libro " + prestamo.getId() + "esta disponible");
        return false;
    }

    @DELETE
    @Path("/{idPrestamo}")
    public void devolverLibro(@PathParam("idPrestamo") long idLibro) {
        boolean encontrado = false;
        for (long i : librosPrestados.keySet()) {
            if (i == idLibro) {
                encontrado = true;
            }
        }
        if (encontrado) {
            repoP.deleteById(idLibro);
        } else {
            librosPrestados.remove(idLibro);
        }
    }
}