package microservices.com.orderservie.repository;

import microservices.com.orderservie.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
