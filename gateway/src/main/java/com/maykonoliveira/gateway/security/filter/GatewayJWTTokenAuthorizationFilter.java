package com.maykonoliveira.gateway.security.filter;

import com.maykonoliveira.core.properties.JwtConfiguration;
import com.maykonoliveira.token.security.filter.JWTTokenAuthorizationFilter;
import com.maykonoliveira.token.security.token.converter.TokenConverter;
import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.maykonoliveira.token.security.util.SecurityContextUtil.setSecurityContext;

/** @author maykon-oliveira */
@Slf4j
public class GatewayJWTTokenAuthorizationFilter extends JWTTokenAuthorizationFilter {
  public GatewayJWTTokenAuthorizationFilter(
      JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
    super(jwtConfiguration, tokenConverter);
  }

  @Override
  @SneakyThrows
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
    String header = req.getHeader(jwtConfiguration.getHeader().getName());

    if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
      chain.doFilter(req, res);
      return;
    }

    final String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

    final String decryptToken = tokenConverter.decryptToken(token);
    tokenConverter.validateTokenSignature(decryptToken);

    setSecurityContext(SignedJWT.parse(decryptToken));

    if (jwtConfiguration.getType().equalsIgnoreCase("signed")) {
      RequestContext.getCurrentContext()
          .addZuulRequestHeader(
              "Authorization", jwtConfiguration.getHeader().getPrefix() + decryptToken);
    }
    chain.doFilter(req, res);
  }
}
