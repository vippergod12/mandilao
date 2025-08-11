package ws.prj.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.PaymentRequest;
import ws.prj.dto.response.PaymentResponse;
import ws.prj.entity.Orders;
import ws.prj.entity.Payment;
import ws.prj.entity.Tables;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.PaymentMapper;
import ws.prj.repository.OrderRepositoryDAO;
import ws.prj.repository.PaymentRepository;
import ws.prj.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    PaymentRepository paymentrepository;
    OrderRepositoryDAO orderRepositoryDAO;
    PaymentMapper mapper;

    @Override
    public List<PaymentResponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return paymentrepository.findAll().stream().map(mapper :: toPaymentResponse).toList();
    }

    @Override
    public PaymentResponse create(PaymentRequest request) {
        System.out.println(request.getId_order());
        Orders orders = orderRepositoryDAO.findById(request.getId_order())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        orders.setStatus("PAID");
        orderRepositoryDAO.save(orders);

        Payment payment = new Payment();
        payment.setName_admin(request.getName_admin());
        payment.setName_user(request.getName_user());
        payment.setTables(orders.getTables());
        payment.setTotailPrice(orders.getTotailPrice());
        payment.setPayment_time(LocalDateTime.now());
        payment.setTables(orders.getTables());
        payment.setOrders(orders);
        payment.setUser(orders.getUser());

       Payment saved = paymentrepository.save(payment);
        return mapper.toPaymentResponse(saved);

    }
}
