package com.kavka.apiservices.service;

import com.kavka.apiservices.model.Invoice;
import com.kavka.apiservices.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice save(Invoice invoice) {
        return this.invoiceRepository.save(invoice);
    }
}
