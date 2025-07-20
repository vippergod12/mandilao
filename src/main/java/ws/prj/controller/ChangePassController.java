package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.ChangePassRequest;
import ws.prj.service.UserService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChangePassController {

    @Autowired
    UserService userService;

    @PostMapping("/change-password/{userId}")
    public ApiResponse<String> changepassword(@RequestBody ChangePassRequest body, @PathVariable("userId") String userId){
        System.out.println(userId);
        String result = userService.changePass(body,userId);
        return ApiResponse.<String>builder()
                .result(result)
                .build();
    }
}
