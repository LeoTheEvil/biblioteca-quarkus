import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
public class RecursoLibroTest {
    @InjectMock
    RepositorioLibro repoLibros;

    @InjectMock
    RepositorioPrestamos repoP;

    @Inject
    RecursoLibro recurso;

    @Test
    void testEliminarLibro_Exito() {
        when(repoLibros.deleteById(1L)).thenReturn(true);

        RestResponse<String> response = recurso.eliminarLibro(1L);

        assertEquals(200, response.getStatus());
        assertEquals("El libro 1 ha sido eliminado.", response.getEntity());
    }

    @Test
    void testEliminarLibro_NoEncontrado() {
        when(repoLibros.deleteById(1L)).thenReturn(false);
        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            recurso.eliminarLibro(1L);
        });
        assertEquals(404, exception.getResponse().getStatus());
        assertEquals("El libro 1 no esta en esta biblioteca.", exception.getResponse().getEntity());
    }
}