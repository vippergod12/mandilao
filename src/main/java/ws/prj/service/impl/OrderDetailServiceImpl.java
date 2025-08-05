package ws.prj.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.OrderDetailRequest;
import ws.prj.dto.request.OrderRequest;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.entity.OrderDetail;
import ws.prj.entity.Orders;
import ws.prj.entity.Product;
import ws.prj.entity.User;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.OrderDetailMapper;
import ws.prj.repository.OrderDetailRepository;
import ws.prj.repository.OrderRepositoryDAO;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.OrderDetailService;
import ws.prj.service.ProductService;
import ws.prj.service.manager.OrderDetailManager;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {
    private static final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);
    OrderDetailRepository orderDetailRepository;
    OrderRepositoryDAO orderRepositoryDAO;
    OrderDetailManager orderDetailManager;
    OrderDetailMapper mapper;

    @Override
    @PreAuthorize("hasRole(ADMIN)")
    public List<OrderDetailResponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return orderDetailRepository.findAll().stream().map(mapper::toOrderDetailResponse).toList();
    }

    @Override
    public List<OrderDetailResponse> finAllByOrderId(UUID orderId) {
        Orders orders = orderRepositoryDAO.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrders(orders);

        return orderDetails.stream().map(od -> OrderDetailResponse.builder()
                        .id(od.getId())
                        .quantity(od.getQuantity())
                        .price(od.getPrice())
                        .build()
                ).collect(Collectors.toList());

    }

    @Override
    public List<OrderDetailResponse> create(List<OrderDetailRequest> requestList, Orders orders) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailRequest req : requestList) {
            Product product = orderDetailManager.getProductOrThrow(req.getId_product());
            orderDetails.add(orderDetailManager.createNewDetail(orders,product,req.getQuantity()));
        }

        List<OrderDetail> savedDetails = orderDetailRepository.saveAll(orderDetails);
        List<OrderDetailResponse> responses = mapper.toOrderDetailResponseList(savedDetails);
        return responses;

    }

    @Override
    public List<OrderDetailResponse> addOrUpdateOrderDetails(Orders orders, List<OrderDetailRequest> requestList) {
        return orderDetailManager.processOrderDetails(orders,requestList);
    }

}
