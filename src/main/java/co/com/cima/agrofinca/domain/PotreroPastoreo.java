package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

import co.com.cima.agrofinca.domain.enumeration.SINO;

/**
 * A PotreroPastoreo.
 */
@Entity
@Table(name = "potrero_pastoreo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "potreropastoreo")
public class PotreroPastoreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "fecha_limpia")
    private LocalDate fechaLimpia;

    @Column(name = "dias_descanso")
    private Integer diasDescanso;

    @Column(name = "dias_carga")
    private Integer diasCarga;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "limpia", nullable = false)
    private SINO limpia;

    @ManyToOne
    @JsonIgnoreProperties(value = "pastoreos", allowSetters = true)
    private Lote lote;

    @ManyToOne
    @JsonIgnoreProperties(value = "pastoreos", allowSetters = true)
    private Potrero potrero;

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

    public PotreroPastoreo fechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public PotreroPastoreo fechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
        return this;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDate getFechaLimpia() {
        return fechaLimpia;
    }

    public PotreroPastoreo fechaLimpia(LocalDate fechaLimpia) {
        this.fechaLimpia = fechaLimpia;
        return this;
    }

    public void setFechaLimpia(LocalDate fechaLimpia) {
        this.fechaLimpia = fechaLimpia;
    }

    public Integer getDiasDescanso() {
        return diasDescanso;
    }

    public PotreroPastoreo diasDescanso(Integer diasDescanso) {
        this.diasDescanso = diasDescanso;
        return this;
    }

    public void setDiasDescanso(Integer diasDescanso) {
        this.diasDescanso = diasDescanso;
    }

    public Integer getDiasCarga() {
        return diasCarga;
    }

    public PotreroPastoreo diasCarga(Integer diasCarga) {
        this.diasCarga = diasCarga;
        return this;
    }

    public void setDiasCarga(Integer diasCarga) {
        this.diasCarga = diasCarga;
    }

    public SINO getLimpia() {
        return limpia;
    }

    public PotreroPastoreo limpia(SINO limpia) {
        this.limpia = limpia;
        return this;
    }

    public void setLimpia(SINO limpia) {
        this.limpia = limpia;
    }

    public Lote getLote() {
        return lote;
    }

    public PotreroPastoreo lote(Lote lote) {
        this.lote = lote;
        return this;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Potrero getPotrero() {
        return potrero;
    }

    public PotreroPastoreo potrero(Potrero potrero) {
        this.potrero = potrero;
        return this;
    }

    public void setPotrero(Potrero potrero) {
        this.potrero = potrero;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PotreroPastoreo)) {
            return false;
        }
        return id != null && id.equals(((PotreroPastoreo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotreroPastoreo{" +
            "id=" + getId() +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", fechaSalida='" + getFechaSalida() + "'" +
            ", fechaLimpia='" + getFechaLimpia() + "'" +
            ", diasDescanso=" + getDiasDescanso() +
            ", diasCarga=" + getDiasCarga() +
            ", limpia='" + getLimpia() + "'" +
            "}";
    }
}
