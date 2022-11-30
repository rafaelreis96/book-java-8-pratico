package dev.rafaelreis.cap2;

import java.util.List;
import java.util.function.Consumer;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo2 {
    public static void main(String[] args) {
        
        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(5);

        /* Loops da maneira antiga nova */
        
        for(Usuario user: usuarios) {
            System.out.println(user.getNome());
        }

        //Utilizando uma classe que interface Consumer
        Mostrador mostrador = new Mostrador();
        usuarios.forEach(mostrador);

        //Interface Consumer via função anônima
        Consumer<Usuario> mostrador2 = new Consumer<Usuario>() {
            public void accept(Usuario usuario) {
                System.out.println(usuario.getNome());
            }
        };
        usuarios.forEach(mostrador2);

        //Outra forma, essa um pouco mais enxuta
        usuarios.forEach(new Consumer<Usuario>() {
            public void accept(Usuario usuario) {
                System.out.println(usuario.getNome());
            }
        });

        /* Que entre o Lambda */
        
        //com tipo e chaves
        Consumer<Usuario> mostrador3 = (Usuario u) -> { System.out.println(u.getNome());};
        
        //sem tipo e com chaves
        Consumer<Usuario> mostrador4 = u -> { System.out.println(u.getNome());};
        
        //sem tipo e chaves
        Consumer<Usuario> mostrador5 = u -> System.out.println(u.getNome());

        //diretamento no foreach
        usuarios.forEach(u -> System.out.println(u.getNome()));

        //acessando outros metodos
        usuarios.forEach(u -> u.tornarModerador());

    }
}