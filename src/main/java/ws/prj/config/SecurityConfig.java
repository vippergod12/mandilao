package ws.prj.config;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @NonFinal
    @Value("${jwt.signerKey}")
    private String signerKey;


    private final String[] PUBLIC_ENPOINTS = { "/users","/auth/login","/auth/introspect","/auth/logout"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(config -> config.disable());
        http.cors(config -> config.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(config -> {
            config.requestMatchers(HttpMethod.POST,PUBLIC_ENPOINTS).permitAll()
                    .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/product").permitAll()
                    .requestMatchers(HttpMethod.GET,"/category").permitAll()
                    .requestMatchers(HttpMethod.GET,"/images").permitAll()
                    .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                    .anyRequest().authenticated();
        });
//        http.oauth2Login(login -> {
//            login.permitAll();
//            login.successHandler(((request, response, authentication) -> {
//                DefaultOidcUser OAuthUser = (DefaultOidcUser) authentication.getPrincipal();
//                String email = OAuthUser.getEmail();
//                String pass = passwordEncoder.encode("");
//                var user = User.withUsername(email).password(pass).roles("USER").build();
//                var newAuth = new UsernamePasswordAuthenticationToken(user,pass, user.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(newAuth);
//                response.sendRedirect("http://localhost:5173/");
////                String url = "/";
////                HttpSession session = request.getSession();
////                String attr = (String) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
////                DefaultSavedRequest req2 = (DefaultSavedRequest) session.getAttribute(attr);
////                if(req2 != null) {
////                    url=req2.getRequestURI();
////                }
////                response.sendRedirect(url);
//            }));
//        });

        http.oauth2ResourceServer(oauth2config ->
                oauth2config.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())));
//        http.formLogin(config -> {
//            config.loginPage("/login/form");
//            config.loginProcessingUrl("/login/check");
//            config.defaultSuccessUrl("/login/success");
//            config.failureUrl("/login/failure");
//            config.permitAll();
//            config.usernameParameter("username");
//            config.passwordParameter("password");
//        });
        http.formLogin(form -> {

        });

//        http.rememberMe(config -> {
//            config.tokenValiditySeconds(3*24*60*60);
//            config.rememberMeCookieName("remember");
//            config.rememberMeParameter("remember-me");
//        });
//        http.logout(Customizer.withDefaults());
//        http.logout(config -> {
//            config.logoutUrl("/logout");
//            config.logoutSuccessUrl("/logout/exit");
//            config.clearAuthentication(true);
//            config.invalidateHttpSession(true);
//            config.deleteCookies("JSESSIONID");
//            config.deleteCookies("remember-me");
//        });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String scope = jwt.getClaimAsString("scope");
            if (scope == null) return List.of();

            return Arrays.stream(scope.split(" "))
                    .map(SimpleGrantedAuthority::new) // scope đã chứa ROLE_
                    .collect(Collectors.toList());
        });
        return converter;
    }
}
