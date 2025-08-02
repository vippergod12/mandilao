package ws.prj.service.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.prj.dto.request.OrderDetailRequest;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.entity.OrderDetail;
import ws.prj.entity.Orders;
import ws.prj.entity.Product;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.OrderDetailMapper;
import ws.prj.repository.ProductRepositoryDAO;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDetailManager {

    private final ProductRepositoryDAO productRepositoryDAO;
    private final OrderDetailMapper mapper;

    public List<OrderDetailResponse> processOrderDetails(Orders order, List<OrderDetailRequest> requestList) {
        Map<UUID, OrderDetail> existingMap = order.getOrderDetails().stream()
                .collect(Collectors.toMap(d -> d.getProduct().getId(), d -> d));

        for (OrderDetailRequest request : requestList) {
            Product product = getProductOrThrow(request.getId_product());
//            updateInventory(product, request.getQuantity());

            OrderDetail detail = existingMap.get(product.getId());
            if (detail != null) {
                updateExistingDetail(detail, request.getQuantity());
            } else {
                OrderDetail newDetail = createNewDetail(order, product, request.getQuantity());
                order.getOrderDetails().add(newDetail);
            }
        }
        return mapper.toOrderDetailResponseList(order.getOrderDetails());
    }

    public Product getProductOrThrow(UUID productId) {
        Product product = productRepositoryDAO.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        return product;
    }

    private void updateInventory(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepositoryDAO.save(product);
    }

    private void updateExistingDetail(OrderDetail detail, int quantity) {
        int newQty = detail.getQuantity() + quantity;
        detail.setQuantity(newQty);
        detail.setPrice(newQty * detail.getProduct().getPrice());
    }

    public OrderDetail createNewDetail(Orders order, Product product, int quantity) {
        return OrderDetail.builder()
                .orders(order)
                .product(product)
                .quantity(quantity)
                .price(quantity * product.getPrice())
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
    }

}
