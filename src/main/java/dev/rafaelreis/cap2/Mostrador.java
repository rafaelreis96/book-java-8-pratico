package dev.rafaelreis.cap2;

import java.util.function.Consumer;

import dev.rafaelreis.geral.Usuario;

public class Mostrador implements Consumer<Usuario>{
    public void accept(Usuario usuario) {
        System.out.println(usuario.getNome());
    }
}
