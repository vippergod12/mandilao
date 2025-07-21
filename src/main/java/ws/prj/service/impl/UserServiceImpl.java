package ws.prj.service.impl;

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
import ws.prj.dto.request.UserCreationRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.Role;
import ws.prj.entity.User;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.mapper.UserMapper;
import ws.prj.repository.RoleRepository;
import ws.prj.repository.UserRepository;
import ws.prj.service.UserService;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;


    @Override
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
}
