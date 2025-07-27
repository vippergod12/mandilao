package ws.prj.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.ChangePassRequest;
import ws.prj.dto.request.ConfirmOtpRequest;
import ws.prj.dto.request.ForgotPassRequest;
import ws.prj.dto.request.UserCreationRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.User;

import java.util.List;

@Service
public interface UserService {
    List<UserResponse> findAll();
    UserResponse create(UserCreationRequest userCreationRequest);
    void update(User user);
    void deleteByUsername(String username);
    boolean existsByUsername(String username);
    void forgotPass(ForgotPassRequest request, HttpSession session);
    String confirmOtp (ConfirmOtpRequest request, HttpSession session);
    void changePass(ChangePassRequest body,String userId);

}
