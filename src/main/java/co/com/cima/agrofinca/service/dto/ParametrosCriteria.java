package co.com.cima.agrofinca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Parametros} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.ParametrosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parametros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParametrosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private LongFilter eventosId;

    private LongFilter medicamentosId;

    private LongFilter parametrosId;

    private LongFilter tipoLotesId;

    private LongFilter tiposId;

    private LongFilter razasId;

    private LongFilter agrupadorId;

    public ParametrosCriteria() {
    }

    public ParametrosCriteria(ParametrosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.eventosId = other.eventosId == null ? null : other.eventosId.copy();
        this.medicamentosId = other.medicamentosId == null ? null : other.medicamentosId.copy();
        this.parametrosId = other.parametrosId == null ? null : other.parametrosId.copy();
        this.tipoLotesId = other.tipoLotesId == null ? null : other.tipoLotesId.copy();
        this.tiposId = other.tiposId == null ? null : other.tiposId.copy();
        this.razasId = other.razasId == null ? null : other.razasId.copy();
        this.agrupadorId = other.agrupadorId == null ? null : other.agrupadorId.copy();
    }

    @Override
    public ParametrosCriteria copy() {
        return new ParametrosCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public LongFilter getEventosId() {
        return eventosId;
    }

    public void setEventosId(LongFilter eventosId) {
        this.eventosId = eventosId;
    }

    public LongFilter getMedicamentosId() {
        return medicamentosId;
    }

    public void setMedicamentosId(LongFilter medicamentosId) {
        this.medicamentosId = medicamentosId;
    }

    public LongFilter getParametrosId() {
        return parametrosId;
    }

    public void setParametrosId(LongFilter parametrosId) {
        this.parametrosId = parametrosId;
    }

    public LongFilter getTipoLotesId() {
        return tipoLotesId;
    }

    public void setTipoLotesId(LongFilter tipoLotesId) {
        this.tipoLotesId = tipoLotesId;
    }

    public LongFilter getTiposId() {
        return tiposId;
    }

    public void setTiposId(LongFilter tiposId) {
        this.tiposId = tiposId;
    }

    public LongFilter getRazasId() {
        return razasId;
    }

    public void setRazasId(LongFilter razasId) {
        this.razasId = razasId;
    }

    public LongFilter getAgrupadorId() {
        return agrupadorId;
    }

    public void setAgrupadorId(LongFilter agrupadorId) {
        this.agrupadorId = agrupadorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParametrosCriteria that = (ParametrosCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(eventosId, that.eventosId) &&
            Objects.equals(medicamentosId, that.medicamentosId) &&
            Objects.equals(parametrosId, that.parametrosId) &&
            Objects.equals(tipoLotesId, that.tipoLotesId) &&
            Objects.equals(tiposId, that.tiposId) &&
            Objects.equals(razasId, that.razasId) &&
            Objects.equals(agrupadorId, that.agrupadorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        descripcion,
        eventosId,
        medicamentosId,
        parametrosId,
        tipoLotesId,
        tiposId,
        razasId,
        agrupadorId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametrosCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (eventosId != null ? "eventosId=" + eventosId + ", " : "") +
                (medicamentosId != null ? "medicamentosId=" + medicamentosId + ", " : "") +
                (parametrosId != null ? "parametrosId=" + parametrosId + ", " : "") +
                (tipoLotesId != null ? "tipoLotesId=" + tipoLotesId + ", " : "") +
                (tiposId != null ? "tiposId=" + tiposId + ", " : "") +
                (razasId != null ? "razasId=" + razasId + ", " : "") +
                (agrupadorId != null ? "agrupadorId=" + agrupadorId + ", " : "") +
            "}";
    }

}
