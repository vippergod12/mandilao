package ws.prj.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ws.prj.contants.PredefineRole;
import ws.prj.dto.request.UserCreationRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.Role;
import ws.prj.entity.User;
import ws.prj.mapper.UserMapper;
import ws.prj.repository.RoleRepository;
import ws.prj.repository.UserResponseDAO;
import ws.prj.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> findAll() {
        log.info("Method findAll with role ADMIN");
        return userResponseDAO.findAll().stream().map(userMapper::toUserResponse).toList();
    }



    @Override
    public User findByUsername(String username) {
        return userResponseDAO.findByUsername(username);
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
}
