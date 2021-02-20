package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Evento.
 */
@Entity
@Table(name = "evento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "observacion")
    private String observacion;

    @OneToMany(mappedBy = "evento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalEvento> eventos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "eventos", allowSetters = true)
    private Parametros evento;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Evento fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public Evento observacion(String observacion) {
        this.observacion = observacion;
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Set<AnimalEvento> getEventos() {
        return eventos;
    }

    public Evento eventos(Set<AnimalEvento> animalEventos) {
        this.eventos = animalEventos;
        return this;
    }

    public Evento addEventos(AnimalEvento animalEvento) {
        this.eventos.add(animalEvento);
        animalEvento.setEvento(this);
        return this;
    }

    public Evento removeEventos(AnimalEvento animalEvento) {
        this.eventos.remove(animalEvento);
        animalEvento.setEvento(null);
        return this;
    }

    public void setEventos(Set<AnimalEvento> animalEventos) {
        this.eventos = animalEventos;
    }

    public Parametros getEvento() {
        return evento;
    }

    public Evento evento(Parametros parametros) {
        this.evento = parametros;
        return this;
    }

    public void setEvento(Parametros parametros) {
        this.evento = parametros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evento)) {
            return false;
        }
        return id != null && id.equals(((Evento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evento{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
