package dev.rafaelreis.cap12;

import java.security.PrivilegedAction;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import dev.rafaelreis.geral.Usuario;
import dev.rafaelreis.geral.UsuarioDataFactory;

public class Capitulo12 {
    public static void main(String[] args) {
        /* 12.1 Novos detalhes na linguagem */

        //#Operador diamante melhorado, agora pode inferido 
        //quando passados por parametros.
        
        //List<Usuario> lista = new ArrayList<>();
        //repositorio.adiciona(new ArrayList<>());
        //repositorio.adiciona(Collections.emptyList());

        //#Situações de ambiguidade
        //Pode ser utilizada em uma sobrecarga de metodos, 
        //utiliando a interface PrivilegedAction
        Supplier<String> supplier = () -> "retorna uma String";
        PrivilegedAction<String> p = () -> "retorna uma String";

        //#Conversões entre interfaces funcionais

        Supplier<String> supplier2 = () -> "executando um supplier";
        execute(supplier2);

        PrivilegedAction<String> action = () -> "executando uma ação";

        //ainda que estas interfaces funcionais sejam
        //equivalentes, não existirá uma conversão automática.
        //Para que essa conversão seja possível podemos utilizar method reference
        PrivilegedAction<String> action2 = () -> "executando uma ação";
        execute(action::run);
        //execute(action); Erro

        /* 12.2 Qual é o tipo de uma expressão Lambda? */
        //Toda expressão lambda tem e precisa ter um tipo.
        //O construtor da Thread espera um Runnable, o codigo compila, 
        //pois o compilador infere o tipo atraves do contrutor que vai recebe-lo.
        //Esse “tipo esperado” é conhecido como Target Type.
        new Thread(() -> {
            System.out.println("eu sou um runnable?");
        }).start();

        //Equivalentes, mas com tipos diferentes, gracças ao Target Type.
        Callable<String> c = () -> "retorna uma String";
        PrivilegedAction<String> p2 = () -> "retorna uma String";

        /* 12.3 Limitações da inferência no lambda */
        //Veja que com method references já vimos que isso compila:
        List<Usuario> usuarios = UsuarioDataFactory.listaDeUsuarios(5);
        
        usuarios.sort(Comparator.comparingInt(Usuario::getPontos)
            .thenComparing(Usuario::getNome));

        //Porém, usando os lambdas que parecem equivalentes, não compila:
        /*usuarios.sort(Comparator.comparingInt(u -> u.getPontos())
            .thenComparing(u -> u.getNome()));*/

        //Se avisarmos que o u é um Usuario, o retorno do comparingInt fica mais
        //óbvio para o compilador, e com o código a seguir conseguimos uma compilação sem
        //erros:
        usuarios.sort(Comparator.comparingInt((Usuario u) -> u.getPontos())
            .thenComparing(u -> u.getNome()));

        usuarios.sort(Comparator.comparingInt(Usuario::getPontos).reversed());    

        //Mas se usar o lambda em vez do method reference, não compila
        //usuarios.sort(Comparator.comparingInt(u -> u.getPontos()).reversed());

        //#Suporte a múltiplas anotações iguais
        //Essa possibilidade, conhecida como repeating annotations,
        //Para tornar isso possível agora podemos marcar a nossa anotação com
        //@Repeatable

        /*
            @Repeatable(Roles.class)
            @Documented
            @Retention(RetentionPolicy.RUNTIME)
            @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
            public @interface Role {
                String value();
            }

            Onde @Roles conterá um array da anotação que ela armazenará:

            @Documented
                @Retention(RetentionPolicy.RUNTIME)
                @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
                public @interface Roles {
                Role[] value();
            }

            @Role("presidente")
            @Role("diretor")
            public class RelatorioController {
            }


            Diversos métodos foram adicionados na API de reflection para recuperar essas
            anotações. Um exemplo é o método getAnnotationsByType.

            RelatorioController controller = new RelatorioController();
            Role[] annotationsByType = controller
                .getClass()
                .getAnnotationsByType(Role.class);

            Arrays.asList(annotationsByType)
                .forEach(a -> System.out.println(a.value()));
            
            Como saída, teremos os valores inseridos nas roles:
            presidente
            diretor
        */

        /* 12.4 Fim da Permgen */
        //Foi removido completamente. Isso significa que nunca
        //mais receberemos o famoso java.lang.OutOfMemoryError: PermGen!

        /* 12.5 Reflection: parameter names */
        //habilidade de recuperar o nome dos parâmetros dos métodos e construtores

        
    }


    private static void execute(Supplier<String> supplier) {
        System.out.println(supplier.get());
    } 
}
