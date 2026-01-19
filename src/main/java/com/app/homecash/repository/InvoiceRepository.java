package com.app.homecash.repository;

import com.app.homecash.domain.Invoice;
import com.app.homecash.domain.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCreditCardId(Long creditCardId);

    List<Invoice> findByCreditCardIdAndStatus(Long creditCardId, InvoiceStatus status);

    Optional<Invoice> findByCreditCardIdAndMonthAndYear(Long creditCardId, Integer month, Integer year);
}

