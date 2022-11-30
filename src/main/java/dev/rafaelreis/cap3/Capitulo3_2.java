package dev.rafaelreis.cap3;

public class Capitulo3_2 {
    public static void main(String[] args) {
        
        /* 3.2 Sua própria interface funcional */

        Validador<String> validadorCEP = new Validador<String>() {
            public boolean valida(String valor) {
                return valor.matches("[0-9]{5}-[0-9]{3}");
            }
        };

        Validador<String> validarCEP2 = valor -> {
            return valor.matches("[0-9]{5}-[0-9]{3}");
        };

        Validador<String> validarCEP3 = valor -> valor.matches("[0-9]{5}-[0-9]{3}");

        System.out.println(validadorCEP.valida("70000-000"));
        System.out.println(validarCEP2.valida("70000-00"));
        System.out.println(validarCEP3.valida("70000-000"));

        /* 3.4 Indo mais a fundo: primeiros detalhes */

        /* error: incompatible types: Object is not a functional interface.
        Object o = () -> {
            System.out.println("O que sou eu? Que lambda?");
        }; */

        Runnable o = () -> System.out.println("O que sou eu? Que lambda?");
        System.out.println(o);
        System.out.println(o.getClass());

        //Captura variaveis locais
        final int numero = 5;
        new Thread(() -> System.out.println(numero)).run();

        int numero2 = 10;
        new Thread(() -> System.out.println(numero2)).run();

        /* Não compila, não pode alterar variaveis se estiver utilizando-as dentro
        do lambda. 
        Não precisa declarar as variáveis locais como final, basta não alterá-las que o Java vai
        permitir acessá-las

        int numero3 = 15;
        new Thread(() -> System.out.println(numero3)).run();
        numero3 = 16;*/
    }
}

/* 3.3 A anotação @FunctionalInterface 
Anotação não é obrigatório, é mais utilizada para que não seja adicionados novos metodos 
e a interface deixe de ser funcional. Caso unma novo metodo for incluido, uma exption é lançada na compilação.*/
@FunctionalInterface
interface Validador<T> {
    boolean valida(T t);
}
 