package com.alura.LiteraluraDesafio.repository;

import com.alura.LiteraluraDesafio.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository <Autores, Long> {

    @Query("""
            SELECT a
            FROM Autores
            a WHERE a.birthYear <= :consulta AND a.deathYear >= :consulta
            """)
    List<Autores> buscarAutorPorDeterminadoAño(int consulta);

    Optional<Autores> findByNombreContains(String nombre);
}
