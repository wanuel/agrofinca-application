package co.com.cima.agrofinca.domain;

import co.com.cima.agrofinca.domain.enumeration.ESTADOSOCIEDAD;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sociedad.
 */
@Entity
@Table(name = "sociedad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sociedad")
public class Sociedad implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private ESTADOSOCIEDAD estado;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "sociedad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Socio> socios = new HashSet<>();

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

    public Sociedad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Sociedad fechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ESTADOSOCIEDAD getEstado() {
        return estado;
    }

    public Sociedad estado(ESTADOSOCIEDAD estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(ESTADOSOCIEDAD estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Sociedad observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<Socio> getSocios() {
        return socios;
    }

    public Sociedad sociedades(Set<Socio> socios) {
        this.socios = socios;
        return this;
    }

    public Sociedad addPotreros(Socio socio) {
        this.socios.add(socio);
        socio.setSociedad(this);
        return this;
    }

    public Sociedad removePotreros(Socio socio) {
        this.socios.remove(socio);
        socio.setSociedad(null);
        return this;
    }

    public void setSocios(Set<Socio> socios) {
        this.socios = socios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sociedad)) {
            return false;
        }
        return id != null && id.equals(((Sociedad) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sociedad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
