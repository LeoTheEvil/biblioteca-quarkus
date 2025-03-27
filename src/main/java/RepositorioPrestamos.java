import Modelo.Prestamo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepositorioPrestamos implements PanacheRepository<Prestamo> {

}