package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.OrderRequest;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.dto.response.OrderReponse;
import ws.prj.service.impl.OrderDetailServiceImpl;
import ws.prj.service.impl.OrderServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class OrderController {

    OrderServiceImpl orderServiceImpl;
    OrderDetailServiceImpl orderDetailServiceImpl;

    @PostMapping("/order/call")
    public ApiResponse<OrderReponse> callOrder(@RequestBody OrderRequest request) {
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.update(request))
                .build();
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderReponse>> getOrdersByUser(@RequestBody OrderRequest request){
        return ApiResponse.<List<OrderReponse>>builder()
                .result(orderServiceImpl.findOrderByUserId(request))
                .build();
    }

    @GetMapping("/order")
    public ApiResponse<OrderReponse> getOrderByUserIdOrTableIdAndStatus(@RequestBody OrderRequest request){
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.findOrderByUserIdOrTableIdAndStatus(request))
                .build();
    }

    @PostMapping("/order-again")
    public ApiResponse<OrderReponse> orderAgain(@RequestBody OrderRequest request){
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.orderAgain(request))
                .build();
    }

    @GetMapping("/orderDetail")
    public ApiResponse<List<OrderDetailResponse>> getlistOrderDetail(@RequestBody OrderRequest request){
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailServiceImpl.finAllByOrderId(request))
                .build();
    }
}
