package com.project.virtualteacher.config;

import com.project.virtualteacher.service.UserSecurityDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserSecurityDetails userDetails;

    public SecurityConfig(UserSecurityDetails userDetails) {
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
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,
                                "/api/v1/course/{id}/basic",
                                "/api/v1/role/{id}",
                                "/api/v1/course/basic",
                                "/api/v1/course/all-public/basic",
                                "/api/v1/lecture/{id}/public/basic",
                                "/api/v1/user/verification/{code}",
                                "/api/v1/user/student/{studentId}").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/topic").authenticated()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/user/{id}",
                                "/api/v1/user/{id}/basic",
                                "/api/v1/course/title",
                                "/api/v1/course/{courseId}/full",
                                "/api/v1/course/all/full",
                                "/api/v1/lecture/{lectureId}",
                                "/api/v1/lecture/{lectureId}/assignment",
                                "/api/v1/user/student/{studentId}",
                                "/api/v1/user/teacher/{teacherId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/topic/{topicId}").authenticated()
                        .requestMatchers(
                                "/api/v1/user/{id}/block",
                                "/api/v1/user/{id}/unblock",
                                "/api/v1/user/{id}/role/{roleId}",
                                "/api/v1/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/course",
                                "/api/v1/lecture").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/course/title/full").authenticated()
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/user/{id}",
                                "/api/v1/topic/{topicId}").authenticated()
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/course/{id}",
                                "/api/v1/lecture/{id}").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/role/{roleId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/course/{id}",
                                "/api/v1/lecture/{id}").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/role/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/course/{id}/enroll").hasRole("STUDENT"))
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
