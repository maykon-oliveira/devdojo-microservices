package com.maykonoliveira.gateway.security.config;

import com.maykonoliveira.core.properties.JwtConfiguration;
import com.maykonoliveira.gateway.security.filter.GatewayJWTTokenAuthorizationFilter;
import com.maykonoliveira.token.security.config.SecurityTokenConfiguration;
import com.maykonoliveira.token.security.token.converter.TokenConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** @author maykon-oliveira */
@EnableWebSecurity
public class SecurityConfiguration extends SecurityTokenConfiguration {
  private final TokenConverter tokenConverter;

  public SecurityConfiguration(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
    super(jwtConfiguration);
    this.tokenConverter = tokenConverter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterAfter(
        new GatewayJWTTokenAuthorizationFilter(jwtConfiguration, tokenConverter),
        UsernamePasswordAuthenticationFilter.class);
    super.configure(http);
  }
}
