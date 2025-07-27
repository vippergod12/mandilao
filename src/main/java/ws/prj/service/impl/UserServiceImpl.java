package ws.prj.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ws.prj.contants.PredefineRole;
import ws.prj.dto.request.ChangePassRequest;
import ws.prj.dto.request.ConfirmOtpRequest;
import ws.prj.dto.request.ForgotPassRequest;
import ws.prj.dto.request.UserCreationRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.Role;
import ws.prj.entity.User;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.UserMapper;
import ws.prj.repository.RoleRepository;
import ws.prj.repository.UserRepository;
import ws.prj.service.MailSerice;
import ws.prj.service.UserService;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    MailSerice mailSerice;


    @Override
    @PreAuthorize("hasRole('ADMIN')") //Spring sẽ tạo ra 1 proxy trước cái hàm. sẽ ktra role là admin thì mơi gọi đến method
    public List<UserResponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse create(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) throw new RuntimeException("User already exists");
        log.info("DOB: {}", request.getDob());
        log.info("Fullname: {}", request.getFullName());

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role>roles = new HashSet<>();
        roleRepository.findById(PredefineRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        User userSaved = userRepository.save(user);
        log.info(">>> User SAVED: {}", userSaved.toString());
        return userMapper.toUserResponse(userSaved);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    @PostAuthorize("returnObject.username == authentication.name")// check sau khi method thuc thi xong
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void forgotPass(ForgotPassRequest request, HttpSession session){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String otp = generateRandomPassword(6);
        session.setAttribute("otp", otp);
        session.setAttribute("otp_created_time", System.currentTimeMillis());
        session.setAttribute("forgot_email", request.getEmail());

        String content = "Đây là mã OTP của bạn: " + otp + "\nVui lòng OTP và đổi mật khẩu ngay!";
        mailSerice.sendMail(request.getEmail(), "Khôi phục mật khẩu", content);
    }

    @Override
    public String confirmOtp (ConfirmOtpRequest request, HttpSession session){
        Object otpObj = session.getAttribute("otp");
        if (otpObj == null) {
            return "OTP đã hết hạn hoặc không tồn tại !";
        }

        Long createdTime = (Long) session.getAttribute("otp_created_time");
        if (createdTime == null || System.currentTimeMillis() - createdTime > 60_000) {
            return "OTP đã hết hạn!";
        }
        String otpStored = otpObj.toString();

        if(!otpStored.equals(request.getOtp())) {
            return "Mã OTP không đúng!";
        }

        session.removeAttribute("otp");
        session.removeAttribute("otp_created_time");
        session.setAttribute("otp_verified", true);
        return "OTP hợp lệ! Vui lòng nhập mật khẩu mới.";
    }

    @Override
    public void changePass(ChangePassRequest request,String userId ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setPassword(passwordEncoder.encode(request.getNewPass()));
        userRepository.save(user);
    }

    private String generateRandomPassword(int length){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(chars.length());
            builder.append(chars.charAt(index));
        }
        return builder.toString();
    }

}
