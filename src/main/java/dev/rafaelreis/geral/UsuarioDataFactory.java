package dev.rafaelreis.geral;

import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;

public class UsuarioDataFactory {

    private static Faker faker;

    private UsuarioDataFactory(){ 
        faker = Faker.instance();
    }

    public static List<Usuario> listaDeUsuarios(int quantidade) {
        List<Usuario> usuarios = new ArrayList<>();
        for(int i=0; i < quantidade; i++) {
            Usuario usuario = Usuario.of(faker.name().fullName(), faker.number().numberBetween(0, 1000));
            usuarios.add(usuario);
        }

        return usuarios;
    }

}
