package ws.prj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.UserUpdateRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.User;
import ws.prj.repository.UserResponseDAO;
import ws.prj.service.impl.UserServiceImpl;

import java.util.Map;

@Controller
@RequestMapping("/api")
public class changePassController {

    @Autowired
    UserResponseDAO userResponseDAO;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/changePass/{userId}")
    public ResponseEntity<String> changePass(@RequestBody Map<String, String> body
            , @PathVariable("userId") String userId){

        User user = userService.findById(userId);

        String pass = body.get("pass");
        String passUser = user.getPassword();

        if(!passwordEncoder.matches(pass,passUser)){
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không đúng !");
        }

        String newPass = body.get("newPass");
        String passConfim = body.get("passConfim");

        if(!newPass.equals(passConfim)){
            return ResponseEntity.badRequest().body("Mật khẩu mới và mật khẩu confim không khớp !");
        }

        user.setPassword(passwordEncoder.encode(newPass));
        userResponseDAO.save(user);

        return ResponseEntity.ok("Thay đổi mật khẩu thành công!");

    }
}
