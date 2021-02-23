package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A AnimalPeso.
 */
@Entity
@Table(name = "animal_peso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "animalpeso")
public class AnimalPeso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "peso", precision = 21, scale = 2, nullable = false)
    private BigDecimal peso;

    @ManyToOne
    @JsonIgnoreProperties(value = "pesos", allowSetters = true)
    private AnimalEvento evento;

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

    public AnimalPeso fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public AnimalPeso peso(BigDecimal peso) {
        this.peso = peso;
        return this;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public AnimalEvento getEvento() {
        return evento;
    }

    public AnimalPeso animal(AnimalEvento animalEvento) {
        this.evento = animalEvento;
        return this;
    }

    public void setAnimal(AnimalEvento animalEvento) {
        this.evento = animalEvento;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalPeso)) {
            return false;
        }
        return id != null && id.equals(((AnimalPeso) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalPeso{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", peso=" + getPeso() +
            "}";
    }
}
