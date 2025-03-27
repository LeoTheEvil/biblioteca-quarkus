package Modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Prestamo {
    @Id
    private long id;
    private String prestatario;

    public Prestamo() {

    }

    public Prestamo(long id, String prestatario) {
        this.id=id;
        this.prestatario=prestatario;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id=id;
    }

    @JsonProperty("prestatario")
    public String getPrestatario() {
        return prestatario;
    }
    public void setPrestatario(String prestatario) {
        this.prestatario=prestatario;
    }

    @Override
    public String toString() {
        return "Id: " + id + ", Prestatario: " + prestatario;
    }
}