package com.project.virtualteacher.service;

import com.project.virtualteacher.dao.contracts.UserDao;
import com.project.virtualteacher.exception_handling.exceptions.UnAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VirtualTeacherUserDetails implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName;
        String passWord;
        List<GrantedAuthority> authorities;
        com.project.virtualteacher.entity.User user = userDao.findByUsename(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for user:" + username));
        if (user.isBlocked()) {
            throw new UnAuthorizeException("User is blocked, please contact support center for further information");
        }
        userName = user.getUsername();
        passWord = user.getPassword();
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getValue()));
        return new User(userName, passWord, authorities);
    }
}
