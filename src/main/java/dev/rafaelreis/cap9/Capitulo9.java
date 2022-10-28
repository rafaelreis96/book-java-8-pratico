package dev.rafaelreis.cap9;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo9 {
    public static void main(String[] args) throws IOException {

        /* 9.1 Coletores gerando mapas */

        //contar linhas do arquivo
        LongStream line = Files.list(Paths.get("./dev/rafaelreis/geral"))
            .filter(p -> p.toString().endsWith(".java"))
            .mapToLong(p -> lines(p).count());

        List<Long> lines = Files.list(Paths.get("./dev/rafaelreis/geral"))
            .filter(p -> p.toString().endsWith(".java"))
            .map(p -> lines(p).count())
            .collect(Collectors.toList());


        Map<Path, Long> linesPerFile = new HashMap<>();
        Files.list(Paths.get("./dev/rafaelreis/geral"))
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> linesPerFile.put(p, lines(p).count()));
        
        System.out.println(linesPerFile);


        //Podemos criar esse mesmo mapa com um outro coletor mais específico para esse
        //tipo de tarefa, o toMap:

        Map<Path, Long> lines2 = Files.list(Paths.get("./dev/rafaelreis/geral"))
            .filter(p -> p.toString().endsWith(".java"))
            .collect(Collectors.toMap(
                p->p, 
                p -> lines(p).count()));

        System.out.println(lines2);
            
        Map<Path, List<String>> content = Files.list(Paths.get("./dev/rafaelreis/geral"))
        .filter(p -> p.toString().endsWith(".java"))
        .collect(Collectors.toMap(
            Function.identity(),
            p -> lines(p).collect(Collectors.toList())));

        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(5);
        
        Map<String, Usuario> nameToUser = usuarios
            .stream()
            .collect(Collectors.toMap(Usuario::getNome, Function.identity()));


        /* 9.2 groupingBy e partitioningBy */
    }

    static Stream<String> lines(Path p) {
        try {
            return Files.lines(p);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
