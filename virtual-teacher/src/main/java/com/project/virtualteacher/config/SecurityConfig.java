package com.project.virtualteacher.config;

import com.project.virtualteacher.service.VirtualTeacherUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final VirtualTeacherUserDetails userDetails;

    public SecurityConfig(VirtualTeacherUserDetails userDetails) {
        this.userDetails = userDetails;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/user/register").permitAll()
                        .requestMatchers("/api/v1/user/{id}"
                                , "/api/v1/user/{id}/basic-details"
                                , "/api/v1/course/title").authenticated()
                        .requestMatchers("/api/v1/user/{id}/block"
                                , "/api/v1/user/{id}/unblock"
                                , "/api/v1/user/{userId}/role/{roleId}").hasRole("ADMIN")
                        .requestMatchers("/api/v1/course").hasRole("TEACHER")
                        .requestMatchers("/api/v1/course/{courseId}").authenticated()).
                        httpBasic(Customizer.withDefaults())
                //.formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsManager userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetails);
        return provider;
    }

}
