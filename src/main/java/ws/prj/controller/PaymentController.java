package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.PaymentRequest;
import ws.prj.dto.response.PaymentResponse;
import ws.prj.service.impl.PaymentServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@RequestMapping("/admin")
public class PaymentController {
    PaymentServiceImpl paymentService;

    @GetMapping("/find_payments")
    public ApiResponse<List<PaymentResponse>> findAll(){
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.findAll())
                .build();
    }

    @PutMapping("/payment")
    public ApiResponse<PaymentResponse> payment(@RequestBody PaymentRequest request){
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.create(request))
                .build();
    }
}
