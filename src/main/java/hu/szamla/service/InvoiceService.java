package hu.szamla.service;

import hu.szamla.controller.dto.InvoiceDTO;
import hu.szamla.entity.Invoice;
import hu.szamla.repository.InvoiceRepository;
import hu.szamla.utils.MapperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(MapperUtils::convertToInvoiceDTO)
                .toList();
    }

    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        return MapperUtils.convertToInvoiceDTO(invoice);
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        if (invoiceDTO.getComment() == null || invoiceDTO.getComment().isBlank() ||
                invoiceDTO.getPrice() == null ||
                invoiceDTO.getCustomerName() == null || invoiceDTO.getCustomerName().isBlank() ||
                invoiceDTO.getItemName() == null || invoiceDTO.getItemName().isBlank() ||
                invoiceDTO.getIssueDate() == null ||
                invoiceDTO.getDeadlineDate() == null) {

            throw new IllegalArgumentException("All fields are required");
        }

        Invoice invoice = new Invoice();
        invoice.setCustomerName(invoiceDTO.getCustomerName());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setDeadlineDate(invoiceDTO.getDeadlineDate());
        invoice.setItemName(invoiceDTO.getItemName());
        invoice.setComment(invoiceDTO.getComment());
        invoice.setPrice(invoiceDTO.getPrice());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return MapperUtils.convertToInvoiceDTO(savedInvoice);
    }
}
