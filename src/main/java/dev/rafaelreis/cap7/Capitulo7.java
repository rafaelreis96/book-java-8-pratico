package dev.rafaelreis.cap7;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo7 {
    public static void main(String[] args) {

        /* 7.1 Tornando moderadores os 10 usuários com mais pontos */
        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(15);

        //filtra os10 usuários com mais pontos e torná-los moderadores
        usuarios.sort(Comparator.comparing(Usuario::getPontos).reversed());
        
        System.out.println("\nUSUARIOS");
        usuarios.forEach(System.out::println);

        System.out.println("\nUSUARIOS TORNADOS MODERADORES");
        usuarios
            .subList(0, 10)
            .forEach( u -> {
                u.tornarModerador();
                System.out.println(u);
            });


        /* 7.2 Streams: tornando moderadores os usuários com mais de 100 pontos */
 
        Stream<Usuario> stream = usuarios.stream();
        stream.filter(u -> u.getPontos() > 100);
        
        //mais enxuto
        usuarios.stream().filter(u -> u.getPontos() > 100);

        /* Assim como os demais métodos da interface Stream, não alteram
        os elementos do stream original!*/
        Stream<Usuario> stream2 = usuarios.stream().filter(u -> u.getPontos() > 100);
        System.out.println("\nPONTOS MAIORES QUE 100");
        stream2.forEach(System.out::println);

        //Encadeado, mais enxuto
        usuarios.stream()
                .filter(u -> u.getPontos() > 100)
                .forEach(System.out::println);

        usuarios.stream().filter(Usuario::isModerador);

        /* 7.3 Como obter de volta uma Lista? */

        //Manualmente
        List<Usuario> maisQue100 = new ArrayList<>();
        usuarios.stream()
                .filter(u -> u.getPontos() > 100)
                .forEach(u -> maisQue100.add(u));
        
        //Manualmente com reference method
        usuarios.stream()
                .filter(u -> u.getPontos() > 100)
                .forEach(maisQue100::add);

        /* 7.4 Collectors */

        //Podemos usar o método collect para resgatar esses elementos
        //do nosso Stream<Usuario> para uma List

        List<Usuario> usuarios2 = usuarios.stream()
            .filter( u -> u.getPontos() > 100)
            .collect(Collectors.toList());

        /* 7.5 Avançado: por que não há um toList em Stream? */

        //Também podemos utilizar o método toSet para coletar as informações desse
        //Stream em um Set<Usuario>:

        Set<Usuario> usuarios3 = usuarios.stream()
            .filter( u -> u.getPontos() > 100)
            .collect(Collectors.toSet());

        //toCollection, que permite que você escolha a 
        //implementação que será devolvida no final da coleta

        Set<Usuario> usuarios4 = stream.collect(Collectors.toCollection(HashSet::new));
        //ou
        Set<Usuario> usuarios5 = stream.collect(Collectors.toCollection(() -> new HashSet()));
    
        //toArray
        Usuario[] array = stream.toArray(Usuario[]::new);

        /* 7.6 Liste apenas os pontos de todos os usuários como map */
        //Manual
        List<Integer> pontos = new ArrayList<>();
        usuarios.forEach(u -> pontos.add(u.getPontos()));

        //Com Map
        List<Integer> pontos2 = usuarios.stream()
            .map(u->u.getPontos())
            .collect(Collectors.toList());

        //Map + method reference
        List<Integer> pontos3 = usuarios.stream()
            .map(Usuario::getPontos)
            .collect(Collectors.toList());

        /* 7.7 IntStream e a família de Streams */
        //Isso gera o boxing dos nossos inteiros. Se formos operar sobre eles, teremos
        //um overhead indesejado
        Stream<Integer> stream3 = usuarios.stream().map(Usuario::getPontos);

        //Podemos usar o IntStream aqui para evitar o autoboxing
        IntStream stream4 = usuarios.stream().mapToInt(Usuario::getPontos);

        //No IntStream, existem métodos que simplificam bastante nosso trabalho
        //quando estamos trabalhando com inteiros, como max, sorted e average.
        
        //obter a média de pontos dos usuários
        double pontuacaoMedia = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .average()
            .getAsDouble();


        /* 7.8 O Optional em java.util */
        OptionalDouble media = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .average();

        double pontuacaoMedia2 = media.orElse(0.0);
        
        //uma linha
        double pontuacaoMedia3 = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .average()
            .orElse(0.0);

        double pontuacaomedia3 = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .average()
            .orElseThrow(IllegalStateException::new);

        
        DoubleConsumer janela = (valor) -> System.out.println(valor);
        usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .average()
            .ifPresent(janela);

        //usuário com maior quantidade de pontos
        Optional<Usuario> max = usuarios.stream()
            .max(Comparator.comparingInt(Usuario::getPontos));

        Optional<String> maxNome = usuarios.stream()
            .max(Comparator.comparingInt(Usuario::getPontos))
            .map( u -> u.getNome());

        if(maxNome.isPresent()) {
            System.out.println(maxNome);
        }
    }
}
