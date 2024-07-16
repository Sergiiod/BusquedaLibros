package com.alura.LiteraluraDesafio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("title")String titulo,
        @JsonAlias("authors")List<DatosAutores> listaAutores,
        @JsonAlias("languages")List<String> idioma,
        @JsonAlias("download_count") Integer numeroDescargas
) {
}
