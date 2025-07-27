package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.*;
import ws.prj.dto.response.OrderReponse;
import ws.prj.dto.response.UserResponse;
import ws.prj.service.impl.OrderServiceImpl;
import ws.prj.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserController {

    UserServiceImpl userServiceImpl;
    OrderServiceImpl orderServiceImpl;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userServiceImpl.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userServiceImpl.findAll())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userServiceImpl.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userServiceImpl.getMyInfo())
                .build();
    }

    @PostMapping("/change-password/{userId}")
    ApiResponse<Void> changepassword(@RequestBody ChangePassRequest body, @PathVariable("userId") String userId){
        userServiceImpl.changePass(body,userId);
        return ApiResponse.<Void>builder()
                .message("Password changed successfully")
                .build();
    }

    @PostMapping("/order/call")
    public ApiResponse<OrderReponse> callOrder(@RequestBody OrderRequest request) {
        return ApiResponse.<OrderReponse>builder()
                .result(orderServiceImpl.update(request))
                .build();
    }




}
