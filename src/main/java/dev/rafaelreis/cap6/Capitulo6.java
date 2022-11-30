package dev.rafaelreis.cap6;

import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo6 {
    public static void main(String[] args) {

        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(10);

        /* 6.1 Tornando todos os usuários moderadores */

        usuarios.forEach(u -> u.tornarModerador());

        //com method reference
        usuarios.forEach(Usuario::tornarModerador);

        Consumer<Usuario> tornaModeador = Usuario::tornarModerador;
        usuarios.forEach(tornaModeador);

        /* 6.2 Comparando de uma forma ainda mais enxuta */

        usuarios.sort(Comparator.comparing(Usuario::getNome));

        Function<Usuario, String> byName = Usuario::getNome;
        usuarios.sort(comparing(byName));

        /* 6.3 Compondo comparators */

        usuarios.sort(Comparator.comparingInt(u -> u.getPontos()));

        //Ordenar por pontos, quando houver empate ordenar por Nome
        Comparator<Usuario> c = Comparator.comparingInt(Usuario::getPontos)
                                        .thenComparing(Usuario::getNome);

        usuarios.sort(c);

        usuarios.sort(Comparator.comparingInt(Usuario::getPontos)
                                .thenComparing(Usuario::getNome));
        
        //Move os valores null para o final 
        usuarios.sort(Comparator.nullsLast(Comparator.comparing((Usuario::getNome))));
        
        //Move os valores null para o inicio 
        usuarios.sort(Comparator.nullsFirst(Comparator.comparing((Usuario::getNome))));

        //usando o reversed
        usuarios.sort(Comparator.comparing(Usuario::getPontos).reversed());

        /* 6.4 Referenciando métodos de instância */

        Usuario rodrigo = Usuario.of("Rodrigo Turint", 50);
        Runnable bloco = rodrigo::tornarModerador;
        bloco.run();

        Runnable bloco1 = rodrigo::tornarModerador;
        Runnable bloco2 = () -> rodrigo.getNome();

        Consumer<Usuario> consumer = Usuario::tornarModerador;
        consumer.accept(rodrigo);

        Consumer<Usuario> consumer1 = Usuario::tornarModerador;
        Consumer<Usuario> consumer2 = u -> u.tornarModerador();

        /* 6.5 Referenciando métodos que recebem argumentos */

        usuarios.forEach(System.out::println);

        /* 6.6 Referenciando construtores */

        Supplier<User> criadorDeUsuario = User::new;
        User novo = criadorDeUsuario.get();

        Function<String, User> criadorDeUsuarios2 = User::new;
        User rodrigo2 = criadorDeUsuarios2.apply("Rodrigo Turini");
        User paulo = criadorDeUsuarios2.apply("Paulo Silveira");
        
        BiFunction<String, Integer, User> criadorDeUsuarios3 = User::new;
        User rodrigo3 = criadorDeUsuarios3.apply("Rodrigo Turini", 50);
        User paulo2 = criadorDeUsuarios3.apply("Paulo Silveira", 300);

        /* 6.7 Outros tipos de referências */

        BiFunction<Integer, Integer, Integer> max = Math::max;
        ToIntBiFunction<Integer, Integer> max2 = Math::max;
        IntBinaryOperator max3 = Math::max;

        int maior = max.apply(20, 50);
        int maior2 = max2.applyAsInt(50, 200);
        int maior3 = max3.applyAsInt(500, 80);
    


        
    }
}
