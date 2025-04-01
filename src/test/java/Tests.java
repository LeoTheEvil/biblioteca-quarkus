import Modelo.Libro;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class Tests {
    @Test
    void debe_crear_un_libro_y_buscarlo_por_su_ID() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(200)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).when().get("/biblioteca/"+id).then().body("id", equalTo(id));
    }
    @Test
    void debe_actualizar_un_libro() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        Long id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Long.class);
        libro.setAuthor("Miguel de Cervantes Saavedra");
        given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().put("/biblioteca/"+id).then().statusCode(200);
        given().port(port).when().get("/biblioteca/"+id).then().body("author", equalTo("Miguel de Cervantes Saavedra"));
    }
    @Test
    void debe_borrar_un_libro() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).when().delete("/biblioteca/"+id).then().statusCode(204);
        given().port(port).when().get("/biblioteca/"+id).then().statusCode(404);
    }
    @Test
    void debe_listar_todos_los_libros() {
        Libro libro1 = new Libro();
        libro1.setTitle("Don Quijote de la Mancha");
        libro1.setAuthor("Miguel de Cervantes");
        libro1.setGenre("Comedia");
        int id1 = given().port(port).body(libro1).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        Libro libro2 = new Libro();
        libro2.setTitle("Cinco Semanas en Globo");
        libro2.setAuthor("Julio Verne");
        libro2.setGenre("Aventura");
        int id2 = given().port(port).body(libro2).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).queryParam("offset", 0).queryParam("size", 100).when().get("/api/books/").then().body("$",
                hasItem(
                        allOf(
                                hasEntry("id", id1)
                        )
                )
        ).body("$",
                hasItem(
                        allOf(
                                hasEntry("id", id2)
                        )
                )
        );
    }
    @Test
    void debe_fallar_en_crear_un_libro_por_titulo_vacio() {
        Libro libro = new Libro();
        libro.setTitle("");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(400);
    }
    @Test
    void debe_fallar_en_crear_un_libro_por_genero_vacio() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("");
        given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(400);
    }
    @Test
    void debe_fallar_en_encontrar_un_libro() {
        given().port(port).when().get("/biblioteca/900").then().statusCode(404);
    }
    @Test
    void debe_fallar_en_actualizar_un_libro() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        libro.setAuthor("");
        given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().put("/biblioteca/"+id).then().statusCode(400);
    }
    @Test
    void debe_fallar_en_borrar_un_libro() {
        given().port(port).when().delete("/biblioteca/901").then().statusCode(404);
    }
    @Test
    void debe_mandar_una_A_en_el_offset() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).queryParam("offset", "A").when().get("/biblioteca/").then().statusCode(400);
    }
    @Test
    void debe_mandar_un_mas_en_el_size() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).queryParam("size", "+").when().get("/biblioteca/").then().statusCode(400);
    }
    @Test
    void debe_mandar_un_0_en_el_size() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).queryParam("size", "0").when().get("/biblioteca/").then().statusCode(400);
    }
    @Test
    void debe_mandar_un_menos_1_en_el_offset() {
        Libro libro = new Libro();
        libro.setTitle("Don Quijote de la Mancha");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).queryParam("offset", "-1").when().get("/biblioteca/").then().statusCode(400);
    }
    @Test
    void debe_mandar_una_comilla_en_el_titulo() {
        Libro libro = new Libro();
        libro.setTitle("\"");
        libro.setAuthor("Miguel de Cervantes");
        libro.setGenre("Comedia");
        int id = given().port(port).body(libro).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
    }
    @Test
    void debe_paginar_la_lista_de_libros() {
        Libro libro1 = new Libro();
        libro1.setTitle("Don Quijote de la Mancha");
        libro1.setAuthor("Miguel de Cervantes");
        libro1.setGenre("Comedia");
        int id1 = given().port(port).body(libro1).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        Libro libro2 = new Libro();
        libro2.setTitle("Cinco Semanas en Globo");
        libro2.setAuthor("Julio Verne");
        libro2.setGenre("Aventura");
        int id2 = given().port(port).body(libro2).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        Libro libro3 = new Libro();
        libro3.setTitle("La Liga de los Pelirrojos");
        libro3.setAuthor("Arthur Conan Doyle");
        libro3.setGenre("Misterio");
        int id3 = given().port(port).body(libro2).contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString()).when().post("/biblioteca").then().statusCode(201)
                .extract().jsonPath().getObject("id",Integer.class);
        given().port(port).queryParam("offset", 0).queryParam("size", 2).when().get("/api/books/").then().body("size()",is(2));
    }
}