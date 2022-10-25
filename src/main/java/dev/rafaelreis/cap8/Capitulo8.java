package dev.rafaelreis.cap8;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo8 {
    public static void main(String[] args) {
        
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
    }
}
