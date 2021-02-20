package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Parametros.
 */
@Entity
@Table(name = "parametros")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "parametros")
public class Parametros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "evento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Evento> eventos = new HashSet<>();

    @OneToMany(mappedBy = "medicamento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalSalud> medicamentos = new HashSet<>();

    @OneToMany(mappedBy = "agrupador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Parametros> parametros = new HashSet<>();

    @OneToMany(mappedBy = "tipo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Lote> tipoLotes = new HashSet<>();

    @OneToMany(mappedBy = "tipo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Animal> tipos = new HashSet<>();

    @OneToMany(mappedBy = "raza")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Animal> razas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "parametros", allowSetters = true)
    private Parametros agrupador;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Parametros nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Parametros descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Evento> getEventos() {
        return eventos;
    }

    public Parametros eventos(Set<Evento> eventos) {
        this.eventos = eventos;
        return this;
    }

    public Parametros addEventos(Evento evento) {
        this.eventos.add(evento);
        evento.setEvento(this);
        return this;
    }

    public Parametros removeEventos(Evento evento) {
        this.eventos.remove(evento);
        evento.setEvento(null);
        return this;
    }

    public void setEventos(Set<Evento> eventos) {
        this.eventos = eventos;
    }

    public Set<AnimalSalud> getMedicamentos() {
        return medicamentos;
    }

    public Parametros medicamentos(Set<AnimalSalud> animalSaluds) {
        this.medicamentos = animalSaluds;
        return this;
    }

    public Parametros addMedicamentos(AnimalSalud animalSalud) {
        this.medicamentos.add(animalSalud);
        animalSalud.setMedicamento(this);
        return this;
    }

    public Parametros removeMedicamentos(AnimalSalud animalSalud) {
        this.medicamentos.remove(animalSalud);
        animalSalud.setMedicamento(null);
        return this;
    }

    public void setMedicamentos(Set<AnimalSalud> animalSaluds) {
        this.medicamentos = animalSaluds;
    }

    public Set<Parametros> getParametros() {
        return parametros;
    }

    public Parametros parametros(Set<Parametros> parametros) {
        this.parametros = parametros;
        return this;
    }

    public Parametros addParametros(Parametros parametros) {
        this.parametros.add(parametros);
        parametros.setAgrupador(this);
        return this;
    }

    public Parametros removeParametros(Parametros parametros) {
        this.parametros.remove(parametros);
        parametros.setAgrupador(null);
        return this;
    }

    public void setParametros(Set<Parametros> parametros) {
        this.parametros = parametros;
    }

    public Set<Lote> getTipoLotes() {
        return tipoLotes;
    }

    public Parametros tipoLotes(Set<Lote> lotes) {
        this.tipoLotes = lotes;
        return this;
    }

    public Parametros addTipoLotes(Lote lote) {
        this.tipoLotes.add(lote);
        lote.setTipo(this);
        return this;
    }

    public Parametros removeTipoLotes(Lote lote) {
        this.tipoLotes.remove(lote);
        lote.setTipo(null);
        return this;
    }

    public void setTipoLotes(Set<Lote> lotes) {
        this.tipoLotes = lotes;
    }

    public Set<Animal> getTipos() {
        return tipos;
    }

    public Parametros tipos(Set<Animal> animals) {
        this.tipos = animals;
        return this;
    }

    public Parametros addTipos(Animal animal) {
        this.tipos.add(animal);
        animal.setTipo(this);
        return this;
    }

    public Parametros removeTipos(Animal animal) {
        this.tipos.remove(animal);
        animal.setTipo(null);
        return this;
    }

    public void setTipos(Set<Animal> animals) {
        this.tipos = animals;
    }

    public Set<Animal> getRazas() {
        return razas;
    }

    public Parametros razas(Set<Animal> animals) {
        this.razas = animals;
        return this;
    }

    public Parametros addRazas(Animal animal) {
        this.razas.add(animal);
        animal.setRaza(this);
        return this;
    }

    public Parametros removeRazas(Animal animal) {
        this.razas.remove(animal);
        animal.setRaza(null);
        return this;
    }

    public void setRazas(Set<Animal> animals) {
        this.razas = animals;
    }

    public Parametros getAgrupador() {
        return agrupador;
    }

    public Parametros agrupador(Parametros parametros) {
        this.agrupador = parametros;
        return this;
    }

    public void setAgrupador(Parametros parametros) {
        this.agrupador = parametros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parametros)) {
            return false;
        }
        return id != null && id.equals(((Parametros) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parametros{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
