package hu.szamla.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceDTO {
    private Long id;
    private String customerName;
    private LocalDate issueDate;
    private LocalDate deadlineDate;
    private String itemName;
    private String comment;
    private Double price;
}
