package com.wayfare.backend.security;

import com.wayfare.backend.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.wayfare.backend.model.RoleEnum.ROLE_USER;
import static com.wayfare.backend.model.RoleEnum.ROLE_WAYFARER;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WayfareSecurityConfig {

    private final WayfareUserDetailService wayfareUserDetailService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WayfareSecurityConfig(WayfareUserDetailService wayfareUserDetailService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.wayfareUserDetailService = wayfareUserDetailService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

//    @Bean
//    public AuthenticationManager authenticationProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(wayfareUserDetailService);
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        return new ProviderManager(provider);
//    }

    @Bean
    public SecurityFilterChain wayfareFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf((csrf) -> csrf.disable())
                        .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/api/auth/**")
                                .permitAll()
                                .requestMatchers("/user/home")
                                .hasAuthority(ROLE_USER.name())
                                .requestMatchers("/user/wayfarer")
                                .hasAuthority(ROLE_WAYFARER.name())
                                .anyRequest().authenticated()
                        ).userDetailsService(wayfareUserDetailService)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}

