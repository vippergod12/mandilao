package ws.prj.service;

import org.springframework.stereotype.Service;
import ws.prj.dto.request.OrderRequest;
import ws.prj.dto.response.OrderReponse;

import java.util.List;

@Service
public interface OrderService {
    OrderReponse create(OrderRequest request);
    OrderReponse update(OrderRequest request);
//    void delete(OrderRequest request);
    List<OrderReponse> findAll();
    List<OrderReponse> findOrderByUserId(OrderRequest request);
    OrderReponse findOrderByUserIdOrTableIdAndStatus(OrderRequest request);


}
