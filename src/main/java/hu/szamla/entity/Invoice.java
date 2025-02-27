package hu.szamla.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate deadlineDate;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Double price;
}
