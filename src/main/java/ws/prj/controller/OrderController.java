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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class OrderController {

    OrderServiceImpl orderServiceImpl;
    OrderDetailServiceImpl orderDetailServiceImpl;

    @GetMapping("/admin/orders")
    public ApiResponse<List<OrderReponse>> getListOrder(){
        return ApiResponse.<List<OrderReponse>>builder()
                .result(orderServiceImpl.findAll())
                .build();
    }

    @PostMapping("/order/call")
    public ApiResponse<OrderReponse> callOrder(@RequestBody OrderRequest request) {
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.update(request))
                .build();
    }

    @GetMapping("/users/orders/{userId}")
    public ApiResponse<List<OrderReponse>> getOrdersByUser(@PathVariable("userId") String userId){
        return ApiResponse.<List<OrderReponse>>builder()
                .result(orderServiceImpl.findOrderByUserId(userId))
                .build();
    }

    @GetMapping("/order/find")
    public ApiResponse<OrderReponse> findOrder( @RequestParam(required = false) String userId,
            @RequestParam(required = false) Long tableId,@RequestParam String status) {
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.findOrderByUserIdOrTableIdAndStatus(userId, tableId,status))
                .build();
    }


    @PostMapping("/order/repeat")
    public ApiResponse<OrderReponse> orderAgain(@RequestBody OrderRequest request){
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.orderAgain(request))
                .build();
    }

    @GetMapping("/order/{orderId}/detail")
    public ApiResponse<List<OrderDetailResponse>> getlistOrderDetail(@PathVariable("orderId") UUID orderId){
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailServiceImpl.finAllByOrderId(orderId))
                .build();
    }
}
