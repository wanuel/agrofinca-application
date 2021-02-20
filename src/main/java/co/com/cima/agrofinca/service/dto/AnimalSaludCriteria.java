package co.com.cima.agrofinca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.AnimalSalud} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.AnimalSaludResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /animal-saluds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnimalSaludCriteria implements Serializable, Criteria {
    /**
     * Class for filtering SINO
     */
    public static class SINOFilter extends Filter<SINO> {

        public SINOFilter() {
        }

        public SINOFilter(SINOFilter filter) {
            super(filter);
        }

        @Override
        public SINOFilter copy() {
            return new SINOFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private StringFilter nombre;

    private StringFilter laboratorio;

    private BigDecimalFilter dosis;

    private SINOFilter inyectado;

    private SINOFilter intramuscular;

    private SINOFilter subcutaneo;

    private StringFilter observacion;

    private LongFilter eventoId;

    private LongFilter medicamentoId;

    public AnimalSaludCriteria() {
    }

    public AnimalSaludCriteria(AnimalSaludCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.laboratorio = other.laboratorio == null ? null : other.laboratorio.copy();
        this.dosis = other.dosis == null ? null : other.dosis.copy();
        this.inyectado = other.inyectado == null ? null : other.inyectado.copy();
        this.intramuscular = other.intramuscular == null ? null : other.intramuscular.copy();
        this.subcutaneo = other.subcutaneo == null ? null : other.subcutaneo.copy();
        this.observacion = other.observacion == null ? null : other.observacion.copy();
        this.eventoId = other.eventoId == null ? null : other.eventoId.copy();
        this.medicamentoId = other.medicamentoId == null ? null : other.medicamentoId.copy();
    }

    @Override
    public AnimalSaludCriteria copy() {
        return new AnimalSaludCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(StringFilter laboratorio) {
        this.laboratorio = laboratorio;
    }

    public BigDecimalFilter getDosis() {
        return dosis;
    }

    public void setDosis(BigDecimalFilter dosis) {
        this.dosis = dosis;
    }

    public SINOFilter getInyectado() {
        return inyectado;
    }

    public void setInyectado(SINOFilter inyectado) {
        this.inyectado = inyectado;
    }

    public SINOFilter getIntramuscular() {
        return intramuscular;
    }

    public void setIntramuscular(SINOFilter intramuscular) {
        this.intramuscular = intramuscular;
    }

    public SINOFilter getSubcutaneo() {
        return subcutaneo;
    }

    public void setSubcutaneo(SINOFilter subcutaneo) {
        this.subcutaneo = subcutaneo;
    }

    public StringFilter getObservacion() {
        return observacion;
    }

    public void setObservacion(StringFilter observacion) {
        this.observacion = observacion;
    }

    public LongFilter getEventoId() {
        return eventoId;
    }

    public void setEventoId(LongFilter eventoId) {
        this.eventoId = eventoId;
    }

    public LongFilter getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(LongFilter medicamentoId) {
        this.medicamentoId = medicamentoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnimalSaludCriteria that = (AnimalSaludCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(laboratorio, that.laboratorio) &&
            Objects.equals(dosis, that.dosis) &&
            Objects.equals(inyectado, that.inyectado) &&
            Objects.equals(intramuscular, that.intramuscular) &&
            Objects.equals(subcutaneo, that.subcutaneo) &&
            Objects.equals(observacion, that.observacion) &&
            Objects.equals(eventoId, that.eventoId) &&
            Objects.equals(medicamentoId, that.medicamentoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fecha,
        nombre,
        laboratorio,
        dosis,
        inyectado,
        intramuscular,
        subcutaneo,
        observacion,
        eventoId,
        medicamentoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalSaludCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fecha != null ? "fecha=" + fecha + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (laboratorio != null ? "laboratorio=" + laboratorio + ", " : "") +
                (dosis != null ? "dosis=" + dosis + ", " : "") +
                (inyectado != null ? "inyectado=" + inyectado + ", " : "") +
                (intramuscular != null ? "intramuscular=" + intramuscular + ", " : "") +
                (subcutaneo != null ? "subcutaneo=" + subcutaneo + ", " : "") +
                (observacion != null ? "observacion=" + observacion + ", " : "") +
                (eventoId != null ? "eventoId=" + eventoId + ", " : "") +
                (medicamentoId != null ? "medicamentoId=" + medicamentoId + ", " : "") +
            "}";
    }

}
