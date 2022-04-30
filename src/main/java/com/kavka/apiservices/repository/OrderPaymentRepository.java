package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Integer> {

    @Query("select p from OrderPayment  p " +
            "inner join p.order o " +
            "where o.user = ?1 and p.creditPaidDate is null and p.creditDueDate < current_date")
    List<OrderPayment> findNotPaidCreditForUser(User user);

    Optional<OrderPayment> findByOrder(Order order);
}
