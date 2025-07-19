package ws.prj.controller;


import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ws.prj.entity.User;
import ws.prj.repository.UserResponseDAO;
import ws.prj.service.MailSerice;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api")
public class forgotController {

    @Autowired
    UserResponseDAO userDao;

    @Autowired
    MailSerice mailSerice;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/forgotPass")
    @ResponseBody
    public ResponseEntity<String> fotgotPass(@RequestBody Map<String,String> body, HttpSession session){
        String email = body.get("email");

        if(email == null || email.trim().isEmpty()){
            return ResponseEntity.badRequest().body("Vui lòng nhập email hợp lệ!");
        }

        Optional<User> optionalUser =userDao.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body("Email không tồn tại!");
        }

        String otp = generateRandomPassword(6);

        session.setAttribute("otp", otp);
        session.setAttribute("otp_created_time", System.currentTimeMillis());


        String content = "Đây là mã OTP của bạn: " + otp + "\nVui lòng OTP và đổi mật khẩu ngay!";
        mailSerice.sendMail(email, "Khôi phục mật khẩu", content);

        return ResponseEntity.ok("Đã gửi OTP về email: " + email);
    }

    @PostMapping("/conformotp")
    @ResponseBody
    public ResponseEntity<String> conformotp(@RequestBody Map<String,String> body,HttpSession session){
        String otpInput = body.get("otp");

        Object otpObj = session.getAttribute("otp");
        if (otpObj == null) {
            return ResponseEntity.badRequest().body("OTP đã hết hạn hoặc không tồn tại !");
        }

        Long createdTime = (Long) session.getAttribute("otp_created_time");
        if (createdTime == null || System.currentTimeMillis() - createdTime > 60_000) {
            return ResponseEntity.badRequest().body("OTP đã hết hạn!");
        }
        String otpStored = otpObj.toString();


        if(!otpStored.equals(otpInput)) {
            return ResponseEntity.badRequest().body("Mã OTP không đúng!");
        }

        return ResponseEntity.ok("OTP hợp lệ! Vui lòng nhập mật khẩu mới.");
    }

    private String generateRandomPassword(int length){
        String chars = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(chars.length());
            builder.append(chars.charAt(index));
        }
        return builder.toString();
    }
}
