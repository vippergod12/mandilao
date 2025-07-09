package ws.prj.service;

import org.springframework.stereotype.Service;
import ws.prj.dto.request.UserCreationRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    List<UserResponse> findAll();
    UserResponse create(UserCreationRequest userCreationRequest);
    void update(User user);
    void deleteByUsername(String username);
    boolean existsByUsername(String username);
}
