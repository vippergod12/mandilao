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
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.dto.response.OrderReponse;
import ws.prj.entity.*;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.OrderMapper;
import ws.prj.repository.OrderRepositoryDAO;
import ws.prj.repository.TableRespositoryDAO;
import ws.prj.repository.UserRepository;
import ws.prj.service.OrderDetailService;
import ws.prj.service.OrderService;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    OrderRepositoryDAO orderRepositoryDAO;
    TableRespositoryDAO tableRepositoryDAO;
    UserRepository userRepository;
    OrderDetailService orderDetailService;
    OrderMapper orderMapper;



    @Override
    public OrderReponse create(OrderRequest request) {
        User user = null;
        Tables table;

        if (request.getId_user() != null) {
            user = userRepository.findById(request.getId_user())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        }

        if (request.getId_table() != null) {
            table = tableRepositoryDAO.findById(request.getId_table())
                    .orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_EXISTED));
        } else {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        Orders orders = orderMapper.toEntity(request);
        orders.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
        orders.setStatus("PENDING");
        if (user != null) {orders.setUser(user);}
        orders.setTables(table);

        Orders saved = orderRepositoryDAO.save(orders);

        List<OrderDetailResponse> detailResponses =
                orderDetailService.create(request.getOrderDetaiList(),orders);

        return OrderReponse.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .orderDetails(detailResponses)
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public OrderReponse update(OrderRequest request) {
        Orders orders = orderRepositoryDAO.findByUserIdAndStatus(request.getId_user(), "PENDING")
                .or(() -> orderRepositoryDAO.findByTablesIdAndStatus(request.getId_table(), "PENDING"))
                .orElse(null);
        if (orders == null) {return create(request);}

        orders.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));
        List<OrderDetailResponse> detailResponses =
                orderDetailService.addOrUpdateOrderDetails(orders, request.getOrderDetaiList());

        Orders saved = orderRepositoryDAO.save(orders);
        return orderMapper.toOrderResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderReponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return orderRepositoryDAO.findAll().stream().map(orderMapper::toOrderResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<OrderReponse> findOrderByUserId(OrderRequest request) {
        User user = userRepository.findById(request.getId_user())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String idUser = user.getId();
        List<Orders> ordersList = orderRepositoryDAO.findAllByUserId(idUser);
        return ordersList.stream().map(orderMapper :: toOrderResponse).toList();
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderReponse findOrderByUserIdOrTableIdAndStatus(OrderRequest request) {
        Orders orders;
        if (request.getId_user() != null) {
            orders = orderRepositoryDAO.findByUserIdAndStatus(request.getId_user(),"PENDING")
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        } else if (request.getId_table() != null) {
            orders = orderRepositoryDAO.findByTablesIdAndStatus(request.getId_table(),"PENDING")
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        }else {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        return orderMapper.toOrderResponse(orders);
    }

    @PreAuthorize("hasRole('USER')")
    public OrderReponse orderAgain(OrderRequest request){
        if (request.getOrderDetaiList() == null || request.getOrderDetaiList().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        List<OrderDetailRequest> orderAgain = request.getOrderDetaiList().stream()
                .map(req -> {
                    req.setQuantity(1);
                    return req;
                })
                .collect(Collectors.toList());
        request.setOrderDetaiList(orderAgain);
        return create(request);
    }


}
