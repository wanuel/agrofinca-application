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

import co.com.cima.agrofinca.domain.enumeration.ESTADOLOTE;

/**
 * A Lote.
 */
@Entity
@Table(name = "lote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "lote")
public class Lote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private ESTADOLOTE estado;

    @OneToMany(mappedBy = "lote")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PotreroPastoreo> pastoreos = new HashSet<>();

    @OneToMany(mappedBy = "lote")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalLote> animales = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "tipoLotes", allowSetters = true)
    private Parametros tipo;

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

    public Lote nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Lote fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ESTADOLOTE getEstado() {
        return estado;
    }

    public Lote estado(ESTADOLOTE estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(ESTADOLOTE estado) {
        this.estado = estado;
    }

    public Set<PotreroPastoreo> getPastoreos() {
        return pastoreos;
    }

    public Lote pastoreos(Set<PotreroPastoreo> potreroPastoreos) {
        this.pastoreos = potreroPastoreos;
        return this;
    }

    public Lote addPastoreos(PotreroPastoreo potreroPastoreo) {
        this.pastoreos.add(potreroPastoreo);
        potreroPastoreo.setLote(this);
        return this;
    }

    public Lote removePastoreos(PotreroPastoreo potreroPastoreo) {
        this.pastoreos.remove(potreroPastoreo);
        potreroPastoreo.setLote(null);
        return this;
    }

    public void setPastoreos(Set<PotreroPastoreo> potreroPastoreos) {
        this.pastoreos = potreroPastoreos;
    }

    public Set<AnimalLote> getAnimales() {
        return animales;
    }

    public Lote animales(Set<AnimalLote> animalLotes) {
        this.animales = animalLotes;
        return this;
    }

    public Lote addAnimales(AnimalLote animalLote) {
        this.animales.add(animalLote);
        animalLote.setLote(this);
        return this;
    }

    public Lote removeAnimales(AnimalLote animalLote) {
        this.animales.remove(animalLote);
        animalLote.setLote(null);
        return this;
    }

    public void setAnimales(Set<AnimalLote> animalLotes) {
        this.animales = animalLotes;
    }

    public Parametros getTipo() {
        return tipo;
    }

    public Lote tipo(Parametros parametros) {
        this.tipo = parametros;
        return this;
    }

    public void setTipo(Parametros parametros) {
        this.tipo = parametros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lote)) {
            return false;
        }
        return id != null && id.equals(((Lote) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lote{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
