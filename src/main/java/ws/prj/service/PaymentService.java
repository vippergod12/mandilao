package ws.prj.service;

import org.springframework.stereotype.Service;
import ws.prj.dto.request.PaymentRequest;
import ws.prj.dto.response.PaymentResponse;

import java.util.List;

@Service
public interface PaymentService {
    List<PaymentResponse> findAll();
    PaymentResponse create (PaymentRequest request);
}
