package com.dias.exercise.firstlist.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Subscription {
    private BigDecimal monthlyPrice;
    private LocalDate begin;
    private LocalDate end;
    private Customer customer;

    public Subscription(BigDecimal monthlyPrice, LocalDate begin, Customer customer) {
        super();
        this.monthlyPrice = monthlyPrice;
        this.begin = begin;
        this.customer = customer;
    }

    public Subscription(BigDecimal monthlyPrice, LocalDate begin, LocalDate end, Customer customer) {
        this(monthlyPrice, begin, customer);
        this.end = end;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
