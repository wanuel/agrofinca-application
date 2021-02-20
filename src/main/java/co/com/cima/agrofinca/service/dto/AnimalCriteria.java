package co.com.cima.agrofinca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.SEXO;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import co.com.cima.agrofinca.domain.enumeration.ESTADOANIMAL;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.Animal} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.AnimalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /animals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnimalCriteria implements Serializable, Criteria {
    /**
     * Class for filtering SINO
     */
    public static class SINOFilter extends Filter<SINO> {

        public SINOFilter() {
        }

        public SINOFilter(SINOFilter filter) {
            super(filter);
        }

        @Override
        public SINOFilter copy() {
            return new SINOFilter(this);
        }

    }
    /**
     * Class for filtering SEXO
     */
    public static class SEXOFilter extends Filter<SEXO> {

        public SEXOFilter() {
        }

        public SEXOFilter(SEXOFilter filter) {
            super(filter);
        }

        @Override
        public SEXOFilter copy() {
            return new SEXOFilter(this);
        }

    }
    /**
     * Class for filtering ESTADOANIMAL
     */
    public static class ESTADOANIMALFilter extends Filter<ESTADOANIMAL> {

        public ESTADOANIMALFilter() {
        }

        public ESTADOANIMALFilter(ESTADOANIMALFilter filter) {
            super(filter);
        }

        @Override
        public ESTADOANIMALFilter copy() {
            return new ESTADOANIMALFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter caracterizacion;

    private SINOFilter hierro;

    private LocalDateFilter fechaNacimiento;

    private LocalDateFilter fechaCompra;

    private SEXOFilter sexo;

    private SINOFilter castrado;

    private LocalDateFilter fechaCastracion;

    private ESTADOANIMALFilter estado;

    private LongFilter lotesId;

    private LongFilter imagenesId;

    private LongFilter eventosId;

    private LongFilter tipoId;

    private LongFilter razaId;

    public AnimalCriteria() {
    }

    public AnimalCriteria(AnimalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.caracterizacion = other.caracterizacion == null ? null : other.caracterizacion.copy();
        this.hierro = other.hierro == null ? null : other.hierro.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.fechaCompra = other.fechaCompra == null ? null : other.fechaCompra.copy();
        this.sexo = other.sexo == null ? null : other.sexo.copy();
        this.castrado = other.castrado == null ? null : other.castrado.copy();
        this.fechaCastracion = other.fechaCastracion == null ? null : other.fechaCastracion.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.lotesId = other.lotesId == null ? null : other.lotesId.copy();
        this.imagenesId = other.imagenesId == null ? null : other.imagenesId.copy();
        this.eventosId = other.eventosId == null ? null : other.eventosId.copy();
        this.tipoId = other.tipoId == null ? null : other.tipoId.copy();
        this.razaId = other.razaId == null ? null : other.razaId.copy();
    }

    @Override
    public AnimalCriteria copy() {
        return new AnimalCriteria(this);
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

    public StringFilter getCaracterizacion() {
        return caracterizacion;
    }

    public void setCaracterizacion(StringFilter caracterizacion) {
        this.caracterizacion = caracterizacion;
    }

    public SINOFilter getHierro() {
        return hierro;
    }

    public void setHierro(SINOFilter hierro) {
        this.hierro = hierro;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateFilter getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateFilter fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public SEXOFilter getSexo() {
        return sexo;
    }

    public void setSexo(SEXOFilter sexo) {
        this.sexo = sexo;
    }

    public SINOFilter getCastrado() {
        return castrado;
    }

    public void setCastrado(SINOFilter castrado) {
        this.castrado = castrado;
    }

    public LocalDateFilter getFechaCastracion() {
        return fechaCastracion;
    }

    public void setFechaCastracion(LocalDateFilter fechaCastracion) {
        this.fechaCastracion = fechaCastracion;
    }

    public ESTADOANIMALFilter getEstado() {
        return estado;
    }

    public void setEstado(ESTADOANIMALFilter estado) {
        this.estado = estado;
    }

    public LongFilter getLotesId() {
        return lotesId;
    }

    public void setLotesId(LongFilter lotesId) {
        this.lotesId = lotesId;
    }

    public LongFilter getImagenesId() {
        return imagenesId;
    }

    public void setImagenesId(LongFilter imagenesId) {
        this.imagenesId = imagenesId;
    }

    public LongFilter getEventosId() {
        return eventosId;
    }

    public void setEventosId(LongFilter eventosId) {
        this.eventosId = eventosId;
    }

    public LongFilter getTipoId() {
        return tipoId;
    }

    public void setTipoId(LongFilter tipoId) {
        this.tipoId = tipoId;
    }

    public LongFilter getRazaId() {
        return razaId;
    }

    public void setRazaId(LongFilter razaId) {
        this.razaId = razaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnimalCriteria that = (AnimalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(caracterizacion, that.caracterizacion) &&
            Objects.equals(hierro, that.hierro) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(fechaCompra, that.fechaCompra) &&
            Objects.equals(sexo, that.sexo) &&
            Objects.equals(castrado, that.castrado) &&
            Objects.equals(fechaCastracion, that.fechaCastracion) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(lotesId, that.lotesId) &&
            Objects.equals(imagenesId, that.imagenesId) &&
            Objects.equals(eventosId, that.eventosId) &&
            Objects.equals(tipoId, that.tipoId) &&
            Objects.equals(razaId, that.razaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        caracterizacion,
        hierro,
        fechaNacimiento,
        fechaCompra,
        sexo,
        castrado,
        fechaCastracion,
        estado,
        lotesId,
        imagenesId,
        eventosId,
        tipoId,
        razaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (caracterizacion != null ? "caracterizacion=" + caracterizacion + ", " : "") +
                (hierro != null ? "hierro=" + hierro + ", " : "") +
                (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
                (fechaCompra != null ? "fechaCompra=" + fechaCompra + ", " : "") +
                (sexo != null ? "sexo=" + sexo + ", " : "") +
                (castrado != null ? "castrado=" + castrado + ", " : "") +
                (fechaCastracion != null ? "fechaCastracion=" + fechaCastracion + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (lotesId != null ? "lotesId=" + lotesId + ", " : "") +
                (imagenesId != null ? "imagenesId=" + imagenesId + ", " : "") +
                (eventosId != null ? "eventosId=" + eventosId + ", " : "") +
                (tipoId != null ? "tipoId=" + tipoId + ", " : "") +
                (razaId != null ? "razaId=" + razaId + ", " : "") +
            "}";
    }

}
