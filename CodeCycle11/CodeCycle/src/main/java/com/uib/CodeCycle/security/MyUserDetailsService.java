package com.uib.CodeCycle.security;

import com.uib.CodeCycle.entities.UserEntity;
import com.uib.CodeCycle.services.UserService;
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
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userService.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not found!");
        }

        List<GrantedAuthority> auths = new ArrayList<>();

        user.getUserRoles().forEach(
                role -> {
                    GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                    auths.add(authority);

        });

        return new User(user.getEmail(),user.getPassword(),auths);
    }



}
