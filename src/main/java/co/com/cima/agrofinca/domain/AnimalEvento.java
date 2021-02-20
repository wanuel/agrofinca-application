package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AnimalEvento.
 */
@Entity
@Table(name = "animal_evento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "animalevento")
public class AnimalEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "evento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalSalud> tratamientos = new HashSet<>();

    @OneToMany(mappedBy = "animal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalPeso> pesos = new HashSet<>();

    @OneToMany(mappedBy = "animal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalCostos> costos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "eventos", allowSetters = true)
    private Animal animal;

    @ManyToOne
    @JsonIgnoreProperties(value = "eventos", allowSetters = true)
    private Evento evento;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AnimalSalud> getTratamientos() {
        return tratamientos;
    }

    public AnimalEvento tratamientos(Set<AnimalSalud> animalSaluds) {
        this.tratamientos = animalSaluds;
        return this;
    }

    public AnimalEvento addTratamientos(AnimalSalud animalSalud) {
        this.tratamientos.add(animalSalud);
        animalSalud.setEvento(this);
        return this;
    }

    public AnimalEvento removeTratamientos(AnimalSalud animalSalud) {
        this.tratamientos.remove(animalSalud);
        animalSalud.setEvento(null);
        return this;
    }

    public void setTratamientos(Set<AnimalSalud> animalSaluds) {
        this.tratamientos = animalSaluds;
    }

    public Set<AnimalPeso> getPesos() {
        return pesos;
    }

    public AnimalEvento pesos(Set<AnimalPeso> animalPesos) {
        this.pesos = animalPesos;
        return this;
    }

    public AnimalEvento addPesos(AnimalPeso animalPeso) {
        this.pesos.add(animalPeso);
        animalPeso.setAnimal(this);
        return this;
    }

    public AnimalEvento removePesos(AnimalPeso animalPeso) {
        this.pesos.remove(animalPeso);
        animalPeso.setAnimal(null);
        return this;
    }

    public void setPesos(Set<AnimalPeso> animalPesos) {
        this.pesos = animalPesos;
    }

    public Set<AnimalCostos> getCostos() {
        return costos;
    }

    public AnimalEvento costos(Set<AnimalCostos> animalCostos) {
        this.costos = animalCostos;
        return this;
    }

    public AnimalEvento addCostos(AnimalCostos animalCostos) {
        this.costos.add(animalCostos);
        animalCostos.setAnimal(this);
        return this;
    }

    public AnimalEvento removeCostos(AnimalCostos animalCostos) {
        this.costos.remove(animalCostos);
        animalCostos.setAnimal(null);
        return this;
    }

    public void setCostos(Set<AnimalCostos> animalCostos) {
        this.costos = animalCostos;
    }

    public Animal getAnimal() {
        return animal;
    }

    public AnimalEvento animal(Animal animal) {
        this.animal = animal;
        return this;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Evento getEvento() {
        return evento;
    }

    public AnimalEvento evento(Evento evento) {
        this.evento = evento;
        return this;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalEvento)) {
            return false;
        }
        return id != null && id.equals(((AnimalEvento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalEvento{" +
            "id=" + getId() +
            "}";
    }
}
