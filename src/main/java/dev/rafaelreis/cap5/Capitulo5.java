package dev.rafaelreis.cap5;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo5 {
    public static void main(String[] args) {

        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(5);
        
        /* 5.1 Comparators como lambda */

        //Com classe anonima
        Comparator<Usuario> comparator = new Comparator<Usuario>() {
            public int compare(Usuario u1, Usuario u2) {
                return u1.getNome().compareTo(u2.getNome());
            }
        };

        Collections.sort(usuarios, comparator);

        //Com lambda
        Comparator<Usuario> comparator2 = (u1, u2) -> u1.getNome().compareTo(u2.getNome());
        Collections.sort(usuarios, comparator2);

        //Mais enxuto ainda
        Collections.sort(usuarios, (u1, u2) -> u1.getNome().compareTo(u2.getNome()));

        Collections.sort(usuarios, (u1, u2) -> String.CASE_INSENSITIVE_ORDER.compare(u1.getNome(), u2.getNome()));

        usuarios.forEach(u -> System.out.println(u.getNome()));

        /* 5.2 O método List.sort */

        usuarios.sort((u1, u2) -> u1.getNome().compareTo(u2.getNome()));

        /* 5.3 Métodos estáticos na interface Comparator */
        Comparator<Usuario> comparator3 = Comparator.comparing(u -> u.getNome());
        usuarios.sort(comparator3);

        //import static
        usuarios.sort(comparing(u -> u.getNome()));

        //Indexando pela ordem natural
        List<String> palavras = Arrays.asList("Casa do Código", "Alura", "Caelum");
        palavras.sort(Comparator.naturalOrder());

        /* 5.4 Conhecendo melhor o Comparator.comparing */

        Function<Usuario, String> extraiNome = u -> u.getNome();
        Comparator<Usuario> comparator4 = Comparator.comparing(extraiNome);
        usuarios.sort(comparator4);

        /* 5.5 Ordenando por pontos e o autoboxing */

        usuarios.sort(Comparator.comparing(u -> u.getPontos()));

        //Faz o autoboxing e unboxing entre Integer e int
        Function<Usuario, Integer> extraiPontos = u -> u.getPontos();
        Comparator<Usuario> comparator5 = Comparator.comparing(extraiPontos);
        usuarios.sort(comparator5);

        //Sem necessidade de fazer autoboxing nos lambdas
        ToIntFunction<Usuario> extraiPontos2 = u -> u.getPontos();
        Comparator<Usuario> comparator6 = Comparator.comparingInt(extraiPontos2);
        usuarios.sort(comparator6);

        //Mais enxuto
        usuarios.sort(Comparator.comparingInt(u -> u.getPontos()));

    }
}
