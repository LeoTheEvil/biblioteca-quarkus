import Modelo.Libro;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.NoSuchElementException;

@Path("/biblioteca")
@Transactional
public class RecursoLibro {

    @Inject
    private RepositorioLibro repo;

    private Validador validador = new Validador();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Libro> guardarLibro(Libro libro) {
        try {
            validador.validar(libro);
        } catch (ParametroIncorrecto error) {
            throw new WebApplicationException(Response.status(400).entity(error.getMessage()).build());
        }
        try {
            repo.persist(libro);
        } catch (Exception e) {
            throw new WebApplicationException("Error al guardar el libro", Response.Status.INTERNAL_SERVER_ERROR);
        }
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
}