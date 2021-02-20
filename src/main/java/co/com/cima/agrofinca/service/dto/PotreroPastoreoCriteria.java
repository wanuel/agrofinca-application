package co.com.cima.agrofinca.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import co.com.cima.agrofinca.domain.enumeration.SINO;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link co.com.cima.agrofinca.domain.PotreroPastoreo} entity. This class is used
 * in {@link co.com.cima.agrofinca.web.rest.PotreroPastoreoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /potrero-pastoreos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PotreroPastoreoCriteria implements Serializable, Criteria {
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaIngreso;

    private LocalDateFilter fechaSalida;

    private LocalDateFilter fechaLimpia;

    private IntegerFilter diasDescanso;

    private IntegerFilter diasCarga;

    private SINOFilter limpia;

    private LongFilter loteId;

    private LongFilter potreroId;

    public PotreroPastoreoCriteria() {
    }

    public PotreroPastoreoCriteria(PotreroPastoreoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaIngreso = other.fechaIngreso == null ? null : other.fechaIngreso.copy();
        this.fechaSalida = other.fechaSalida == null ? null : other.fechaSalida.copy();
        this.fechaLimpia = other.fechaLimpia == null ? null : other.fechaLimpia.copy();
        this.diasDescanso = other.diasDescanso == null ? null : other.diasDescanso.copy();
        this.diasCarga = other.diasCarga == null ? null : other.diasCarga.copy();
        this.limpia = other.limpia == null ? null : other.limpia.copy();
        this.loteId = other.loteId == null ? null : other.loteId.copy();
        this.potreroId = other.potreroId == null ? null : other.potreroId.copy();
    }

    @Override
    public PotreroPastoreoCriteria copy() {
        return new PotreroPastoreoCriteria(this);
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

    public LocalDateFilter getFechaLimpia() {
        return fechaLimpia;
    }

    public void setFechaLimpia(LocalDateFilter fechaLimpia) {
        this.fechaLimpia = fechaLimpia;
    }

    public IntegerFilter getDiasDescanso() {
        return diasDescanso;
    }

    public void setDiasDescanso(IntegerFilter diasDescanso) {
        this.diasDescanso = diasDescanso;
    }

    public IntegerFilter getDiasCarga() {
        return diasCarga;
    }

    public void setDiasCarga(IntegerFilter diasCarga) {
        this.diasCarga = diasCarga;
    }

    public SINOFilter getLimpia() {
        return limpia;
    }

    public void setLimpia(SINOFilter limpia) {
        this.limpia = limpia;
    }

    public LongFilter getLoteId() {
        return loteId;
    }

    public void setLoteId(LongFilter loteId) {
        this.loteId = loteId;
    }

    public LongFilter getPotreroId() {
        return potreroId;
    }

    public void setPotreroId(LongFilter potreroId) {
        this.potreroId = potreroId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PotreroPastoreoCriteria that = (PotreroPastoreoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fechaIngreso, that.fechaIngreso) &&
            Objects.equals(fechaSalida, that.fechaSalida) &&
            Objects.equals(fechaLimpia, that.fechaLimpia) &&
            Objects.equals(diasDescanso, that.diasDescanso) &&
            Objects.equals(diasCarga, that.diasCarga) &&
            Objects.equals(limpia, that.limpia) &&
            Objects.equals(loteId, that.loteId) &&
            Objects.equals(potreroId, that.potreroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fechaIngreso,
        fechaSalida,
        fechaLimpia,
        diasDescanso,
        diasCarga,
        limpia,
        loteId,
        potreroId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotreroPastoreoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fechaIngreso != null ? "fechaIngreso=" + fechaIngreso + ", " : "") +
                (fechaSalida != null ? "fechaSalida=" + fechaSalida + ", " : "") +
                (fechaLimpia != null ? "fechaLimpia=" + fechaLimpia + ", " : "") +
                (diasDescanso != null ? "diasDescanso=" + diasDescanso + ", " : "") +
                (diasCarga != null ? "diasCarga=" + diasCarga + ", " : "") +
                (limpia != null ? "limpia=" + limpia + ", " : "") +
                (loteId != null ? "loteId=" + loteId + ", " : "") +
                (potreroId != null ? "potreroId=" + potreroId + ", " : "") +
            "}";
    }

}
