package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A AnimalCostos.
 */
@Entity
@Table(name = "animal_costos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "animalcostos")
public class AnimalCostos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JsonIgnoreProperties(value = "costos", allowSetters = true)
    private AnimalEvento animal;

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

    public AnimalCostos fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public AnimalCostos valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public AnimalEvento getAnimal() {
        return animal;
    }

    public AnimalCostos animal(AnimalEvento animalEvento) {
        this.animal = animalEvento;
        return this;
    }

    public void setAnimal(AnimalEvento animalEvento) {
        this.animal = animalEvento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalCostos)) {
            return false;
        }
        return id != null && id.equals(((AnimalCostos) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalCostos{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
