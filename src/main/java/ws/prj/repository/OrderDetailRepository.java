package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ws.prj.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

}
