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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Socio} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.SocioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /socios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SocioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaIngreso;

    private BigDecimalFilter participacion;

    private LongFilter personasId;

    private LongFilter sociedadesId;

    public SocioCriteria() {
    }

    public SocioCriteria(SocioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaIngreso = other.fechaIngreso == null ? null : other.fechaIngreso.copy();
        this.participacion = other.participacion == null ? null : other.participacion.copy();
        this.personasId = other.personasId == null ? null : other.personasId.copy();
        this.sociedadesId = other.sociedadesId == null ? null : other.sociedadesId.copy();
    }

    @Override
    public SocioCriteria copy() {
        return new SocioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateFilter fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimalFilter getParticipacion() {
        return participacion;
    }

    public void setParticipacion(BigDecimalFilter participacion) {
        this.participacion = participacion;
    }

    public LongFilter getPersonasId() {
        return personasId;
    }

    public void setPersonasId(LongFilter personasId) {
        this.personasId = personasId;
    }

    public LongFilter getSociedadesId() {
        return sociedadesId;
    }

    public void setSociedadesId(LongFilter sociedadesId) {
        this.sociedadesId = sociedadesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SocioCriteria that = (SocioCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fechaIngreso, that.fechaIngreso) &&
            Objects.equals(participacion, that.participacion) &&
            Objects.equals(personasId, that.personasId) &&
            Objects.equals(sociedadesId, that.sociedadesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fechaIngreso,
        participacion,
        personasId,
        sociedadesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocioCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fechaIngreso != null ? "fechaIngreso=" + fechaIngreso + ", " : "") +
                (participacion != null ? "participacion=" + participacion + ", " : "") +
                (personasId != null ? "personasId=" + personasId + ", " : "") +
                (sociedadesId != null ? "sociedadesId=" + sociedadesId + ", " : "") +
            "}";
    }

}
