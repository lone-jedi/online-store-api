package com.yarkin.onlineshop.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class Product {
    private int id;
    private String name;
    private double price;
    private LocalDateTime creationDate;

    @Override
    public String toString() {
        return "\nProduct #" + id +
                "\n{\n\tname='" + name + '\'' +
                "\n\tprice=" + price +
                "\n\tcreationDate=" + creationDate +
                "\n}";
    }
}
