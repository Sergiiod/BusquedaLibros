package com.alura.LiteraluraDesafio.principal;

import com.alura.LiteraluraDesafio.model.Autores;
import com.alura.LiteraluraDesafio.model.DatosTotales;
import com.alura.LiteraluraDesafio.model.DatosLibros;
import com.alura.LiteraluraDesafio.model.Libro;
import com.alura.LiteraluraDesafio.repository.AutorRepository;
import com.alura.LiteraluraDesafio.repository.LibroRepository;
import com.alura.LiteraluraDesafio.service.ConsumoApi;
import com.alura.LiteraluraDesafio.service.ConvierteDatos;

import java.util.*;

public class Principal {

    Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_Base = "https://gutendex.com/books/?search=";
    private ConvierteDatos convierte = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository LibroRepository, AutorRepository AutorRepository) {
        this.libroRepository = LibroRepository;
        this.autorRepository = AutorRepository;
    }

    public void mostrarMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """

                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Libros por idiomas
                    6 - Top 10 libros mas descargados
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listaLibrosRegistrados();
                    break;
                case 3:
                    listaAutoresRegistrados();
                    break;
                case 4:
                    autoresVivosDate();
                    break;
                case 5:
                    listaLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosDescargados();
                    break;
                case 0:
                    System.out.println("Cerrando app, gracias");
                    break;
                default:
                    System.out.println("opción no valida");
                    break;
            }
        }
    }


    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        String buscarTitulo = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_Base + buscarTitulo.replace(" ", "%20"));
        System.out.println(json);
        var datos = convierte.obtenerDatos(json, DatosTotales.class);
        System.out.println(datos);
        if (datos.resultadoLibros().isEmpty()) {
            System.out.println("Libro " + buscarTitulo + " no encontrado");
        } else {
            DatosLibros datosLibros = datos.resultadoLibros().get(0);
            System.out.println(datosLibros);
            Libro libros = new Libro(datosLibros);
            System.out.println(libros);
            Autores autores = new Autores().getPrimerAutor(datosLibros);
            System.out.println(autores);

            guardarDatos(libros, autores);
        }
    }

    private void guardarDatos(Libro libros, Autores autores) {
        Optional<Libro> registroLibro = libroRepository.findByTituloContains(libros.getTitulo());

        if (registroLibro.isPresent()) {
            System.out.println("Libro ya registrado");
        } else {
            try {
                libroRepository.save(libros);
                System.out.println("El libro se ha registrado");
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }

        Optional<Autores> encontrarAutor = autorRepository.findByNombreContains(autores.getNombre());

        if (encontrarAutor.isPresent()) {
            System.out.println("Autor ya registrado");
        } else {
            try {
                autorRepository.save(autores);
                System.out.println("Autor registrado satisfactoriamente");
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }
    }

    private void listaLibrosRegistrados() {
        System.out.println("\nLibros Registrados");
        List<Libro> librosRegistados = libroRepository.findAll();
        librosRegistados.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listaAutoresRegistrados() {
        System.out.println("Autores Registrados");
        List<Autores> autoresRegistrados = autorRepository.findAll();
        autoresRegistrados.stream()
                .sorted(Comparator.comparing(Autores::getNombre))
                .forEach(System.out::println);
    }

    private void autoresVivosDate() {
        System.out.println("\nAutores vivos en este año");
        System.out.println("escriba el año que desea consultar:");
        Integer consulta = Integer.valueOf(teclado.nextLine());
        List<Autores> autores = autorRepository.buscarAutorPorDeterminadoAño(consulta);
        if (autores.isEmpty()) {
            System.out.println("No hay autores vivos en este año");
        } else {
            System.out.println("Autores vivos en este año");
            autores.stream()
                    .sorted(Comparator.comparing(Autores::getNombre))
                    .forEach(System.out::println);
        }
    }

    private void listaLibrosPorIdioma() {
        System.out.println("""
                Buscar libros por idioma 
                "es"   Español
                "en"   Inglés
                "fr"   Francés
                "jp"   Japonés
                "pt"   Portugués
                "de"   Alemán
                ingrese el id del idioma:
                """);
        String idioma = teclado.nextLine();
        List<Libro> libros = libroRepository.buscarPorIdioma(idioma);
        if (!libros.isEmpty()) {
            System.out.println(libros);
        } else {
            System.out.println("No se encuentran libros con este idioma");
        }
    }

    private void top10LibrosDescargados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay datos registrados");
        } else {
            System.out.println("\n Top 10 libros más descargados");
            System.out.println(libroRepository.top10Libros().toString());
        }

    }
}

