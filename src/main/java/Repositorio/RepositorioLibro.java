package Repositorio;

import Modelo.Libro;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepositorioLibro implements PanacheRepository<Libro> {

}