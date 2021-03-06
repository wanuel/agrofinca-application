package co.com.cima.agrofinca.domain;

import co.com.cima.agrofinca.domain.enumeration.GENERO;
import co.com.cima.agrofinca.domain.enumeration.TIPODOCUMENTO;
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
 * A Persona.
 */
@Entity
@Table(name = "persona")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "persona")
public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TIPODOCUMENTO tipoDocumento;

    @NotNull
    @Column(name = "num_documento", nullable = false)
    private Long numDocumento;

    @NotNull
    @Column(name = "primer_nombre", nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private GENERO genero;

    @OneToMany(mappedBy = "persona")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Socio> sociedades = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TIPODOCUMENTO getTipoDocumento() {
        return tipoDocumento;
    }

    public Persona tipoDocumento(TIPODOCUMENTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public void setTipoDocumento(TIPODOCUMENTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getNumDocumento() {
        return numDocumento;
    }

    public Persona numDocumento(Long numDocumento) {
        this.numDocumento = numDocumento;
        return this;
    }

    public void setNumDocumento(Long numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public Persona primerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
        return this;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public Persona segundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
        return this;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public Persona primerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
        return this;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public Persona segundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
        return this;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Persona fechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public GENERO getGenero() {
        return genero;
    }

    public Persona genero(GENERO genero) {
        this.genero = genero;
        return this;
    }

    public void setGenero(GENERO genero) {
        this.genero = genero;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<Socio> getSociedades() {
        return sociedades;
    }

    public Persona sociedades(Set<Socio> sociedades) {
        this.sociedades = sociedades;
        return this;
    }

    public Persona addPotreros(Socio socio) {
        this.sociedades.add(socio);
        socio.setPersona(this);
        return this;
    }

    public Persona removePotreros(Socio socio) {
        this.sociedades.remove(socio);
        socio.setPersona(null);
        return this;
    }

    public void setSociedades(Set<Socio> sociedades) {
        this.sociedades = sociedades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Persona)) {
            return false;
        }
        return id != null && id.equals(((Persona) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", numDocumento=" + getNumDocumento() +
            ", primerNombre='" + getPrimerNombre() + "'" +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", primerApellido='" + getPrimerApellido() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", genero='" + getGenero() + "'" +
            "}";
    }
}
