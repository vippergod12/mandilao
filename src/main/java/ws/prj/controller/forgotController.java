package Web.shop.controller;

import Web.shop.DAO.UserDao;
import Web.shop.entity.User;
import Web.shop.service.MailSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class api {

    @Autowired
    UserDao userDao;

    @Autowired
    MailSerice mailSerice;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/forgotPass")
    public  ResponseEntity<String> fotgotPass(@RequestParam("email") String email){
        Optional<User> optionalUser = userDao.findByEmail(email);

        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body("Email không tồn tại!");
        }

        User user = optionalUser.get();
        String newPass = generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(newPass));
        userDao.save(user);

        String content = "Mật khẩu mới của bạn là: " + newPass + "\nVui lòng đăng nhập và đổi mật khẩu ngay!";
        mailSerice.sendMail(email, "Khôi phục mật khẩu", content);

        return ResponseEntity.ok("Đã gửi mật khẩu mới về email: " + email);
    }

    private String generateRandomPassword(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(chars.length());
            builder.append(chars.charAt(index));
        }
        return builder.toString();
    }
}
