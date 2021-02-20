package co.com.cima.agrofinca.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Socio.
 */
@Entity
@Table(name = "socio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "socio")
public class Socio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @NotNull
    @Column(name = "participacion", precision = 21, scale = 2, nullable = false)
    private BigDecimal participacion;

    @OneToMany(mappedBy = "socio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Persona> personas = new HashSet<>();

    @OneToMany(mappedBy = "socio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Sociedad> sociedades = new HashSet<>();

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

    public Socio fechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getParticipacion() {
        return participacion;
    }

    public Socio participacion(BigDecimal participacion) {
        this.participacion = participacion;
        return this;
    }

    public void setParticipacion(BigDecimal participacion) {
        this.participacion = participacion;
    }

    public Set<Persona> getPersonas() {
        return personas;
    }

    public Socio personas(Set<Persona> personas) {
        this.personas = personas;
        return this;
    }

    public Socio addPersonas(Persona persona) {
        this.personas.add(persona);
        persona.setSocio(this);
        return this;
    }

    public Socio removePersonas(Persona persona) {
        this.personas.remove(persona);
        persona.setSocio(null);
        return this;
    }

    public void setPersonas(Set<Persona> personas) {
        this.personas = personas;
    }

    public Set<Sociedad> getSociedades() {
        return sociedades;
    }

    public Socio sociedades(Set<Sociedad> sociedads) {
        this.sociedades = sociedads;
        return this;
    }

    public Socio addSociedades(Sociedad sociedad) {
        this.sociedades.add(sociedad);
        sociedad.setSocio(this);
        return this;
    }

    public Socio removeSociedades(Sociedad sociedad) {
        this.sociedades.remove(sociedad);
        sociedad.setSocio(null);
        return this;
    }

    public void setSociedades(Set<Sociedad> sociedads) {
        this.sociedades = sociedads;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Socio)) {
            return false;
        }
        return id != null && id.equals(((Socio) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Socio{" +
            "id=" + getId() +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", participacion=" + getParticipacion() +
            "}";
    }
}
