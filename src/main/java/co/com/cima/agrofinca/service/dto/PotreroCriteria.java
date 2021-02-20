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

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Potrero} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.PotreroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /potreros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PotreroCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private StringFilter pasto;

    private BigDecimalFilter area;

    private LongFilter pastoreosId;

    private LongFilter fincaId;

    public PotreroCriteria() {
    }

    public PotreroCriteria(PotreroCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.pasto = other.pasto == null ? null : other.pasto.copy();
        this.area = other.area == null ? null : other.area.copy();
        this.pastoreosId = other.pastoreosId == null ? null : other.pastoreosId.copy();
        this.fincaId = other.fincaId == null ? null : other.fincaId.copy();
    }

    @Override
    public PotreroCriteria copy() {
        return new PotreroCriteria(this);
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

    public StringFilter getPasto() {
        return pasto;
    }

    public void setPasto(StringFilter pasto) {
        this.pasto = pasto;
    }

    public BigDecimalFilter getArea() {
        return area;
    }

    public void setArea(BigDecimalFilter area) {
        this.area = area;
    }

    public LongFilter getPastoreosId() {
        return pastoreosId;
    }

    public void setPastoreosId(LongFilter pastoreosId) {
        this.pastoreosId = pastoreosId;
    }

    public LongFilter getFincaId() {
        return fincaId;
    }

    public void setFincaId(LongFilter fincaId) {
        this.fincaId = fincaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PotreroCriteria that = (PotreroCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(pasto, that.pasto) &&
            Objects.equals(area, that.area) &&
            Objects.equals(pastoreosId, that.pastoreosId) &&
            Objects.equals(fincaId, that.fincaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        descripcion,
        pasto,
        area,
        pastoreosId,
        fincaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotreroCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (pasto != null ? "pasto=" + pasto + ", " : "") +
                (area != null ? "area=" + area + ", " : "") +
                (pastoreosId != null ? "pastoreosId=" + pastoreosId + ", " : "") +
                (fincaId != null ? "fincaId=" + fincaId + ", " : "") +
            "}";
    }

}
