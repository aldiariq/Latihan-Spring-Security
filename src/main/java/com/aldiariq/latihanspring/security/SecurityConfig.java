package com.aldiariq.latihanspring.security;

import com.aldiariq.latihanspring.utils.Autentikasi;
import com.aldiariq.latihanspring.utils.Autorisasi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Autentikasi autentikasi = new Autentikasi(authenticationManager());
        autentikasi.setFilterProcessesUrl("/api/autentikasi");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/autentikasi").permitAll();
        http.authorizeRequests().antMatchers(GET, "/api/penggunas").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/pengguna/**").hasAnyAuthority("MANAGER");
        http.authorizeRequests().antMatchers(POST, "/api/aksespengguna/**").hasAnyAuthority("MANAGER");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(autentikasi);
        http.addFilterBefore(new Autorisasi(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
