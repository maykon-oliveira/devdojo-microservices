package com.maykonoliveira.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maykonoliveira.core.entities.ApplicationUser;
import com.maykonoliveira.core.properties.JwtConfiguration;
import com.maykonoliveira.token.security.token.creator.TokenCreator;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/** @author maykon-oliveira */
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager manager;
  private final JwtConfiguration jwtConfiguration;
  private final TokenCreator tokenCreator;

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    log.info("Attempting authentication...");
    final ApplicationUser applicationUser =
        new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);

    if (applicationUser == null) {
      throw new UsernameNotFoundException("Unable to retrive the username or password");
    }

    log.info("Creating authentication token '{}'", applicationUser.getUsername());

    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());

    authenticationToken.setDetails(applicationUser);

    return manager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    log.info("Authentication success '{}'", authResult.getName());
    final SignedJWT signedJWT = tokenCreator.createSignedJWT(authResult);
    final String s = tokenCreator.encryptToken(signedJWT);
    response.addHeader(
        "Access-Control-Expose-Headers", "XSRF-TOKEN, " + jwtConfiguration.getHeader().getName());
    response.addHeader(
        jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + s);
  }
}
