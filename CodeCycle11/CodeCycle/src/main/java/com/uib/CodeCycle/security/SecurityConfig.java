package com.uib.CodeCycle.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http, AuthenticationConfiguration authConfig)throws Exception{

        AuthenticationManager authMgr = authConfig.getAuthenticationManager();

        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ).csrf(
                csrf -> csrf.disable()).cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration cors = new CorsConfiguration();
                        cors.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        cors.setAllowedMethods(Collections.singletonList("*"));
                        cors.setAllowedHeaders(Collections.singletonList("*"));
                        cors.setExposedHeaders(Collections.singletonList("Authorization"));
                        return cors;
                    }
                }))

                .authorizeHttpRequests(
                req -> req.requestMatchers("/login").permitAll()
                        .requestMatchers("/all").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers("/getbyid/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/adduser/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/updateuser").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/deluser/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/user/*/roles/*").hasAuthority("ADMIN")
                        .requestMatchers("/api/role/**").hasAuthority("ADMIN")
                        .requestMatchers("/byroles/**").hasAuthority("ADMIN")
                        .requestMatchers("/rolesByName/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/role").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/addmission").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,"/user/*/mission/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/mission").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.DELETE,"/delMission/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers("/usersMissions/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.PUT, "/updatemission").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/mission/byUserId/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers("/missionusers/**").hasAnyAuthority("ADMIN:,USER")
                        .requestMatchers("/usersOnMission/{idMission}").hasAnyAuthority("ADMIN:,USER")
                        .requestMatchers("/userMission/**").hasAnyAuthority("ADMIN","USER")
                        .requestMatchers(HttpMethod.POST,"/addScenario").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST,"/ide/launch").permitAll()
                        .requestMatchers(HttpMethod.POST,"/execute").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST,"/execute-all").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/test-events").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/history/latest-standalone-test").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/history/user/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/history").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/mission/*/complete").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/mission/*/tests").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/mission/*/latest-test").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/mission/*/user/*/tests").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/mission/*/user/*/latest-test").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/user/email/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/checkEmail/**").permitAll()

        ).addFilterBefore(new JWTAuthenticationFilter(authMgr), UsernamePasswordAuthenticationFilter.class).
            addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
