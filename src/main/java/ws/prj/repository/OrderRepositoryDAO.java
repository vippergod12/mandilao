package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.prj.entity.Orders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepositoryDAO extends JpaRepository<Orders, UUID> {
    Optional<Orders> findByUserIdAndStatus(String userId, String status);
    Optional<Orders> findByTablesIdAndStatus(Long tableId, String status);
    List<Orders> findByUser_Id(String userId);


}
