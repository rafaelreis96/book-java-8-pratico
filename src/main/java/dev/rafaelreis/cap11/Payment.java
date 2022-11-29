package dev.rafaelreis.cap11;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Payment {
    private List<Product> products;
    private LocalDateTime date;
    private Customer customer;

    public Payment(List<Product> products, LocalDateTime date, Customer customer) {
        this.products = products;
        this.date = date;
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "{Payment: " + 
            date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
            + " " + customer + " " + products + "}";
    }

}