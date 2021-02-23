package co.com.cima.agrofinca.service.dto;

import co.com.cima.agrofinca.domain.enumeration.GENERO;
import co.com.cima.agrofinca.domain.enumeration.TIPODOCUMENTO;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Persona} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.PersonaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /personas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TIPODOCUMENTO
     */
    public static class TIPODOCUMENTOFilter extends Filter<TIPODOCUMENTO> {

        public TIPODOCUMENTOFilter() {}

        public TIPODOCUMENTOFilter(TIPODOCUMENTOFilter filter) {
            super(filter);
        }

        @Override
        public TIPODOCUMENTOFilter copy() {
            return new TIPODOCUMENTOFilter(this);
        }
    }

    /**
     * Class for filtering GENERO
     */
    public static class GENEROFilter extends Filter<GENERO> {

        public GENEROFilter() {}

        public GENEROFilter(GENEROFilter filter) {
            super(filter);
        }

        @Override
        public GENEROFilter copy() {
            return new GENEROFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TIPODOCUMENTOFilter tipoDocumento;

    private LongFilter numDocumento;

    private StringFilter primerNombre;

    private StringFilter segundoNombre;

    private StringFilter primerApellido;

    private StringFilter segundoApellido;

    private LocalDateFilter fechaNacimiento;

    private GENEROFilter genero;

    private LongFilter socioId;

    public PersonaCriteria() {}

    public PersonaCriteria(PersonaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipoDocumento = other.tipoDocumento == null ? null : other.tipoDocumento.copy();
        this.numDocumento = other.numDocumento == null ? null : other.numDocumento.copy();
        this.primerNombre = other.primerNombre == null ? null : other.primerNombre.copy();
        this.segundoNombre = other.segundoNombre == null ? null : other.segundoNombre.copy();
        this.primerApellido = other.primerApellido == null ? null : other.primerApellido.copy();
        this.segundoApellido = other.segundoApellido == null ? null : other.segundoApellido.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.genero = other.genero == null ? null : other.genero.copy();
        this.socioId = other.socioId == null ? null : other.socioId.copy();
    }

    @Override
    public PersonaCriteria copy() {
        return new PersonaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TIPODOCUMENTOFilter getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TIPODOCUMENTOFilter tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LongFilter getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(LongFilter numDocumento) {
        this.numDocumento = numDocumento;
    }

    public StringFilter getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(StringFilter primerNombre) {
        this.primerNombre = primerNombre;
    }

    public StringFilter getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(StringFilter segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public StringFilter getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(StringFilter primerApellido) {
        this.primerApellido = primerApellido;
    }

    public StringFilter getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(StringFilter segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public GENEROFilter getGenero() {
        return genero;
    }

    public void setGenero(GENEROFilter genero) {
        this.genero = genero;
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
        final PersonaCriteria that = (PersonaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoDocumento, that.tipoDocumento) &&
            Objects.equals(numDocumento, that.numDocumento) &&
            Objects.equals(primerNombre, that.primerNombre) &&
            Objects.equals(segundoNombre, that.segundoNombre) &&
            Objects.equals(primerApellido, that.primerApellido) &&
            Objects.equals(segundoApellido, that.segundoApellido) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(genero, that.genero) &&
            Objects.equals(socioId, that.socioId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipoDocumento,
            numDocumento,
            primerNombre,
            segundoNombre,
            primerApellido,
            segundoApellido,
            fechaNacimiento,
            genero,
            socioId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tipoDocumento != null ? "tipoDocumento=" + tipoDocumento + ", " : "") +
                (numDocumento != null ? "numDocumento=" + numDocumento + ", " : "") +
                (primerNombre != null ? "primerNombre=" + primerNombre + ", " : "") +
                (segundoNombre != null ? "segundoNombre=" + segundoNombre + ", " : "") +
                (primerApellido != null ? "primerApellido=" + primerApellido + ", " : "") +
                (segundoApellido != null ? "segundoApellido=" + segundoApellido + ", " : "") +
                (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
                (genero != null ? "genero=" + genero + ", " : "") +
                (socioId != null ? "socioId=" + socioId + ", " : "") +
            "}";
    }
}
