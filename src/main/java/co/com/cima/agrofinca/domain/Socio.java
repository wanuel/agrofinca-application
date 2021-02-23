package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @ManyToOne
    @JsonIgnoreProperties(value = "sociedades", allowSetters = true)
    private Persona persona;

    @ManyToOne
    @JsonIgnoreProperties(value = "sociedades", allowSetters = true)
    private Sociedad sociedad;

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Persona getPersona() {
        return persona;
    }

    public Socio persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Sociedad getSociedad() {
        return sociedad;
    }

    public Socio sociedad(Sociedad sociedad) {
        this.sociedad = sociedad;
        return this;
    }

    public void setSociedad(Sociedad sociedad) {
        this.sociedad = sociedad;
    }

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
