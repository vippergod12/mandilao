package ws.prj.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.OrderDetailRequest;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.entity.OrderDetail;
import ws.prj.entity.Orders;
import ws.prj.entity.Product;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.OrderDetailMapper;
import ws.prj.repository.OrderDetailRepository;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.OrderDetailService;
import ws.prj.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {
    private static final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);
    OrderDetailRepository orderDetailRepository;
    ProductRepositoryDAO productRepositoryDAO;
    ProductService productService;
    OrderDetailMapper mapper;

    @Override
    @PreAuthorize("hasRole(ADMIN)")
    public List<OrderDetailResponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return orderDetailRepository.findAll().stream().map(mapper::toOrderDetailResponse).toList();
    }


    @Override
    public List<OrderDetailResponse> create(List<OrderDetailRequest> requestList, Orders orders) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailRequest req : requestList) {
            Product product = productRepositoryDAO.findById(req.getId_product())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

            product.setQuantity(product.getQuantity() - req.getQuantity());
            productRepositoryDAO.save(product);

            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID());
            detail.setOrders(orders);
            detail.setProduct(product);
            detail.setQuantity(req.getQuantity());
            detail.setPrice(product.getPrice());
            orderDetails.add(detail);
        }

        List<OrderDetail> savedDetails = orderDetailRepository.saveAll(orderDetails);
        return savedDetails.stream()
                .map(mapper::toOrderDetailResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<OrderDetailResponse> addOrUpdateOrderDetails(Orders orders, List<OrderDetailRequest> requestList) {
        Map<UUID, OrderDetail> existingDetailsMap = orders.getOrderDetails().stream()
                .collect(Collectors.toMap(d -> d.getProduct().getId(), d -> d));

        for (OrderDetailRequest detailRequest : requestList) {
            UUID productId = detailRequest.getId_product();
            int quantity = detailRequest.getQuantity();
            Product product = productRepositoryDAO.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

            if (product.getQuantity() < quantity) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }

            product.setQuantity(product.getQuantity() - detailRequest.getQuantity());
            productRepositoryDAO.save(product);

            OrderDetail detail = existingDetailsMap.get(productId);
            if (detail != null) {
                detail.setQuantity(detail.getQuantity() + quantity);
                detail.setPrice(detail.getQuantity() * detail.getProduct().getPrice());
            } else {
                OrderDetail newDetail = new OrderDetail();
                newDetail.setId(UUID.randomUUID());
                newDetail.setOrders(orders);
                newDetail.setProduct(productRepositoryDAO.findById(productId)
                        .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)));
                newDetail.setQuantity(quantity);
                newDetail.setPrice(quantity * newDetail.getProduct().getPrice());

                orders.getOrderDetails().add(newDetail);
            }
        }
        return orders.getOrderDetails().stream()
                .map(mapper::toOrderDetailResponse)
                .collect(Collectors.toList());
    }


}
