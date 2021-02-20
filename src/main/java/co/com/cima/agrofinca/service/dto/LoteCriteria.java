package co.com.cima.agrofinca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import co.com.cima.agrofinca.domain.enumeration.ESTADOLOTE;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Lote} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.LoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lotes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoteCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ESTADOLOTE
     */
    public static class ESTADOLOTEFilter extends Filter<ESTADOLOTE> {

        public ESTADOLOTEFilter() {
        }

        public ESTADOLOTEFilter(ESTADOLOTEFilter filter) {
            super(filter);
        }

        @Override
        public ESTADOLOTEFilter copy() {
            return new ESTADOLOTEFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private LocalDateFilter fecha;

    private ESTADOLOTEFilter estado;

    private LongFilter pastoreosId;

    private LongFilter animalesId;

    private LongFilter tipoId;

    public LoteCriteria() {
    }

    public LoteCriteria(LoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.pastoreosId = other.pastoreosId == null ? null : other.pastoreosId.copy();
        this.animalesId = other.animalesId == null ? null : other.animalesId.copy();
        this.tipoId = other.tipoId == null ? null : other.tipoId.copy();
    }

    @Override
    public LoteCriteria copy() {
        return new LoteCriteria(this);
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

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public ESTADOLOTEFilter getEstado() {
        return estado;
    }

    public void setEstado(ESTADOLOTEFilter estado) {
        this.estado = estado;
    }

    public LongFilter getPastoreosId() {
        return pastoreosId;
    }

    public void setPastoreosId(LongFilter pastoreosId) {
        this.pastoreosId = pastoreosId;
    }

    public LongFilter getAnimalesId() {
        return animalesId;
    }

    public void setAnimalesId(LongFilter animalesId) {
        this.animalesId = animalesId;
    }

    public LongFilter getTipoId() {
        return tipoId;
    }

    public void setTipoId(LongFilter tipoId) {
        this.tipoId = tipoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LoteCriteria that = (LoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(pastoreosId, that.pastoreosId) &&
            Objects.equals(animalesId, that.animalesId) &&
            Objects.equals(tipoId, that.tipoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        fecha,
        estado,
        pastoreosId,
        animalesId,
        tipoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (fecha != null ? "fecha=" + fecha + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (pastoreosId != null ? "pastoreosId=" + pastoreosId + ", " : "") +
                (animalesId != null ? "animalesId=" + animalesId + ", " : "") +
                (tipoId != null ? "tipoId=" + tipoId + ", " : "") +
            "}";
    }

}
