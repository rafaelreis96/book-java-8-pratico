package dev.rafaelreis.cap8;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo8 {
    public static void main(String[] args) throws IOException {
        
        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(10);

        /* 8.1 Ordenando um Stream */
        usuarios.stream()
            .filter(u -> u.getPontos() > 100)
            .sorted(Comparator.comparing(Usuario::getNome));

        List<Usuario> filtradosOrdenados = usuarios.stream()
            .filter(u -> u.getPontos() > 100)
            .sorted(Comparator.comparing(Usuario::getNome))
            .collect(Collectors.toList());

        /* 8.3 Qual é a vantagem dos métodos serem lazy? */  
        Optional<Usuario> usuarioOpcional = usuarios.stream()
            .filter(u -> u.getPontos() > 100)//executa até encontrar o primeiro elemento que satisfaz o filtro
            .findAny();//força o inicio do pipeline

        /* 8.4 Enxergando a execução do pipeline com peek */

        //Podemos ver que só serão mostrados os elementos até que seja encontrado algum
        //elemento que cumpra o predicado
        usuarios.stream()
            .filter(u -> u.getPontos() > 100)
            .peek(System.out::println)
            .findAny();

        /* 8.5 Operações de redução */

        //buscar valor maximo
        Optional<Usuario> max = usuarios.stream()
        .max(Comparator.comparing(Usuario::getPontos));
        Usuario maximaPontuacao = max.get();

        //somar todos os pontos
        int total = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .sum();

        //quebrando operação para exergar a operação de redução
        int valorInicial = 0;
        IntBinaryOperator operacao = (a, b) -> a + b;

        int total2 = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .reduce(0, (a, b) -> a + b);

        //usando novo metodo do Integer, o sum, que recebe dois inteitos
        int total3 = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .reduce(0, Integer::sum);

        /* Qual é a vantagem de usarmos a redução em vez do sum? Nenhuma. 
        O importante é conhecê-lo para poder realizar operações que não 
        se encontram no Stream.
        Por exemplo? Multiplicar todos os pontos: */

        int multiplicacao = usuarios.stream()
            .mapToInt(Usuario::getPontos)
            .reduce(1, (a,b) -> a * b);

        //Soma sem o Map é menos custosa
        int total4 = usuarios.stream()
            .reduce(0, (atual, u) -> atual + u.getPontos(), Integer::sum);

        /* 8.6 Conhecendo mais métodos do Stream */

        //Não funciona, é esperado um List, não um Stream
       /*for (Usuario u : usuarios.stream()){
         
        } */ 

        //Usando interator
        Iterator<Usuario> i = usuarios.stream().iterator();

        //percorre o iterator, recebe um Consumer como parametro
        usuarios.stream().iterator().forEachRemaining(System.out::println);

        //estando Predicates

        //Algum moderadores?
        boolean hasModerador = usuarios.stream().anyMatch(Usuario::isModerador);

        //Todos são moderadores?
        boolean hasModerador1 = usuarios.stream().allMatch(Usuario::isModerador);

        //Nenum moderador?
        boolean hasModerador2 = usuarios.stream().noneMatch(Usuario::isModerador);

        //quantidade
        usuarios.stream().count();

        //skip, pulando os dois primeiros
        usuarios.stream().skip(2);

        //limite
        usuarios.stream().limit(5);

        //Criando Stream

        Usuario user1 = UsuarioDataFactory.listaDeUsuarios(1).get(0);
        Usuario user2 = UsuarioDataFactory.listaDeUsuarios(1).get(0);
        Usuario user3 = UsuarioDataFactory.listaDeUsuarios(1).get(0);

        Stream<Usuario> usuarios2 = Stream.of(user1, user2, user3);
        Stream<Usuario> usuarios3 = Stream.of(user1, user2, user3);

        //concatenando Stream
        Stream.concat(usuarios2, usuarios3);

        /* 8.7 Streams primitivos e infinitos */

        //Método factory, Imprime de 0 a 10
        IntStream.range(0, 10).forEach(System.out::println);
    
        // # Streams infinitos
        //Um outro recurso poderoso do Java 8: através da interface de factory Supplier,
        //podemos definir um Stream infinito

        //gerar uma lista “infinita” de números aleatórios
        Random random = new Random(0);
        Supplier<Integer> supplier = () -> random.nextInt();
        Stream<Integer> stream = Stream.generate(supplier);

        // O Stream gerado por generate é lazy. Certamente ele não vai gerar infinitos
        //números aleatórios. Eles só serão gerados à medida que forem necessários.
        
        //evitando boxing
        IntStream intStream = IntStream.generate(() -> random.nextInt());

        //Podemos apenas utilizar operações de curto-circuito em Streams infinitos.
        
        //Nunca pararia de executar
        //int valor = intStream.sum();

        // # Operações de curto circuito
        //São operações que não precisam processar todos os elementos. Um exemplo
        //seria pegar apenas os 100 primeiros elementos com limit:

        Random random2 = new Random(0);
        IntStream stream2 = IntStream.generate(() -> random.nextInt());
        List<Integer> lista = stream2
            .limit(100)
            .boxed()//retorna um Stream<Integer>
            .collect(Collectors.toList());

        
        Random random3 = new Random(0);
        List<Integer> lista2 = IntStream
            .generate(() -> random.nextInt())
            .limit(100)
            .boxed()
            .collect(Collectors.toList());

        //gerar a sequência infinita de números de Fibonacci de maneira
        //lazy e imprimir seus 10 primeiros elementos   

        class Fibonacci implements IntSupplier {
            private int anterior = 0;
            private int proximo = 0;

            public int getAsInt() {
                proximo += anterior;
                anterior -= proximo;
                return anterior;
            }
        }

        IntStream.generate(new Fibonacci())
            .limit(100)
            .forEach(System.out::println);

        //Veremos que manter o estado em uma interface funcional pode 
        //limitar os recursos de paralelização que um Stream fornece.

        int maiorQue100 = IntStream
            .generate(new Fibonacci())
            .filter(f -> f > 100)
            .findFirst()
            .getAsInt();

        System.out.println(maiorQue100);

        //Quando for necessário manter o estado de apenas uma variável, podemos usar
        //o iterate em vez do generate, que recebe um UnaryOperator.
        IntStream
            .iterate(0, x -> x + 1)
            .limit(10)
            .forEach(System.out::println);

        /* Praticando o que aprendemos com java.nio.file.Files */

        //listar arquivos do diretorio
        Files.list(Paths.get("./dev/rafaelreis"))
            .forEach(System.out::println);

        //Quer apenas os arquivos java? Pode usar um filter:
        Files.list(Paths.get("./dev/rafaelreis"))
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(System.out::println);

        //E se quisermos todo o conteúdo dos arquivos? Vamos tentar usar o
        //Files.lines para ler todas as linhas de cada arquivo. 
        Files.list(Paths.get("./dev/rafaelreis/cap8"))
            .filter(p -> p.toString().endsWith(".java"))
            .map(p -> lines(p)) // usando metodo statico por causa da checked exception
            .forEach(System.out::println);

        //O problema é que, com esse map, teremos um Stream<Stream<String>>,
        //pois a invocação de lines(p) devolve um Stream<String> para cada Path do
        //nosso Stream<Path> original!
        Stream<Stream<String>> strings = Files.list(Paths.get("./dev/rafaelreis/cap8"))
            .filter(p -> p.toString().endsWith(".java"))
            .map(p -> lines(p));


        /* 8.9 FlatMap */

        //Podemos achatar um Stream de Streams com o flatMap. 
        //Basta trocar a invocação, que teremos no final um Stream<String>:
        Stream<String> strings2 = Files.list(Paths.get("./dev/rafaelreis/cap8"))
            .filter(p -> p.toString().endsWith(".java"))
            .flatMap(p -> lines(p));

        IntStream chars = Files.list(Paths.get("./dev/rafaelreis/cap8"))
            .filter(p -> p.toString().endsWith(".java"))
            .flatMap(p -> lines(p))
            .flatMapToInt((String s)->s.chars());

        // # Mais um exemplo de flatMap

        //Quando trabalhamos com coleções de coleções, usamos o flatMap quando
        //queremos que o resultado da nossa transformação seja reduzido a um 
        //Stream ‘simples’, sem composição.

        //Imagine que temos grupos de usuários:
        class Grupo {
            private Set<Usuario> usuarios = new HashSet<>();

            public void add(Usuario u) {
                usuarios.add(u);
            }

            public Set<Usuario> getUsuarios() {
                return Collections.unmodifiableSet(this.usuarios);
            }
        }

        //E que tenhamos alguns grupos,separando quem fala inglês e quem fala espanhol

        Grupo englishSpeakers = new Grupo();
        englishSpeakers.add(user1);
        englishSpeakers.add(user2);

        Grupo spanishSpeakers = new Grupo();
        spanishSpeakers.add(user2);
        spanishSpeakers.add(user3);

        //Se temos esses grupos dentro de uma coleção:
        List<Grupo> groups = Arrays.asList(englishSpeakers, spanishSpeakers);

        //Pode ser que queiramos todos os usuários desses grupos
        groups.stream()
            .flatMap(g -> g.getUsuarios().stream())
            .distinct()
            .forEach(System.out::println);

        //sem Distinct
        groups.stream()
            .flatMap(g -> g.getUsuarios().stream())
            .collect(Collectors.toSet())
            .forEach(System.out::println);
    }

    static Stream<String> lines(Path p) {
        try{
            return Files.lines(p);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
