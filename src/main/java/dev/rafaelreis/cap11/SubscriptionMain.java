package dev.rafaelreis.cap11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class SubscriptionMain {
    public static void main(String[] args) {

        //Teremos três usuários com assinaturas de 99.90. Dois deles encerraram suas
        //assinaturas:
        BigDecimal monthlyFee = new BigDecimal("99.99");

        LocalDateTime yesterday = LocalDateTime.now();

        Customer paulo = new Customer("Paulo");
        Customer rodrigo = new Customer("Rodrigo");
        Customer adriano = new Customer("Adriano");

        Subscription s1 = new Subscription(monthlyFee, yesterday.minusMonths(5),  paulo);

        Subscription s2 = new Subscription(monthlyFee, yesterday.minusMonths(8), rodrigo);
       
        Subscription s3 = new Subscription(monthlyFee, yesterday.minusMonths(5), adriano);
        
        List<Subscription> subscriptions = Arrays.asList(s1, s2, s3);

        //meses pagos
        long mesesAtivas = ChronoUnit.MONTHS.between(s1.getBegin(), LocalDateTime.now());

        long mesesInativas = ChronoUnit.MONTHS.between(s1.getBegin(), s1.getEnd().orElse(LocalDateTime.now()));
    
        //valor gerado da assinatura
        BigDecimal total = s1.getMenthlyFee().multiply(
            new BigDecimal(ChronoUnit.MONTHS.between(
                s1.getBegin(), s1.getEnd().orElse(LocalDateTime.now()))));

        System.out.printf("Total da Assinatura de %s: R$%.2f\n", 
            s1.getCustomer().getName(), total);
    
        BigDecimal totalPaid = subscriptions.stream()
            .map(Subscription::getTotalPaid)
            .reduce(BigDecimal.ZERO, BigDecimal::add);      
    
        System.out.println("Total da Assinaturas: R$" + totalPaid);
    }
}
