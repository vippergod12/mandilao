package ws.prj.controller;

import com.fasterxml.jackson.core.io.JsonEOFException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.AuthenticationRequest;
import ws.prj.dto.response.AuthenticationResponse;
import ws.prj.service.AuthenticationService;
import ws.prj.service.impl.UserServiceImpl;

import java.text.ParseException;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AdminController {
    private final AuthenticationService authService;



    public AdminController(AuthenticationService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) throws JsonEOFException, ParseException {
        var result = authService.authenticateAdmin(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}
