package com.dias.exercise.firstlist.model;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private Customer customer;

    private LocalDate orderDate;

    private List<Product> products;

    public Order(Customer customer, LocalDate orderDate, List<Product> products) {
        super();
        this.customer = customer;
        this.orderDate = orderDate;
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
            "customer=" + customer +
            ", orderDate=" + orderDate +
            ", products=" + products +
            '}';
    }
}
