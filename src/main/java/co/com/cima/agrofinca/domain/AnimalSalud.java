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

import co.com.cima.agrofinca.domain.enumeration.SINO;

/**
 * A AnimalSalud.
 */
@Entity
@Table(name = "animal_salud")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "animalsalud")
public class AnimalSalud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "laboratorio")
    private String laboratorio;

    @NotNull
    @Column(name = "dosis", precision = 21, scale = 2, nullable = false)
    private BigDecimal dosis;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "inyectado", nullable = false)
    private SINO inyectado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "intramuscular", nullable = false)
    private SINO intramuscular;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "subcutaneo", nullable = false)
    private SINO subcutaneo;

    @Column(name = "observacion")
    private String observacion;

    @ManyToOne
    @JsonIgnoreProperties(value = "tratamientos", allowSetters = true)
    private AnimalEvento evento;

    @ManyToOne
    @JsonIgnoreProperties(value = "medicamentos", allowSetters = true)
    private Parametros medicamento;

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

    public AnimalSalud fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public AnimalSalud nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public AnimalSalud laboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
        return this;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public BigDecimal getDosis() {
        return dosis;
    }

    public AnimalSalud dosis(BigDecimal dosis) {
        this.dosis = dosis;
        return this;
    }

    public void setDosis(BigDecimal dosis) {
        this.dosis = dosis;
    }

    public SINO getInyectado() {
        return inyectado;
    }

    public AnimalSalud inyectado(SINO inyectado) {
        this.inyectado = inyectado;
        return this;
    }

    public void setInyectado(SINO inyectado) {
        this.inyectado = inyectado;
    }

    public SINO getIntramuscular() {
        return intramuscular;
    }

    public AnimalSalud intramuscular(SINO intramuscular) {
        this.intramuscular = intramuscular;
        return this;
    }

    public void setIntramuscular(SINO intramuscular) {
        this.intramuscular = intramuscular;
    }

    public SINO getSubcutaneo() {
        return subcutaneo;
    }

    public AnimalSalud subcutaneo(SINO subcutaneo) {
        this.subcutaneo = subcutaneo;
        return this;
    }

    public void setSubcutaneo(SINO subcutaneo) {
        this.subcutaneo = subcutaneo;
    }

    public String getObservacion() {
        return observacion;
    }

    public AnimalSalud observacion(String observacion) {
        this.observacion = observacion;
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public AnimalEvento getEvento() {
        return evento;
    }

    public AnimalSalud evento(AnimalEvento animalEvento) {
        this.evento = animalEvento;
        return this;
    }

    public void setEvento(AnimalEvento animalEvento) {
        this.evento = animalEvento;
    }

    public Parametros getMedicamento() {
        return medicamento;
    }

    public AnimalSalud medicamento(Parametros parametros) {
        this.medicamento = parametros;
        return this;
    }

    public void setMedicamento(Parametros parametros) {
        this.medicamento = parametros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalSalud)) {
            return false;
        }
        return id != null && id.equals(((AnimalSalud) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalSalud{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", laboratorio='" + getLaboratorio() + "'" +
            ", dosis=" + getDosis() +
            ", inyectado='" + getInyectado() + "'" +
            ", intramuscular='" + getIntramuscular() + "'" +
            ", subcutaneo='" + getSubcutaneo() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
