package org.t2404e.sendmail.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "mails")
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String recipient_ids;
    private String title;
    private String content;

    // Quan hệ Many-to-Many với Product
    @ManyToMany
    @JoinTable(
            name = "mail_products",
            joinColumns = @JoinColumn(name = "mail_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    // Quan hệ Many-to-Many với Users
    @ManyToMany
    @JoinTable(
            name = "mail_users",
            joinColumns = @JoinColumn(name = "mail_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> users;
}
