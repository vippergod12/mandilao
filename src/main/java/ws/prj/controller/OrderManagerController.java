package ws.prj.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.dto.response.OrderReponse;
import ws.prj.entity.OrderDetail;
import ws.prj.service.OrderDetailService;
import ws.prj.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class OrderManagerController {
OrderService orderService;
OrderDetailService orderDetailService;

@GetMapping("/orders")
    public ApiResponse<List<OrderReponse>> getAll(){
    return ApiResponse.<List<OrderReponse>>builder()
            .result(orderService.findAll())
            .build();
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<List<OrderDetailResponse>> findById(@PathVariable UUID id){
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailService.findByOrderId(id))
                .build();
    }

}
