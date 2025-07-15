package ws.prj.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ws.prj.contants.PredefineRole;
import ws.prj.entity.Role;
import ws.prj.entity.User;
import ws.prj.mapper.UserMapperImpl;
import ws.prj.repository.RoleRepository;
import ws.prj.repository.UserResponseDAO;
import ws.prj.service.impl.UserServiceImpl;

import java.util.HashSet;

// tu dong tao 1 user admin khi start
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
     final PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";
    @Bean
    ApplicationRunner applicationRunner(UserResponseDAO userResponseDAO, RoleRepository roleRepository, UserMapperImpl userMapperImpl){
        return args -> {
            if(userResponseDAO.findByUsername(ADMIN_USER_NAME) == null){
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefineRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());
                 roleRepository.save(Role.builder()
                        .name(PredefineRole.USER_ROLE)
                        .description("User role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);
                User u = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();
                userResponseDAO.save(u);
            }
        };
    }
}
