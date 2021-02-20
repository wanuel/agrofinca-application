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
 * Criteria class for the {@link co.com.cima.agrofinca.domain.AnimalEvento} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.AnimalEventoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /animal-eventos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnimalEventoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter tratamientosId;

    private LongFilter pesosId;

    private LongFilter costosId;

    private LongFilter animalId;

    private LongFilter eventoId;

    public AnimalEventoCriteria() {
    }

    public AnimalEventoCriteria(AnimalEventoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tratamientosId = other.tratamientosId == null ? null : other.tratamientosId.copy();
        this.pesosId = other.pesosId == null ? null : other.pesosId.copy();
        this.costosId = other.costosId == null ? null : other.costosId.copy();
        this.animalId = other.animalId == null ? null : other.animalId.copy();
        this.eventoId = other.eventoId == null ? null : other.eventoId.copy();
    }

    @Override
    public AnimalEventoCriteria copy() {
        return new AnimalEventoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTratamientosId() {
        return tratamientosId;
    }

    public void setTratamientosId(LongFilter tratamientosId) {
        this.tratamientosId = tratamientosId;
    }

    public LongFilter getPesosId() {
        return pesosId;
    }

    public void setPesosId(LongFilter pesosId) {
        this.pesosId = pesosId;
    }

    public LongFilter getCostosId() {
        return costosId;
    }

    public void setCostosId(LongFilter costosId) {
        this.costosId = costosId;
    }

    public LongFilter getAnimalId() {
        return animalId;
    }

    public void setAnimalId(LongFilter animalId) {
        this.animalId = animalId;
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
        final AnimalEventoCriteria that = (AnimalEventoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tratamientosId, that.tratamientosId) &&
            Objects.equals(pesosId, that.pesosId) &&
            Objects.equals(costosId, that.costosId) &&
            Objects.equals(animalId, that.animalId) &&
            Objects.equals(eventoId, that.eventoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tratamientosId,
        pesosId,
        costosId,
        animalId,
        eventoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalEventoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tratamientosId != null ? "tratamientosId=" + tratamientosId + ", " : "") +
                (pesosId != null ? "pesosId=" + pesosId + ", " : "") +
                (costosId != null ? "costosId=" + costosId + ", " : "") +
                (animalId != null ? "animalId=" + animalId + ", " : "") +
                (eventoId != null ? "eventoId=" + eventoId + ", " : "") +
            "}";
    }

}
