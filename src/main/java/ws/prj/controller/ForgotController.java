package ws.prj.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.ConfirmOtpRequest;
import ws.prj.dto.request.ForgotPassRequest;
import ws.prj.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ForgotController {

    @Autowired
    UserService userService;

    @PostMapping("/forgot-password")
    @ResponseBody
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPassRequest request, HttpSession session) {
        userService.forgotPass(request,session);
        return ApiResponse.<Void>builder()
                .message("Send OTP to mail")
                .build();
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public ApiResponse<String> verifyOtp(@RequestBody @Validated ConfirmOtpRequest request, HttpSession session) {
        String result = userService.confirmOtp(request, session);
        return ApiResponse.<String>builder()
                .result(result)
                .build();
    }


}
