package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ws.prj.entity.OrderDetail;
import ws.prj.entity.Orders;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findByOrders_Id(UUID orderId);

    List<OrderDetail> findByOrders(Orders orders);
}
