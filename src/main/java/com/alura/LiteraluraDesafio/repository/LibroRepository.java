package com.alura.LiteraluraDesafio.repository;

import com.alura.LiteraluraDesafio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

    Optional<Libro> findByTituloContains(String titulo);

    @Query("""
            SELECT l
            FROM Libro
            l WHERE l.idioma = :idioma
            """)
    List<Libro> buscarPorIdioma(String idioma);

    @Query("""
            SELECT l
            FROM Libro
            l ORDER BY l.numeroDeDescargas DESC LIMIT 10
            """)
    List<Libro>top10Libros();
}
