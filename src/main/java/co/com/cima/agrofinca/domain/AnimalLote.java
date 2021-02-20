package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A AnimalLote.
 */
@Entity
@Table(name = "animal_lote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "animallote")
public class AnimalLote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @ManyToOne
    @JsonIgnoreProperties(value = "lotes", allowSetters = true)
    private Animal animal;

    @ManyToOne
    @JsonIgnoreProperties(value = "animales", allowSetters = true)
    private Lote lote;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public AnimalLote fechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public AnimalLote fechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
        return this;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Animal getAnimal() {
        return animal;
    }

    public AnimalLote animal(Animal animal) {
        this.animal = animal;
        return this;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Lote getLote() {
        return lote;
    }

    public AnimalLote lote(Lote lote) {
        this.lote = lote;
        return this;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalLote)) {
            return false;
        }
        return id != null && id.equals(((AnimalLote) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalLote{" +
            "id=" + getId() +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", fechaSalida='" + getFechaSalida() + "'" +
            "}";
    }
}
