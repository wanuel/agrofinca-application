package co.com.cima.agrofinca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import co.com.cima.agrofinca.domain.enumeration.ESTADOSOCIEDAD;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Sociedad} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.SociedadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sociedads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SociedadCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ESTADOSOCIEDAD
     */
    public static class ESTADOSOCIEDADFilter extends Filter<ESTADOSOCIEDAD> {

        public ESTADOSOCIEDADFilter() {
        }

        public ESTADOSOCIEDADFilter(ESTADOSOCIEDADFilter filter) {
            super(filter);
        }

        @Override
        public ESTADOSOCIEDADFilter copy() {
            return new ESTADOSOCIEDADFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private LocalDateFilter fechaCreacion;

    private ESTADOSOCIEDADFilter estado;

    private StringFilter observaciones;

    private LongFilter socioId;

    public SociedadCriteria() {
    }

    public SociedadCriteria(SociedadCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.fechaCreacion = other.fechaCreacion == null ? null : other.fechaCreacion.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.socioId = other.socioId == null ? null : other.socioId.copy();
    }

    @Override
    public SociedadCriteria copy() {
        return new SociedadCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public LocalDateFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ESTADOSOCIEDADFilter getEstado() {
        return estado;
    }

    public void setEstado(ESTADOSOCIEDADFilter estado) {
        this.estado = estado;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
    }

    public LongFilter getSocioId() {
        return socioId;
    }

    public void setSocioId(LongFilter socioId) {
        this.socioId = socioId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SociedadCriteria that = (SociedadCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(socioId, that.socioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        fechaCreacion,
        estado,
        observaciones,
        socioId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SociedadCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
                (socioId != null ? "socioId=" + socioId + ", " : "") +
            "}";
    }

}
