package co.com.cima.agrofinca.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import co.com.cima.agrofinca.domain.enumeration.SINO;

import co.com.cima.agrofinca.domain.enumeration.SEXO;

import co.com.cima.agrofinca.domain.enumeration.ESTADOANIMAL;

/**
 * A Animal.
 */
@Entity
@Table(name = "animal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "animal")
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "caracterizacion")
    private String caracterizacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "hierro")
    private SINO hierro;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private SEXO sexo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "castrado", nullable = false)
    private SINO castrado;

    @Column(name = "fecha_castracion")
    private LocalDate fechaCastracion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private ESTADOANIMAL estado;

    @OneToMany(mappedBy = "animal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalLote> lotes = new HashSet<>();

    @OneToMany(mappedBy = "animal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalImagen> imagenes = new HashSet<>();

    @OneToMany(mappedBy = "animal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnimalEvento> eventos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "tipos", allowSetters = true)
    private Parametros tipo;

    @ManyToOne
    @JsonIgnoreProperties(value = "razas", allowSetters = true)
    private Parametros raza;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Animal nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCaracterizacion() {
        return caracterizacion;
    }

    public Animal caracterizacion(String caracterizacion) {
        this.caracterizacion = caracterizacion;
        return this;
    }

    public void setCaracterizacion(String caracterizacion) {
        this.caracterizacion = caracterizacion;
    }

    public SINO getHierro() {
        return hierro;
    }

    public Animal hierro(SINO hierro) {
        this.hierro = hierro;
        return this;
    }

    public void setHierro(SINO hierro) {
        this.hierro = hierro;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Animal fechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public Animal fechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
        return this;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public SEXO getSexo() {
        return sexo;
    }

    public Animal sexo(SEXO sexo) {
        this.sexo = sexo;
        return this;
    }

    public void setSexo(SEXO sexo) {
        this.sexo = sexo;
    }

    public SINO getCastrado() {
        return castrado;
    }

    public Animal castrado(SINO castrado) {
        this.castrado = castrado;
        return this;
    }

    public void setCastrado(SINO castrado) {
        this.castrado = castrado;
    }

    public LocalDate getFechaCastracion() {
        return fechaCastracion;
    }

    public Animal fechaCastracion(LocalDate fechaCastracion) {
        this.fechaCastracion = fechaCastracion;
        return this;
    }

    public void setFechaCastracion(LocalDate fechaCastracion) {
        this.fechaCastracion = fechaCastracion;
    }

    public ESTADOANIMAL getEstado() {
        return estado;
    }

    public Animal estado(ESTADOANIMAL estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(ESTADOANIMAL estado) {
        this.estado = estado;
    }

    public Set<AnimalLote> getLotes() {
        return lotes;
    }

    public Animal lotes(Set<AnimalLote> animalLotes) {
        this.lotes = animalLotes;
        return this;
    }

    public Animal addLotes(AnimalLote animalLote) {
        this.lotes.add(animalLote);
        animalLote.setAnimal(this);
        return this;
    }

    public Animal removeLotes(AnimalLote animalLote) {
        this.lotes.remove(animalLote);
        animalLote.setAnimal(null);
        return this;
    }

    public void setLotes(Set<AnimalLote> animalLotes) {
        this.lotes = animalLotes;
    }

    public Set<AnimalImagen> getImagenes() {
        return imagenes;
    }

    public Animal imagenes(Set<AnimalImagen> animalImagens) {
        this.imagenes = animalImagens;
        return this;
    }

    public Animal addImagenes(AnimalImagen animalImagen) {
        this.imagenes.add(animalImagen);
        animalImagen.setAnimal(this);
        return this;
    }

    public Animal removeImagenes(AnimalImagen animalImagen) {
        this.imagenes.remove(animalImagen);
        animalImagen.setAnimal(null);
        return this;
    }

    public void setImagenes(Set<AnimalImagen> animalImagens) {
        this.imagenes = animalImagens;
    }

    public Set<AnimalEvento> getEventos() {
        return eventos;
    }

    public Animal eventos(Set<AnimalEvento> animalEventos) {
        this.eventos = animalEventos;
        return this;
    }

    public Animal addEventos(AnimalEvento animalEvento) {
        this.eventos.add(animalEvento);
        animalEvento.setAnimal(this);
        return this;
    }

    public Animal removeEventos(AnimalEvento animalEvento) {
        this.eventos.remove(animalEvento);
        animalEvento.setAnimal(null);
        return this;
    }

    public void setEventos(Set<AnimalEvento> animalEventos) {
        this.eventos = animalEventos;
    }

    public Parametros getTipo() {
        return tipo;
    }

    public Animal tipo(Parametros parametros) {
        this.tipo = parametros;
        return this;
    }

    public void setTipo(Parametros parametros) {
        this.tipo = parametros;
    }

    public Parametros getRaza() {
        return raza;
    }

    public Animal raza(Parametros parametros) {
        this.raza = parametros;
        return this;
    }

    public void setRaza(Parametros parametros) {
        this.raza = parametros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Animal)) {
            return false;
        }
        return id != null && id.equals(((Animal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Animal{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", caracterizacion='" + getCaracterizacion() + "'" +
            ", hierro='" + getHierro() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", fechaCompra='" + getFechaCompra() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", castrado='" + getCastrado() + "'" +
            ", fechaCastracion='" + getFechaCastracion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
