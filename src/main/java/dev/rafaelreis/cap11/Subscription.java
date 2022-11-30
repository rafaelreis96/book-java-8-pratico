package dev.rafaelreis.cap11;

import java.lang.StackWalker.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Subscription {
    private BigDecimal menthlyFee;
    private LocalDateTime begin;
    private Optional<LocalDateTime> end;
    private Customer customer;


    public Subscription(BigDecimal menthlyFee, LocalDateTime begin, 
        Customer customer) {
        this.menthlyFee = menthlyFee;
        this.begin = begin;
        this.end = Optional.empty();
        this.customer = customer;
    }

    public Subscription(BigDecimal menthlyFee, LocalDateTime begin, 
        LocalDateTime end, Customer customer) {
        this.menthlyFee = menthlyFee;
        this.begin = begin;
        this.end = Optional.of(end);
        this.customer = customer;
    }

    public BigDecimal getTotalPaid() {
        return getMenthlyFee()
                .multiply(new BigDecimal(
                    ChronoUnit.MONTHS.between(
                        getBegin(), getEnd().orElse(LocalDateTime.now()))));
    }

    public BigDecimal getMenthlyFee() {
        return this.menthlyFee;
    }

    public void setMenthlyFee(BigDecimal menthlyFee) {
        this.menthlyFee = menthlyFee;
    }

    public LocalDateTime getBegin() {
        return this.begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public Optional<LocalDateTime> getEnd() {
        return this.end;
    }

    public void setEnd(Optional<LocalDateTime> end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    
}
