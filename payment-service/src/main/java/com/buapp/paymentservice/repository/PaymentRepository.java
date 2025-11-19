package com.buapp.paymentservice.repository;

import com.buapp.paymentservice.model.Payment;
import com.buapp.paymentservice.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
    List<Payment> findByUserId(Long userId);
    List<Payment> findByTypeAndReferenceId(PaymentType type, Long referenceId);
}
