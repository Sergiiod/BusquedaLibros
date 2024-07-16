package com.alura.LiteraluraDesafio.model;


import jakarta.persistence.*;

@Entity
@Table(name="libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String idioma;
    private Integer numeroDeDescargas;

    public Libro(){}

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor = getPrimerAutor(datosLibros).getNombre();
        this.idioma =getPrimerIdioma(datosLibros);
        this.numeroDeDescargas = datosLibros.numeroDescargas();
    }

    private String getPrimerIdioma(DatosLibros datosLibros) {
        return datosLibros.idioma().get(0);
    }

    private Autores getPrimerAutor(DatosLibros datosLibros) {
        DatosAutores datosAutores = datosLibros.listaAutores().get(0);
        return new Autores(datosAutores);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }


    @Override
    public String toString() {
        return "\n" +
                "\n Titulo: " + titulo +
                "\n Autor: " + autor +
                "\n Idioma: " + idioma +
                "\n Numero de Descargas: " + numeroDeDescargas +
                "\n" ;
    }

}
