package co.com.cima.agrofinca.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Finca} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.FincaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fincas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FincaCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private BigDecimalFilter area;

    private StringFilter matricula;

    private StringFilter codigoCatastral;

    private StringFilter municipio;

    private StringFilter vereda;

    private StringFilter observaciones;

    private LongFilter potrerosId;

    public FincaCriteria() {}

    public FincaCriteria(FincaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.area = other.area == null ? null : other.area.copy();
        this.matricula = other.matricula == null ? null : other.matricula.copy();
        this.codigoCatastral = other.codigoCatastral == null ? null : other.codigoCatastral.copy();
        this.municipio = other.municipio == null ? null : other.municipio.copy();
        this.vereda = other.vereda == null ? null : other.vereda.copy();
        this.observaciones = other.observaciones == null ? null : other.observaciones.copy();
        this.potrerosId = other.potrerosId == null ? null : other.potrerosId.copy();
    }

    @Override
    public FincaCriteria copy() {
        return new FincaCriteria(this);
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

    public BigDecimalFilter getArea() {
        return area;
    }

    public void setArea(BigDecimalFilter area) {
        this.area = area;
    }

    public StringFilter getMatricula() {
        return matricula;
    }

    public void setMatricula(StringFilter matricula) {
        this.matricula = matricula;
    }

    public StringFilter getCodigoCatastral() {
        return codigoCatastral;
    }

    public void setCodigoCatastral(StringFilter codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public StringFilter getMunicipio() {
        return municipio;
    }

    public void setMunicipio(StringFilter municipio) {
        this.municipio = municipio;
    }

    public StringFilter getVereda() {
        return vereda;
    }

    public void setVereda(StringFilter vereda) {
        this.vereda = vereda;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
    }

    public LongFilter getPotrerosId() {
        return potrerosId;
    }

    public void setPotrerosId(LongFilter potrerosId) {
        this.potrerosId = potrerosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FincaCriteria that = (FincaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(area, that.area) &&
            Objects.equals(matricula, that.matricula) &&
            Objects.equals(codigoCatastral, that.codigoCatastral) &&
            Objects.equals(municipio, that.municipio) &&
            Objects.equals(vereda, that.vereda) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(potrerosId, that.potrerosId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, area, matricula, codigoCatastral, municipio, vereda, observaciones, potrerosId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FincaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (area != null ? "area=" + area + ", " : "") +
                (matricula != null ? "matricula=" + matricula + ", " : "") +
                (codigoCatastral != null ? "codigoCatastral=" + codigoCatastral + ", " : "") +
                (municipio != null ? "municipio=" + municipio + ", " : "") +
                (vereda != null ? "vereda=" + vereda + ", " : "") +
                (observaciones != null ? "observaciones=" + observaciones + ", " : "") +
                (potrerosId != null ? "potrerosId=" + potrerosId + ", " : "") +
            "}";
    }
}
