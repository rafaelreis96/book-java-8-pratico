package dev.rafaelreis.cap11;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Capitulo11 {
    public static void main(String[] args) {
        /* Um modelo de pagamentos com Java 8 */

        /* 11.1 Uma loja de digital goodies */

        Customer paulo = new Customer("Paulo Silveira");
        Customer rodrigo = new Customer("Rodrigo Turini");
        Customer guilherme = new Customer("PGuilherme Silveira");
        Customer adriano = new Customer("Adriano Almeida");

        Product bach = new Product("Bach Completo", Paths.get("/music/bach.mp3"), new BigDecimal(100));
        Product poderosas = new Product("Poderosas Anita", Paths.get("/music/poderosas.mp3"), new BigDecimal(90));
        Product bandeira = new Product("Bandeira Brasil", Paths.get("/music/brasil.jpg"), new BigDecimal(50));
        Product beauty = new Product("Beleza Americana", Paths.get("beauty.mov"), new BigDecimal(150));
        Product vingadores = new Product("Os Vingadores", Paths.get("/movies/vingadores.mov"), new BigDecimal(200));
        Product amelie = new Product("Amelie Poulain", Paths.get("/movies/amelie.mov"), new BigDecimal(100));

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime lastMonth = today.minusMonths(1);

        Payment payment1 = new Payment(Arrays.asList(bach, poderosas), today, paulo);
        Payment payment2 = new Payment(Arrays.asList(bach, bandeira, amelie), yesterday, rodrigo);
        Payment payment3 = new Payment(Arrays.asList(beauty, vingadores, bach), today, adriano);
        Payment payment4 = new Payment(Arrays.asList(bach, poderosas, amelie), lastMonth, guilherme);
        Payment payment5 = new Payment(Arrays.asList(beauty, amelie), yesterday, paulo);

        List<Payment> payments = Arrays.asList(payment1, payment2, payment3, payment4, payment5);
   
        /* 11.2 Ordenando nossos pagamentos */

        payments.stream()
            .sorted(Comparator.comparing(Payment::getDate))
            .forEach(System.out::println);

        /* 11.3 Reduzindo BigDecimal em somas */

        payment1.getProducts().stream()
            .map(Product::getPrice)
            .reduce(BigDecimal::add)
            .ifPresent(System.out::println);

        BigDecimal total = payment1.getProducts().stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Stream<BigDecimal> priceStream = payments.stream()
            .map(p -> p.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));


        BigDecimal total2 = payments.stream()
            .map(p -> p.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Stream<BigDecimal> priceOfEachProduct = payments.stream()
            .flatMap(p -> p.getProducts().stream().map(Product::getPrice));
        
        Function<Payment, Stream<BigDecimal>> mapper = p -> p.getProducts().stream().map(Product::getPrice);
        
        BigDecimal totalFlat = payments.stream()
            .flatMap(p -> p.getProducts().stream().map(Product::getPrice))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        /* 11.4 Produtos mais vendidos */

        Stream<Product> products = payments.stream()
            .map(Payment::getProducts)
            .flatMap(p -> p.stream());

        Stream<Product> products2 = payments.stream()
            .map(Payment::getProducts)
            .flatMap(List::stream);

        Stream<Product> products3 = payments.stream()
            .flatMap(p -> p.getProducts().stream());

        //indica quantas vezes o produto foi vendido
        Map<Product, Long> topProducts = payments.stream()
            .flatMap(p -> p.getProducts().stream())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
      
        topProducts.entrySet().stream().forEach(System.out::println);

        topProducts.entrySet().stream()
            .max(Comparator.comparing(Map.Entry::getValue))
            .ifPresent(System.out::println);


        /* 11.5 Valores gerados por produto */

        Map<Product, BigDecimal> totalValuePerProduct = payments.stream()
        .flatMap(p -> p.getProducts().stream())
        .collect(
            Collectors.groupingBy(Function.identity(), 
            Collectors.reducing(BigDecimal.ZERO, Product::getPrice, BigDecimal::add)));
        
        /* 11.6 Quais são os produtos de cada cliente? */

        //Customer com sua lista de payments
        Map<Customer, List<Payment>> custumerToPayments = payments.stream()
            .collect(Collectors.groupingBy(Payment::getCustomer));

        //Lista de Produtos de cada payments
        Map<Customer, List<List<Product>>> customerToProductsList = payments.stream()
            .collect(Collectors
            .groupingBy(
                Payment::getCustomer, Collectors.mapping(Payment::getProducts, Collectors.toList())));
        
        //Achatar lista de produtos 
        Map<Customer, List<Product>> customerToProducts2steps = customerToProductsList.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, 
            e -> e.getValue().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList())));

        customerToProducts2steps.entrySet().stream()
            .sorted(Comparator.comparing(e -> e.getKey().getName()))
            .forEach(System.out::println);
        
        //Usando o reducing
        Map<Customer, List<Product>> customerToProducts = payments.stream()
            .collect(Collectors.groupingBy(Payment::getCustomer,
                Collectors.reducing(Collections.emptyList(),
                Payment::getProducts, (l1, l2) -> {
                    List<Product> l = new ArrayList<>();
                    l.addAll(l1);
                    l.addAll(l2);
                    return l;
                })));


        /* 11.7 Qual é nosso cliente mais especial? */

        Map<Customer, BigDecimal> totalValuePerCustomer = payments.stream()
            .collect(Collectors.groupingBy(Payment::getCustomer, 
                    Collectors.reducing(BigDecimal.ZERO, 
                    p -> p.getProducts().stream()
                        .map(Product::getPrice)
                        .reduce(
                            BigDecimal.ZERO, BigDecimal::add), 
                            BigDecimal::add)));

        //quebrando o codigo
        Function<Payment, BigDecimal> paymentToTotal = p -> p.getProducts()
            .stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        //Usuando a function no reducing       
        Map<Customer, BigDecimal> totalValuePerCustomer2 = payments.stream()
                .collect(Collectors.groupingBy(Payment::getCustomer, 
                    Collectors.reducing(BigDecimal.ZERO, paymentToTotal, BigDecimal::add)));

        totalValuePerCustomer.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue))
            .forEach(System.out::println);


        /* 11.8 Relatórios com datas */

        Map<YearMonth, List<Payment>> paymentsPerMonth = payments.stream()
            .collect(Collectors.groupingBy(p -> YearMonth.from(p.getDate())));

        paymentsPerMonth.entrySet().stream().forEach(System.out::println);

        //faturado no mes
        Map<YearMonth, BigDecimal> paymentsValuePerMonth = payments.stream()
            .collect(Collectors.groupingBy(p -> YearMonth.from(p.getDate()),
                Collectors.reducing(BigDecimal.ZERO,
                    p -> p.getProducts().stream()
                        .map(Product::getPrice)
                        .reduce(BigDecimal.ZERO,
                            BigDecimal::add),
                            BigDecimal::add)));

        paymentsValuePerMonth.entrySet().stream().forEach(System.out::println);

        /* 11.9 Sistema de assinaturas */

        
    }
}
