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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Evento} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.EventoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eventos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private StringFilter observacion;

    private LongFilter eventosId;

    private LongFilter eventoId;

    public EventoCriteria() {
    }

    public EventoCriteria(EventoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.observacion = other.observacion == null ? null : other.observacion.copy();
        this.eventosId = other.eventosId == null ? null : other.eventosId.copy();
        this.eventoId = other.eventoId == null ? null : other.eventoId.copy();
    }

    @Override
    public EventoCriteria copy() {
        return new EventoCriteria(this);
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

    public StringFilter getObservacion() {
        return observacion;
    }

    public void setObservacion(StringFilter observacion) {
        this.observacion = observacion;
    }

    public LongFilter getEventosId() {
        return eventosId;
    }

    public void setEventosId(LongFilter eventosId) {
        this.eventosId = eventosId;
    }

    public LongFilter getEventoId() {
        return eventoId;
    }

    public void setEventoId(LongFilter eventoId) {
        this.eventoId = eventoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventoCriteria that = (EventoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(observacion, that.observacion) &&
            Objects.equals(eventosId, that.eventosId) &&
            Objects.equals(eventoId, that.eventoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fecha,
        observacion,
        eventosId,
        eventoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fecha != null ? "fecha=" + fecha + ", " : "") +
                (observacion != null ? "observacion=" + observacion + ", " : "") +
                (eventosId != null ? "eventosId=" + eventosId + ", " : "") +
                (eventoId != null ? "eventoId=" + eventoId + ", " : "") +
            "}";
    }

}
