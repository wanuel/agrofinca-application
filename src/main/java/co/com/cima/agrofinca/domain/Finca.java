package co.com.cima.agrofinca.domain;

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
 * A Finca.
 */
@Entity
@Table(name = "finca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "finca")
public class Finca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "area", precision = 21, scale = 2)
    private BigDecimal area;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "codigo_catastral")
    private String codigoCatastral;

    @Column(name = "municipio")
    private String municipio;

    @Column(name = "vereda")
    private String vereda;

    @Column(name = "obserrvaciones")
    private String obserrvaciones;

    @OneToMany(mappedBy = "finca")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Potrero> potreros = new HashSet<>();

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

    public Finca nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getArea() {
        return area;
    }

    public Finca area(BigDecimal area) {
        this.area = area;
        return this;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getMatricula() {
        return matricula;
    }

    public Finca matricula(String matricula) {
        this.matricula = matricula;
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCodigoCatastral() {
        return codigoCatastral;
    }

    public Finca codigoCatastral(String codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
        return this;
    }

    public void setCodigoCatastral(String codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public String getMunicipio() {
        return municipio;
    }

    public Finca municipio(String municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getVereda() {
        return vereda;
    }

    public Finca vereda(String vereda) {
        this.vereda = vereda;
        return this;
    }

    public void setVereda(String vereda) {
        this.vereda = vereda;
    }

    public String getObserrvaciones() {
        return obserrvaciones;
    }

    public Finca obserrvaciones(String obserrvaciones) {
        this.obserrvaciones = obserrvaciones;
        return this;
    }

    public void setObserrvaciones(String obserrvaciones) {
        this.obserrvaciones = obserrvaciones;
    }

    public Set<Potrero> getPotreros() {
        return potreros;
    }

    public Finca potreros(Set<Potrero> potreros) {
        this.potreros = potreros;
        return this;
    }

    public Finca addPotreros(Potrero potrero) {
        this.potreros.add(potrero);
        potrero.setFinca(this);
        return this;
    }

    public Finca removePotreros(Potrero potrero) {
        this.potreros.remove(potrero);
        potrero.setFinca(null);
        return this;
    }

    public void setPotreros(Set<Potrero> potreros) {
        this.potreros = potreros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Finca)) {
            return false;
        }
        return id != null && id.equals(((Finca) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Finca{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", area=" + getArea() +
            ", matricula='" + getMatricula() + "'" +
            ", codigoCatastral='" + getCodigoCatastral() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", vereda='" + getVereda() + "'" +
            ", obserrvaciones='" + getObserrvaciones() + "'" +
            "}";
    }
}
