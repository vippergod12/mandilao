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
import ws.prj.repository.UserResponseDAO;
import ws.prj.service.UserService;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    UserResponseDAO userResponseDAO;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;


    @Override
    @PreAuthorize("hasRole('ADMIN')") //Spring sẽ tạo ra 1 proxy trước cái hàm. sẽ ktra role là admin thì mơi gọi đến method
    public List<UserResponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return userResponseDAO.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse create(UserCreationRequest request) {
        if(userResponseDAO.existsByUsername(request.getUsername())) throw new RuntimeException("User already exists");
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role>roles = new HashSet<>();
        roleRepository.findById(PredefineRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        return userMapper.toUserResponse(userResponseDAO.save(user));
    }

    @Override
    public void update(User user) {
        userResponseDAO.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        userResponseDAO.deleteByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userResponseDAO.existsByUsername(username);
    }

    @Override
    public User findById(String userId) {
        return userResponseDAO.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @PostAuthorize("returnObject.username == authentication.name")// check sau khi method thuc thi xong
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userResponseDAO.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userResponseDAO.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
}
