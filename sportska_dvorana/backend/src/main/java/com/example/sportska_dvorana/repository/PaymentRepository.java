package com.example.sportska_dvorana.repository;

import com.example.sportska_dvorana.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>  {
}

