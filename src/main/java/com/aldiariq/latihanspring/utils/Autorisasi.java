package com.aldiariq.latihanspring.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.Loader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class Autorisasi extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/autentikasi")){
            filterChain.doFilter(request, response);
        }else {
            String autorisasiHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(autorisasiHeader != null && autorisasiHeader.startsWith("Bearer ")){
                try {
                    String tokenPengguna = autorisasiHeader.substring("Bearer ".length());
                    Algorithm algorithmJwt = Algorithm.HMAC256("SECRETKEY-2021Corona".getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithmJwt).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(tokenPengguna);
                    String usernamePengguna = decodedJWT.getSubject();
                    String[] aksesPengguna = decodedJWT.getClaim("akses_pengguna").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(aksesPengguna).forEach(akses -> {
                        authorities.add(new SimpleGrantedAuthority(akses));
                    });
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usernamePengguna, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                }catch (Exception e){
                    log.info(e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
