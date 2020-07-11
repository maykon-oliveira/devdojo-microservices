package com.maykonoliveira.token.security.filter;

import com.maykonoliveira.core.properties.JwtConfiguration;
import com.maykonoliveira.token.security.token.converter.TokenConverter;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.maykonoliveira.token.security.util.SecurityContextUtil.setSecurityContext;

/** @author maykon-oliveira */
@Slf4j
@AllArgsConstructor
public class JWTTokenAuthorizationFilter extends OncePerRequestFilter {
  protected final JwtConfiguration jwtConfiguration;
  protected final TokenConverter tokenConverter;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String header = req.getHeader(jwtConfiguration.getHeader().getName());

    if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
      chain.doFilter(req, res);
      return;
    }

    final String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

    setSecurityContext(
        jwtConfiguration.getType().equalsIgnoreCase("signed")
            ? validate(token)
            : decryptValidating(token));

    chain.doFilter(req, res);
  }

  @SneakyThrows
  private SignedJWT decryptValidating(String encryptedToken) {
    final String decryptToken = tokenConverter.decryptToken(encryptedToken);

    tokenConverter.validateTokenSignature(decryptToken);
    return SignedJWT.parse(decryptToken);
  }

  @SneakyThrows
  private SignedJWT validate(String signedToken) {
    tokenConverter.validateTokenSignature(signedToken);
    return SignedJWT.parse(signedToken);
  }
}
