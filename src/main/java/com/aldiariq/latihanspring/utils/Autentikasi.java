package com.aldiariq.latihanspring.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class Autentikasi extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public Autentikasi(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String usernamePengguna = request.getParameter("usernamePengguna");
        String passwordPengguna = request.getParameter("passwordPengguna");
        log.info("Username : " + usernamePengguna);
        log.info("Password : " + passwordPengguna);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usernamePengguna, passwordPengguna);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithmJwt = Algorithm.HMAC256("SECRETKEY-2021Corona".getBytes());
        String tokenPengguna = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 100 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .withClaim("akses_pengguna", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithmJwt);
        String refreshtokenPengguna = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 500 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithmJwt);
        Map<String, String> dataToken = new HashMap<>();
        dataToken.put("token_pengguna", tokenPengguna);
        dataToken.put("refresh_token_pengguna", refreshtokenPengguna);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), dataToken);
    }
}
