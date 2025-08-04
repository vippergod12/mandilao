package ws.prj.service;

import org.springframework.stereotype.Service;
import ws.prj.dto.request.OrderDetailRequest;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.entity.Orders;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderDetailService {
    List<OrderDetailResponse> findAll();
    List<OrderDetailResponse> create(List<OrderDetailRequest> requestList, Orders orders);
    List<OrderDetailResponse> addOrUpdateOrderDetails(Orders orders, List<OrderDetailRequest> requestList);
}
