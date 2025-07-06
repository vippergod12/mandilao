package ws.prj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(config -> config.disable());
        http.cors(config -> config.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(config -> {
            config.requestMatchers("/admin/**").authenticated();
            config.anyRequest().permitAll();
        });
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

        http.rememberMe(config -> {
            config.tokenValiditySeconds(3*24*60*60);
            config.rememberMeCookieName("remmeber");
            config.rememberMeParameter("remember-me");
        });
        http.logout(Customizer.withDefaults());
        http.logout(config -> {
            config.logoutUrl("/logout");
            config.logoutSuccessUrl("/logout/exit");
            config.clearAuthentication(true);
            config.invalidateHttpSession(true);
            config.deleteCookies("JSESSIONID");
            config.deleteCookies("remember-me");
        });
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
}
