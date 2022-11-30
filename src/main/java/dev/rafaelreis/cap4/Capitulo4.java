package dev.rafaelreis.cap4;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo4 {
    public static void main(String[] args) {
        //Lista imutavel
        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(5);

        /* 4.2 A interface Consumer não tem só um método! 
        Tomamos o cuidado, nos capítulos anteriores, de deixar claro que 
        uma interface funcional é aquela que possui apenas um método abstrato! 
        Ela pode ter sim mais métodos, desde que sejam métodos default. */
        Consumer<Usuario> mostrarMensagem = u -> System.out.println("Antes de Imprimir");

        Consumer<Usuario> imprimeNota = u -> System.out.println(u.getNome());

        usuarios.forEach(mostrarMensagem.andThen(imprimeNota));

        /* 4.3 Mais um novo método em Collection: removeIf */

        Predicate<Usuario> predicado = new Predicate<Usuario>() {
            @Override
            public boolean test(Usuario u) {
                return u.getPontos() > 160;
            }
        };

        //Lista Mutavel
        List<Usuario> usuarios2 = UsuarioDataFactory.listaDeUsuarios(5);

        usuarios2.removeIf(predicado);//com predicate
        usuarios2.removeIf(u -> u.getPontos() > 160);//com lambda
        usuarios.forEach(u -> System.out.println(u));

        /* Métodos defaults foram adicionados para permitir 
        que interfaces evoluam sem quebrar código existente. */
    }
}
