package com.maykonoliveira.auth.security.config;

import com.maykonoliveira.auth.security.filter.JwtAuthenticationFilter;
import com.maykonoliveira.core.properties.JwtConfiguration;
import com.maykonoliveira.token.security.config.SecurityTokenConfiguration;
import com.maykonoliveira.token.security.token.creator.TokenCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** @author maykon-oliveira */
@EnableWebSecurity
public class SecurityCredentialsSecurity extends SecurityTokenConfiguration {
  private final TokenCreator tokenCreator;
  private final UserDetailsService userDetailsService;

  public SecurityCredentialsSecurity(
      JwtConfiguration jwtConfiguration,
      TokenCreator tokenCreator,
      @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
    super(jwtConfiguration);
    this.tokenCreator = tokenCreator;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilter(
        new JwtAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator));
    super.configure(http);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
