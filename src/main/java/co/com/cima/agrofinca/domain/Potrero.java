package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Potrero.
 */
@Entity
@Table(name = "potrero")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "potrero")
public class Potrero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "pasto")
    private String pasto;

    @Column(name = "area", precision = 21, scale = 2)
    private BigDecimal area;

    @OneToMany(mappedBy = "potrero")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PotreroPastoreo> pastoreos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "potreros", allowSetters = true)
    private Finca finca;

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

    public Potrero nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Potrero descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPasto() {
        return pasto;
    }

    public Potrero pasto(String pasto) {
        this.pasto = pasto;
        return this;
    }

    public void setPasto(String pasto) {
        this.pasto = pasto;
    }

    public BigDecimal getArea() {
        return area;
    }

    public Potrero area(BigDecimal area) {
        this.area = area;
        return this;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Set<PotreroPastoreo> getPastoreos() {
        return pastoreos;
    }

    public Potrero pastoreos(Set<PotreroPastoreo> potreroPastoreos) {
        this.pastoreos = potreroPastoreos;
        return this;
    }

    public Potrero addPastoreos(PotreroPastoreo potreroPastoreo) {
        this.pastoreos.add(potreroPastoreo);
        potreroPastoreo.setPotrero(this);
        return this;
    }

    public Potrero removePastoreos(PotreroPastoreo potreroPastoreo) {
        this.pastoreos.remove(potreroPastoreo);
        potreroPastoreo.setPotrero(null);
        return this;
    }

    public void setPastoreos(Set<PotreroPastoreo> potreroPastoreos) {
        this.pastoreos = potreroPastoreos;
    }

    public Finca getFinca() {
        return finca;
    }

    public Potrero finca(Finca finca) {
        this.finca = finca;
        return this;
    }

    public void setFinca(Finca finca) {
        this.finca = finca;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Potrero)) {
            return false;
        }
        return id != null && id.equals(((Potrero) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Potrero{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", pasto='" + getPasto() + "'" +
            ", area=" + getArea() +
            "}";
    }
}
