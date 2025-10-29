package ru.example.taskservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

          @Bean
          public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                              .csrf(csrf -> csrf.disable())
                              .authorizeHttpRequests(authz -> authz
                                        .anyRequest().authenticated()
                              )
                              .oauth2ResourceServer(oauth2 -> oauth2
                                        .jwt(Customizer.withDefaults()) // ✅ Стандартная проверка из заголовка Authorization
                              );

                    return http.build();
          }

          @Bean
          public JwtDecoder jwtDecoder() {
                    return NimbusJwtDecoder.withJwkSetUri("http://localhost:9000/oauth2/jwks").build();
          }
}