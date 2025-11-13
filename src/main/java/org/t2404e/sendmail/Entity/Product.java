package org.t2404e.sendmail.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private String original_price;
    private String discount_price;

    @ManyToMany(mappedBy = "products")
    private Set<Mail> mails;
}
