package com.uib.CodeCycle.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uib.CodeCycle.entities.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserEntity user = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(),UserEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User usr = (User) authResult.getPrincipal();
        List<String> roles = new ArrayList<>();
        usr.getAuthorities().forEach(
                auth -> {
                    roles.add(auth.getAuthority());
        });

        String jwt = JWT.create().
                withSubject(usr.getUsername()).
                withArrayClaim("roles",roles.toArray(new String[roles.size()])).
                withExpiresAt(new Date(System.currentTimeMillis()+SecParams.EXP_TIME)).
                sign(Algorithm.HMAC256(SecParams.SECRET)
        );

        response.addHeader("Authorization", jwt);

    }
}
