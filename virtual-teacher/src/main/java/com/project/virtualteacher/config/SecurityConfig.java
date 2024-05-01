package com.project.virtualteacher.config;

import com.project.virtualteacher.service.VirtualTeacherUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/api/v1/user/register","/api/v1/course/{courseId}/public/basic-details","/api/v1/role/{roleId}","/api/v1/course/title/basic-details","/api/v1/course/all-public/basic-details").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/user/{id}", "/api/v1/user/{id}/basic-details", "/api/v1/course/title","/api/v1/course/{courseId}/full-details","/api/v1/course/all/full-details","/api/v1/lecture/{lectureId}").authenticated()
                        .requestMatchers("/api/v1/user/{id}/block", "/api/v1/user/{id}/unblock", "/api/v1/user/{userId}/role/{roleId}","/api/v1/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/course","/api/v1/lecture").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/course/title/full-details").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/course/{courseId}").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/role/{roleId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/course/{courseId}","/api/v1/lecture/{lectureId}").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/role/{roleId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/course/{courseId}/enroll").hasRole("STUDENT"))
                        .httpBasic(Customizer.withDefaults())
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
