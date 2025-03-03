import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.NoSuchElementException;

@Path("/biblioteca")
@Transactional
public class RecursoLibro {

    @Inject
    private RepositorioLibro repo;

    @Inject
    private Validador validador;

    @POST
    public Libro guardarLibro(Libro libro) {
        validador.validar(libro);
        repo.persist(libro);
        return libro;
    }

    @GET
    public List<Libro> obtenerTodosLibros() {
        return repo.listAll();
    }

    @GET
    @Path("/{idLibro}")
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