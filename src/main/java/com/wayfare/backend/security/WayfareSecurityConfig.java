package com.wayfare.backend.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WayfareSecurityConfig {

//    @bean
//    public userdetailsservice userdetailsservice() {
//        inmemoryuserdetailsmanager manager = new inmemoryuserdetailsmanager();
//        manager.createuser(user.withdefaultpasswordencoder().username("user").password("password").roles("user").build());
//        return manager;
//    }

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                //REMOVE FOR PRODUCTION
                .csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                );
        // ...

        return http.build();
    }
}
