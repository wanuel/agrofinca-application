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
 * Criteria class for the {@link co.com.cima.agrofinca.domain.AnimalLote} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.AnimalLoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /animal-lotes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnimalLoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaIngreso;

    private LocalDateFilter fechaSalida;

    private LongFilter animalId;

    private LongFilter loteId;

    public AnimalLoteCriteria() {
    }

    public AnimalLoteCriteria(AnimalLoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaIngreso = other.fechaIngreso == null ? null : other.fechaIngreso.copy();
        this.fechaSalida = other.fechaSalida == null ? null : other.fechaSalida.copy();
        this.animalId = other.animalId == null ? null : other.animalId.copy();
        this.loteId = other.loteId == null ? null : other.loteId.copy();
    }

    @Override
    public AnimalLoteCriteria copy() {
        return new AnimalLoteCriteria(this);
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

    public LocalDateFilter getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateFilter fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LongFilter getAnimalId() {
        return animalId;
    }

    public void setAnimalId(LongFilter animalId) {
        this.animalId = animalId;
    }

    public LongFilter getLoteId() {
        return loteId;
    }

    public void setLoteId(LongFilter loteId) {
        this.loteId = loteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnimalLoteCriteria that = (AnimalLoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fechaIngreso, that.fechaIngreso) &&
            Objects.equals(fechaSalida, that.fechaSalida) &&
            Objects.equals(animalId, that.animalId) &&
            Objects.equals(loteId, that.loteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fechaIngreso,
        fechaSalida,
        animalId,
        loteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalLoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fechaIngreso != null ? "fechaIngreso=" + fechaIngreso + ", " : "") +
                (fechaSalida != null ? "fechaSalida=" + fechaSalida + ", " : "") +
                (animalId != null ? "animalId=" + animalId + ", " : "") +
                (loteId != null ? "loteId=" + loteId + ", " : "") +
            "}";
    }

}
