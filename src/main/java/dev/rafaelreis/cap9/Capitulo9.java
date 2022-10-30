package dev.rafaelreis.cap9;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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

        List<Usuario> usuarios2 = UsuarioDataFactory.listaDeUsuarios(5);

        //tradicional
        Map<Integer, List<Usuario>> pontuacao = new HashMap<>();

        for(Usuario u: usuarios) {
            if(pontuacao.containsKey(u.getPontos())) {
                pontuacao.put(u.getPontos(), new ArrayList<>());
            }
            pontuacao.get(u.getPontos()).add(u);
        }

        System.out.println(pontuacao);

        //No Java 8
        for(Usuario u: usuarios2) {
            pontuacao
                .computeIfAbsent(u.getPontos(), user -> new ArrayList<>())
                .add(u);
        }

        System.out.println(pontuacao);

        //Com Stream
        Map<Integer, List<Usuario>> pontuacao2 = usuarios2
            .stream().collect(Collectors.groupingBy(Usuario::getPontos));

        //particionar todos os usuários entre moderadores e não moderadores
        //O partitioningBy nada mais é do que uma versão mais eficiente para 
        //ser usada ao agrupar booleans
        Map<Boolean, List<Usuario>> moderadores = usuarios
            .stream()
            .collect(Collectors.partitioningBy(Usuario::isModerador));

        System.out.println(moderadores);

        Map<Boolean, List<String>> nomesPorTipo = usuarios
            .stream()
            .collect(Collectors.partitioningBy(
                Usuario::isModerador, 
                Collectors.mapping(
                    Usuario::getNome, 
                    Collectors.toList())));

        System.out.println(nomesPorTipo);


        /*Queremos particionar por moderação, mas ter
        como valor não os usuários, mas sim a soma de seus pontos. Também existe um
        coletor para realizar essas somatórias, que pode ser usado em conjunto com o
        partitioningBy e groupingBy: */
        Map<Boolean, Integer> pontuacaoPorTipo = usuarios
            .stream()
            .collect(Collectors.partitioningBy(
                Usuario::isModerador,
                Collectors.summingInt(Usuario::getPontos)));

        System.out.println(pontuacaoPorTipo);

        //Até mesmo para concatenar todos os nomes dos usuários há um coletor
        String nomes = usuarios
            .stream()
            .map(Usuario::getNome)
            .collect(Collectors.joining(", "));


        /* 9.3 Executando o pipeline em paralelo */

        //Comum
        List<Usuario> filtradosOredenados = usuarios
            .stream()
            .filter(u -> u.getPontos() > 100)
            .sorted(Comparator.comparing(Usuario::getNome))
            .collect(Collectors.toList());

        //Com execucao em Parelelo
        List<Usuario> filtradosOredenados2 = usuarios
            .parallelStream()
            .filter(u -> u.getPontos() > 100)
            .sorted(Comparator.comparing(Usuario::getNome))
            .collect(Collectors.toList());


        long sum = LongStream.range(0, 1_000_000_000)
            .parallel()
            .filter(x -> x % 2 == 0)
            .sum();
            
    }

    static Stream<String> lines(Path p) {
        try {
            return Files.lines(p);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
