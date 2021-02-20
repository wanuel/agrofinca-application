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
 * Criteria class for the {@link co.com.cima.agrofinca.domain.AnimalPeso} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.AnimalPesoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /animal-pesos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnimalPesoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private BigDecimalFilter peso;

    private LongFilter animalId;

    public AnimalPesoCriteria() {
    }

    public AnimalPesoCriteria(AnimalPesoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.peso = other.peso == null ? null : other.peso.copy();
        this.animalId = other.animalId == null ? null : other.animalId.copy();
    }

    @Override
    public AnimalPesoCriteria copy() {
        return new AnimalPesoCriteria(this);
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

    public BigDecimalFilter getPeso() {
        return peso;
    }

    public void setPeso(BigDecimalFilter peso) {
        this.peso = peso;
    }

    public LongFilter getAnimalId() {
        return animalId;
    }

    public void setAnimalId(LongFilter animalId) {
        this.animalId = animalId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnimalPesoCriteria that = (AnimalPesoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(peso, that.peso) &&
            Objects.equals(animalId, that.animalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fecha,
        peso,
        animalId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalPesoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fecha != null ? "fecha=" + fecha + ", " : "") +
                (peso != null ? "peso=" + peso + ", " : "") +
                (animalId != null ? "animalId=" + animalId + ", " : "") +
            "}";
    }

}
